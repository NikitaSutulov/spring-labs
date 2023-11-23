<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>--%>
<%--if you uncomment the line above, the 500 error will be thrown --%>
<html>
<head>
    <title>Hello!</title>
</head>
<body>
    <p>Hello world!</p>
    <%
        java.util.Date today = new java.util.Date();
        out.write("Today is " + today);
    %>
</body>
</html>