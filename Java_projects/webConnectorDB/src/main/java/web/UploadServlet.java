package web;

import org.w3c.dom.ls.LSOutput;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;


@WebServlet("/uploadServlet")
@MultipartConfig
public class UploadServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("文件上传开始了");
        //设置请求编码
        req.setCharacterEncoding("UTF-8");


        //获取表单参数
        String uname  = req.getParameter("uname");
        System.out.println("unama:"+uname);

        //获取part对象
        Part part = req.getPart("myfile");
        //通过part对象得到上传的文件名
        String filename = part.getSubmittedFileName();
        System.out.println("上传文件名；"+filename);

        //得到文件存放的路径
        String filepath = req.getServletContext().getRealPath("/");
        part.write("D:"+"/"+filename);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
    }
}
