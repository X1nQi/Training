package web;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;

public class ChineseCookie extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("utf-8");

        Cookie[] cks = req.getCookies();
        if(cks != null && cks.length>0){
            for(Cookie ck:cks){
                String decode_value = URLDecoder.decode(ck.getValue(),"utf-8");

                resp.getWriter().println("cookie name:"+ck.getName()+" cookie value:"+decode_value);
            }
        }else{
            resp.getWriter().println("没有Cookie");
        }
    }

}
