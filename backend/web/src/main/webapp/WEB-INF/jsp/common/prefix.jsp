<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<html>
<head>
    <title>Welcome To Spring Bootstrap Project</title>
    <link href="${STATIC_URL}css/bootstrap.min.css" rel="stylesheet">
    <link href="${STATIC_URL}css/custom.css" rel="stylesheet">
</head>
<body>

<div class="topbar">
    <div class="fill">
        <div class="container fixed">
            <h3><a href="${CONTEXT_ROOT}/">Main Page</a></h3>
            <ul>
                <li><a href="${CONTEXT_ROOT}/userspace">UserSpace</a></li>
            </ul>
            <ul class="nav secondary-nav">
                <security:authorize ifAllGranted="ROLE_USER">
                    <li><a href="j_spring_security_logout">Logout</a></li>
                </security:authorize>
                <security:authorize ifNotGranted="ROLE_USER">
                    <li><a href="register">Register</a></li>
                    <li><a href="login">Login</a></li>
                </security:authorize>
            </ul>
        </div>
    </div>
</div>

<div class="container"></div>