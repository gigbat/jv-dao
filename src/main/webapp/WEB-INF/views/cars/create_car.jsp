<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Create Car</title>
</head>
<body>
<h1 style="text-align: center; font-size: 25px; margin-top: 20px; color: blue;">Create Car</h1>
<form action="${pageContext.request.contextPath}/cars/create" method="post">
    Manufacturer id: <input type="text" placeholder="Enter manufacturer id" name="manufacturer_id">
    Model: <input type="text" placeholder="Enter model" name="model">
    <button type="submit">Submit</button>
</form>
<a href="${pageContext.request.contextPath}/">Go to main page</a>
</body>
</html>
