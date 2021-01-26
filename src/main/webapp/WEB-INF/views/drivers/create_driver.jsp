<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Create Driver</title>
</head>
<body>
<h1 style="text-align: center; font-size: 25px; margin-top: 20px; color: blue;">Create Driver</h1>
<h2 style="color: red">${exception}</h2>
<form action="${pageContext.request.contextPath}/drivers/create" method="post">
    Driver name: <input type="text" placeholder="Enter name" name="name">
    Driver country: <input type="text" placeholder="Enter license number" name="ln">
    Driver login: <input type="text" placeholder="Enter your login" name="login" required>
    Driver password: <input type="password" placeholder="Enter your password" name="password" required>
    Driver repeat password: <input type="password" placeholder="Enter your password" name="rpassword" required>
    <button type="submit">Submit</button>
</form>
<a href="${pageContext.request.contextPath}/">Go to main page</a>
</body>
</html>
