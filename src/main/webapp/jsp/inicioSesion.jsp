<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8"><title>Iniciar Sesión</title>
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
                    <div>
                        <div class="brand-name">Quito Descubre</div>
                    </div>
                </div>
            </div>
            <div class="auth-quote">Lugares, sabores y eventos que hacen de Quito <span>Patrimonio vivo</span> de América.</div>
            <div class="auth-foot">Centro Histórico · La Ronda · Mitad del Mundo</div>
        </div>
        <div class="auth-form-col">
            <div class="auth-card">
                <h1>Bienvenido de vuelta</h1>
                <p class="lead">Ingresa para continuar explorando Quito.</p>

                <c:if test="${not empty mensajeExito}">
                    <div class="alert alert-success">${mensajeExito}</div>
                </c:if>
                <c:if test="${not empty mensajeError}">
                    <div class="alert alert-error">${mensajeError}</div>
                </c:if>

                <!-- Formulario de login (corresponde a CU02) -->
                <form method="POST" action="${pageContext.request.contextPath}/IniciarSesionController?ruta=ingresar">
                    <div class="form-field">
                        <label for="correo">Correo</label>
                        <input type="email" name="correo" id="correo" value="${param.correo}" placeholder="tucorreo@ejemplo.com" required />
                    </div>
                    <div class="form-field">
                        <label for="contrasena">Contraseña</label>
                        <input type="password" name="contrasena" id="contrasena" placeholder="••••••••" required />
                    </div>
                    <button type="submit" class="btn btn-primary btn-block">Ingresar</button>
                </form>

                <!-- Precondición CU01: el usuario está en el apartado de inicio de sesión -->
                <p class="form-foot">¿No tienes cuenta?
                    <a href="${pageContext.request.contextPath}/RegistrarseController?ruta=registrarse">Regístrate</a>
                </p>
            </div>
        </div>
    </div>
</body>
</html>
