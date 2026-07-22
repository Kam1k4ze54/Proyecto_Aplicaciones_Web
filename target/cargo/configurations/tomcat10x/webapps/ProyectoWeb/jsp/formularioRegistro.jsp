<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8"><title>Registrarse</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Piazzolla:wght@500;600;700&family=Manrope:wght@400;600;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilo.css?v=2">
</head>
<body>
    <div class="auth-shell">
        <div class="auth-side">
            <div class="auth-side-content">
                <div class="brand">
                    <div class="brand-mark">Q</div>
                    <div class="brand-name">Quito Descubre</div>
                </div>
            </div>
            <div class="auth-quote">Únete y crea tu propio itinerario por <span>lugares, sabores y eventos</span> quiteños.</div>
            <div class="auth-foot">Centro Histórico · La Ronda · Mitad del Mundo</div>
        </div>
        <div class="auth-form-col">
            <div class="auth-card">
                <h1>Crear cuenta</h1>
                <p class="lead">Regístrate para empezar a descubrir Quito.</p>

                <c:if test="${not empty mensajeError}">
                    <div class="alert alert-error">${mensajeError}</div>
                </c:if>

                <form method="POST" action="${pageContext.request.contextPath}/RegistrarseController?ruta=guardarRegistro">
                    <div class="form-field">
                        <label for="nombres">Nombres</label>
                        <input type="text" name="nombres" id="nombres" value="${param.nombres}" required />
                    </div>
                    <div class="form-field">
                        <label for="apellidos">Apellidos</label>
                        <input type="text" name="apellidos" id="apellidos" value="${param.apellidos}" required />
                    </div>
                    <div class="form-field">
                        <label for="correo">Correo electrónico</label>
                        <input type="email" name="correo" id="correo" value="${param.correo}" required />
                    </div>
                    <div class="form-field">
                        <label for="contrasena">Contraseña</label>
                        <input type="password" name="contrasena" id="contrasena" required />
                    </div>
                    <button type="submit" class="btn btn-primary btn-block">Registrarse</button>
                </form>

                <p class="form-foot">¿Ya tienes cuenta?
                    <a href="${pageContext.request.contextPath}/IniciarSesionController?ruta=ingresar">Inicia sesión</a>
                </p>
            </div>
        </div>
    </div>
</body>
</html>
