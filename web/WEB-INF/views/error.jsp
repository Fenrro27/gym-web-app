<%@ page isErrorPage="true" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Error</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/general1.css">
    </head>
    <body style="background-image: url('${pageContext.request.contextPath}/resources/images/GymGeneral.jpg'); background-size: cover; background-position: center; background-attachment: fixed;">

        <div class="error-container">
            <h2>Ha ocurrido un error</h2>
            <p><strong>Descripción del error:</strong></p>
            <p class="error-message">${requestScope.msg}</p> <!-- Aquí se muestra el mensaje de error -->
        
        </div>
    </body>
</html>
