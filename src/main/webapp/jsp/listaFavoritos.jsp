<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8"><title>Mis Favoritos · Quito Descubre</title>
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
                <a href="${pageContext.request.contextPath}/GestionarFavoritosController" class="active"><span class="nav-icon">♡</span>Favoritos</a>
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
                    <div class="section-title"><h2>Mis favoritos</h2></div>
                    <div id="avisoFavoritos" class="alert" style="display:none;"></div>

                    <%-- CU06 alterno "Lista vacía" --%>
                    <div id="favoritosVacio" style="${empty favoritos ? '' : 'display:none;'}">
                        <div class="alert alert-error">Tu lista de favoritos está vacía.
                            <a href="${pageContext.request.contextPath}/DescubrirContenidoController?ruta=inicio">Explora el catálogo</a> y guarda lo que más te guste.</div>
                    </div>

                    <%-- CU06-B: lista de favoritos guardados --%>
                    <div class="row-grid cols-3" id="gridFavoritos">
                        <c:forEach var="e" items="${favoritos}">
                            <div class="card" id="fav-${e.id}">
                                <a class="card-link" href="${pageContext.request.contextPath}/DescubrirContenidoController?ruta=detalle&id=${e.id}&tipo=${e.tipo}">
                                    <div class="card-thumb ${e.tipo == 'LugarTuristico' ? 'ph-lugar' : 'ph-gastro'}">
                                        <c:if test="${e.destacado}"><span class="tag">Destacado</span></c:if>
                                    </div>
                                    <div class="card-body">
                                        <h3>${e.nombre}</h3>
                                        <div class="card-meta">
                                            <span class="rating">★ <fmt:formatNumber value="${e.calificacionPromedio}" maxFractionDigits="1" minFractionDigits="1"/></span>
                                            <span class="dot">·</span><span>${e.sector}</span>
                                        </div>
                                        <div class="form-actions" style="margin-top:10px;">
                                            <%-- CU06-C: eliminar con confirmación (AJAX) --%>
                                            <button class="btn btn-peligro btn-sm"
                                                    onclick="event.preventDefault(); eliminarFavorito(${e.id}, document.getElementById('fav-${e.id}'));">
                                                Eliminar
                                            </button>
                                        </div>
                                    </div>
                                </a>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </main>
        </div>
    </div>
    <script src="${pageContext.request.contextPath}/js/favoritos.js"></script>
</body>
</html>
