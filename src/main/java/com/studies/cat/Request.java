package com.studies.cat;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

/**
 * @author： yangh
 * 请求对象 ：是对Socket.InputStream的处理
 * @date： Created on 2020/6/2 8:53
 * @version： v1.0
 * @modified By:
 */

public class Request implements ServletRequest {
    private InputStream inputStream;

    private String uri;
    public Request(InputStream inputStream){
        this.inputStream = inputStream;
    }
    //    GET /index.html HTTP/1.1 \r\n
    //    Host: 127.0.0.1:8080
    //    Connection: keep-alive
    //    Cache-Control: max-age=0
    //    Upgrade-Insecure-Requests: 1
    //    User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.61 Safari/537.36
    //    Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9
    //    Sec-Fetch-Site: cross-site
    //    Sec-Fetch-Mode: navigate
    //    Sec-Fetch-User: ?1
    //    Sec-Fetch-Dest: document
    //    Accept-Encoding: gzip, deflate, br
    //    Accept-Language: zh-CN,zh;q=0.9,en;q=0.8
    public void parse() throws Exception{
        //解析HTTP协议
        byte[] bytes = new byte[1024];
        //将数据读入到byte数组中
        inputStream.read(bytes);
        String string = new String(bytes);
        uri = parseUri(string);

    }
    public String getUri(){
        return uri;
    }
    private String parseUri(String httpRequestString){
        //Get index.html 200  协议形式
        int indexStart = httpRequestString.indexOf(" ");
        if(indexStart!=-1){
            int indexEnd = httpRequestString.indexOf(" ",indexStart+1);
            if(indexEnd>indexStart){
                return httpRequestString.substring(indexStart+1,indexEnd);
            }
        }
        return null;
    }

    @Override
    public Object getAttribute(String s) {
        return null;
    }

    @Override
    public Enumeration getAttributeNames() {
        return null;
    }

    @Override
    public String getCharacterEncoding() {
        return null;
    }

    @Override
    public void setCharacterEncoding(String s) throws UnsupportedEncodingException {

    }

    @Override
    public int getContentLength() {
        return 0;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return null;
    }

    @Override
    public String getParameter(String s) {
        return null;
    }

    @Override
    public Enumeration getParameterNames() {
        return null;
    }

    @Override
    public String[] getParameterValues(String s) {
        return new String[0];
    }

    @Override
    public Map getParameterMap() {
        return null;
    }

    @Override
    public String getProtocol() {
        return null;
    }

    @Override
    public String getScheme() {
        return null;
    }

    @Override
    public String getServerName() {
        return null;
    }

    @Override
    public int getServerPort() {
        return 0;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return null;
    }

    @Override
    public String getRemoteAddr() {
        return null;
    }

    @Override
    public String getRemoteHost() {
        return null;
    }

    @Override
    public void setAttribute(String s, Object o) {

    }

    @Override
    public void removeAttribute(String s) {

    }

    @Override
    public Locale getLocale() {
        return null;
    }

    @Override
    public Enumeration getLocales() {
        return null;
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String s) {
        return null;
    }

    @Override
    public String getRealPath(String s) {
        return null;
    }

    @Override
    public int getRemotePort() {
        return 0;
    }

    @Override
    public String getLocalName() {
        return null;
    }

    @Override
    public String getLocalAddr() {
        return null;
    }

    @Override
    public int getLocalPort() {
        return 0;
    }
}
