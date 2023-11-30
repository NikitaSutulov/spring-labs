<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
<head>
    <title>Edit Dictionary</title>
</head>
<body>
<h1>Edit Dictionary</h1>

<form action="<c:url value='/dictionaries/' />${dictionary.name}/edit" modelAttribute="dictionary" method="post">
    <label for="name">Name:</label>
    <input type="text" id="name" name="name" value="${dictionary.name}" />

    <input type="submit" value="Edit" />
</form>
</body>
</html>
