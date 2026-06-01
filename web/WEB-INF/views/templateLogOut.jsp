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
                <li><a href="${pageContext.request.contextPath}/catalogoActividades">Catálogo Actividades</a></li>
                <li><a href="${pageContext.request.contextPath}/login">Login</a></li>
                <li><a href="${pageContext.request.contextPath}/register">Registrarse</a></li>
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
