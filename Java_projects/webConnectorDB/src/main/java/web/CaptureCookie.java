package web;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CaptureCookie extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取cookie数组
        Cookie[] cks = req.getCookies();
        if(cks != null && cks.length>0){
            for(Cookie ck:cks){
                resp.getWriter().println("cookie name:"+ck.getName()+" cookie value:"+ck.getValue());
            }
        }else{
            resp.getWriter().println("没有Cookie");
        }

    }
}
