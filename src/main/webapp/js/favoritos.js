/* CU06-A (agregar) y CU06-C (eliminar) — AJAX contra /api/favoritos */
(function () {
    var BASE = '/' + location.pathname.split('/')[1];

    // IDs de elementos con una petición de favorito en curso: evita que un
    // doble clic (o clics muy rápidos) dispare dos POST/DELETE simultáneos
    // para el mismo elemento.
    var enVuelo = {};

    function mostrarAviso(texto, esError) {
        var aviso = document.getElementById('avisoFavoritos');
        if (!aviso) return;
        aviso.className = 'alert ' + (esError ? 'alert-error' : 'alert-success');
        aviso.setAttribute('role', esError ? 'alert' : 'status');
        aviso.textContent = texto;
        aviso.style.display = '';
    }

    /* ---------- Modal de confirmación reutilizable ----------
       Sustituye a confirm() nativo del navegador para mantener el mismo
       estilo visual que el resto de la aplicación (usa las mismas clases
       .modal-overlay/.modal-box que ya existen en perfil.jsp y
       seleccionPreferencias.jsp). Se construye una sola vez y se reutiliza. */
    var modal = null;
    function obtenerModal() {
        if (modal) return modal;
        var overlay = document.createElement('div');
        overlay.className = 'modal-overlay';
        overlay.style.display = 'none';
        overlay.innerHTML =
            '<div class="modal-box" role="alertdialog" aria-modal="true" aria-labelledby="modalConfirmTitulo" aria-describedby="modalConfirmTexto">' +
            '<h3 id="modalConfirmTitulo"></h3>' +
            '<p id="modalConfirmTexto"></p>' +
            '<div class="form-actions">' +
            '<button type="button" class="btn btn-ghost" data-accion="cancelar">Cancelar</button>' +
            '<button type="button" class="btn btn-peligro" data-accion="confirmar">Eliminar</button>' +
            '</div></div>';
        document.body.appendChild(overlay);
        modal = overlay;
        return overlay;
    }

    // Devuelve una Promise<boolean>: true si el usuario confirma, false si cancela.
    function confirmarAccion(titulo, texto) {
        var overlay = obtenerModal();
        var elementoActivo = document.activeElement;
        overlay.querySelector('#modalConfirmTitulo').textContent = titulo;
        overlay.querySelector('#modalConfirmTexto').textContent = texto;
        overlay.style.display = 'flex';

        var btnConfirmar = overlay.querySelector('[data-accion="confirmar"]');
        var btnCancelar = overlay.querySelector('[data-accion="cancelar"]');
        btnCancelar.focus();

        return new Promise(function (resolve) {
            function cerrar(resultado) {
                overlay.style.display = 'none';
                btnConfirmar.removeEventListener('click', onConfirmar);
                btnCancelar.removeEventListener('click', onCancelar);
                overlay.removeEventListener('click', onOverlay);
                document.removeEventListener('keydown', onTecla);
                if (elementoActivo && elementoActivo.focus) elementoActivo.focus();
                resolve(resultado);
            }
            function onConfirmar() { cerrar(true); }
            function onCancelar() { cerrar(false); }
            function onOverlay(ev) { if (ev.target === overlay) cerrar(false); }
            function onTecla(ev) { if (ev.key === 'Escape') cerrar(false); }

            btnConfirmar.addEventListener('click', onConfirmar);
            btnCancelar.addEventListener('click', onCancelar);
            overlay.addEventListener('click', onOverlay);
            document.addEventListener('keydown', onTecla);
        });
    }

    // Aplica/retira el estado visual de "cargando" a un botón sin perder su
    // contenido original, para poder restaurarlo si la petición falla.
    function marcarCargando(boton, cargando, textoCargando) {
        if (!boton) return;
        if (cargando) {
            boton.dataset.textoOriginal = boton.textContent;
            boton.disabled = true;
            boton.setAttribute('aria-busy', 'true');
            boton.classList.add('is-cargando');
            if (textoCargando) boton.textContent = textoCargando;
        } else {
            boton.disabled = false;
            boton.removeAttribute('aria-busy');
            boton.classList.remove('is-cargando');
            if (boton.dataset.textoOriginal !== undefined) {
                boton.textContent = boton.dataset.textoOriginal;
                delete boton.dataset.textoOriginal;
            }
        }
    }

    // Aplica el estado visual "favorito"/"no favorito" a un botón
    function aplicarEstadoFavorito(boton, activo, esCorazon) {
        if (!boton) return;
        boton.classList.toggle('favorito-activo', activo);
        boton.setAttribute('aria-pressed', activo ? 'true' : 'false');
        if (esCorazon) {
            boton.textContent = activo ? '♥' : '♡';
            boton.title = activo ? 'Quitar de favoritos' : 'Agregar a favoritos';
            boton.setAttribute('aria-label', activo ? 'Quitar de favoritos' : 'Agregar a favoritos');
        } else {
            boton.textContent = activo ? '♥ En favoritos' : '♡ Agregar a favoritos';
        }
    }

    // Quita un favorito sin modal: se usa cuando se pulsa un corazón ya activo
    function quitarFavoritoDirecto(elementoId, boton, esCorazon) {
        if (enVuelo[elementoId]) return;
        enVuelo[elementoId] = true;
        marcarCargando(boton, true, esCorazon ? '…' : 'Quitando…');

        fetch(BASE + '/api/favoritos/' + elementoId, { method: 'DELETE' })
            .then(function (r) {
                delete enVuelo[elementoId];
                marcarCargando(boton, false);
                if (r.status === 204 || r.status === 404) {
                    aplicarEstadoFavorito(boton, false, esCorazon);
                    mostrarAviso('Elemento eliminado de favoritos.', false);
                    window.anunciar && window.anunciar('Elemento eliminado de favoritos.');
                } else {
                    mostrarAviso('No se pudo eliminar el elemento. Intenta de nuevo.', true);
                }
            })
            .catch(function () {
                delete enVuelo[elementoId];
                marcarCargando(boton, false);
                mostrarAviso('No se pudo conectar con el servidor. Verifica tu conexión e intenta de nuevo.', true);
                window.anunciar && window.anunciar('Error de conexión al eliminar de favoritos.');
            });
    }

    // CU06-A / CU06-C combinados: agrega o quita según el estado actual del botón
    window.agregarFavorito = function (elementoId, tipo, boton) {
        if (enVuelo[elementoId]) return;

        var esCorazon = boton && boton.classList.contains('card-fav');
        var yaEsFavorito = boton && boton.classList.contains('favorito-activo');

        if (yaEsFavorito) {
            quitarFavoritoDirecto(elementoId, boton, esCorazon);
            return;
        }

        enVuelo[elementoId] = true;
        marcarCargando(boton, true, esCorazon ? '…' : 'Agregando…');

        fetch(BASE + '/api/favoritos', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json', 'Accept': 'application/json' },
            body: JSON.stringify({ elementoId: elementoId, tipo: tipo })
        })
            .then(function (r) {
                delete enVuelo[elementoId];
                if (r.status === 201) {
                    marcarCargando(boton, false);
                    aplicarEstadoFavorito(boton, true, esCorazon);
                    mostrarAviso('Agregado a favoritos.', false);
                    window.anunciar && window.anunciar('Elemento agregado a favoritos.');
                } else if (r.status === 409) {
                    marcarCargando(boton, false);
                    aplicarEstadoFavorito(boton, true, esCorazon);
                    mostrarAviso('Ese elemento ya estaba en tus favoritos.', false);
                } else if (r.status === 400) {
                    marcarCargando(boton, false);
                    mostrarAviso('Este tipo de contenido no se puede agregar a favoritos.', true);
                } else {
                    marcarCargando(boton, false);
                    mostrarAviso('No se pudo agregar a favoritos. Intenta de nuevo.', true);
                }
            })
            .catch(function () {
                delete enVuelo[elementoId];
                marcarCargando(boton, false);
                mostrarAviso('No se pudo conectar con el servidor. Verifica tu conexión e intenta de nuevo.', true);
                window.anunciar && window.anunciar('Error de conexión al agregar a favoritos.');
            });
    };

    // CU06-C: eliminar de favoritos (con confirmación) y quitar la tarjeta del DOM
    window.eliminarFavorito = function (elementoId, tarjeta, boton) {
        if (enVuelo[elementoId]) return;

        confirmarAccion(
            '¿Eliminar de favoritos?',
            '¿Deseas eliminar este elemento de tus favoritos? Esta acción no se puede deshacer.'
        ).then(function (confirmado) {
            if (!confirmado) return;

            enVuelo[elementoId] = true;
            marcarCargando(boton, true, 'Eliminando…');

            fetch(BASE + '/api/favoritos/' + elementoId, { method: 'DELETE' })
                .then(function (r) {
                    delete enVuelo[elementoId];
                    if (r.status === 204) {
                        if (tarjeta) {
                            tarjeta.classList.add('card-saliendo');
                            window.setTimeout(function () { tarjeta.remove(); }, 150);
                        }
                        mostrarAviso('Elemento eliminado de favoritos.', false);
                        window.anunciar && window.anunciar('Elemento eliminado de favoritos.');
                        window.setTimeout(function () {
                            var lista = document.getElementById('gridFavoritos');
                            if (lista && lista.children.length === 0) {
                                var vacio = document.getElementById('favoritosVacio');
                                if (vacio) vacio.style.display = '';
                            }
                        }, 160);
                    } else {
                        marcarCargando(boton, false);
                        mostrarAviso('No se pudo eliminar el elemento. Intenta de nuevo.', true);
                    }
                })
                .catch(function () {
                    delete enVuelo[elementoId];
                    marcarCargando(boton, false);
                    mostrarAviso('No se pudo conectar con el servidor. Verifica tu conexión e intenta de nuevo.', true);
                    window.anunciar && window.anunciar('Error de conexión al eliminar de favoritos.');
                });
        });
    };

    // Corazones de las tarjetas (paneles y resultados de búsqueda)
    document.addEventListener('click', function (ev) {
        var btn = ev.target.closest('.card-fav');
        if (btn && btn.dataset.id && !btn.disabled) {
            ev.preventDefault();
            window.agregarFavorito(parseInt(btn.dataset.id, 10), btn.dataset.tipo, btn);
        }
    });

    // Botones "Eliminar" de la vista de favoritos (listaFavoritos.jsp) —
    // delegado por data-attributes en vez de onclick inline en el HTML,
    // igual que el resto de interacciones de esta página.
    document.addEventListener('click', function (ev) {
        var btn = ev.target.closest('.btn-eliminar-favorito');
        if (btn && btn.dataset.id && !btn.disabled) {
            ev.preventDefault();
            var tarjeta = document.getElementById('fav-' + btn.dataset.id);
            window.eliminarFavorito(parseInt(btn.dataset.id, 10), tarjeta, btn);
        }
    });
})();
