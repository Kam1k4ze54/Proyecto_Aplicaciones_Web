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
        var imgHtml = e.urlImagen
            ? '<img src="' + e.urlImagen + '" alt="' + e.nombre + '" class="card-img" loading="lazy" onerror="this.remove();" />'
            : '';
        return '<div class="card">' +
            '<a class="card-link" href="' + BASE + '/DescubrirContenidoController?ruta=detalle&id=' + e.id + '&tipo=' + e.tipo + '">' +
            '<div class="card-thumb ' + (CLASES_THUMB[e.tipo] || '') + '">' + imgHtml +
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

    /* ---------- CU04-C: ver más — revela una fila a la vez ---------- */
    var cacheContenido = {};
    var filasMostradas = {};

    function getIdsVisibles(grid) {
        var ids = [];
        grid.querySelectorAll('.card-link').forEach(function (link) {
            var m = link.href.match(/id=(\d+)/);
            if (m) ids.push(parseInt(m[1], 10));
        });
        return ids;
    }

    function elementosPorFila(grid) {
        var cols = getComputedStyle(grid).gridTemplateColumns.split(' ').length;
        return cols > 0 ? cols : 3;
    }

    function revelarSiguienteFila(tipo, btn) {
        var grid = document.getElementById('grid-' + tipo);
        if (!grid) return;
        var lista = cacheContenido[tipo] || [];
        var porFila = elementosPorFila(grid);

        var idsVisibles = getIdsVisibles(grid);
        var noMostrados = lista.filter(function (e) { return idsVisibles.indexOf(e.id) === -1; });

        filasMostradas[tipo] = (filasMostradas[tipo] || 1) + 1;
        var total = porFila * filasMostradas[tipo];
        var siguientes = noMostrados.slice(0, total - idsVisibles.length);

        if (siguientes.length === 0 && idsVisibles.length === 0) {
            grid.innerHTML = '<p>No hay contenido disponible en ' + NOMBRES_TIPO[tipo] + '.</p>';
            btn.style.display = 'none';
            return;
        }

        var html = '';
        siguientes.forEach(function (e) { html += tarjetaHtml(e); });
        grid.insertAdjacentHTML('beforeend', html);

        var idsAhora = getIdsVisibles(grid);
        if (idsAhora.length >= lista.length) {
            btn.style.display = 'none';
        }
    }

    document.querySelectorAll('.btn-ver-mas').forEach(function (btn) {
        var tipo = btn.dataset.tipo;

        btn.addEventListener('click', function (ev) {
            ev.preventDefault();
            if (cacheContenido[tipo]) {
                revelarSiguienteFila(tipo, btn);
                return;
            }
            var textoOriginal = btn.textContent;
            btn.disabled = true;
            btn.textContent = 'Cargando…';
            fetch(BASE + '/api/contenido/tipo/' + tipo, { headers: { 'Accept': 'application/json' } })
                .then(function (r) { return r.json(); })
                .then(function (lista) {
                    cacheContenido[tipo] = lista;
                    btn.disabled = false;
                    btn.textContent = textoOriginal;
                    revelarSiguienteFila(tipo, btn);
                })
                .catch(function () {
                    btn.disabled = false;
                    btn.textContent = 'Error al cargar, reintentar';
                });
        });
    });
})();
