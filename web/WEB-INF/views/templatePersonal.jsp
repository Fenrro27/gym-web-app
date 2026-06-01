<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Aplicación Web</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/general1.css">
      
    </head>
    <body>
        <header>
            <h1>Gimnasio UHU</h1>
        </header>

        <!-- Menú de navegación -->
        <nav>
            <ul>
                <li>
                    <a href="#">Actividades</a> <!-- Encabezado del desplegable -->
                    <ul>
                        <li><a href="${pageContext.request.contextPath}/catalogoActividades">Catálogo de Actividades</a></li>
                        <li><a href="${pageContext.request.contextPath}/crearActividad">Crear Actividad</a></li>
                        <li><a href="${pageContext.request.contextPath}/actividadesInscritas">Actividades Inscritas</a></li>
                    </ul>
                </li>
                <li><a href="${pageContext.request.contextPath}/cuentasUsuarios">Cuentas de Usuarios</a></li>
                <li><a href="${pageContext.request.contextPath}/registerUser">Registrar</a></li>
                <li><a href="${pageContext.request.contextPath}/carrito">Carrito</a></li>
                <li><a href="${pageContext.request.contextPath}/miCuenta">Mi Cuenta</a></li>
                <li><a href="${pageContext.request.contextPath}/logout">Logout</a></li>
            </ul>
        </nav>

        <!-- Contenido dinámico -->
        <main>
            <jsp:include page="${requestScope.vista}.jsp" />
        </main>

        <footer>
            &copy; 2024. Diseño de Aplicaciones Web - Universidad de Huelva.
        </footer>
    </body>
</html>
