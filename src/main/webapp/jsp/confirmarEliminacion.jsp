<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8"><title>Confirmar eliminación · Quito Descubre</title>
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
                <a href="${pageContext.request.contextPath}/GestionarContenidoController?ruta=listar&tipo=${tipo}" class="active"><span class="nav-icon">◆</span>Volver al listado</a>
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
                    <%-- CU07-D paso 2: confirmación de eliminación --%>
                    <div class="panel-advertencia">
                        <h2>¿Eliminar «${elemento.nombre}»?</h2>
                        <p>Esta acción no se puede deshacer.</p>

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
</body>
</html>
