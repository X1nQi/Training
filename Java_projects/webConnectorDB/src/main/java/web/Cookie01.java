package web;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

public class Cookie01 extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //创建cookie
        Cookie ck =new Cookie("name","admin");
        String chineseValue = URLEncoder.encode("张三","utf-8");

        Cookie ck2 = new Cookie("other",chineseValue);
        //添加cookie
        resp.addCookie(ck);
        resp.addCookie(ck2);
    }
}
