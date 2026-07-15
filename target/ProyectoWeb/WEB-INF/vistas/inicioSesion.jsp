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

    <h1>Iniciar Sesión</h1>
    <!-- Formulario de login (corresponde a CU02) -->
    <form method="POST" action="${pageContext.request.contextPath}/IniciarSesionController?ruta=iniciar">
        <label for="correo">Correo:</label>
        <input type="email" name="correo" id="correo" /><br>
        <label for="clave">Contraseña:</label>
        <input type="password" name="clave" id="clave" /><br><br>
        <input type="submit" value="Ingresar" />
    </form>
</body>
</html>