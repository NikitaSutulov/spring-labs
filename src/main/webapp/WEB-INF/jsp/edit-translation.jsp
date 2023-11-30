<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Edit Translation</title>
</head>
<body>
<h1>Edit Translation</h1>

<form action="${pageContext.request.contextPath}/dictionaries/${dictionaryName}/${translation.word.value}-${translation.translatedWord.value}" method="post">
    <input type="hidden" name="_method" value="put">

    <label for="word">Word:</label>
    <input type="text" id="word" name="word" value="${translation.word.value}" />

    <label for="translatedWord">Translated Word:</label>
    <input type="text" id="translatedWord" name="translatedWord" value="${translation.translatedWord.value}" />

    <input type="submit" value="Save" />
</form>
</body>
</html>
