<%--
  Created by IntelliJ IDEA.
  User: Fixer
  Date: 2024/5/19
  Time: 16:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>session域对象和request域对象测试</title>
</head>
<body>
<%
    String s_name = (String) session.getAttribute("s_name");
    String r_name = (String) request.getAttribute("r_name");

    out.print("session域对象中的name：" + s_name + "<br/> "+"request域对象中的name：" + r_name + "<br/>");
%>
</body>
</html>
