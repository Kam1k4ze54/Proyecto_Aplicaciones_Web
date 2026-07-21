INSERT INTO usuario (nombres, apellidos, correo, contrasena, estado, rol) VALUES
('Admin', 'Principal', 'admin@quitodescubre.ec', 'admin123', 1, 'administrador'),
('Maria', 'Perez', 'maria@correo.com', 'usuario123', 1, 'usuario');

INSERT INTO categoria (nombre, descripcion, tipo) VALUES
('Historico', 'Patrimonio e historia de la ciudad', 'Turistica'),
('Naturaleza', 'Parques, miradores y aire libre', 'Turistica'),
('Aventura', 'Actividades de adrenalina', 'Turistica'),
('Comida tipica', 'Sabores tradicionales quitenos', 'Gastronomica'),
('Cafeterias', 'Cafe de especialidad y postres', 'Gastronomica'),
('Restaurantes', 'Cocina nacional e internacional', 'Gastronomica'),
('Musica', 'Conciertos y festivales musicales', 'Cultural'),
('Tradiciones', 'Fiestas populares y religiosas', 'Cultural'),
('Arte', 'Exposiciones y teatro', 'Cultural');

INSERT INTO elemento_contenido (tipo, nombre, descripcion, sector, calificacion_promedio, destacado, activo, categoria_id, horario) VALUES
('LugarTuristico', 'Centro Historico', 'El casco colonial mejor conservado de America Latina, Patrimonio de la Humanidad.', 'Centro', 4.8, 1, 1, (SELECT id FROM categoria WHERE nombre='Historico' AND tipo='Turistica' LIMIT 1), 'Lun-Dom 08:00-18:00'),
('LugarTuristico', 'Mitad del Mundo', 'Monumento y museo sobre la linea ecuatorial.', 'San Antonio', 4.6, 1, 1, (SELECT id FROM categoria WHERE nombre='Historico' AND tipo='Turistica' LIMIT 1), 'Lun-Dom 09:00-18:00'),
('LugarTuristico', 'Teleferico de Quito', 'Ascenso al volcan Pichincha con vistas panoramicas de la ciudad.', 'Cruz Loma', 4.7, 0, 1, (SELECT id FROM categoria WHERE nombre='Aventura' AND tipo='Turistica' LIMIT 1), 'Lun-Dom 09:00-17:00'),
('LugarTuristico', 'Parque La Carolina', 'El pulmon verde mas visitado de la ciudad moderna.', 'La Carolina', 4.4, 0, 1, (SELECT id FROM categoria WHERE nombre='Naturaleza' AND tipo='Turistica' LIMIT 1), 'Lun-Dom 05:00-19:00');

INSERT INTO elemento_contenido (tipo, nombre, descripcion, sector, calificacion_promedio, destacado, activo, categoria_id, especialidad, horario) VALUES
('EstablecimientoGastronomico', 'Hornado Dona Rosa', 'Hornado tradicional con tortillas de papa y agrio.', 'Mercado Central', 4.7, 1, 1, (SELECT id FROM categoria WHERE nombre='Comida tipica' AND tipo='Gastronomica' LIMIT 1), 'Hornado quiteno', 'Mar-Dom 08:00-16:00'),
('EstablecimientoGastronomico', 'Cafe de la Ronda', 'Canelazos, empanadas de viento y musica en vivo en la calle La Ronda.', 'La Ronda', 4.5, 0, 1, (SELECT id FROM categoria WHERE nombre='Cafeterias' AND tipo='Gastronomica' LIMIT 1), 'Canelazo y empanadas', 'Mie-Dom 16:00-23:00'),
('EstablecimientoGastronomico', 'Fritada de la Floresta', 'Fritada, mote y maduro frito al estilo tradicional.', 'La Floresta', 4.3, 0, 1, (SELECT id FROM categoria WHERE nombre='Comida tipica' AND tipo='Gastronomica' LIMIT 1), 'Fritada', 'Lun-Dom 11:00-20:00');

INSERT INTO elemento_contenido (tipo, nombre, descripcion, sector, calificacion_promedio, destacado, activo, categoria_id, fecha_inicio, fecha_fin) VALUES
('Evento', 'Fiesta de la Virgen del Quinche', 'Peregrinacion y festividades religiosas tradicionales.', 'El Quinche', 4.9, 1, 1, (SELECT id FROM categoria WHERE nombre='Tradiciones' AND tipo='Cultural' LIMIT 1), '2026-11-15', '2026-11-21'),
('Evento', 'Concierto de la Orquesta Sinfonica', 'Temporada de conciertos en el Teatro Sucre.', 'Teatro Sucre', 4.8, 0, 1, (SELECT id FROM categoria WHERE nombre='Musica' AND tipo='Cultural' LIMIT 1), '2026-08-18', '2026-08-18'),
('Evento', 'Festival de la Luz', 'Proyecciones artisticas sobre las fachadas del Centro Historico.', 'Centro Historico', 4.9, 1, 1, (SELECT id FROM categoria WHERE nombre='Arte' AND tipo='Cultural' LIMIT 1), '2026-08-09', '2026-08-13');
