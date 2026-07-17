<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"><title>Panel de Usuario</title></head>
<body>
    <c:if test="${not empty mensajeExito}">
        <div style="color:white;background-color:#2e7d4f;padding:10px;border-radius:5px;margin-bottom:15px;">
            ${mensajeExito}
        </div>
    </c:if>

    <h1>Bienvenido, ${usuario.nombres} ${usuario.apellidos}</h1>
    <p>Correo: ${usuario.correo}</p>
    <p>Rol: ${usuario.rol}</p>

    <p><a href="${pageContext.request.contextPath}/GestionarPerfilController?ruta=gestionarPerfil">Gestionar mi perfil</a></p>
</body>
</html>
