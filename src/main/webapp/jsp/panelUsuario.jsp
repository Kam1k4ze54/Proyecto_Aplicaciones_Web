<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8"><title>Quito Descubre</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Piazzolla:wght@500;600;700&family=Manrope:wght@400;600;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilo.css?v=2">
</head>
<body>
    <div class="app-shell">
        <aside class="sidebar">
            <div class="brand">
                <div class="brand-mark">Q</div>
                <div class="brand-name">Quito Descubre</div>
            </div>
            <nav class="sidebar-nav">
                <a href="${pageContext.request.contextPath}/PanelUsuarioController?ruta=inicio" class="active"><span class="nav-icon">⌂</span>Inicio</a>
                <a href="#"><span class="nav-icon">◆</span>Lugares turísticos</a>
                <a href="#"><span class="nav-icon">◆</span>Gastronomía</a>
                <a href="#"><span class="nav-icon">◆</span>Eventos</a>
                <a href="#"><span class="nav-icon">♡</span>Favoritos</a>
            </nav>
        </aside>

        <div class="main-area">
            <header class="topbar">
                <input type="text" class="search-input" placeholder="Buscar lugares, eventos, gastronomía…" />
                <div class="user-menu">
                    <button class="user-trigger">
                        <div class="avatar-mini">${usuario.nombres.substring(0,1)}${usuario.apellidos.substring(0,1)}</div>
                        <span class="hola">Hola, ${usuario.nombres}</span>
                        <span class="chevron">⌄</span>
                    </button>
                    <div class="user-dropdown">
                        <a href="${pageContext.request.contextPath}/GestionarPerfilController?ruta=gestionarPerfil">Mi perfil</a>
                        <a href="#">Favoritos</a>
                        <hr />
                        <a href="${pageContext.request.contextPath}/IniciarSesionController?ruta=iniciarSesion">Cerrar sesión</a>
                    </div>
                </div>
            </header>

            <main class="content">
                <div class="container" style="padding:0;max-width:none;">
                    <c:if test="${not empty mensajeExito}">
                        <div class="alert alert-success">${mensajeExito}</div>
                    </c:if>

                    <div class="hero">
                        <div class="hero-inner">
                            <div class="hero-eyebrow">Tu ciudad, redescubierta</div>
                            <h1>Descubre lo mejor de Quito</h1>
                            <p>Lugares increíbles, sabores únicos y eventos que te conectan con nuestra cultura.</p>
                            <a href="#" class="btn btn-primary">Explorar ahora →</a>
                        </div>
                    </div>

                    <section>
                        <div class="section-title">
                            <h2>Destacados para ti</h2>
                            <a href="#" class="link-all">Ver todos →</a>
                        </div>
                        <div class="row-grid cols-3">
                            <div class="card">
                                <div class="card-thumb ph-lugar">
                                    <span class="tag">Lugar destacado</span>
                                    <button class="card-fav">♡</button>
                                </div>
                                <div class="card-body">
                                    <h3>Mitad del Mundo</h3>
                                    <div class="card-meta"><span class="rating">★ 4.8</span><span class="dot">·</span><span>San Antonio</span></div>
                                </div>
                            </div>
                            <div class="card">
                                <div class="card-thumb ph-gastro">
                                    <span class="tag">Gastronomía</span>
                                    <button class="card-fav">♡</button>
                                </div>
                                <div class="card-body">
                                    <h3>Hornado Quiteño</h3>
                                    <div class="card-meta"><span class="rating">★ 4.7</span><span class="dot">·</span><span>Comida típica</span></div>
                                </div>
                            </div>
                            <div class="card">
                                <div class="card-thumb ph-evento">
                                    <span class="tag">Evento</span>
                                    <button class="card-fav">♡</button>
                                </div>
                                <div class="card-body">
                                    <h3>Fiesta de la Virgen del Quinche</h3>
                                    <div class="card-meta"><span class="rating">★ 4.9</span><span class="dot">·</span><span>Julio</span></div>
                                </div>
                            </div>
                        </div>
                    </section>

                    <section>
                        <div class="row-grid cols-2">
                            <div>
                                <div class="section-title">
                                    <h2>Lugares turísticos</h2>
                                    <a href="#" class="link-all">Ver más →</a>
                                </div>
                                <div class="row-grid cols-2">
                                    <div class="card">
                                        <div class="card-thumb ph-lugar"><button class="card-fav">♡</button></div>
                                        <div class="card-body">
                                            <h3>Centro Histórico</h3>
                                            <div class="card-meta"><span class="rating">★ 4.8</span><span class="dot">·</span><span>Centro</span></div>
                                        </div>
                                    </div>
                                    <div class="card">
                                        <div class="card-thumb ph-lugar"><button class="card-fav">♡</button></div>
                                        <div class="card-body">
                                            <h3>Teleférico de Quito</h3>
                                            <div class="card-meta"><span class="rating">★ 4.7</span><span class="dot">·</span><span>Cruz Loma</span></div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div>
                                <div class="section-title">
                                    <h2>Eventos gastronómicos</h2>
                                    <a href="#" class="link-all">Ver más →</a>
                                </div>
                                <div class="row-grid cols-2">
                                    <div class="card">
                                        <div class="card-thumb ph-gastro"><button class="card-fav">♡</button></div>
                                        <div class="card-body">
                                            <h3>Festival del Chocolate</h3>
                                            <div class="card-meta"><span>Julio · Centro</span></div>
                                        </div>
                                    </div>
                                    <div class="card">
                                        <div class="card-thumb ph-gastro"><button class="card-fav">♡</button></div>
                                        <div class="card-body">
                                            <h3>Quito Gastronómico</h3>
                                            <div class="card-meta"><span>Octubre · Varios lugares</span></div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </section>

                    <section>
                        <div class="row-grid" style="grid-template-columns:2.4fr 1fr;">
                            <div>
                                <div class="section-title">
                                    <h2>Eventos en Quito</h2>
                                    <a href="#" class="link-all">Ver más →</a>
                                </div>
                                <div class="row-grid cols-2">
                                    <div class="card">
                                        <div class="card-thumb ph-evento"><button class="card-fav">♡</button></div>
                                        <div class="card-body">
                                            <h3>Concierto de la Orquesta Sinfónica</h3>
                                            <div class="card-meta"><span>18 Jul · Teatro Sucre</span></div>
                                        </div>
                                    </div>
                                    <div class="card">
                                        <div class="card-thumb ph-evento"><button class="card-fav">♡</button></div>
                                        <div class="card-body">
                                            <h3>Festival de la Luz</h3>
                                            <div class="card-meta"><span>9 Ago · Centro Histórico</span></div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="feature-banner">
                                <h2>Vive la cultura quiteña</h2>
                                <p>Explora nuestras tradiciones, historia y la calidez de nuestra gente.</p>
                            </div>
                        </div>
                    </section>
                </div>
            </main>
        </div>
    </div>
</body>
</html>
