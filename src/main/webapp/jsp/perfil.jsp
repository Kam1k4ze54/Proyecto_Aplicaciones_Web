<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8"><script>(function(){try{document.documentElement.setAttribute('data-theme', localStorage.getItem('qd-tema')||'light');}catch(e){}})();</script><title>Mi Perfil · Quito Descubre</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Piazzolla:wght@500;600;700&family=Manrope:wght@400;600;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilo.css?v=2">
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
                <a href="${pageContext.request.contextPath}/DescubrirContenidoController?ruta=descubrir"><span class="nav-icon">⌂</span>Inicio</a>
                <a href="${pageContext.request.contextPath}/GestionarFavoritosController"><span class="nav-icon">♡</span>Favoritos</a>
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
                        <a href="${pageContext.request.contextPath}/GestionarPerfilController?ruta=gestionarPerfil">Mi perfil</a>
                        <a href="${pageContext.request.contextPath}/GestionarFavoritosController">Favoritos</a>
                        <hr />
                        <a href="${pageContext.request.contextPath}/IniciarSesionController?ruta=cerrarSesion">Cerrar sesión</a>
                    </div>
                </div>
            </header>

            <main class="content">
                <div class="container" style="padding:0;max-width:none;">
                    <c:if test="${not empty mensajeCorreoEnUso}">
                        <div class="alert alert-error">${mensajeCorreoEnUso}</div>
                    </c:if>

                    <div class="profile-header">
                        <div class="avatar-lg">${usuario.nombres.substring(0,1)}${usuario.apellidos.substring(0,1)}</div>
                        <div>
                            <h1>${usuario.nombres} ${usuario.apellidos}</h1>
                            <p style="margin:0;">${usuario.correo}</p>
                        </div>
                    </div>

                    <c:choose>
                        <%-- Modo edición: el controlador marcó explícitamente esta bandera --%>
                        <c:when test="${modoEdicion}">
                            <form class="panel" style="max-width:640px;" method="POST" action="${pageContext.request.contextPath}/GestionarPerfilController?ruta=guardarPerfil">
                                <h2>Datos personales</h2>
                                <div class="form-field">
                                    <label for="nombres">Nombres</label>
                                    <input type="text" name="nombres" id="nombres" value="${usuario.nombres}" />
                                </div>
                                <div class="form-field">
                                    <label for="apellidos">Apellidos</label>
                                    <input type="text" name="apellidos" id="apellidos" value="${usuario.apellidos}" />
                                </div>
                                <div class="form-field">
                                    <label for="correo">Correo electrónico</label>
                                    <input type="email" name="correo" id="correo" value="${usuario.correo}" />
                                </div>

                                <h2>Preferencias</h2>
                                <div class="pref-grid">
                                    <c:forEach var="cat" items="${categorias}">
                                        <label class="pref-chip">
                                            <input type="checkbox" name="preferencias" value="${cat.id}"
                                                ${preferenciaIds.contains(cat.id) ? 'checked' : ''} />
                                            <span class="cat-name">${cat.nombre}</span>
                                            <span class="cat-type">${cat.tipo}</span>
                                        </label>
                                    </c:forEach>
                                </div>
                                <div class="form-actions">
                                    <button type="submit" class="btn btn-primary">Guardar cambios</button>
                                    <a class="btn btn-ghost" href="${pageContext.request.contextPath}/GestionarPerfilController?ruta=gestionarPerfil" id="btnCancelarEdicion">Cancelar</a>
                                </div>
                            </form>
                        </c:when>

                        <%-- Modo lectura --%>
                        <c:otherwise>
                            <div class="profile-layout">
                                <div class="panel">
                                    <h2>Datos personales</h2>
                                    <div class="data-row"><span class="k">Nombres</span><span class="v">${usuario.nombres}</span></div>
                                    <div class="data-row"><span class="k">Apellidos</span><span class="v">${usuario.apellidos}</span></div>
                                    <div class="data-row"><span class="k">Correo</span><span class="v">${usuario.correo}</span></div>
                                    <div class="form-actions">
                                        <a class="btn btn-ghost" href="${pageContext.request.contextPath}/GestionarPerfilController?ruta=editarPerfil">Editar perfil</a>
                                    </div>
                                </div>
                                <div class="panel">
                                    <h2>Mis preferencias</h2>
                                    <ul class="pref-list">
                                        <c:forEach var="pref" items="${preferencias}">
                                            <li>${pref.nombre} (${pref.tipo})</li>
                                        </c:forEach>
                                    </ul>
                                </div>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </main>
        </div>
    </div>
<c:if test="${modoEdicion}">
<div class="modal-overlay" id="modalDescartar" style="display:none;">
  <div class="modal-box">
    <h3>Tienes cambios sin guardar</h3>
    <p>Si sales ahora, perderás los cambios que hiciste en tu perfil.</p>
    <div class="form-actions">
      <button type="button" class="btn btn-ghost" id="btnSeguirEditando">Seguir editando</button>
      <button type="button" class="btn btn-peligro" id="btnDescartarCambios">Descartar cambios</button>
    </div>
  </div>
</div>

<script>
(function () {
    var form = document.querySelector('form[action*="guardarPerfil"]');
    if (!form) return;

    var estadoInicial = new FormData(form);
    var huboEnvio = false;
    form.addEventListener('submit', function () { huboEnvio = true; });

    function hayCambios() {
        var actual = new FormData(form), a = [], b = [];
        estadoInicial.forEach(function (v, k) { a.push(k + '=' + v); });
        actual.forEach(function (v, k) { b.push(k + '=' + v); });
        return a.sort().join('&') !== b.sort().join('&');
    }

    var modal = document.getElementById('modalDescartar');
    var destinoPendiente = null;

    function interceptar(ev) {
        if (huboEnvio) return;
        var link = ev.target.closest('a');
        if (!link || !hayCambios()) return;
        ev.preventDefault();
        destinoPendiente = link.href;
        modal.style.display = 'flex';
    }

    document.querySelector('.sidebar').addEventListener('click', interceptar, true);
    document.querySelector('.user-dropdown').addEventListener('click', interceptar, true);
    document.getElementById('btnCancelarEdicion').addEventListener('click', interceptar, true);

    document.getElementById('btnSeguirEditando').addEventListener('click', function () {
        modal.style.display = 'none';
        destinoPendiente = null;
    });
    document.getElementById('btnDescartarCambios').addEventListener('click', function () {
        if (destinoPendiente) window.location.href = destinoPendiente;
    });

    window.addEventListener('beforeunload', function (ev) {
        if (!huboEnvio && hayCambios()) { ev.preventDefault(); ev.returnValue = ''; }
    });
})();
</script>
</c:if>
    <script src="${pageContext.request.contextPath}/js/ui.js"></script>
</body>
</html>
