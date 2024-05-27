package web;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/useSession")
public class UseSessionAndReq extends HttpServlet {



    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取session
        HttpSession session = req.getSession();

        //设置session域对象
        session.setAttribute("s_name","sessionName");
        //设置request域对象
        req.setAttribute("r_name","requestName");

        //设置请求转发
        //req.getRequestDispatcher("/sessiontest.jsp").forward(req,resp);
        //设置重定向
        resp.sendRedirect("/sessiontest.jsp");
    }


}
