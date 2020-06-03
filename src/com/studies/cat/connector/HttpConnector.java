package com.studies.cat.connector;

import com.studies.cat.Request;
import com.studies.cat.Response;
import com.studies.cat.processor.HttpProcessor;
import com.studies.cat.processor.ServletProcessor;
import com.studies.cat.processor.StaticResourceProcessor;
import sun.plugin2.main.server.HeartbeatThread;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author： yangh
 * @date： Created on 2020/6/3 10:37
 * @version： v1.0
 * @modified By:
 */
//连接器类：该类默认使用Http协议进行连接服务器
public class HttpConnector implements Runnable {
    //关闭命令
    private String shutdownCommand = "/shutdown";
    //默认开启状态
    private Boolean shutdownState = false;

    @Override
    public void run() {
        //具体的启动逻辑

        //port,backlog,ip ;初始化一台服务器
        ServerSocket serverSocket=null;
        try{
            serverSocket = new ServerSocket(8080,1, InetAddress.getByName("127.0.0.1"));
        }catch (UnknownHostException ex){
            ex.printStackTrace();
            System.exit(1);
        }catch (IOException ioe){
            ioe.printStackTrace();
            System.exit(1);
        }

        while(! shutdownState){
            //每次进入一个新请求时，都是一个新的sokcet,新的流；所以需要在开始的地方设置为null
            Socket socket;
            try{
                try{
                    //连接器目前只接受连接；如果连接失败，则继续等待；连接成功后；则让其他的处理器对socket对象进行处理
                    socket = serverSocket.accept();
                }catch (Exception e){
                    continue;
                }
                //使用HttpProcessor处理器处理Http协议的Socket对象
                HttpProcessor httpProcessor = new HttpProcessor();
                httpProcessor.process(socket);
            }catch(Exception e){
                e.printStackTrace();
                continue;
            }
        }

    }

    //连接器启动入口
    public void start(){
        //将自身包装程序线程，然后运行；这也是为什么去实现Runnable接口的原因；将自身转变成任务；可能会有多个连接器任务
        //当前是哪个HttpConnector调用，this就指向谁
        Thread thread = new Thread(this);
        thread.start();
    }
}

