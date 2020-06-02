package com.studies.cat;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

/**
 * @author： yangh
 * 返回对象：将数据写入到Socket.OutputStream
 * @date： Created on 2020/6/2 8:53
 * @version： v1.0
 * @modified By:
 */

public class Response {
    private OutputStream outputStream;
    //获取当前socket的request对象；可以方便response获取一些客户端传入的数据
    private Request request;
    public Response(OutputStream outputStream){
        this.outputStream = outputStream;
    }

    public void setRequest(Request request){
        this.request = request;
    }
    //response对象返回数据给前端 ；资源文件有两种：静态资源文件 数据
    public void  sendData() throws Exception {
        // webroot目录下。是否存在请求uri 是的话，则表示是一个静态资源文件的路径；静态资源文件，不是则进行数据的返回
        File staticFile = new File(HttpServer.webRoot,request.getUri());
        if(staticFile.exists()){
            FileInputStream fileInputStream = new FileInputStream(staticFile);
            byte[] filedata = new byte[2048];
            fileInputStream.read(filedata);
            outputStream.write(filedata);
        }else{
            //结尾需要加\r\n
            String contentContent="<h1>file not found</h1>";
            //String errorMassge = "HTTP/1.1 500 File Not Found"+"\r\n";
            String errorMassge = "HTTP/1.0 404 File Not Found"+"\r\n";
            errorMassge+="Content-type: text/html"+"\r\n";
            errorMassge+="Content-length: "+contentContent.length()+"\r\n";
            errorMassge+=contentContent;
            outputStream.write(errorMassge.getBytes());
        }

    }
}
