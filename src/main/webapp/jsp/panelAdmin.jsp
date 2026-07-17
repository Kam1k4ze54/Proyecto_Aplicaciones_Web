<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"><title>Panel de Administrador</title></head>
<body>
    <h1>Panel de Administrador</h1>
    <p>Bienvenido, ${usuario.nombres} ${usuario.apellidos}</p>
    <p>Correo: ${usuario.correo}</p>
    <p>Rol: ${usuario.rol}</p>
</body>
</html>
