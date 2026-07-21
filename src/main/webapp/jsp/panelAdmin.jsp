<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8"><script>(function(){try{document.documentElement.setAttribute('data-theme', localStorage.getItem('qd-tema')||'light');}catch(e){}})();</script><title>Panel de Administración · Quito Descubre</title>
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
                <a href="${pageContext.request.contextPath}/GestionarContenidoController?ruta=panel" class="active"><span class="nav-icon">⌂</span>Inicio</a>
                <a href="${pageContext.request.contextPath}/GestionarContenidoController?ruta=listar&tipo=LugarTuristico"><span class="nav-icon">◆</span>Lugares turísticos</a>
                <a href="${pageContext.request.contextPath}/GestionarContenidoController?ruta=listar&tipo=EstablecimientoGastronomico"><span class="nav-icon">◆</span>Gastronomía</a>
                <a href="${pageContext.request.contextPath}/GestionarContenidoController?ruta=listar&tipo=Evento"><span class="nav-icon">◆</span>Eventos</a>
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
                    <div class="hero">
                        <div class="hero-inner">
                            <div class="hero-eyebrow">Panel de administración</div>
                            <h1>Bienvenido, ${usuario.nombres}</h1>
                            <p>Gestiona el catálogo de contenido y las cuentas de usuario de la plataforma.</p>
                        </div>
                    </div>

                    <div class="row-grid cols-2">
                        <div class="feature-banner">
                            <h2>Gestionar contenido (CU07)</h2>
                            <p>Crea, edita, elimina y destaca lugares turísticos, establecimientos gastronómicos y eventos.</p>
                            <div class="form-actions">
                                <a class="btn btn-primary btn-sm" href="${pageContext.request.contextPath}/GestionarContenidoController?ruta=listar&tipo=LugarTuristico">Lugares</a>
                                <a class="btn btn-primary btn-sm" href="${pageContext.request.contextPath}/GestionarContenidoController?ruta=listar&tipo=EstablecimientoGastronomico">Establecimientos</a>
                                <a class="btn btn-primary btn-sm" href="${pageContext.request.contextPath}/GestionarContenidoController?ruta=listar&tipo=Evento">Eventos</a>
                            </div>
                        </div>
                        <div class="feature-banner">
                            <h2>Gestionar usuarios (CU08)</h2>
                            <p>Consulta las cuentas registradas y controla su acceso habilitando o inhabilitando cuentas.</p>
                            <div class="form-actions">
                                <a class="btn btn-primary btn-sm" href="${pageContext.request.contextPath}/GestionarUsuariosController?ruta=listar">Ver usuarios</a>
                            </div>
                        </div>
                    </div>
                </div>
            </main>
        </div>
    </div>
    <script src="${pageContext.request.contextPath}/js/ui.js"></script>
</body>
</html>
