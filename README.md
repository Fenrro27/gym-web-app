# Gym Web App (DAW)

Proyecto de la asignatura **Diseño de Aplicaciones Web (DAW)**. Se trata de una aplicación web para la gestión de un gimnasio, con conexión a base de datos.

## 🛠️ Tecnologías utilizadas
* **Java (Jakarta EE):** Lógica del backend de la aplicación (Servlets, JPA, etc).
* **GlassFish 7 (OmniFish):** Servidor de aplicaciones web.
* **Apache Derby (JavaDB):** Base de datos relacional integrada.
* **Docker & Docker Compose:** Contenedorización del entorno completo (Aplicación + Base de Datos).

## 🚀 Cómo desplegar el proyecto

El proyecto está preparado para ejecutarse en un único contenedor de Docker que se encarga de compilar el código, levantar la base de datos y desplegar el servidor web. No necesitas tener instalado Java ni NetBeans localmente, solo tener Docker funcionando.

1. Abre una terminal en la carpeta raíz del proyecto.
2. Ejecuta el siguiente comando para construir la imagen y levantar el contenedor en segundo plano:
   ```bash
   docker-compose up -d --build
   ```
3. Docker usará un *multi-stage build* para descargar el entorno, compilar los archivos fuente, generar el archivo `.war` internamente y desplegarlo en GlassFish de manera automática.

## 🔗 Accesos y Credenciales

Una vez que el contenedor esté en marcha (puede tardar unos 15-20 segundos la primera vez en inicializar la base de datos y el servidor), puedes acceder a los siguientes servicios a través de tu navegador:

### 1. Aplicación Web
* **URL:** [http://localhost:8080/gym](http://localhost:8080/gym)
* **Descripción:** Interfaz principal de la aplicación del gimnasio (definida en el `glassfish-web.xml`).

### 2. Panel de Administración de GlassFish
* **URL:** [http://localhost:4848](http://localhost:4848)
* **Usuario:** `admin`
* **Contraseña:** `admin`
* **Descripción:** Consola administrativa para revisar los *connection pools*, recursos JDBC y despliegues del servidor.

### 3. Base de Datos (Apache Derby)
* **URL JDBC:** `jdbc:derby://localhost:1527/gym_db`
* **Usuario:** `app`
* **Contraseña:** `123`

### 4. Usuarios de prueba (Web)
Para acceder a la aplicación en `https://localhost:8181/gym/`, puedes usar alguna de estas cuentas preconfiguradas:
* **Administrador:** Nombre: `admin` / Correo: `admin` / Contraseña: `admin`
* **Administrador:** Nombre: `Francisco Vazquez` / Correo: `FV@gmail.com` / Contraseña: `admin`
* **Usuario Estándar:** Nombre: `Usuario1` / Correo: `usuario1@gmail.com` / Contraseña: `admin`
* **Usuario Estándar:** Nombre: `lola` / Correo: `lola@gmail.com` / Contraseña: `test`

## 🗄️ Persistencia de Datos
La base de datos (Derby) se inicializa sola cuando arranca el contenedor gracias a la configuración de JPA (`drop-and-create` en `persistence.xml`) y se nutre con los datos iniciales (usuarios, actividades, etc.) mediante el script `runSQL_Init.sql`. 

Los datos de la base de datos persistirán de forma segura en tu equipo local a través de un volumen de Docker llamado `derby_data`, por lo que no perderás información si reinicias el contenedor.