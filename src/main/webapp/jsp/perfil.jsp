<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"><title>Mi Perfil</title></head>
<body>
    <h1>Mi Perfil</h1>

    <c:if test="${not empty mensajeCorreoEnUso}">
        <div style="color:white;background-color:#ff4d4d;padding:10px;border-radius:5px;margin-bottom:15px;">
            ${mensajeCorreoEnUso}
        </div>
    </c:if>

    <c:choose>
        <%-- Modo edición: el controlador marcó explícitamente esta bandera --%>
        <c:when test="${modoEdicion}">
            <form method="POST" action="${pageContext.request.contextPath}/GestionarPerfilController?ruta=guardarPerfil">
                <label for="nombres">Nombres:</label>
                <input type="text" name="nombres" id="nombres" value="${usuario.nombres}" /><br>
                <label for="apellidos">Apellidos:</label>
                <input type="text" name="apellidos" id="apellidos" value="${usuario.apellidos}" /><br>
                <label for="correo">Correo electrónico:</label>
                <input type="email" name="correo" id="correo" value="${usuario.correo}" /><br><br>

                <h2>Preferencias</h2>
                <c:forEach var="cat" items="${categorias}">
                    <label>
                        <input type="checkbox" name="preferencias" value="${cat.id}"
                            ${preferenciaIds.contains(cat.id) ? 'checked' : ''} />
                        ${cat.nombre} (${cat.tipo})
                    </label><br>
                </c:forEach>
                <br>
                <input type="submit" value="Guardar cambios" />
            </form>
        </c:when>

        <%-- Modo lectura --%>
        <c:otherwise>
            <p>Nombres: ${usuario.nombres}</p>
            <p>Apellidos: ${usuario.apellidos}</p>
            <p>Correo: ${usuario.correo}</p>

            <h2>Mis preferencias</h2>
            <ul>
                <c:forEach var="pref" items="${preferencias}">
                    <li>${pref.nombre} (${pref.tipo})</li>
                </c:forEach>
            </ul>

            <a href="${pageContext.request.contextPath}/GestionarPerfilController?ruta=editarPerfil">Editar perfil</a>
        </c:otherwise>
    </c:choose>
</body>
</html>
