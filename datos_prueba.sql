-- ============================================================
-- Datos de prueba — Quito Descubre
-- Ejecutar DESPUÉS de arrancar la app una vez (EclipseLink crea
-- las tablas con ddl-generation=create-or-extend-tables).
--   mysql -u root --default-character-set=utf8mb4 proyectoweb_db < datos_prueba.sql
-- Nota: los subqueries filtran también por tipo y usan LIMIT 1 porque
-- la base puede tener categorías antiguas con nombres parecidos
-- (la colación de MySQL no distingue mayúsculas ni acentos).
-- ============================================================
SET NAMES utf8mb4;

USE proyectoweb_db;

-- Administrador (contraseña: admin123) y un usuario de prueba (usuario123)
INSERT INTO
    usuario (
        nombres,
        apellidos,
        correo,
        contrasena,
        estado,
        rol
    )
VALUES (
        'Admin',
        'Principal',
        'admin@quitodescubre.ec',
        'admin123',
        1,
        'administrador'
    ),
    (
        'María',
        'Pérez',
        'maria@correo.com',
        'usuario123',
        1,
        'usuario'
    );

-- Categorías (tipo controla en qué formulario del admin aparecen)
INSERT INTO
    categoria (nombre, descripcion, tipo)
VALUES (
        'Histórico',
        'Patrimonio e historia de la ciudad',
        'Turística'
    ),
    (
        'Naturaleza',
        'Parques, miradores y aire libre',
        'Turística'
    ),
    (
        'Aventura',
        'Actividades de adrenalina',
        'Turística'
    ),
    (
        'Comida típica',
        'Sabores tradicionales quiteños',
        'Gastronómica'
    ),
    (
        'Cafeterías',
        'Café de especialidad y postres',
        'Gastronómica'
    ),
    (
        'Restaurantes',
        'Cocina nacional e internacional',
        'Gastronómica'
    ),
    (
        'Música',
        'Conciertos y festivales musicales',
        'Cultural'
    ),
    (
        'Tradiciones',
        'Fiestas populares y religiosas',
        'Cultural'
    ),
    (
        'Arte',
        'Exposiciones y teatro',
        'Cultural'
    );

-- Lugares turísticos
INSERT INTO
    elemento_contenido (
        tipo,
        nombre,
        descripcion,
        sector,
        calificacion_promedio,
        destacado,
        activo,
        categoria_id,
        horario
    )
VALUES (
        'LugarTuristico',
        'Centro Histórico',
        'El casco colonial mejor conservado de América Latina, Patrimonio de la Humanidad.',
        'Centro',
        4.8,
        1,
        1,
        (
            SELECT id
            FROM categoria
            WHERE
                nombre = 'Histórico'
                AND tipo = 'Turística'
            LIMIT 1
        ),
        'Lun-Dom 08:00-18:00'
    ),
    (
        'LugarTuristico',
        'Mitad del Mundo',
        'Monumento y museo sobre la línea ecuatorial.',
        'San Antonio',
        4.6,
        1,
        1,
        (
            SELECT id
            FROM categoria
            WHERE
                nombre = 'Histórico'
                AND tipo = 'Turística'
            LIMIT 1
        ),
        'Lun-Dom 09:00-18:00'
    ),
    (
        'LugarTuristico',
        'Teleférico de Quito',
        'Ascenso al volcán Pichincha con vistas panorámicas de la ciudad.',
        'Cruz Loma',
        4.7,
        0,
        1,
        (
            SELECT id
            FROM categoria
            WHERE
                nombre = 'Aventura'
                AND tipo = 'Turística'
            LIMIT 1
        ),
        'Lun-Dom 09:00-17:00'
    ),
    (
        'LugarTuristico',
        'Parque La Carolina',
        'El pulmón verde más visitado de la ciudad moderna.',
        'La Carolina',
        4.4,
        0,
        1,
        (
            SELECT id
            FROM categoria
            WHERE
                nombre = 'Naturaleza'
                AND tipo = 'Turística'
            LIMIT 1
        ),
        'Lun-Dom 05:00-19:00'
    );

-- Establecimientos gastronómicos
INSERT INTO
    elemento_contenido (
        tipo,
        nombre,
        descripcion,
        sector,
        calificacion_promedio,
        destacado,
        activo,
        categoria_id,
        especialidad,
        horario
    )
VALUES (
        'EstablecimientoGastronomico',
        'Hornado Doña Rosa',
        'Hornado tradicional con tortillas de papa y agrio.',
        'Mercado Central',
        4.7,
        1,
        1,
        (
            SELECT id
            FROM categoria
            WHERE
                nombre = 'Comida típica'
                AND tipo = 'Gastronómica'
            LIMIT 1
        ),
        'Hornado quiteño',
        'Mar-Dom 08:00-16:00'
    ),
    (
        'EstablecimientoGastronomico',
        'Café de la Ronda',
        'Canelazos, empanadas de viento y música en vivo en la calle La Ronda.',
        'La Ronda',
        4.5,
        0,
        1,
        (
            SELECT id
            FROM categoria
            WHERE
                nombre = 'Cafeterías'
                AND tipo = 'Gastronómica'
            LIMIT 1
        ),
        'Canelazo y empanadas',
        'Mié-Dom 16:00-23:00'
    ),
    (
        'EstablecimientoGastronomico',
        'Fritada de la Floresta',
        'Fritada, mote y maduro frito al estilo tradicional.',
        'La Floresta',
        4.3,
        0,
        1,
        (
            SELECT id
            FROM categoria
            WHERE
                nombre = 'Comida típica'
                AND tipo = 'Gastronómica'
            LIMIT 1
        ),
        'Fritada',
        'Lun-Dom 11:00-20:00'
    );

-- Eventos
INSERT INTO
    elemento_contenido (
        tipo,
        nombre,
        descripcion,
        sector,
        calificacion_promedio,
        destacado,
        activo,
        categoria_id,
        fecha_inicio,
        fecha_fin
    )
VALUES (
        'Evento',
        'Fiesta de la Virgen del Quinche',
        'Peregrinación y festividades religiosas tradicionales.',
        'El Quinche',
        4.9,
        1,
        1,
        (
            SELECT id
            FROM categoria
            WHERE
                nombre = 'Tradiciones'
                AND tipo = 'Cultural'
            LIMIT 1
        ),
        '2026-11-15',
        '2026-11-21'
    ),
    (
        'Evento',
        'Concierto de la Orquesta Sinfónica',
        'Temporada de conciertos en el Teatro Sucre.',
        'Teatro Sucre',
        4.8,
        0,
        1,
        (
            SELECT id
            FROM categoria
            WHERE
                nombre = 'Música'
                AND tipo = 'Cultural'
            LIMIT 1
        ),
        '2026-08-18',
        '2026-08-18'
    ),
    (
        'Evento',
        'Festival de la Luz',
        'Proyecciones artísticas sobre las fachadas del Centro Histórico.',
        'Centro Histórico',
        4.9,
        1,
        1,
        (
            SELECT id
            FROM categoria
            WHERE
                nombre = 'Arte'
                AND tipo = 'Cultural'
            LIMIT 1
        ),
        '2026-08-09',
        '2026-08-13'
    );