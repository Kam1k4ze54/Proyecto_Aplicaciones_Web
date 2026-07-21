/* CU05-B: publicar/actualizar evaluación — AJAX contra /api/evaluaciones */
(function () {
    var BASE = '/' + location.pathname.split('/')[1];

    var form = document.getElementById('formEvaluacion');
    if (!form) return;

    form.addEventListener('submit', function (ev) {
        ev.preventDefault();

        var elementoId = parseInt(form.dataset.elementoId, 10);
        var marcada = form.querySelector('input[name="calificacion"]:checked');
        var resena = form.querySelector('#resena').value.trim();
        var aviso = document.getElementById('avisoEvaluacion');

        // Validación en cliente: datos incompletos
        if (!marcada || resena === '') {
            aviso.className = 'alert alert-error';
            aviso.textContent = 'Debe seleccionar una calificación y escribir una reseña antes de publicar.';
            aviso.style.display = '';
            return;
        }

        fetch(BASE + '/api/evaluaciones', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json', 'Accept': 'application/json' },
            body: JSON.stringify({
                elementoId: elementoId,
                tipo: form.dataset.tipo,
                calificacion: parseInt(marcada.value, 10),
                resena: resena
            })
        })
            .then(function (r) { return r.json().then(function (datos) { return { ok: r.ok, datos: datos }; }); })
            .then(function (resp) {
                if (resp.ok) {
                    aviso.className = 'alert alert-success';
                    aviso.textContent = resp.datos.mensaje +
                        ' Nueva calificación promedio: ★ ' + resp.datos.nuevoPromedio.toFixed(1);
                    var promedio = document.getElementById('promedioElemento');
                    if (promedio) {
                        promedio.textContent = '★ ' + resp.datos.nuevoPromedio.toFixed(1);
                    }
                } else {
                    aviso.className = 'alert alert-error';
                    aviso.textContent = resp.datos.mensaje || 'No se pudo publicar la evaluación.';
                }
                aviso.style.display = '';
            });
    });
})();
