package com.studies.cat.test;

import javax.servlet.*;
import java.io.IOException;

/**
 * @author： yangh
 * @date： Created on 2020/6/2 15:59
 * @version： v1.0
 * @modified By:
 */
//serlvet容器测试类
public class MyServlet implements Servlet {
    @Override
    public void init(ServletConfig servletConfig) throws ServletException {

    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        System.out.println("OK");
    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {

    }
}
