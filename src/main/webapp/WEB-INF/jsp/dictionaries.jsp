<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <title>Dictionaries</title>
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
<h1>Dictionaries List</h1>

<div class="table-container">
    <c:if test="${user eq 'admin'}">
        <a href="<c:out value='/dictionaries/create-dictionary'/>" class="btn btn-primary btn-space">Create New Dictionary</a>
    </c:if>

    <table class="custom-table">
        <thead>
        <tr>
            <th>Name</th>
            <c:if test="${user eq 'admin'}"><th>Edit</th></c:if>
            <c:if test="${user eq 'admin'}"><th>Delete</th></c:if>
            <th>Open</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="dictionary" items="${dictionaries}">
            <tr>
                <td><c:out value="${dictionary.name}"/></td>
                <c:if test="${user eq 'admin'}">
                    <td><a href="<c:out value='/dictionaries/${dictionary.name}/edit'/>" class="btn btn-primary">Edit</a></td>
                    <td>
                        <form action="<c:out value='/dictionaries/${dictionary.name}'/>" method="post">
                            <input type="hidden" name="_method" value="DELETE" />
                            <button type="submit">Delete</button>
                        </form>
                    </td>
                </c:if>
                <td><a href="<c:out value='/dictionaries/${dictionary.name}'/>" class="btn btn-success">Open</a></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>
