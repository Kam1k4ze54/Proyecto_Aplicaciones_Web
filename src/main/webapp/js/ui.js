(function () {
    /* ---------- Anuncios accesibles (aria-live) reutilizables ----------
       window.anunciar(texto) crea (una sola vez) una región aria-live
       visualmente oculta y actualiza su contenido. La usan panelUsuario.js
       y favoritos.js para notificar a lectores de pantalla resultados de
       búsqueda, nuevos elementos cargados y errores, sin recargar la página. */
    window.anunciar = function (texto) {
        var region = document.getElementById('anuncioAria');
        if (!region) {
            region = document.createElement('div');
            region.id = 'anuncioAria';
            region.className = 'sr-only';
            region.setAttribute('aria-live', 'polite');
            region.setAttribute('aria-atomic', 'true');
            document.body.appendChild(region);
        }
        // Vaciar primero para que lectores de pantalla anuncien igual
        // aunque el texto sea idéntico al anterior.
        region.textContent = '';
        window.setTimeout(function () { region.textContent = texto; }, 50);
    };

    /* ---------- Dropdown de usuario: clic en vez de :hover ---------- */
    var trigger = document.getElementById('btnUserTrigger');
    var dropdown = document.getElementById('userDropdown');
    if (trigger && dropdown) {
        function cerrarDropdown() {
            dropdown.classList.remove('open');
            trigger.setAttribute('aria-expanded', 'false');
        }
        trigger.addEventListener('click', function (ev) {
            ev.stopPropagation();
            var abierto = dropdown.classList.toggle('open');
            trigger.setAttribute('aria-expanded', abierto ? 'true' : 'false');
        });
        document.addEventListener('click', function (ev) {
            if (!dropdown.contains(ev.target) && ev.target !== trigger) cerrarDropdown();
        });
        document.addEventListener('keydown', function (ev) {
            if (ev.key === 'Escape') cerrarDropdown();
        });
    }

    /* ---------- Sidebar móvil (hamburguesa) ---------- */
    var btnMenu = document.getElementById('btnMenuToggle');
    var sidebar = document.querySelector('.sidebar');
    var overlay = document.getElementById('sidebarOverlay');
    if (btnMenu && sidebar) {
        btnMenu.addEventListener('click', function () {
            var abierto = sidebar.classList.toggle('open');
            btnMenu.setAttribute('aria-expanded', abierto ? 'true' : 'false');
            if (overlay) overlay.classList.toggle('visible', abierto);
        });
        if (overlay) overlay.addEventListener('click', function () {
            sidebar.classList.remove('open');
            btnMenu.setAttribute('aria-expanded', 'false');
            overlay.classList.remove('visible');
        });
    }

    /* ---------- Modo claro / oscuro ---------- */
    var btnTheme = document.getElementById('btnThemeToggle');
    var raiz = document.documentElement;

    function aplicarTema(tema) {
        raiz.setAttribute('data-theme', tema);
        if (btnTheme) {
            btnTheme.textContent = tema === 'dark' ? '\u2600\uFE0F' : '\uD83C\uDF19';
            btnTheme.setAttribute('aria-pressed', tema === 'dark' ? 'true' : 'false');
        }
        try { localStorage.setItem('qd-tema', tema); } catch (e) {}
    }

    var guardado = 'light';
    try { guardado = localStorage.getItem('qd-tema') || 'light'; } catch (e) {}
    aplicarTema(guardado);

    if (btnTheme) {
        btnTheme.addEventListener('click', function () {
            aplicarTema(raiz.getAttribute('data-theme') === 'dark' ? 'light' : 'dark');
        });
    }
})();
