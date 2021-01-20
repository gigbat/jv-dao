    <%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add Driver to Car</title>
</head>
<body>
<h1 style="text-align: center; font-size: 25px; margin-top: 20px; color: blue;">Create Driver</h1>
<h2>${exception}</h2>
<form action="${pageContext.request.contextPath}/car/create/driver" method="post">
    Car id: <input type="text" placeholder="Enter car id" name="car_id">
    Driver id: <input type="text" placeholder="Enter driver id" name="driver_id">
    <button type="submit">Submit</button>
</form>
<a href="${pageContext.request.contextPath}/">Go to main page</a>
</body>
</html>
