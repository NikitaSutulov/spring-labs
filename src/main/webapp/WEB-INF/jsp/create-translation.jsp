<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <title>Create Translation</title>
</head>
<body>
<h1>Create Translation</h1>
<form action="<c:url value='/dictionaries/${dictionary.name}/create-dictionary'/>" method="post">
    <label for="word">Word:</label>
    <input type="text" id="word" name="word" value="${translation.word}" />

    <label for="translation">Translation:</label>
    <input type="text" id="translation" name="translation" value="${translation.translatedWord}" />

    <input type="submit" value="Create" />
</form>
</body>
</html>