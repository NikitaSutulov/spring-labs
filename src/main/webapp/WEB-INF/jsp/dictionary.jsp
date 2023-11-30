<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <title>Dictionary</title>
    <style>
        .custom-table {
            border-collapse: collapse;
            width: 100%;
        }
        .custom-table th, .custom-table td {
            border: 1px solid #ccc;
            padding: 5px;
            text-align: center;
        }
        .table-container {
            margin-top: 20px;
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
<h1><c:out value="${dictionary.name}"/></h1>

<div class="search-form-container">
    <form action="<c:out value='/dictionaries/${dictionary.name}/search' />" method="get">
        <label for="word">Word to translate:</label>
        <input type="text" id="word" name="word" />

        <input type="submit" value="Search" />
    </form>
</div>

<div class="table-container">
    <div>
        <c:if test="${user == 'admin'}">
        <a href="<c:url value='/dictionaries/${dictionary.name}/create-translation' />" class="btn btn-primary">Add Translation</a>
        </c:if>
    </div>

    <table class="custom-table">
        <thead>
        <tr>
            <th>Word</th>
            <th>Translated Word</th>
            <c:if test="${user == 'admin'}">
                <th>Edit</th>
                <th>Delete</th>
            </c:if>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="transaltion" items="${dictionary.translations}">
            <tr>
                <td><c:out value="${transaltion.word.value}"/></td>
                <td><c:out value="${transaltion.translatedWord.value}"/></td>
                <c:if test="${user == 'admin'}">
                    <td>
                        <a href="<c:out value="/dictionaries/${dictionary.name}/edit/${transaltion.word.value}-${transaltion.translatedWord.value}"/>" class="btn btn-primary">Edit</a>
                    </td>
                    <td>
                        <form action="<c:out value="/dictionaries/${dictionary.name}/delete-translation/${transaltion.word.value}-${transaltion.translatedWord.value}" />" method="post">
                            <input type="hidden" name="_method" value="DELETE" />
                            <button type="submit">Delete</button>
                        </form>
                    </td>
                </c:if>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>