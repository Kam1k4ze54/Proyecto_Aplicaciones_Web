<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8"><title>Evaluar: ${elemento.nombre} · Quito Descubre</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Piazzolla:wght@500;600;700&family=Manrope:wght@400;600;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilo.css?v=3">
</head>
<body>
    <div class="app-shell">
        <aside class="sidebar">
            <div class="brand">
                <div class="brand-mark">Q</div>
                <div class="brand-name">Quito Descubre</div>
            </div>
            <nav class="sidebar-nav">
                <a href="${pageContext.request.contextPath}/DescubrirContenidoController?ruta=inicio"><span class="nav-icon">⌂</span>Inicio</a>
                <a href="${pageContext.request.contextPath}/GestionarFavoritosController"><span class="nav-icon">♡</span>Favoritos</a>
            </nav>
        </aside>

        <div class="main-area">
            <header class="topbar">
                <div class="user-menu">
                    <button class="user-trigger">
                        <div class="avatar-mini">${usuario.nombres.substring(0,1)}${usuario.apellidos.substring(0,1)}</div>
                        <span class="hola">Hola, ${usuario.nombres}</span>
                        <span class="chevron">⌄</span>
                    </button>
                    <div class="user-dropdown">
                        <a href="${pageContext.request.contextPath}/GestionarPerfilController?ruta=gestionarPerfil">Mi perfil</a>
                        <hr />
                        <a href="${pageContext.request.contextPath}/IniciarSesionController?ruta=cerrarSesion">Cerrar sesión</a>
                    </div>
                </div>
            </header>

            <main class="content">
                <div class="container" style="padding:0;max-width:none;">
                    <p><a href="${pageContext.request.contextPath}/DescubrirContenidoController?ruta=detalle&id=${elemento.id}&tipo=${elemento.tipo}">← Volver al detalle</a></p>

                    <%-- CU05 paso 2: formulario de calificación y reseña.
                         Alterno "Evaluación previa": llega precargado y editable. --%>
                    <form class="panel" style="max-width:640px;" id="formEvaluacion" data-elemento-id="${elemento.id}" data-tipo="${elemento.tipo}">
                        <h2>
                            <c:choose>
                                <c:when test="${not empty evaluacion}">Editar mi evaluación de «${elemento.nombre}»</c:when>
                                <c:otherwise>Evaluar «${elemento.nombre}»</c:otherwise>
                            </c:choose>
                        </h2>

                        <div id="avisoEvaluacion" class="alert" style="display:none;"></div>

                        <div class="form-field">
                            <label>Calificación</label>
                            <div class="stars">
                                <c:forEach var="i" begin="1" end="5" step="1">
                                    <c:set var="valor" value="${6 - i}"/>
                                    <input type="radio" name="calificacion" id="estrella${valor}" value="${valor}"
                                        ${not empty evaluacion and evaluacion.calificacion == valor ? 'checked' : ''} />
                                    <label for="estrella${valor}" title="${valor} estrellas">★</label>
                                </c:forEach>
                            </div>
                        </div>

                        <div class="form-field">
                            <label for="resena">Reseña</label>
                            <textarea id="resena" name="resena" placeholder="Comparte tu experiencia…">${not empty evaluacion ? evaluacion.resena : ''}</textarea>
                        </div>

                        <div class="form-actions">
                            <button type="submit" class="btn btn-primary">
                                <c:choose>
                                    <c:when test="${not empty evaluacion}">Guardar cambios</c:when>
                                    <c:otherwise>Publicar</c:otherwise>
                                </c:choose>
                            </button>
                            <a class="btn btn-ghost" href="${pageContext.request.contextPath}/DescubrirContenidoController?ruta=detalle&id=${elemento.id}&tipo=${elemento.tipo}">Cancelar</a>
                        </div>
                    </form>
                </div>
            </main>
        </div>
    </div>
    <script src="${pageContext.request.contextPath}/js/evaluacion.js"></script>
</body>
</html>
