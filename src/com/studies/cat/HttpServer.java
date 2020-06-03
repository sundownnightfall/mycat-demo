package com.studies.cat;

import com.studies.cat.processor.ServletProcessor;
import com.studies.cat.processor.StaticResourceProcessor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author： yangh
 * 服务器端
 * @date： Created on 2020/6/2 8:53
 * @version： v1.0
 * @modified By:
 */

public class HttpServer {
    //关闭命令
    private String shutdownCommand = "/shutdown";
    //默认开启状态
    private Boolean shutdownState = false;

    public static void main(String[] args) throws Exception{
        //服务器启动地方
        HttpServer httpServer = new HttpServer();
        httpServer.await();

    }

    public void await()  throws UnknownHostException,IOException{
        //port,backlog,ip ;初始化一台服务器
        ServerSocket serverSocket;
        try{
            serverSocket = new ServerSocket(8080,1, InetAddress.getByName("127.0.0.1"));
        }catch (UnknownHostException ex){
            throw ex;
        }catch (IOException ioe){
            throw ioe;
        }

        while(! shutdownState){
            //每次进入一个新请求时，都是一个新的sokcet,新的流；所以需要在开始的地方设置为null
            Socket socket;
            InputStream  inputStream;
            OutputStream outputStream;
            try{
                socket = serverSocket.accept();
                //这两个流分别是数据和浏览器端的输入输出；可以处理流中的数据；
                //将输入流，交给Request对象处理
                inputStream = socket.getInputStream();
                Request request = new Request(inputStream);
                //处理逻辑
                request.parse();
                //校验是否是关闭命令
                shutdownState = request.getUri().equalsIgnoreCase(shutdownCommand);
                //将输出流交给Response对象处理
                outputStream = socket.getOutputStream();
                Response response = new Response(outputStream);
                response.setRequest(request);
                //sendData 要区分是静态资源文件；还是数据返回（需要到Controller层）
                //response.sendData();
                //此处不在直接调用Response的sendData方法进行数据的返回

                //检测是servlet请求还是静态资源请求
                if(request.getUri().startsWith("/servlet/")){
                    ServletProcessor servletProcessor = new ServletProcessor();
                    servletProcessor.process(request,response);
                }else{
                    StaticResourceProcessor staticResourceProcessor = new StaticResourceProcessor();
                    staticResourceProcessor.process(request,response);
                }

                //用完socket后将其关闭
                socket.close();
            }catch(Exception e){
                e.printStackTrace();
                continue;
            }
        }

    }
}
