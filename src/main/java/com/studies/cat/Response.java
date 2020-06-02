package com.studies.cat;

import com.studies.cat.constant.Constant;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import java.io.*;
import java.util.Locale;

/**
 * @author： yangh
 * 返回对象：将数据写入到Socket.OutputStream
 * @date： Created on 2020/6/2 8:53
 * @version： v1.0
 * @modified By:
 */

public class Response implements ServletResponse {
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
    public void  sendStaticData() throws Exception {
        // webroot目录下。是否存在请求uri 是的话，则表示是一个静态资源文件的路径；静态资源文件，不是则进行数据的返回
        File staticFile = new File(Constant.webRoot,request.getUri());
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

    @Override
    public String getCharacterEncoding() {
        return null;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return null;
    }

    //实现printwriter
    @Override
    public PrintWriter getWriter() throws IOException {
        PrintWriter pw = new PrintWriter(this.outputStream,true);
        //print不会输出（刷新） println会输出刷新
        return pw;
    }

    @Override
    public void setCharacterEncoding(String s) {

    }

    @Override
    public void setContentLength(int i) {

    }

    @Override
    public void setContentType(String s) {

    }

    @Override
    public void setBufferSize(int i) {

    }

    @Override
    public int getBufferSize() {
        return 0;
    }

    @Override
    public void flushBuffer() throws IOException {

    }

    @Override
    public void resetBuffer() {

    }

    @Override
    public boolean isCommitted() {
        return false;
    }

    @Override
    public void reset() {

    }

    @Override
    public void setLocale(Locale locale) {

    }

    @Override
    public Locale getLocale() {
        return null;
    }
}
