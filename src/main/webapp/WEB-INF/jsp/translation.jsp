<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Translation</title>
</head>
<body>
<b>Word: </b><span><c:out value="${word}"/></span><br>
<b>Translation: </b><span><c:out value="${result.getValue()}"/></span><br>
</body>
</html>