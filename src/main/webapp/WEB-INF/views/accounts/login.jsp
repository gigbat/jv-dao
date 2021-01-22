<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
</head>
<body>
<h1 style="text-align: center; font-size: 25px; margin-top: 20px; color: blue;">Login page</h1>
<h2 style="color: red">${exception}</h2>
<form action="${pageContext.request.contextPath}/login" method="post">
    Enter your login: <input type="text" name="login" placeholder="Enter login" required>
    Enter your password: <input type="password" name="password" placeholder="Enter password" required>
    <button type="submit">Submit</button>
</form>
<a href="${pageContext.request.contextPath}/drivers/create">Go to create a new driver</a>
<a href="${pageContext.request.contextPath}/">Go to main page</a>
</body>
</html>
