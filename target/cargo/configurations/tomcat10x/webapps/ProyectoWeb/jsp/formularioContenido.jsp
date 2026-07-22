<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8"><script>(function(){try{document.documentElement.setAttribute('data-theme', localStorage.getItem('qd-tema')||'light');}catch(e){}})();</script><title>${empty elemento or elemento.id == 0 ? 'Crear' : 'Editar'} contenido · Quito Descubre</title>
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
                    <p><a href="${pageContext.request.contextPath}/GestionarContenidoController?ruta=listar&tipo=${tipo}">← Volver al listado</a></p>

                    <c:set var="esEdicion" value="${not empty elemento and elemento.id > 0}"/>

                    <%-- CU07 paso 4.1: errores de validación con los datos conservados --%>
                    <c:if test="${not empty listaErrores}">
                        <div class="alert alert-error">
                            <ul style="margin:0;padding-left:18px;">
                                <c:forEach var="error" items="${listaErrores}">
                                    <li>${error}</li>
                                </c:forEach>
                            </ul>
                        </div>
                    </c:if>

                    <%-- CU07-B/C paso 2: atributos comunes + específicos según el tipo --%>
                    <form class="panel" style="max-width:640px;" method="POST"
                          action="${pageContext.request.contextPath}/GestionarContenidoController">
                        <input type="hidden" name="ruta" value="${esEdicion ? 'actualizar' : 'guardar'}" />
                        <input type="hidden" name="tipo" value="${tipo}" />
                        <c:if test="${esEdicion}">
                            <input type="hidden" name="id" value="${elemento.id}" />
                        </c:if>
                        <h2>
                            ${esEdicion ? 'Editar' : 'Crear'}
                            <c:choose>
                                <c:when test="${tipo == 'LugarTuristico'}">lugar turístico</c:when>
                                <c:when test="${tipo == 'EstablecimientoGastronomico'}">establecimiento gastronómico</c:when>
                                <c:otherwise>evento</c:otherwise>
                            </c:choose>
                        </h2>

                        <div class="form-field">
                            <label for="nombre">Nombre</label>
                            <input type="text" name="nombre" id="nombre" value="${elemento.nombre}" />
                        </div>
                        <div class="form-field">
                            <label for="descripcion">Descripción</label>
                            <textarea name="descripcion" id="descripcion">${elemento.descripcion}</textarea>
                        </div>
                        <div class="form-field">
                            <label for="sector">Sector / Ubicación</label>
                            <input type="text" name="sector" id="sector" value="${elemento.sector}" />
                        </div>
                        <div class="form-field">
                            <label for="urlImagen">URL de la imagen</label>
                            <input type="url" name="urlImagen" id="urlImagen" value="${elemento.urlImagen}"
                                   placeholder="https://ejemplo.com/imagen.jpg" />
                            <c:if test="${not empty elemento.urlImagen}">
                                <img src="${elemento.urlImagen}" alt="Vista previa"
                                     style="max-width:160px;border-radius:8px;margin-top:8px;"
                                     onerror="this.style.display='none';" />
                            </c:if>
                        </div>
                        <div class="form-field">
                            <label for="categoriaId">Categoría</label>
                            <div class="pref-grid">
                                <c:forEach var="cat" items="${categorias}">
                                    <label class="pref-chip">
                                        <input type="radio" name="categoriaId" value="${cat.id}"
                                            ${not empty elemento.categoria and elemento.categoria.id == cat.id ? 'checked' : ''} />
                                        <span class="cat-name">${cat.nombre}</span>
                                        <span class="cat-type">${cat.tipo}</span>
                                    </label>
                                </c:forEach>
                            </div>
                        </div>

                        <%-- Atributos específicos por subclase --%>
                        <c:if test="${tipo == 'LugarTuristico'}">
                            <div class="form-field">
                                <label for="horario">Horario</label>
                                <input type="text" name="horario" id="horario" value="${elemento.horario}" placeholder="Ej: Lun-Dom 09:00-17:00" />
                            </div>
                        </c:if>
                        <c:if test="${tipo == 'EstablecimientoGastronomico'}">
                            <div class="form-field">
                                <label for="especialidad">Especialidad</label>
                                <input type="text" name="especialidad" id="especialidad" value="${elemento.especialidad}" placeholder="Ej: Comida típica quiteña" />
                            </div>
                            <div class="form-field">
                                <label for="horario">Horario</label>
                                <input type="text" name="horario" id="horario" value="${elemento.horario}" placeholder="Ej: Lun-Sáb 12:00-22:00" />
                            </div>
                        </c:if>
                        <c:if test="${tipo == 'Evento'}">
                            <div class="form-field">
                                <label for="fechaInicio">Fecha de inicio</label>
                                <input type="date" name="fechaInicio" id="fechaInicio" value="${elemento.fechaInicio}" />
                            </div>
                            <div class="form-field">
                                <label for="fechaFin">Fecha de fin</label>
                                <input type="date" name="fechaFin" id="fechaFin" value="${elemento.fechaFin}" />
                            </div>
                        </c:if>

                        <div class="form-actions">
                            <button type="submit" class="btn btn-primary">${esEdicion ? 'Guardar cambios' : 'Guardar'}</button>
                            <a class="btn btn-ghost" href="${pageContext.request.contextPath}/GestionarContenidoController?ruta=listar&tipo=${tipo}">Cancelar</a>
                        </div>
                    </form>
                </div>
            </main>
        </div>
    </div>
    <script src="${pageContext.request.contextPath}/js/ui.js"></script>
</body>
</html>
