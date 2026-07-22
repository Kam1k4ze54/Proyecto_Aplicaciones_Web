<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8"><title>${elemento.nombre} · Quito Descubre</title>
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
                <a href="${pageContext.request.contextPath}/DescubrirContenidoController?ruta=descubrir"><span class="nav-icon">⌂</span>Inicio</a>
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
                        <a href="${pageContext.request.contextPath}/GestionarFavoritosController">Favoritos</a>
                        <hr />
                        <a href="${pageContext.request.contextPath}/IniciarSesionController?ruta=cerrarSesion">Cerrar sesión</a>
                    </div>
                </div>
            </header>

            <main class="content">
                <div class="container" style="padding:0;max-width:none;">
                    <p><a href="${pageContext.request.contextPath}/DescubrirContenidoController?ruta=descubrir">← Volver al panel</a></p>
                    <div id="avisoFavoritos" class="alert" style="display:none;"></div>

                    <%-- CU04 paso 4: ficha de detalle completa --%>
                    <div class="detalle-header">
                        <span class="tag-tipo">
                            <c:choose>
                                <c:when test="${elemento.tipo == 'LugarTuristico'}">Lugar turístico</c:when>
                                <c:when test="${elemento.tipo == 'EstablecimientoGastronomico'}">Establecimiento gastronómico</c:when>
                                <c:otherwise>Evento</c:otherwise>
                            </c:choose>
                        </span>
                        <h1>${elemento.nombre}</h1>
                        <div class="detalle-meta">
                            <span class="rating" id="promedioElemento">★ <fmt:formatNumber value="${elemento.calificacionPromedio}" maxFractionDigits="1" minFractionDigits="1"/></span>
                            <span>·</span><span>${elemento.sector}</span>
                            <c:if test="${not empty elemento.categoria}">
                                <span>·</span><span>${elemento.categoria.nombre}</span>
                            </c:if>
                        </div>
                    </div>

                    <div class="detalle-layout">
                        <div class="panel">
                            <h2>Descripción</h2>
                            <p>${elemento.descripcion}</p>

                            <c:if test="${elemento.tipo == 'LugarTuristico' and not empty elemento.horario}">
                                <div class="data-row"><span class="k">Horario</span><span class="v">${elemento.horario}</span></div>
                            </c:if>
                            <c:if test="${elemento.tipo == 'EstablecimientoGastronomico'}">
                                <c:if test="${not empty elemento.especialidad}">
                                    <div class="data-row"><span class="k">Especialidad</span><span class="v">${elemento.especialidad}</span></div>
                                </c:if>
                                <c:if test="${not empty elemento.horario}">
                                    <div class="data-row"><span class="k">Horario</span><span class="v">${elemento.horario}</span></div>
                                </c:if>
                            </c:if>
                            <c:if test="${elemento.tipo == 'Evento'}">
                                <div class="data-row"><span class="k">Fecha de inicio</span><span class="v">${elemento.fechaInicio}</span></div>
                                <div class="data-row"><span class="k">Fecha de fin</span><span class="v">${elemento.fechaFin}</span></div>
                            </c:if>

                            <%-- Desde la ficha se continúa a CU05 (evaluar) o CU06 (favoritos) --%>
                            <div class="detalle-acciones">
                                <a class="btn btn-primary" href="${pageContext.request.contextPath}/EvaluarContenidoController?elementoId=${elemento.id}&tipo=${elemento.tipo}">★ Evaluar</a>
                                <c:if test="${esFavoriteable}">
                                    <button id="btnFavorito" class="btn btn-ghost ${estaEnFavoritos ? 'favorito-activo' : ''}"
                                            onclick="agregarFavorito(${elemento.id}, '${elemento.tipo}', this)">
                                        <c:choose>
                                            <c:when test="${estaEnFavoritos}">♥ En favoritos</c:when>
                                            <c:otherwise>♡ Agregar a favoritos</c:otherwise>
                                        </c:choose>
                                    </button>
                                </c:if>
                            </div>
                        </div>

                        <div class="panel">
                            <h2>Reseñas de la comunidad</h2>
                            <c:choose>
                                <c:when test="${empty evaluaciones}">
                                    <p>Aún no hay reseñas. ¡Sé el primero en evaluar!</p>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="ev" items="${evaluaciones}">
                                        <div class="resena-item">
                                            <div class="resena-cabecera">
                                                <span class="autor">${ev.usuario.nombres} ${ev.usuario.apellidos}</span>
                                                <span class="rating">★ ${ev.calificacion}</span>
                                            </div>
                                            <p>${ev.resena}</p>
                                        </div>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </main>
        </div>
    </div>
    <script src="${pageContext.request.contextPath}/js/favoritos.js"></script>
</body>
</html>
