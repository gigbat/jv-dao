<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>My current cars</title>
</head>
<body>
<table border="1">
    <tr>
        <th>ID</th>
        <th>Manufacturer id</th>
        <th>Model</th>
    </tr>
    <c:forEach var="car" items="${cars}">
        <tr>
            <td>
                <c:out value="${car.id}"/>
            </td>
            <td>
                <c:out value="${car.manufacturerId}"/>
            </td>
            <td>
                <c:out value="${car.model}"/>
            </td>
        </tr>
    </c:forEach>
</table>
<a href="${pageContext.request.contextPath}/">Go to main page</a>
</body>
</html>
