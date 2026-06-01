<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Registro de Usuario</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/general1.css">
    </head>
    <body style="background-image: url('${pageContext.request.contextPath}/resources/images/Portada.jpeg'); background-size: cover; background-position: center; background-attachment: fixed;">
        <div class="main-container registration-container">
            <h2>Registro de Usuario</h2>

            <!-- Mostrar mensaje de error si existe -->
            <c:if test="${not empty msg}">
                <div class="error-message">${msg}</div>
            </c:if>

            <form action="${pageContext.request.contextPath}/register" method="POST" enctype="multipart/form-data" onsubmit="return checkData(event)">
                <label for="name">Nombre:</label>
                <input type="text" id="name" name="name" required value="${not empty param.name ? param.name : ''}">

               <label for="dni">DNI:</label>
                <input type="text" id="dni" name="dni" required value="${not empty param.dni ? param.dni : ''}">
                <span id="dniError" class="text-danger"></span>

                <label for="email">Email:</label>
                <input type="text" id="email" name="email" required value="${not empty param.email ? param.email : ''}">
                <span id="emailError" class="text-danger"></span>

                <label for="phone">Teléfono:</label>
                <input type="number" id="phone" name="phone" required value="${not empty param.phone ? param.phone : ''}">
                <span id="phoneError" class="text-danger"></span>

                <label for="fechanacimiento">Fecha de Nacimiento:</label>
                <input type="date" id="fechanacimiento" name="fechanacimiento" required value="${not empty param.fechanacimiento ? param.fechanacimiento : ''}">

                <label for="password">Contraseña:</label>
                <input type="password" id="password" name="password" required>

                <label for="confirmPassword">Confirmar Contraseña:</label>
                <input type="password" id="confirmPassword" name="confirmPassword" required>

                <!-- Campo para subir la foto -->
                <label for="photo">Foto de Perfil:</label>
                <input type="file" id="photo" name="photo" accept="image/*">
                <input type="submit" value="Registrar">
            </form>
        </div>
        <script src="${pageContext.request.contextPath}/js/functions.js" charset="UTF-8"></script>
    </body>
</html>
