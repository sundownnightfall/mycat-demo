package com.studies.cat.connector;

import com.studies.cat.Request;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Parameter;
import java.net.Socket;
import java.security.Principal;
import java.util.*;

/**
 * @author： yangh
 * @date： Created on 2020/6/3 14:01
 * @version： v1.0
 * @modified By:
 */
//http协议的Request类
public class HttpRequest implements HttpServletRequest {
    private HashMap header = new HashMap();
    //private ParameterMap parameters;
    private InputStream inputStream;
    private ServletInputStream servletInputStream;

    public HttpRequest(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public String getAuthType() {
        return null;
    }

    @Override
    public long getDateHeader(String s) {
        return 0;
    }

    @Override
    public String getHeader(String s) {
        return null;
    }

    @Override
    public Enumeration getHeaders(String s) {
        return null;
    }

    @Override
    public Enumeration getHeaderNames() {
        return null;
    }

    @Override
    public int getIntHeader(String s) {
        return 0;
    }

    @Override
    public String getPathInfo() {
        return null;
    }

    @Override
    public String getPathTranslated() {
        return null;
    }

    @Override
    public String getContextPath() {
        return null;
    }

    @Override
    public String getRemoteUser() {
        return null;
    }

    @Override
    public boolean isUserInRole(String s) {
        return false;
    }

    @Override
    public Principal getUserPrincipal() {
        return null;
    }

    @Override
    public StringBuffer getRequestURL() {
        return null;
    }

    @Override
    public String getServletPath() {
        return null;
    }

    @Override
    public HttpSession getSession(boolean b) {
        return null;
    }

    @Override
    public HttpSession getSession() {
        return null;
    }

    @Override
    public boolean isRequestedSessionIdValid() {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromUrl() {
        return false;
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
    /**
     * 请求参数字符串
     */
    private String queryString;
    @Override
    public String getQueryString() {
        return this.queryString;
    }

    /**
     * 自定义方法 设置请求参数字符串 从connector处
     */
    public void setQueryString(String queryString){
        this.queryString = queryString;
    }

    private String requestURI;
    @Override
    public String getRequestURI() {
        return this.requestURI;
    }

    /**
     * 自定义方法-设置URI,从connector处
     * @param requestURI
     */
    public void setRequestURI(String requestURI) {
        this.requestURI = requestURI;
    }

    /**
     * jsessionid
     */
    private String requestedSessionId;
    /**
     * jsessionid处理
     * @return
     */
    @Override
    public String getRequestedSessionId() {
        return null;
    }

    /**
     * 自定义方法--设置sessionid
     * @param requestedSessionId
     */
    public void setRequestedSessionId(String requestedSessionId) {
        this.requestedSessionId = requestedSessionId;
    }

//    老版本自定义requestSessionURL属性
//    private Boolean requestSessionURL;
//
//    public Boolean getRequestSessionURL() {
//        return requestSessionURL;
//    }
//
//    public void setRequestSessionURL(Boolean requestSessionURL) {
//        this.requestSessionURL = requestSessionURL;
//    }

    //新版本--用来表示jsessionid是否是在请求的URL中的
    private boolean isRequestedSessionIdFromURL;
    @Override
    public boolean isRequestedSessionIdFromURL() {
        return isRequestedSessionIdFromURL;
    }

    public void setRequestedSessionIdFromURL(boolean requestedSessionIdFromURL) {
        isRequestedSessionIdFromURL = requestedSessionIdFromURL;
    }
    //新版本--用来表示jsessionid是否是在请求的Cookie中的 ;旧版本为requestSeesionCookie
    private boolean isRequestedSessionIdFromCookie;
    @Override
    public boolean isRequestedSessionIdFromCookie() {
        return isRequestedSessionIdFromCookie;
    }

    public void setRequestedSessionIdFromCookie(boolean requestedSessionIdFromCookie) {
        isRequestedSessionIdFromCookie = requestedSessionIdFromCookie;
    }

    /**
     * 请求类型
     */
    private String method;
    @Override
    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    /**
     * 请求使用的协议
     */
    private String protocol;
    @Override
    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    /**
     * content-length属性
     */
    private Integer contentLength;
    @Override
    public int getContentLength() {
        return contentLength;
    }

    public void setContentLength(Integer contentLength) {
        this.contentLength = contentLength;
    }

    /**
     * content-type属性
     */
    private String contentType;
    @Override
    public String getContentType() {
        return contentType;
    }
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
    private List<Cookie> cookies = new ArrayList();

    @Override
    public Cookie[] getCookies() {
        return cookies.toArray(new Cookie[cookies.size()]);
    }

    /**
     * 添加cookie
     * @param cookie
     */
    public void addCookie(Cookie cookie){
        cookies.add(cookie);
    }

}
