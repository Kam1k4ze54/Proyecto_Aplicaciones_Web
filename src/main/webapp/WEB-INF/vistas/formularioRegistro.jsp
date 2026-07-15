<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"><title>Registrarse</title></head>
<body>
    <h1>Crear cuenta</h1>

    <c:if test="${not empty mensajeError}">
        <div style="color:white;background-color:#ff4d4d;padding:10px;border-radius:5px;margin-bottom:15px;">
            ${mensajeError}
        </div>
    </c:if>

    <form method="POST" action="${pageContext.request.contextPath}/RegistrarseController?ruta=guardarRegistro">
        <label for="nombres">Nombres:</label>
        <input type="text" name="nombres" id="nombres" value="${param.nombres}" /><br>
        <label for="apellidos">Apellidos:</label>
        <input type="text" name="apellidos" id="apellidos" value="${param.apellidos}" /><br>
        <label for="correo">Correo electrónico:</label>
        <input type="email" name="correo" id="correo" value="${param.correo}" /><br>
        <label for="contrasena">Contraseña:</label>
        <input type="password" name="contrasena" id="contrasena" /><br><br>
        <input type="submit" value="Registrarse" />
    </form>
</body>
</html>