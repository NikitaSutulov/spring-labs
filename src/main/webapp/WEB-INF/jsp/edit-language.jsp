<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Edit Language</title>
</head>
<body>
<h1>Edit Language</h1>

<form action="${pageContext.request.contextPath}/languages/edit-language/${language.code}" method="post">

    <label for="name">Name:</label>
    <input type="text" id="name" name="name" value="${language.name}" />

    <label for="code">Code:</label>
    <input type="text" id="code" name="code" value="${language.code}" />

    <input type="submit" value="Save" />
</form>
</body>
</html>