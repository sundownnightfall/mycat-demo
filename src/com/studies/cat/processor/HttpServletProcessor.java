package com.studies.cat.processor;

import com.studies.cat.Request;
import com.studies.cat.Response;
import com.studies.cat.connector.HttpRequest;
import com.studies.cat.connector.HttpResponse;
import com.studies.cat.constant.Constant;
import com.studies.cat.facade.HttpRequestFacade;
import com.studies.cat.facade.HttpResponseFacade;
import com.studies.cat.facade.RequestFacade;
import com.studies.cat.facade.ResponseFacade;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandler;

/**
 * @author： yangh
 * @date： Created on 2020/6/2 15:08
 * @version： v1.0
 * @modified By:
 */
//serlvet请求处理器
public class HttpServletProcessor {
    //servlet请求处理逻辑 ;具体的访问请求为：IP:port/servlet/servletName
    //通过请求来查找具体的Servlet对象；然后在把Socket中对应的数据，交给Servlet对象进行处理
    //Socket中的数据被封装至Request和Response对象中；但是客户端写入的数据都在InputStream中（被封装至Request）;
    //service之所以需要传入Request/Response对象；是因为；这两个对象中封装了Socket对象的输入输出流
    //service中通过操作Request/Response对象来进行数据的交换
    public void process(HttpRequest request, HttpResponse response) throws Exception{
        //基本逻辑
        //找到servlet类文件 :使用URLClassLoader --他需要一个URL数组为参数；其数组中要求存在
        //加载servlet类文件
        //实例化servlet类文件对象
        //执行需要的方法

        //获取uri是为了找到servlet类
        String uri = request.getRequestURI();
        String servletName = uri.substring(uri.lastIndexOf("/")+1);

        URLClassLoader classLoader;
        try{
            //准备类存在的路径
            URL[] urls = new URL[1];
            URLStreamHandler streamHandler = null;
            File filepath = new File(Constant.webRoot);
            String repository = new URL("file",null,filepath.getCanonicalPath()+File.separator).toString();
            urls[0] = new URL(null,repository,streamHandler);
            classLoader=new URLClassLoader(urls);
        }catch(IOException e){
            e.printStackTrace();
            throw e;
        }

        //加载类
        Class serlveClass;
        try{
            serlveClass = classLoader.loadClass(servletName);
        }catch (ClassNotFoundException cnf){
            throw cnf;
        }

        //实例化
        HttpServlet servletObject= (HttpServlet)serlveClass.newInstance();
        HttpRequestFacade requestFacade = new HttpRequestFacade(request);
        HttpResponseFacade responseFacade = new HttpResponseFacade(response);
        servletObject.service((HttpServletRequest)requestFacade,(HttpServletResponse)responseFacade);
    }
}
