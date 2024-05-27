package web;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
@WebServlet("/downloadservlet")
public class downloadservlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //设置请求的编码
        req.setCharacterEncoding("utf-8");
        resp.setContentType("text/html;charset=utf-8");

        //获取请求参数
        String filename = req.getParameter("filename");
        //非空判断
        if(filename == null || "".equals(filename.trim())) {
            resp.getWriter().write("请输入正确的文件名");
            resp.getWriter().close();
            return;
        }

        //获取文件的真实路径
        String path = req.getServletContext().getRealPath("/resources/");
        //建立文件对象
        File file = new File(path+filename);
        //判断资源是否存在
        if(file.exists() &&  file.isFile()) {
            resp.setContentType("application/x-msdownload");
            resp.setHeader("Content-Disposition", "attachment;filename="+filename);

            //创建读取文件的输入流
            FileInputStream filein = new FileInputStream(file);

            //获取输出流
            ServletOutputStream Servletstream = resp.getOutputStream();
            //定义一个byte数组
            byte[] bytes = new byte[1024];
            int len = 0;

            while ((len = filein.read(bytes)) != -1){
                Servletstream.write(bytes,0,len);
            }
            filein.close();
            Servletstream.close();
        }
        }
    }


