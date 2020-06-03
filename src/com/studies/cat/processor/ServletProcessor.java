package com.studies.cat.processor;

import com.studies.cat.Request;
import com.studies.cat.Response;
import com.studies.cat.constant.Constant;
import com.studies.cat.facade.RequestFacade;
import com.studies.cat.facade.ResponseFacade;

import javax.servlet.Servlet;
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
public class ServletProcessor {
    //servlet请求处理逻辑 ;具体的访问请求为：IP:port/servlet/servletName
    //通过请求来查找具体的Servlet对象；然后在把Socket中对应的数据，交给Servlet对象进行处理
    //Socket中的数据被封装至Request和Response对象中；但是客户端写入的数据都在InputStream中（被封装至Request）;
    //service之所以需要传入Request/Response对象；是因为；这两个对象中封装了Socket对象的输入输出流
    //service中通过操作Request/Response对象来进行数据的交换
    public void process(Request request, Response response) throws Exception{
        //基本逻辑
        //找到servlet类文件 :使用URLClassLoader --他需要一个URL数组为参数；其数组中要求存在
        //加载servlet类文件
        //实例化servlet类文件对象
        //执行需要的方法

        //获取uri是为了找到servlet类
        String uri = request.getUri();
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
        Servlet servletObject= (Servlet)serlveClass.newInstance();

        //调用Servlet对象的service方法；为了不让用户看见自定义Request/Response的对象中的一些私密/不重要的东西；需要使用外观模式来进行对象的代理
        //之所以这样做：是为了防止Servlet中；将request强转会我们自定义的Reuqest/Response对象；从而暴露了不必要的东西
        //servletObject.service(request,response);//如果在service方法中进行将request对象强转成自定义的Request对象，回导致request对象的数据暴露
        RequestFacade requestFacade = new RequestFacade(request);
        ResponseFacade responseFacade = new ResponseFacade(response);
        //现在在servlet.service服务中；不管怎么强转；可看见的信息始终都和ServletRequest/ServletResposne的一致；不会导致真正的request对象的数据暴露；
        //门面类只是持有一个request/response对象，他本身不是request/response对象
        servletObject.service(requestFacade,responseFacade);

    }
}
