<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Languages</title>
    <style>
        .custom-table {
            border-collapse: collapse;
            width: 100%;
        }
        .custom-table th, .custom-table td {
            border: 1px solid #ccc;
            padding: 5px;
            text-align: center; /* Center align text within cells */
        }
        .table-container {
            margin-top: 20px; /* Add spacing at the top */
            margin-bottom: 20px; /* Add spacing at the bottom */
        }
    </style>
</head>
<body>
<h1>Languages List</h1>

<!-- Add spacing at the top and bottom of the table -->
<div class="table-container">
    <!-- Add spacing between the "Create New Language" button -->
    <c:choose>
        <c:when test="${user eq 'admin'}">
            <a href="languages/create-language" class="btn btn-primary btn-space">Create New Language</a>
        </c:when>
        <c:when test="${user eq 'Bob'}">
            <p>Hello Bob</p>
        </c:when>
        <c:otherwise>
            <p>No admin rights for you buddy</p>
        </c:otherwise>
    </c:choose>

    <!-- Display the list of languages -->
    <table class="custom-table">
        <thead>
        <tr>
            <th>Name</th>
            <th>Code</th>
            <c:if test="${user == 'admin'}"><th>Edit</th></c:if>
            <c:if test="${user == 'admin'}"><th>Delete</th></c:if>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="language" items="${languages}">
            <tr>
                <td><c:out value="${language.name}"/></td>
                <td><c:out value="${language.code}"/></td>
                <c:if test="${user == 'admin'}">
                    <td>
                        <a href="<c:url value="/languages/edit-language/${language.code}"/>" class="btn btn-primary">Edit</a>
                    </td>
                </c:if>
                <c:if test="${user == 'admin'}">
                    <td>
                        <form action="<c:url value="/languages/${language.code}"/>" method="post">
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
