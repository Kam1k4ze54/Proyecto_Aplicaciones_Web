<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"><title>Iniciar Sesión</title></head>
<body>
    <c:if test="${not empty mensajeExito}">
        <div style="color:white;background-color:#2e7d4f;padding:10px;border-radius:5px;margin-bottom:15px;">
            ${mensajeExito}
        </div>
    </c:if>

    <c:if test="${not empty mensajeError}">
        <div style="color:white;background-color:#ff4d4d;padding:10px;border-radius:5px;margin-bottom:15px;">
            ${mensajeError}
        </div>
    </c:if>

    <h1>Iniciar Sesión</h1>
    <!-- Formulario de login (corresponde a CU02) -->
    <form method="POST" action="${pageContext.request.contextPath}/IniciarSesionController?ruta=ingresar">
        <label for="correo">Correo:</label>
        <input type="email" name="correo" id="correo" value="${param.correo}" /><br>
        <label for="contrasena">Contraseña:</label>
        <input type="password" name="contrasena" id="contrasena" /><br><br>
        <input type="submit" value="Ingresar" />
    </form>

    <!-- Precondición CU01: el usuario está en el apartado de inicio de sesión -->
    <p>¿No tienes cuenta?
        <a href="${pageContext.request.contextPath}/RegistrarseController?ruta=registrarse">Regístrate</a>
    </p>
</body>
</html>
