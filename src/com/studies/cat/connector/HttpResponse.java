package com.studies.cat.connector;

import com.studies.cat.Request;
import com.studies.cat.constant.Constant;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Locale;

/**
 * @author： yangh
 * @date： Created on 2020/6/3 14:04
 * @version： v1.0
 * @modified By:
 */
//Http协议的Response对象 ;通过实现servlet的HttpServletResponse协议实现
public class HttpResponse implements HttpServletResponse {

    private OutputStream outputStream;
    private HttpRequest httpRequest;
    public HttpResponse(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public HttpRequest getHttpRequest() {
        return httpRequest;
    }

    public void setHttpRequest(HttpRequest httpRequest) {
        this.httpRequest = httpRequest;
    }

    @Override
    public void addCookie(Cookie cookie) {

    }

    @Override
    public boolean containsHeader(String s) {
        return false;
    }

    @Override
    public String encodeURL(String s) {
        return null;
    }

    @Override
    public String encodeRedirectURL(String s) {
        return null;
    }

    @Override
    public String encodeUrl(String s) {
        return null;
    }

    @Override
    public String encodeRedirectUrl(String s) {
        return null;
    }

    @Override
    public void sendError(int i, String s) throws IOException {

    }

    @Override
    public void sendError(int i) throws IOException {

    }

    @Override
    public void sendRedirect(String s) throws IOException {

    }

    @Override
    public void setDateHeader(String s, long l) {

    }

    @Override
    public void addDateHeader(String s, long l) {

    }

    @Override
    public void setHeader(String s, String s1) {

    }

    @Override
    public void addHeader(String s, String s1) {

    }

    @Override
    public void setIntHeader(String s, int i) {

    }

    @Override
    public void addIntHeader(String s, int i) {

    }

    @Override
    public void setStatus(int i) {

    }

    @Override
    public void setStatus(int i, String s) {

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

    @Override
    public PrintWriter getWriter() throws IOException {
        return null;
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

    //response对象返回数据给前端 ；资源文件有两种：静态资源文件 数据
    public void  sendStaticData() throws Exception {
        // webroot目录下。是否存在请求uri 是的话，则表示是一个静态资源文件的路径；静态资源文件，不是则进行数据的返回
        File staticFile = new File(Constant.webRoot,httpRequest.getRequestURI());
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
