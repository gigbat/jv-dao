<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Create Manufacturers</title>
</head>
<body>
<h1 style="text-align: center; font-size: 25px; margin-top: 20px; color: blue;">Create Manufacturers</h1>
<form action="${pageContext.request.contextPath}/manufacturer/create" method="post">
    Manufacturer name: <input type="text" placeholder="Enter name" name="name">
    Manufacturer country: <input type="text" placeholder="Enter country" name="country">
    <button type="submit">Submit</button>
</form>
<a href="${pageContext.request.contextPath}/">Go to main page</a>
</body>
</html>
