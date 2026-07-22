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

    // Escapa texto que viene del servidor antes de insertarlo como HTML
    // (nombre, sector, etc. podrían contener < > & si el admin los ingresó así).
    function escaparHtml(texto) {
        return String(texto == null ? '' : texto)
            .replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;')
            .replace(/"/g, '&quot;');
    }

    // Tarjeta de un elemento (mismo diseño que las tarjetas del JSP).
    // IMPORTANTE: si cambias el HTML de la card en panelUsuario.jsp,
    // refleja el mismo cambio aquí para no romper la consistencia visual.
    function tarjetaHtml(e) {
        var nombre = escaparHtml(e.nombre);
        var meta = '<span class="rating">★ ' + (e.calificacionPromedio || 0).toFixed(1) + '</span>';
        if (e.sector) {
            meta += '<span class="dot">·</span><span>' + escaparHtml(e.sector) + '</span>';
        }
        if (e.fechaInicio) {
            meta += '<span class="dot">·</span><span>' + escaparHtml(e.fechaInicio) + '</span>';
        }
        var imgHtml = e.urlImagen
            ? '<img src="' + escaparHtml(e.urlImagen) + '" alt="' + nombre + '" class="card-img" loading="lazy" onerror="this.remove();" />'
            : '';
        return '<div class="card">' +
            '<a class="card-link" href="' + BASE + '/DescubrirContenidoController?ruta=detalle&id=' + e.id + '&tipo=' + e.tipo + '">' +
            '<div class="card-thumb ' + (CLASES_THUMB[e.tipo] || '') + '">' + imgHtml +
            (e.destacado ? '<span class="tag">Destacado</span>' : '') + '</div>' +
            '<div class="card-body"><h3>' + nombre + '</h3>' +
            '<div class="card-meta">' + meta + '</div></div></a></div>';
    }

    // Tarjetas "esqueleto" mientras se espera una respuesta del servidor.
    function skeletonHtml(cantidad) {
        var html = '';
        for (var i = 0; i < cantidad; i++) {
            html += '<div class="card skeleton-card" aria-hidden="true">' +
                '<div class="card-thumb skeleton-shimmer"></div>' +
                '<div class="card-body">' +
                '<div class="skeleton-line skeleton-shimmer" style="width:70%;"></div>' +
                '<div class="skeleton-line skeleton-shimmer" style="width:40%;"></div>' +
                '</div></div>';
        }
        return html;
    }

    /* ---------- CU04-B: búsqueda por nombre ---------- */
    var input = document.getElementById('busquedaInput');
    var contResultados = document.getElementById('resultadosBusqueda');
    var secciones = document.getElementById('seccionesPanel');
    var temporizador = null;
    var controladorBusqueda = null; // AbortController de la petición en curso
    var LARGO_MINIMO_BUSQUEDA = 2;

    if (input) {
        input.addEventListener('input', function () {
            clearTimeout(temporizador);
            var termino = input.value.trim();

            // Cancela cualquier búsqueda anterior todavía en vuelo: evita que
            // una respuesta vieja llegue después de una más reciente y la
            // sobrescriba con resultados desactualizados (condición de carrera).
            if (controladorBusqueda) {
                controladorBusqueda.abort();
                controladorBusqueda = null;
            }

            if (termino === '') {
                contResultados.style.display = 'none';
                contResultados.setAttribute('aria-busy', 'false');
                secciones.style.display = '';
                return;
            }

            // Con 1 solo carácter la búsqueda suele ser poco útil y dispara
            // demasiadas peticiones; se espera un mínimo de caracteres.
            if (termino.length < LARGO_MINIMO_BUSQUEDA) {
                return;
            }

            temporizador = setTimeout(function () {
                secciones.style.display = 'none';
                contResultados.style.display = '';
                contResultados.setAttribute('aria-busy', 'true');
                contResultados.innerHTML =
                    '<div class="section-title"><h2>Buscando “' + escaparHtml(termino) + '”…</h2></div>' +
                    '<div class="row-grid cols-3">' + skeletonHtml(3) + '</div>';

                controladorBusqueda = new AbortController();
                fetch(BASE + '/api/contenido/buscar?termino=' + encodeURIComponent(termino), {
                    headers: { 'Accept': 'application/json' },
                    signal: controladorBusqueda.signal
                })
                    .then(function (r) {
                        if (!r.ok) { throw new Error('Respuesta no válida del servidor'); }
                        return r.json();
                    })
                    .then(function (resultados) {
                        contResultados.setAttribute('aria-busy', 'false');
                        actualizarDOMBusqueda(termino, resultados);
                    })
                    .catch(function (err) {
                        if (err && err.name === 'AbortError') { return; } // cancelada a propósito
                        contResultados.setAttribute('aria-busy', 'false');
                        contResultados.innerHTML =
                            '<div class="section-title"><h2>Resultados para “' + escaparHtml(termino) + '”</h2></div>' +
                            '<div class="alert alert-error" role="alert">No se pudo completar la búsqueda por un problema de conexión. ' +
                            '<button type="button" class="btn btn-ghost btn-sm" id="btnReintentarBusqueda">Reintentar</button></div>';
                        window.anunciar && window.anunciar('No se pudo completar la búsqueda.');
                        var btnReintentar = document.getElementById('btnReintentarBusqueda');
                        if (btnReintentar) {
                            btnReintentar.addEventListener('click', function () {
                                input.dispatchEvent(new Event('input'));
                            });
                        }
                    });
            }, 300);
        });
    }

    function actualizarDOMBusqueda(termino, resultados) {
        if (resultados.length === 0) {
            // Alterno 8: sin resultados de búsqueda
            contResultados.innerHTML =
                '<div class="section-title"><h2>Resultados para “' + escaparHtml(termino) + '”</h2></div>' +
                '<div class="alert alert-error" role="alert">No se encontraron resultados. ' +
                'Verifique la escritura o intente con otro término.</div>';
            window.anunciar && window.anunciar('No se encontraron resultados para ' + termino + '.');
            return;
        }
        var html = '<div class="section-title"><h2>Resultados para “' + escaparHtml(termino) + '” (' +
            resultados.length + ')</h2></div><div class="row-grid cols-3">';
        resultados.forEach(function (e) { html += tarjetaHtml(e); });
        contResultados.innerHTML = html + '</div>';
        window.anunciar && window.anunciar(resultados.length + ' resultados encontrados para ' + termino + '.');
    }

    /* ---------- CU04-C: ver más — revela una fila a la vez ---------- */
    var cacheContenido = {};
    var filasMostradas = {};

    // Breakpoints y columnas base EXACTAMENTE sincronizados con estilo.css:
    //   .row-grid            -> 4 columnas (por defecto)
    //   .row-grid.cols-3     -> 3 columnas
    //   .row-grid.cols-2     -> 2 columnas
    //   @media (max-width: 1024px): .row-grid y .row-grid.cols-3 -> 2 columnas
    //   @media (max-width: 640px):  todas las variantes -> 1 columna
    // Si cambias esos breakpoints en el CSS, actualízalos también aquí.
    var BREAKPOINT_TABLET = 1024;
    var BREAKPOINT_MOVIL = 640;

    function columnasBase(grid) {
        if (grid.classList.contains('cols-2')) return 2;
        if (grid.classList.contains('cols-3')) return 3;
        return 4;
    }

    // Reemplaza el cálculo frágil basado en getComputedStyle (que depende del
    // repintado del navegador y puede devolver "none" o un valor inconsistente
    // antes del primer layout) por una consulta directa de media queries,
    // reflejando las mismas reglas que ya existen en el CSS.
    function elementosPorFila(grid) {
        var base = columnasBase(grid);
        if (window.matchMedia('(max-width: ' + BREAKPOINT_MOVIL + 'px)').matches) {
            return 1;
        }
        if (window.matchMedia('(max-width: ' + BREAKPOINT_TABLET + 'px)').matches) {
            return Math.min(base, 2);
        }
        return base;
    }

    function getIdsVisibles(grid) {
        var ids = [];
        grid.querySelectorAll('.card-link').forEach(function (link) {
            var m = link.href.match(/id=(\d+)/);
            if (m) ids.push(parseInt(m[1], 10));
        });
        return ids;
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

        // Fade-in suave de las tarjetas recién insertadas para que el usuario
        // note que se agregó contenido, sin que la página "salte" ni se mueva
        // el scroll.
        var nuevas = Array.prototype.slice.call(grid.querySelectorAll('.card')).slice(-siguientes.length);
        nuevas.forEach(function (tarjeta) { tarjeta.classList.add('card-nueva'); });
        window.requestAnimationFrame(function () {
            nuevas.forEach(function (tarjeta) { tarjeta.classList.add('card-nueva-visible'); });
        });

        window.anunciar && window.anunciar(siguientes.length + ' elementos más cargados en ' + NOMBRES_TIPO[tipo] + '.');

        var idsAhora = getIdsVisibles(grid);
        if (idsAhora.length >= lista.length) {
            btn.style.display = 'none';
        }
    }

    document.querySelectorAll('.btn-ver-mas').forEach(function (btn) {
        var tipo = btn.dataset.tipo;

        btn.addEventListener('click', function (ev) {
            ev.preventDefault();
            if (btn.disabled) return; // evita doble clic mientras carga
            if (cacheContenido[tipo]) {
                revelarSiguienteFila(tipo, btn);
                return;
            }
            var grid = document.getElementById('grid-' + tipo);
            var textoOriginal = btn.textContent;
            btn.disabled = true;
            btn.setAttribute('aria-busy', 'true');
            btn.textContent = 'Cargando…';

            var skeletons = null;
            if (grid) {
                var envoltorio = document.createElement('div');
                envoltorio.innerHTML = skeletonHtml(elementosPorFila(grid));
                skeletons = Array.prototype.slice.call(envoltorio.children);
                skeletons.forEach(function (n) { grid.appendChild(n); });
            }

            fetch(BASE + '/api/contenido/tipo/' + tipo, { headers: { 'Accept': 'application/json' } })
                .then(function (r) {
                    if (!r.ok) { throw new Error('Respuesta no válida del servidor'); }
                    return r.json();
                })
                .then(function (lista) {
                    cacheContenido[tipo] = lista;
                    btn.disabled = false;
                    btn.removeAttribute('aria-busy');
                    btn.textContent = textoOriginal;
                    if (skeletons) skeletons.forEach(function (n) { n.remove(); });
                    revelarSiguienteFila(tipo, btn);
                })
                .catch(function () {
                    btn.disabled = false;
                    btn.removeAttribute('aria-busy');
                    btn.textContent = 'Error al cargar, reintentar';
                    if (skeletons) skeletons.forEach(function (n) { n.remove(); });
                    window.anunciar && window.anunciar('No se pudo cargar más contenido de ' + NOMBRES_TIPO[tipo] + '.');
                });
        });
    });
})();
