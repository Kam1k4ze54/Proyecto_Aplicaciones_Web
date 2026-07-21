<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8"><title>Quito Descubre</title>
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
                <a href="${pageContext.request.contextPath}/DescubrirContenidoController?ruta=inicio" class="active"><span class="nav-icon">⌂</span>Inicio</a>
                <a href="#seccion-LugarTuristico"><span class="nav-icon">◆</span>Lugares turísticos</a>
                <a href="#seccion-EstablecimientoGastronomico"><span class="nav-icon">◆</span>Gastronomía</a>
                <a href="#seccion-Evento"><span class="nav-icon">◆</span>Eventos</a>
                <a href="${pageContext.request.contextPath}/GestionarFavoritosController"><span class="nav-icon">♡</span>Favoritos</a>
            </nav>
        </aside>

        <div class="main-area">
            <header class="topbar">
                <%-- CU04 alterno 5: barra de búsqueda global (AJAX) --%>
                <input type="text" id="busquedaInput" class="search-input" placeholder="Buscar lugares, eventos, gastronomía…" autocomplete="off" />
                <div class="user-menu">
                    <button class="user-trigger">
                        <div class="avatar-mini">${usuario.nombres.substring(0,1)}${usuario.apellidos.substring(0,1)}</div>
                        <span class="hola">Hola, ${usuario.nombres}</span>
                        <span class="chevron">⌄</span>
                    </button>
                    <div class="user-dropdown">
                        <a href="${pageContext.request.contextPath}/GestionarPerfilController?ruta=gestionarPerfil">Mi perfil</a>
                        <a href="${pageContext.request.contextPath}/GestionarFavoritosController">Favoritos</a>
                        <hr />
                        <a href="${pageContext.request.contextPath}/IniciarSesionController?ruta=cerrarSesion">Cerrar sesión</a>
                    </div>
                </div>
            </header>

            <main class="content">
                <div class="container" style="padding:0;max-width:none;">
                    <c:if test="${not empty mensajeExito}">
                        <div class="alert alert-success">${mensajeExito}</div>
                    </c:if>
                    <div id="avisoFavoritos" class="alert" style="display:none;"></div>

                    <%-- Resultados de búsqueda (los llena panelUsuario.js) --%>
                    <div id="resultadosBusqueda" style="display:none;"></div>

                    <div id="seccionesPanel">
                        <div class="hero">
                            <div class="hero-inner">
                                <div class="hero-eyebrow">Tu ciudad, redescubierta</div>
                                <h1>Descubre lo mejor de Quito</h1>
                                <p>Lugares increíbles, sabores únicos y eventos que te conectan con nuestra cultura.</p>
                                <a href="#seccion-LugarTuristico" class="btn btn-primary">Explorar ahora →</a>
                            </div>
                        </div>

                        <%-- CU04-A paso 2: 3 elementos por sección según preferencias
                             (o destacados como alternativa — alterno 7) --%>
                        <c:forEach var="seccion" items="${vistaPrevia}">
                            <section id="seccion-${seccion.key}">
                                <div class="section-title">
                                    <h2>
                                        <c:choose>
                                            <c:when test="${seccion.key == 'LugarTuristico'}">Lugares turísticos</c:when>
                                            <c:when test="${seccion.key == 'EstablecimientoGastronomico'}">Establecimientos gastronómicos</c:when>
                                            <c:otherwise>Eventos</c:otherwise>
                                        </c:choose>
                                        <c:if test="${seccion.value.origen == 'preferencias'}">
                                            <span class="seccion-origen">Sugerido para ti</span>
                                        </c:if>
                                        <c:if test="${seccion.value.origen == 'destacados'}">
                                            <span class="seccion-origen">Destacados</span>
                                        </c:if>
                                    </h2>
                                    <c:if test="${seccion.value.origen != 'vacio'}">
                                        <a href="#" class="link-all btn-ver-mas" data-tipo="${seccion.key}">Ver más →</a>
                                    </c:if>
                                </div>

                                <c:choose>
                                    <c:when test="${seccion.value.origen == 'vacio'}">
                                        <%-- Alterno 7.3: ni recomendaciones ni destacados --%>
                                        <p>Aún no hay contenido disponible en esta sección.</p>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="row-grid cols-3" id="grid-${seccion.key}">
                                            <c:forEach var="e" items="${seccion.value.elementos}">
                                                <div class="card">
                                                    <a class="card-link" href="${pageContext.request.contextPath}/DescubrirContenidoController?ruta=detalle&id=${e.id}&tipo=${e.tipo}">
                                                        <div class="card-thumb ${e.tipo == 'LugarTuristico' ? 'ph-lugar' : e.tipo == 'EstablecimientoGastronomico' ? 'ph-gastro' : 'ph-evento'}">
                                                            <c:if test="${e.destacado}"><span class="tag">Destacado</span></c:if>
                                                        </div>
                                                        <div class="card-body">
                                                            <h3>${e.nombre}</h3>
                                                            <div class="card-meta">
                                                                <span class="rating">★ <fmt:formatNumber value="${e.calificacionPromedio}" maxFractionDigits="1" minFractionDigits="1"/></span>
                                                                <span class="dot">·</span><span>${e.sector}</span>
                                                            </div>
                                                        </div>
                                                    </a>
                                                    <c:if test="${e.tipo != 'Evento'}">
                                                        <button class="card-fav" data-id="${e.id}" data-tipo="${e.tipo}" title="Agregar a favoritos">♡</button>
                                                    </c:if>
                                                </div>
                                            </c:forEach>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </section>
                        </c:forEach>
                    </div>
                </div>
            </main>
        </div>
    </div>
    <script src="${pageContext.request.contextPath}/js/favoritos.js"></script>
    <script src="${pageContext.request.contextPath}/js/panelUsuario.js"></script>
</body>
</html>
