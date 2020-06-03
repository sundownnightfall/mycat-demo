package com.studies.cat.processor;

import com.studies.cat.Request;
import com.studies.cat.Response;
import com.studies.cat.connector.HttpRequest;
import com.studies.cat.connector.SocketInputStream;
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

    public void process(Socket socket){

        try{
            //这两个流分别是数据和浏览器端的输入输出；可以处理流中的数据；
            //将输入流，交给Request对象处理
            SocketInputStream socketInputStream = new SocketInputStream(socket.getInputStream(),2048);


            //Request request = new Request(socketInputStream);无协议的请求对象
            //处理逻辑
            //request.parse();

            HttpRequest httpRequest = new HttpRequest(socketInputStream);


            //校验是否是关闭命令
            //shutdownState = request.getUri().equalsIgnoreCase(shutdownCommand);
            //将输出流交给Response对象处理
            OutputStream outputStream = socket.getOutputStream();
            Response response = new Response(outputStream);
            response.setRequest(httpRequest);
            //sendData 要区分是静态资源文件；还是数据返回（需要到Controller层）
            //response.sendData();
            //此处不在直接调用Response的sendData方法进行数据的返回

            //检测是servlet请求还是静态资源请求
            if(httpRequest.getUri().startsWith("/servlet/")){
                ServletProcessor servletProcessor = new ServletProcessor();
                servletProcessor.process(httpRequest,response);
            }else{
                StaticResourceProcessor staticResourceProcessor = new StaticResourceProcessor();
                staticResourceProcessor.process(httpRequest,response);
            }

            //用完socket后将其关闭
            socket.close();
        }catch (IOException ioe){
            ioe.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
