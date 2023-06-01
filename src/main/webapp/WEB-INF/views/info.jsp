<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<%--
  Created by IntelliJ IDEA.
  User: olik0
  Date: 01.06.2023
  Time: 17:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<c:if test="${error.getCode()==null}">
    <p>${json}</p>
</c:if>
<c:if test="${error.getCode()!=null}">

    <p> {

        „status”: ${error.getCode()}

        „Wiadomość”: ${error.getWhy()}

        }</p>
</c:if>
</body>
</html>
