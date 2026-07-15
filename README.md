# ProyectoWeb

Proyecto Maven Web (Servlets + JavaBeans + JPA/EclipseLink), desplegado en Tomcat 10/11.

## Requisitos

- JDK 17+
- Maven
- Tomcat 10.x o 11.x instalado en tu máquina (descárgalo de https://tomcat.apache.org/ y descomprímelo donde quieras)
- MySQL corriendo (por ejemplo con XAMPP)

## Setup (una sola vez por máquina)

1. **Clona el repo y entra a la carpeta del proyecto.**

2. **Crea la base de datos** en MySQL (vacía, las tablas las genera EclipseLink solo):
   ```sql
   CREATE DATABASE proyectoweb_db;
   ```
   Si tu MySQL usa otro usuario/contraseña, ajústalo ahí.

3. **Define la variable de entorno `CATALINA_HOME`** apuntando a tu instalación de Tomcat:

   - **Windows (PowerShell, una vez, queda persistente):**
     ```powershell
     [System.Environment]::SetEnvironmentVariable("CATALINA_HOME", "C:\ruta\a\tu\apache-tomcat-10.x", "User")
     ```
     Cierra y vuelve a abrir la terminal después de esto.

   - **Mac/Linux (agrega a `~/.bashrc` o `~/.zshrc`):**
     ```bash
     export CATALINA_HOME=/ruta/a/tu/apache-tomcat-10.x
     ```

## Correr el proyecto

```
mvn package cargo:run
```

Esto compila, empaqueta, y levanta Tomcat con la app desplegada en:

```
http://localhost:8080/ProyectoWeb/
```

Para detenerlo: `Ctrl+C` en la misma terminal.

## Estructura

```
src/main/java/com/proyectoweb/
  modelo/       -> Entidades JPA (JavaBeans)
  dao/          -> Acceso a datos (EclipseLink)
  controller/   -> Servlets (controladores)
  util/         -> Utilidades (JPAUtil: EntityManagerFactory)
src/main/resources/META-INF/persistence.xml  -> config de conexión JPA
src/main/webapp/
  index.jsp
  WEB-INF/web.xml
  WEB-INF/vistas/   -> JSPs (vistas)
```
