<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8"><title>Gestión de Contenido · Quito Descubre</title>
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
                <a href="${pageContext.request.contextPath}/GestionarContenidoController?ruta=panel"><span class="nav-icon">⌂</span>Inicio</a>
                <a href="${pageContext.request.contextPath}/GestionarContenidoController?ruta=listar&tipo=LugarTuristico" class="${tipo == 'LugarTuristico' ? 'active' : ''}"><span class="nav-icon">◆</span>Lugares turísticos</a>
                <a href="${pageContext.request.contextPath}/GestionarContenidoController?ruta=listar&tipo=EstablecimientoGastronomico" class="${tipo == 'EstablecimientoGastronomico' ? 'active' : ''}"><span class="nav-icon">◆</span>Gastronomía</a>
                <a href="${pageContext.request.contextPath}/GestionarContenidoController?ruta=listar&tipo=Evento" class="${tipo == 'Evento' ? 'active' : ''}"><span class="nav-icon">◆</span>Eventos</a>
                <a href="${pageContext.request.contextPath}/GestionarUsuariosController?ruta=listar"><span class="nav-icon">☺</span>Usuarios</a>
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
                        <a href="${pageContext.request.contextPath}/IniciarSesionController?ruta=cerrarSesion">Cerrar sesión</a>
                    </div>
                </div>
            </header>

            <main class="content">
                <div class="container" style="padding:0;max-width:none;">
                    <c:if test="${not empty mensajeExito}">
                        <div class="alert alert-success">${mensajeExito}</div>
                    </c:if>
                    <c:if test="${not empty mensajeCancelado}">
                        <div class="alert alert-error">${mensajeCancelado}</div>
                    </c:if>

                    <div class="section-title">
                        <h2>
                            <c:choose>
                                <c:when test="${tipo == 'LugarTuristico'}">Lugares turísticos</c:when>
                                <c:when test="${tipo == 'EstablecimientoGastronomico'}">Establecimientos gastronómicos</c:when>
                                <c:otherwise>Eventos</c:otherwise>
                            </c:choose>
                        </h2>
                        <%-- CU07-A: acción Crear --%>
                        <a class="btn btn-primary btn-sm" href="${pageContext.request.contextPath}/GestionarContenidoController?ruta=nuevo&tipo=${tipo}">+ Crear</a>
                    </div>

                    <div class="tipo-tabs">
                        <a href="${pageContext.request.contextPath}/GestionarContenidoController?ruta=listar&tipo=LugarTuristico" class="${tipo == 'LugarTuristico' ? 'activa' : ''}">Lugares</a>
                        <a href="${pageContext.request.contextPath}/GestionarContenidoController?ruta=listar&tipo=EstablecimientoGastronomico" class="${tipo == 'EstablecimientoGastronomico' ? 'activa' : ''}">Establecimientos</a>
                        <a href="${pageContext.request.contextPath}/GestionarContenidoController?ruta=listar&tipo=Evento" class="${tipo == 'Evento' ? 'activa' : ''}">Eventos</a>
                    </div>

                    <c:choose>
                        <c:when test="${empty elementos}">
                            <p>No hay elementos registrados de este tipo. Usa «+ Crear» para agregar el primero.</p>
                        </c:when>
                        <c:otherwise>
                            <%-- CU07 paso 2: listado con atributos, estado y acciones --%>
                            <div class="tabla-wrap">
                                <table class="tabla-admin">
                                    <thead>
                                        <tr>
                                            <th>Nombre</th>
                                            <th>Categoría</th>
                                            <th>Sector</th>
                                            <th>Calificación</th>
                                            <th>Destacado</th>
                                            <th>Acciones</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="e" items="${elementos}">
                                            <tr>
                                                <td><strong>${e.nombre}</strong></td>
                                                <td>${not empty e.categoria ? e.categoria.nombre : '—'}</td>
                                                <td>${e.sector}</td>
                                                <td>★ <fmt:formatNumber value="${e.calificacionPromedio}" maxFractionDigits="1" minFractionDigits="1"/></td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${e.destacado}"><span class="badge badge-ok">Destacado</span></c:when>
                                                        <c:otherwise><span class="badge badge-neutro">Normal</span></c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>
                                                    <div class="acciones">
                                                        <a class="btn btn-ghost btn-sm" href="${pageContext.request.contextPath}/GestionarContenidoController?ruta=editar&tipo=${tipo}&id=${e.id}">Editar</a>
                                                        <%-- CU07-E: toggle destacado --%>
                                                        <form method="POST" action="${pageContext.request.contextPath}/GestionarContenidoController?ruta=destacar&tipo=${tipo}&id=${e.id}" style="display:inline;">
                                                            <button type="submit" class="btn btn-ghost btn-sm">${e.destacado ? 'Quitar destacado' : 'Destacar'}</button>
                                                        </form>
                                                        <%-- CU07-D paso 1: solicitar eliminación --%>
                                                        <form method="POST" action="${pageContext.request.contextPath}/GestionarContenidoController?ruta=eliminar&tipo=${tipo}&id=${e.id}" style="display:inline;">
                                                            <button type="submit" class="btn btn-peligro btn-sm">Eliminar</button>
                                                        </form>
                                                    </div>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </main>
        </div>
    </div>
</body>
</html>
