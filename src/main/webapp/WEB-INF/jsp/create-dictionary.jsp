<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <title>Create Dictionary</title>
</head>
<body>
<h1>Create Dictionary</h1>
<form action="<c:out value='/dictionaries/create-dictionary'/>" method="post">
    <label for="name">Name:</label>
    <input type="text" id="name" name="name" value="${name}" />

    <input type="submit" value="Create" />
</form>
</body>
</html>
