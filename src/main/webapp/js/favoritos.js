/* CU06-A (agregar) y CU06-C (eliminar) — AJAX contra /api/favoritos */
(function () {
    var BASE = '/' + location.pathname.split('/')[1];

    function mostrarAviso(texto, esError) {
        var aviso = document.getElementById('avisoFavoritos');
        if (!aviso) return;
        aviso.className = 'alert ' + (esError ? 'alert-error' : 'alert-success');
        aviso.textContent = texto;
        aviso.style.display = '';
    }

    // CU06-A: agregar a favoritos (botón de la ficha de detalle o corazones de tarjetas)
    window.agregarFavorito = function (elementoId, tipo, boton) {
        fetch(BASE + '/api/favoritos', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json', 'Accept': 'application/json' },
            body: JSON.stringify({ elementoId: elementoId, tipo: tipo })
        }).then(function (r) {
            if (r.status === 201) {
                if (boton) {
                    boton.textContent = boton.classList.contains('card-fav') ? '♥' : '♥ En favoritos';
                    boton.classList.add('favorito-activo');
                }
                mostrarAviso('Agregado a favoritos.', false);
            } else if (r.status === 409) {
                // Alterno: elemento ya en favoritos
                mostrarAviso('El elemento ya se encuentra en favoritos.', true);
            } else if (r.status === 400) {
                mostrarAviso('Este tipo de contenido no se puede agregar a favoritos.', true);
            }
        });
    };

    // CU06-C: eliminar de favoritos (con confirmación) y quitar la tarjeta del DOM
    window.eliminarFavorito = function (elementoId, tarjeta) {
        if (!confirm('¿Desea eliminar este elemento de sus favoritos?')) {
            return;
        }
        fetch(BASE + '/api/favoritos/' + elementoId, { method: 'DELETE' })
            .then(function (r) {
                if (r.status === 204) {
                    if (tarjeta) {
                        tarjeta.remove();
                    }
                    mostrarAviso('Elemento eliminado de favoritos.', false);
                    var lista = document.getElementById('gridFavoritos');
                    if (lista && lista.children.length === 0) {
                        document.getElementById('favoritosVacio').style.display = '';
                    }
                }
            });
    };

    // Corazones de las tarjetas (paneles y resultados de búsqueda)
    document.addEventListener('click', function (ev) {
        var btn = ev.target.closest('.card-fav');
        if (btn && btn.dataset.id) {
            ev.preventDefault();
            window.agregarFavorito(parseInt(btn.dataset.id, 10), btn.dataset.tipo, btn);
        }
    });
})();
