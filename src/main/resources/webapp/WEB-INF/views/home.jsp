<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
    <title>Home</title>
</head>
<body>
<h1>Hello world! This is a JSP.</h1>

<P>The time on the server is ${serverTime}.</P>

<p>Here are some items:</p>
<ul>
    <c:forEach var="item" items="${someItems}">
        <li>${item}</li>
    </c:forEach>
</ul>

<p>Do we have an echo service?</p>
<c:if test="${empty echoService}">
    <p>No, echo service is null.</p>
</c:if>
<c:if test="${not empty echoService}">
    <p>Yes: sending "echo": got ${echoService.echo("echo")}</p>
</c:if>

<p><a href="static.txt">A static file.</a></p>

<p><a href="metrics">Yammer Metrics</a></p>

<p><a href="admin">Admin (admin/password)</a></p>

</body>
</html>
