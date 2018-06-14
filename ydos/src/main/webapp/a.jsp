<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 2017/11/7
  Time: 9:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Summernote</title>
    <link href="http://www.jq22.com/jquery/bootstrap-3.3.4.css" rel="stylesheet">
    <script src="http://www.jq22.com/jquery/2.1.1/jquery.min.js"></script>
    <script src="http://www.jq22.com/jquery/bootstrap-3.3.4.js"></script>
    <link href="dist/summernote.css" rel="stylesheet">
    <script src="dist/summernote.js"></script>
</head>
<body>
<div id="summernote"><p>Hello Summernote ,<b>Hello jQuery插件库</b></p></div>
<script>
    $(document).ready(function() {
        $('#summernote').summernote();
    });
</script>
</body>
</html>
