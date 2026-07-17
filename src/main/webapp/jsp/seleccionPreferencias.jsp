<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"><title>Seleccionar Preferencias</title></head>
<body>
    <h1>Selecciona tus preferencias</h1>

    <form method="POST" action="${pageContext.request.contextPath}/RegistrarseController?ruta=guardarPreferencias">
        <c:forEach var="cat" items="${categorias}">
            <label>
                <input type="checkbox" name="preferencias" value="${cat.id}" />
                ${cat.nombre} (${cat.tipo})
            </label><br>
        </c:forEach>
        <br>
        <input type="submit" value="Guardar preferencias" />
    </form>
</body>
</html>
