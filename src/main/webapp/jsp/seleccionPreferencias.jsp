<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8"><title>Seleccionar Preferencias</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Piazzolla:wght@500;600;700&family=Manrope:wght@400;600;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilo.css?v=2">
</head>
<body>
    <header class="app-header">
        <div class="container">
            <div class="brand">
                <div class="brand-mark">Q</div>
                <div class="brand-name">Quito Descubre</div>
            </div>
        </div>
    </header>
    <main>
        <div class="container pref-shell">
            <h1>Selecciona tus preferencias</h1>
            <p>Elige las categorías que más te interesan para personalizar tus recomendaciones.</p>

            <form method="POST" action="${pageContext.request.contextPath}/RegistrarseController?ruta=guardarPreferencias" id="formPreferencias">
                <div class="pref-grid">
                    <c:forEach var="cat" items="${categorias}">
                        <label class="pref-chip">
                            <input type="checkbox" name="preferencias" value="${cat.id}" />
                            <span class="cat-name">${cat.nombre}</span>
                            <span class="cat-type">${cat.tipo}</span>
                        </label>
                    </c:forEach>
                </div>
                <button type="submit" class="btn btn-primary">Guardar preferencias</button>
            </form>
        </div>
    </main>
<div class="modal-overlay" id="modalPreferencias" style="display:none;">
  <div class="modal-box">
    <h3>Selecciona al menos una preferencia</h3>
    <p>Debes marcar al menos una categoría para continuar y personalizar tus recomendaciones.</p>
    <div class="form-actions">
      <button type="button" class="btn btn-primary" id="btnCerrarModalPref">Entendido</button>
    </div>
  </div>
</div>

<script>
(function () {
  var modal = document.getElementById('modalPreferencias');
  var form = document.getElementById('formPreferencias');

  function abrirModal() { modal.style.display = 'flex'; }
  function cerrarModal() { modal.style.display = 'none'; }

  document.getElementById('btnCerrarModalPref').addEventListener('click', cerrarModal);
  modal.addEventListener('click', function (ev) { if (ev.target === modal) cerrarModal(); });

  <c:if test="${not empty mensajeError}">
  abrirModal();
  </c:if>

  form.addEventListener('submit', function (ev) {
    if (form.querySelectorAll('input[name="preferencias"]:checked').length === 0) {
      ev.preventDefault();
      abrirModal();
    }
  });
})();
</script>
</body>
</html>
