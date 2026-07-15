<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Usuarios</title>
</head>
<body>
    <h1>Usuarios</h1>

    <form action="${pageContext.request.contextPath}/usuarios" method="post">
        <label>Nombre: <input type="text" name="nombre" required></label>
        <label>Email: <input type="email" name="email" required></label>
        <button type="submit">Guardar</button>
    </form>

    <table border="1">
        <tr>
            <th>ID</th>
            <th>Nombre</th>
            <th>Email</th>
        </tr>
        <c:forEach var="usuario" items="${usuarios}">
            <tr>
                <td>${usuario.id}</td>
                <td>${usuario.nombre}</td>
                <td>${usuario.email}</td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>
