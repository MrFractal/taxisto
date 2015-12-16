<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html
        xmlns:jsp="http://java.sun.com/JSP/Page">

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta charset="UTF-8">
    <title>Enable Hover State on Twitter Bootstrap 3 Table Rows</title>
    <link rel="stylesheet" href="http://netdna.bootstrapcdn.com/bootstrap/3.0.3/css/bootstrap.min.css">
    <link rel="stylesheet" href="http://netdna.bootstrapcdn.com/bootstrap/3.0.3/css/bootstrap-theme.min.css">
    <script type="text/javascript" src="http://code.jquery.com/jquery.min.js"></script>
    <script src="http://netdna.bootstrapcdn.com/bootstrap/3.0.3/js/bootstrap.min.js"></script>
    <style type="text/css">
        /*.bs-example {*/
        /*margin: 20px;*/
        /*}*/
    </style>
</head>
<body>

<nav id="myNavbar" class="navbar navbar-default navbar-inverse navbar-fixed-top" role="navigation">
    <!-- Brand and toggle get grouped for better mobile display -->
    <div class="container-fluid">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#navbarCollapse">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="#">Taxisto Администратор</a>
        </div>
        <!-- Collect the nav links, forms, and other content for toggling -->
        <div class="collapse navbar-collapse" id="navbarCollapse">
            <ul class="nav navbar-nav">
                <li class="active"><a href="http://www.tutorialrepublic.com" target="_blank">Home</a></li>
                <li><a href="admin/driver" target="_blank">Drivers</a></li>
                <li><a href="admin/client" target="_blank">Clients</a></li>
            </ul>
        </div>
    </div>
</nav>

<div class="container" style="padding-top: 60px;">
    <div class="row" style="width: 100%;">
        <div class="col-xs-12">
            <table class="table table-hover">
                <thead>
                <tr>
                    <th>Логин</th>
                    <th>Имя</th>
                    <th>Фамилия</th>
                    <th>Email</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>1</td>
                    <td>John</td>
                    <td>Carter</td>
                    <td>johncarter@mail.com</td>
                </tr>
                <tr>
                    <td>2</td>
                    <td>Peter</td>
                    <td>Parker</td>
                    <td>peterparker@mail.com</td>
                </tr>
                <tr>
                    <td>3</td>
                    <td>John</td>
                    <td>Rambo</td>
                    <td>johnrambo@mail.com</td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>

</body>
</html>                                		