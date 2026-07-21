<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8"><script>(function(){try{document.documentElement.setAttribute('data-theme', localStorage.getItem('qd-tema')||'light');}catch(e){}})();</script><title>Confirmar eliminación · Quito Descubre</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Piazzolla:wght@500;600;700&family=Manrope:wght@400;600;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilo.css?v=3">
</head>
<body>
    <div class="app-shell">
        <aside class="sidebar">
            <div class="sidebar-overlay" id="sidebarOverlay"></div>
            <div class="brand">
                <div class="brand-mark">Q</div>
                <div class="brand-name">Quito Descubre</div>
            </div>
            <nav class="sidebar-nav">
                <a href="${pageContext.request.contextPath}/GestionarContenidoController?ruta=panel"><span class="nav-icon">⌂</span>Inicio</a>
                <a href="${pageContext.request.contextPath}/GestionarContenidoController?ruta=listar&tipo=LugarTuristico" class="${tipo == 'LugarTuristico' ? 'active' : ''}"><span class="nav-icon">◆</span>Lugares turísticos</a>
                <a href="${pageContext.request.contextPath}/GestionarContenidoController?ruta=listar&tipo=EstablecimientoGastronomico" class="${tipo == 'EstablecimientoGastronomico' ? 'active' : ''}"><span class="nav-icon">◆</span>Gastronomía</a>
                <a href="${pageContext.request.contextPath}/GestionarContenidoController?ruta=listar&tipo=Evento" class="${tipo == 'Evento' ? 'active' : ''}"><span class="nav-icon">◆</span>Eventos</a>
                <a href="${pageContext.request.contextPath}/GestionarUsuariosController?ruta=listar"><span class="nav-icon">☺</span>Usuarios</a>
            </nav>
        </aside>

        <div class="main-area">
            <header class="topbar">
                <button class="menu-toggle" id="btnMenuToggle" aria-label="Abrir menú" aria-expanded="false">☰</button>
                <button class="theme-toggle" id="btnThemeToggle" aria-label="Cambiar tema">🌙</button>
                <div class="user-menu">
                    <button class="user-trigger" id="btnUserTrigger" aria-haspopup="true" aria-expanded="false">
                        <div class="avatar-mini">${usuario.nombres.substring(0,1)}${usuario.apellidos.substring(0,1)}</div>
                        <span class="hola">Hola, ${usuario.nombres}</span>
                        <span class="chevron">⌄</span>
                    </button>
                    <div class="user-dropdown" id="userDropdown">
                        <a href="${pageContext.request.contextPath}/IniciarSesionController?ruta=cerrarSesion">Cerrar sesión</a>
                    </div>
                </div>
            </header>

            <main class="content">
                <div class="container" style="padding:0;max-width:none;">
                    <%-- CU07-D paso 2: confirmación de eliminación --%>
                    <div class="panel-advertencia">
                        <h2>¿Eliminar «${elemento.nombre}»?</h2>
                        <p>Esta acción no se puede deshacer.</p>

                        <div class="elemento-resumen">
                            <c:if test="${not empty elemento.urlImagen}">
                                <img src="${elemento.urlImagen}" alt="${elemento.nombre}" class="elemento-resumen-img" onerror="this.remove();" />
                            </c:if>
                            <div class="elemento-resumen-datos">
                                <div class="data-row"><span class="k">Tipo</span><span class="v">
                                    <c:choose>
                                        <c:when test="${tipo == 'LugarTuristico'}">Lugar turístico</c:when>
                                        <c:when test="${tipo == 'EstablecimientoGastronomico'}">Establecimiento gastronómico</c:when>
                                        <c:otherwise>Evento</c:otherwise>
                                    </c:choose>
                                </span></div>
                                <div class="data-row"><span class="k">Descripción</span><span class="v">${elemento.descripcion}</span></div>
                                <div class="data-row"><span class="k">Sector</span><span class="v">${elemento.sector}</span></div>
                                <c:if test="${not empty elemento.categoria}">
                                    <div class="data-row"><span class="k">Categoría</span><span class="v">${elemento.categoria.nombre}</span></div>
                                </c:if>
                                <div class="data-row"><span class="k">Calificación</span><span class="v">★ <fmt:formatNumber value="${elemento.calificacionPromedio}" maxFractionDigits="1" minFractionDigits="1"/></span></div>
                                <div class="data-row"><span class="k">Destacado</span><span class="v">${elemento.destacado ? 'Sí' : 'No'}</span></div>
                            </div>
                        </div>

                        <%-- CU07-D 2.1: advertencia de dependencias vinculadas --%>
                        <c:if test="${dependencias.tieneDependencias}">
                            <div class="alert alert-error">
                                Este elemento tiene contenido vinculado que también será eliminado:
                                <strong>${dependencias.cantidadResenias}</strong> reseña(s) y
                                <strong>${dependencias.cantidadFavoritos}</strong> favorito(s).
                            </div>
                        </c:if>

                        <div class="form-actions">
                            <%-- 3.A: confirmar --%>
                            <form method="POST" action="${pageContext.request.contextPath}/GestionarContenidoController?ruta=eliminarConfirmar&tipo=${tipo}&id=${elemento.id}">
                                <button type="submit" class="btn btn-peligro">Sí, eliminar</button>
                            </form>
                            <%-- 3.B: cancelar — el elemento permanece sin cambios --%>
                            <form method="POST" action="${pageContext.request.contextPath}/GestionarContenidoController?ruta=eliminarCancelar&tipo=${tipo}">
                                <button type="submit" class="btn btn-ghost">Cancelar</button>
                            </form>
                        </div>
                    </div>
                </div>
            </main>
        </div>
    </div>
    <script src="${pageContext.request.contextPath}/js/ui.js"></script>
</body>
</html>
