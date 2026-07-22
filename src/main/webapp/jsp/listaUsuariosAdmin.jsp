<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8"><script>(function(){try{document.documentElement.setAttribute('data-theme', localStorage.getItem('qd-tema')||'light');}catch(e){}})();</script><title>Gestión de Usuarios · Quito Descubre</title>
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
                <a href="${pageContext.request.contextPath}/GestionarContenidoController?ruta=listar&tipo=LugarTuristico"><span class="nav-icon">◆</span>Lugares turísticos</a>
                <a href="${pageContext.request.contextPath}/GestionarContenidoController?ruta=listar&tipo=EstablecimientoGastronomico"><span class="nav-icon">◆</span>Gastronomía</a>
                <a href="${pageContext.request.contextPath}/GestionarContenidoController?ruta=listar&tipo=Evento"><span class="nav-icon">◆</span>Eventos</a>
                <a href="${pageContext.request.contextPath}/GestionarUsuariosController?ruta=listar" class="active"><span class="nav-icon">☺</span>Usuarios</a>
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
                    <c:if test="${not empty mensajeExito}">
                        <div class="alert alert-success">${mensajeExito}</div>
                    </c:if>
                    <%-- CU08 1.1: la cuenta ya estaba en ese estado --%>
                    <c:if test="${not empty mensajeAviso}">
                        <div class="alert alert-error">${mensajeAviso}</div>
                    </c:if>

                    <div class="section-title"><h2>Cuentas de usuario</h2></div>

                    <%-- CU08 paso 2: listado con estado y acciones --%>
                    <div class="tabla-wrap">
                        <table class="tabla-admin">
                            <thead>
                                <tr>
                                    <th>Nombre</th>
                                    <th>Correo</th>
                                    <th>Rol</th>
                                    <th>Estado</th>
                                    <th>Acciones</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="u" items="${usuarios}">
                                    <tr>
                                        <td><strong>${u.nombres} ${u.apellidos}</strong></td>
                                        <td>${u.correo}</td>
                                        <td>${u.rol}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${u.estado}"><span class="badge badge-ok">Habilitada</span></c:when>
                                                <c:otherwise><span class="badge badge-off">Inhabilitada</span></c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <div class="acciones">
                                                <c:choose>
                                                    <c:when test="${u.id == usuario.id}">
                                                        <span class="badge badge-neutro">Tu cuenta</span>
                                                    </c:when>
                                                    <c:when test="${u.estado}">
                                                        <form method="POST" action="${pageContext.request.contextPath}/GestionarUsuariosController?ruta=inhabilitar&id=${u.id}">
                                                            <button type="submit" class="btn btn-peligro btn-sm">Inhabilitar</button>
                                                        </form>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <form method="POST" action="${pageContext.request.contextPath}/GestionarUsuariosController?ruta=habilitar&id=${u.id}">
                                                            <button type="submit" class="btn btn-ghost btn-sm">Habilitar</button>
                                                        </form>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </main>
        </div>
    </div>
    <script src="${pageContext.request.contextPath}/js/ui.js"></script>
</body>
</html>
