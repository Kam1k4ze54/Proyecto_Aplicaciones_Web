/* CU04-B (búsqueda por nombre) y CU04-C (ver más) — AJAX contra /api */
(function () {
    var BASE = '/' + location.pathname.split('/')[1];

    var NOMBRES_TIPO = {
        'LugarTuristico': 'Lugares turísticos',
        'EstablecimientoGastronomico': 'Establecimientos gastronómicos',
        'Evento': 'Eventos'
    };
    var CLASES_THUMB = {
        'LugarTuristico': 'ph-lugar',
        'EstablecimientoGastronomico': 'ph-gastro',
        'Evento': 'ph-evento'
    };

    // Tarjeta de un elemento (mismo diseño que las tarjetas del JSP)
    function tarjetaHtml(e) {
        var meta = '<span class="rating">★ ' + (e.calificacionPromedio || 0).toFixed(1) + '</span>';
        if (e.sector) {
            meta += '<span class="dot">·</span><span>' + e.sector + '</span>';
        }
        if (e.fechaInicio) {
            meta += '<span class="dot">·</span><span>' + e.fechaInicio + '</span>';
        }
        var fav = '';
        if (e.tipo !== 'Evento') {
            fav = '<button class="card-fav" data-id="' + e.id + '" data-tipo="' + e.tipo + '" title="Agregar a favoritos">♡</button>';
        }
        return '<div class="card">' +
            '<a class="card-link" href="' + BASE + '/DescubrirContenidoController?ruta=detalle&id=' + e.id + '">' +
            '<div class="card-thumb ' + (CLASES_THUMB[e.tipo] || '') + '">' +
            (e.destacado ? '<span class="tag">Destacado</span>' : '') + '</div>' +
            '<div class="card-body"><h3>' + e.nombre + '</h3>' +
            '<div class="card-meta">' + meta + '</div></div></a>' + fav + '</div>';
    }

    /* ---------- CU04-B: búsqueda por nombre ---------- */
    var input = document.getElementById('busquedaInput');
    var contResultados = document.getElementById('resultadosBusqueda');
    var secciones = document.getElementById('seccionesPanel');
    var temporizador = null;

    if (input) {
        input.addEventListener('input', function () {
            clearTimeout(temporizador);
            var termino = input.value.trim();
            if (termino === '') {
                contResultados.style.display = 'none';
                secciones.style.display = '';
                return;
            }
            temporizador = setTimeout(function () {
                fetch(BASE + '/api/contenido/buscar?termino=' + encodeURIComponent(termino), {
                    headers: { 'Accept': 'application/json' }
                })
                    .then(function (r) { return r.json(); })
                    .then(function (resultados) { actualizarDOMBusqueda(termino, resultados); });
            }, 300);
        });
    }

    function actualizarDOMBusqueda(termino, resultados) {
        secciones.style.display = 'none';
        contResultados.style.display = '';
        if (resultados.length === 0) {
            // Alterno 8: sin resultados de búsqueda
            contResultados.innerHTML =
                '<div class="section-title"><h2>Resultados para “' + termino + '”</h2></div>' +
                '<div class="alert alert-error">No se encontraron resultados. ' +
                'Verifique la escritura o intente con otro término.</div>';
            return;
        }
        var html = '<div class="section-title"><h2>Resultados para “' + termino + '” (' +
            resultados.length + ')</h2></div><div class="row-grid cols-3">';
        resultados.forEach(function (e) { html += tarjetaHtml(e); });
        contResultados.innerHTML = html + '</div>';
    }

    /* ---------- CU04-C: ver más (expande la sección in-place) ---------- */
    document.querySelectorAll('.btn-ver-mas').forEach(function (btn) {
        btn.addEventListener('click', function (ev) {
            ev.preventDefault();
            var tipo = btn.dataset.tipo;
            fetch(BASE + '/api/contenido/tipo/' + tipo, { headers: { 'Accept': 'application/json' } })
                .then(function (r) { return r.json(); })
                .then(function (lista) { expandirSeccion(tipo, lista, btn); });
        });
    });

    function expandirSeccion(tipo, listaCompleta, btn) {
        var grid = document.getElementById('grid-' + tipo);
        if (!grid) return;
        if (listaCompleta.length === 0) {
            grid.innerHTML = '<p>No hay contenido disponible en ' + NOMBRES_TIPO[tipo] + '.</p>';
        } else {
            var html = '';
            listaCompleta.forEach(function (e) { html += tarjetaHtml(e); });
            grid.innerHTML = html;
        }
        btn.style.display = 'none';
    }
})();
