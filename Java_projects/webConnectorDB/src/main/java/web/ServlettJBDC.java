package web;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ServlettJBDC extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("fight!!!!!!!!!");
        PrintWriter writer = resp.getWriter();
        writer.println("<h1>Hello World</h1>");

//        容器的ServletContext
        ServletContext sl = this.getServletContext();
        sl.setAttribute("name","我草死你的妈!");

    }
}
