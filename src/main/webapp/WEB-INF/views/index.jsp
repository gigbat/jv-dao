<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Internet Shop</title>
    <style>
        a {
            display: block;
            margin: 20px auto;
            border: 2px;
            border-radius: 5px;
            text-align: center;
            height: 30px;
            width: 200px;
            line-height: 30px;
            color: cadetblue;
            background-color: darkblue;
            text-decoration: none;
        }
        a:hover {
            background-color: cadetblue;
            color: wheat;
            transition: 1s;
            text-decoration: none;
        }
    </style>
</head>
<body>
<h1 style="text-align: center; font-size: 25px; margin-top: 20px; color: blue;">Welcome, dear User!</h1>
<a href="${pageContext.request.contextPath}/manufacturer/create">Page to create manufacturer</a>
<a href="${pageContext.request.contextPath}/manufacturer">Page to get all manufacturer</a>
<a href="${pageContext.request.contextPath}/driver/create">Page to create driver</a>
<a href="${pageContext.request.contextPath}/driver">Page to get all driver</a>
<a href="${pageContext.request.contextPath}/car/create/car">Page to create car</a>
<a href="${pageContext.request.contextPath}/car/create/driver">Page to add driver to car</a>
<a href="${pageContext.request.contextPath}/car">Page to get all car</a>
</body>
</html>