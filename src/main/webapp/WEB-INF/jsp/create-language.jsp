<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Create Language</title>
</head>
<body>
<h1>Create Language</h1>

<form action="${pageContext.request.contextPath}/languages/create-language" method="post">
    <label for="name">Name:</label>
    <input type="text" id="name" name="name" value="" />

    <label for="code">Code:</label>
    <input type="text" id="code" name="code" value="" />

    <input type="submit" value="Create" />
</form>
</body>
</html>