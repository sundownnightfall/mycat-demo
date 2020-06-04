package com.studies.cat.processor;

import com.studies.cat.connector.*;

import javax.servlet.ServletException;
import javax.xml.ws.spi.http.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author： yangh
 * @date： Created on 2020/6/3 10:49
 * @version： v1.0
 * @modified By:
 */
//专门处理；使用了Http协议方式请求的Socket对象
public class HttpProcessor {

    //创建一个空对象，httpRequestLine在parseRequest方法进行填充
    private HttpRequestLine httpRequestLine = new HttpRequestLine();

    //之所以要提出成实例遍历，是因为需要在httpProcessor的parseHeader和paserRequestLine方法中，为request/response对象赋值
    private HttpRequest httpRequest;
    private HttpResponse httpResponse;


    private HttpHeader httpHeader = new HttpHeader();
    public void process(Socket socket){

        try{
            //这两个流分别是数据和浏览器端的输入输出；可以处理流中的数据；
            //将输入流，交给Request对象处理
            SocketInputStream socketInputStream = new SocketInputStream(socket.getInputStream(),2048);
            //将输出流交给Response对象处理
            OutputStream outputStream = socket.getOutputStream();
            httpRequest = new HttpRequest(socketInputStream);
            parseRequest(socketInputStream,outputStream);
            parseHeader(socketInputStream);

            httpResponse = new HttpResponse(outputStream);
            httpResponse.setHeader("server","yhong's server container");
            httpResponse.setHttpRequest(httpRequest);
            /*
            sendData 要区分是静态资源文件；还是数据返回（需要到Controller层）
            response.sendData();
            此处不在直接调用Response的sendData方法进行数据的返回
            检测是servlet请求还是静态资源请求
            */
            if(httpRequest.getRequestURI().startsWith("/servlet/")){
                HttpServletProcessor servletProcessor = new HttpServletProcessor();
                servletProcessor.process(httpRequest,httpResponse);
            }else{
                HttpStaticResourceProcessor staticResourceProcessor = new HttpStaticResourceProcessor();
                staticResourceProcessor.process(httpRequest,httpResponse);
            }

            //用完socket后将其关闭
            socket.close();
        }catch (IOException ioe){
            ioe.printStackTrace();
        }catch (ServletException se){
            se.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 解析请求行--找出使用方式 uri 请求参数 使用协议等并将这些信息设置到request对象中
     * 查询字符串；特殊参数 uri
     * @param socketInputStream
     * @param outputStream
     * @throws ServletException
     */
    private void parseRequest(SocketInputStream socketInputStream,OutputStream outputStream) throws ServletException{
        try{
            //请求头就请求的第一行：描述了请求方式，请求的uri,参数。。。，使用的协议
            socketInputStream.readRequestLine(httpRequestLine);
            //HttpRequestLine{
            // method=[G, E, T,  ,  ,  ,  ,  ],
            // methodEnd=3, //描述method的有效子长
            // uri=[/, s, e, r, v, l, e, t, /, M, y, D, S, S, e, r, v, l, e, t,  ,  ,  ,  ,  ,  ,  ,  ,  ,  ,  ,  ,  ,  ,  ,  ,  ,  ,  ,  ,  ,  ,  ,  ,  ,  ,  ,  ,  ,  ,  ,  ,  ,  ,  ,  ,  ,  ,  ,  ,  ,  ,  ,  ],
            // uriEnd=20, 描述uri的有效字长
            // protocol=[H, T, T, P, /, 1, ., 1,  ,  ,  ,  ,  ,  ,  ,  ],
            // protocolEnd=8} 描述协议的有效字长
            if(httpRequestLine.methodEnd<1){
                throw new ServletException("missing request method  ");
            }
            if(httpRequestLine.uriEnd<1){
                throw new ServletException("missing request uri  ");
            }
            if(httpRequestLine.protocolEnd<1){
                throw new ServletException("missing request protocal  ");
            }

            System.out.println(httpRequestLine);
            //使用字符数组创建字符串：并指定开始和结束:是post..还是get,还是其他
            //post??get??..
            String requestMethod = new String(httpRequestLine.method,0,httpRequestLine.methodEnd);
            //直接在uri中查询；uri中是否含有请求参数，存在的话，返回这个？的位置 //此处传入一个字符串
            int paremterStart = httpRequestLine.indexOf("?");
            String requestURI;
            if(paremterStart>=0){
                //有请求参数存在；将请求参数设置到request对象中
                String parametes = new String(httpRequestLine.uri,paremterStart+1,httpRequestLine.uriEnd);
                System.out.printf("请求参数为：%s",parametes);
                httpRequest.setQueryString(parametes);

                requestURI = new String(httpRequestLine.uri,0,paremterStart);
                System.out.printf("请求URI为：%s",requestURI);
                httpRequest.setRequestURI(requestURI);

            }else{
                //没有请求参数存在,则设置request的请求参数为null
                httpRequest.setQueryString(null);
                //假设整个字符串都是uri
                requestURI = new String(httpRequestLine.uri,0,httpRequestLine.uriEnd);
                httpRequest.setRequestURI(requestURI);
            }

            //检测uri是否合法，必须以/开头 ;如果不是，则进行检测是否是一个绝对路径例如带有http://www.baidu.com
            //之所以这样做，可能是需要进行页面调整等操作

            if(requestURI.startsWith("/")){
                int httpPost = requestURI.indexOf("://");
                //没有找到
                if(httpPost==-1){
                    requestURI="";
                }else{
                    //http://www.baidu.com --->www.baidu.com
                    requestURI=requestURI.substring(httpPost);
                }
            }

            //主要作用：从URI中找出jsessionid并将其填充到request对象中；然后将uri重新赋值为剔除掉jsessionid参数后的新uri
            //uri= myselvet/doit;kkk=bbbb;jsessionid=xxx;ppp=jjj
            //uri= myselvet/doit;kkk=bbbb;ppp=jjj

            //协议://主机:端口/路径;特殊参数1=值得?服务器使用的参数1=1 fragment
            //现在从uri中取出特殊参数
            //JSESSIONID=D17164809A66DF704F38F7D1BF101452; _gscu_422057653=9004275881a7ol58; gabUuid=15900427588927676; c=I77k04wY-1590042766186-9f5abfc68f49e1219327392;
            String sessionMatch = ";jsessionid=";
            //返回满足的开始位置
            int sessionPositon = requestURI.indexOf(sessionMatch);
            if(sessionPositon>-1){
                //解析出jsessionid后面的字符串；但是特殊参数有很多；并且以;分割；所以要找出具体的jsessionid的值，还需要进行数据切割
                //JSESSIONID=D17164809A66DF704F38F7D1BF101452; _gscu_422057653=9004275881a7ol58; gabUuid=15900427588927676; c=I77k04wY-1590042766186-9f5abfc68f49e1219327392;
                String specialParameters= requestURI.substring(sessionPositon+sessionMatch.length());
                //当前为：D17164809A66DF704F38F7D1BF101452; _gscu_422057653=9004275881a7ol58; gabUuid=15900427588927676; c=I77k04wY-1590042766186-9f5abfc68f49e1219327392;
                int index = specialParameters.indexOf(";");
                //用来存储，特殊参数中除去jsessionid的其他特殊参数
                String otherSpecialParam="";
                if(index==-1){
                    //只有一个jessionid特殊参数，其后面的字符串全部都是jsessionid的值
                    //原值为：jsessionid=D17164809A66DF704F38F7D1BF101452
                    String jsessionidValue=specialParameters;
                    System.out.printf("jsessionidValue为：%s",jsessionidValue);
                    httpRequest.setRequestedSessionId(jsessionidValue);
                    otherSpecialParam="";
                }else{
                    //切割出具体的值
                    //原值可能为：jsessionid=D17164809A66DF704F38F7D1BF101452;xxx=dddddd;xxxxcc=rrrrr
                    String jsessionidValue=specialParameters.substring(0,index);
                    System.out.printf("jsessionidValue为：%s",jsessionidValue);
                    httpRequest.setRequestedSessionId(jsessionidValue);
                    otherSpecialParam=specialParameters.substring(index);
                }
                httpRequest.setRequestSessionURL(true);
                //重新计算uri
                requestURI = requestURI.substring(0,sessionPositon)+otherSpecialParam;
            }else{
                //没有jsessionid时
                httpRequest.setRequestedSessionId(null);
                httpRequest.setRequestSessionURL(false);
            }

            //标准化URI
            String normalizeURI = normalize(requestURI);
            //设置请求的methodType: get/post/delete/trace/put/option/...
            httpRequest.setMethod(requestMethod);
            //设置请求使用的协议
            String protocol = new String(httpRequestLine.method,0,httpRequestLine.protocolEnd);
            httpRequest.setProtocol(protocol);
            //如果标准化后的uri是空的，则进行提示，否者就用规范话的URI
            if(normalizeURI==null){
                throw new ServletException("normalized URI failure!!! the reason is invalid uri:"+requestURI);
            }else{
                httpRequest.setRequestURI(normalizeURI);
            }

        }catch (IOException ioe){
            ioe.printStackTrace();
        }



    }

    private void parseHeader(SocketInputStream socketInputStream){
        try{
            socketInputStream.readHeader(httpHeader);
            //HttpHeader{
            // name=[a, c, c, e, p, t, :,  ,  ,  ,  ,  ,  ,  ,  ,  ,  ,  ,  ,  ,  ,  ,  ,  ,  ,  ,  ,  ,  ,  ,  ,  ],
            // nameEnd=6,
            // value=[t, e, x, t, /, h, t, m, l, ,,  , a, p, p, l, i, c, a, t, i, o, n, /, x, h, t, m, l, +, x, m, l, ,,  , i, m, a, g, e, /, j, x, r, ,,  , *, /, *,  ,  ,  ,  ,  ,  ,  ,  ,  ,  ,  ,  ,  ,  ,  ,  ],
            // valueEnd=48,
            // hashCode=0}
            System.out.println(httpHeader);
        }catch (IOException ioe){
            ioe.printStackTrace();
        }

    }

    /**
     * Return a context-relative path, beginning with a "/", that represents
     * the canonical version of the specified path after ".." and "." elements
     * are resolved out.  If the specified path attempts to go outside the
     * boundaries of the current context (i.e. too many ".." path elements
     * are present), return <code>null</code> instead.
     *
     * @param path Path to be normalized
     */
    protected String normalize(String path) {
        if (path == null){
            return null;
        }
        // Create a place for the normalized path
        String normalized = path;

        // Normalize "/%7E" and "/%7e" at the beginning to "/~"
        if (normalized.startsWith("/%7E") || normalized.startsWith("/%7e")){
            normalized = "/~" + normalized.substring(4);
        }

        // Prevent encoding '%', '/', '.' and '\', which are special reserved
        // characters
        if ((normalized.indexOf("%25") >= 0)
                || (normalized.indexOf("%2F") >= 0)
                || (normalized.indexOf("%2E") >= 0)
                || (normalized.indexOf("%5C") >= 0)
                || (normalized.indexOf("%2f") >= 0)
                || (normalized.indexOf("%2e") >= 0)
                || (normalized.indexOf("%5c") >= 0)) {
            return null;
        }

        if (normalized.equals("/.")){
            return "/";
        }


        // Normalize the slashes and add leading slash if necessary
        if (normalized.indexOf('\\') >= 0){
            normalized = normalized.replace('\\', '/');
        }

        if (!normalized.startsWith("/")){
            normalized = "/" + normalized;
        }

        // Resolve occurrences of "//" in the normalized path
        while (true) {
            int index = normalized.indexOf("//");
            if (index < 0){
                break;
            }
            normalized = normalized.substring(0, index) + normalized.substring(index + 1);
        }

        // Resolve occurrences of "/./" in the normalized path
        while (true) {
            int index = normalized.indexOf("/./");
            if (index < 0){
                break;
            }
            normalized = normalized.substring(0, index) + normalized.substring(index + 2);
        }

        // Resolve occurrences of "/../" in the normalized path
        while (true) {
            int index = normalized.indexOf("/../");
            if (index < 0){
                break;
            }
            if (index == 0){
                return (null);  // Trying to go outside our context
            }
            int index2 = normalized.lastIndexOf('/', index - 1);
            normalized = normalized.substring(0, index2) +
                    normalized.substring(index + 3);
        }

        // Declare occurrences of "/..." (three or more dots) to be invalid
        // (on some Windows platforms this walks the directory tree!!!)
        if (normalized.indexOf("/...") >= 0)
            return (null);

        // Return the normalized path that we have completed
        return (normalized);

    }
}
