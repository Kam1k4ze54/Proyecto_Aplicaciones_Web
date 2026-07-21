(function () {
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
        if (btnTheme) btnTheme.textContent = tema === 'dark' ? '\u2600\uFE0F' : '\uD83C\uDF19';
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
