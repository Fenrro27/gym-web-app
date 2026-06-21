<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Login</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="/gym/css/general1.css"> <!-- Vincula el archivo CSS -->
    </head>
    <body style="background-image: url('${pageContext.request.contextPath}/resources/images/Portada.jpeg'); background-size: cover; background-position: center; background-attachment: fixed;">
        <div class="main-container login-container">
            <h2>Iniciar Sesión</h2>

            <!-- Mostrar mensaje de error si existe -->
            <c:if test="${not empty msg}">
                <div class="error-message">${msg}</div>
            </c:if>

            <form action="${pageContext.request.contextPath}/login" method="POST">
                <label for="email">Email:</label>
                <input type="text" id="email" name="email" required>

                <label for="password">Contraseña:</label>
                <input type="password" id="password" name="password" required>

                <input type="submit" value="Iniciar Sesión">
            </form>

        </div>
    </body>
</html>
