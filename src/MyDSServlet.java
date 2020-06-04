import javax.servlet.*;
import java.io.IOException;

/**
 * @author： yangh
 * @date： Created on 2020/6/2 17:16
 * @version： v1.0
 * @modified By:
 */
//测试的类：测试路径为：http://127.0.0.1:8080/servlet/MyDSServlet ；此Servlet没有包含协议
public class MyDSServlet implements Servlet {
    @Override
    public void init(ServletConfig servletConfig) throws ServletException {

    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        System.out.println("not ok");
    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {

    }
}
