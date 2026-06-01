<%@page import="java.io.File"%>
<%@page import="daw.model.Usuario"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    Usuario user = (Usuario) request.getAttribute("usuario");

    // Formatear la fecha de nacimiento
    String fechaNacimientoFormatted = "";
    if (user != null && user.getFechaNacimiento() != null) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        fechaNacimientoFormatted = sdf.format(user.getFechaNacimiento());
    }

    // Ruta base del perfil
    String baseImagePath = application.getRealPath("/resources/photos/");
    String id = user != null ? user.getId().toString() : null;
    String fullPhotoPath = null;

    if (id != null) {
        String potentialPath = baseImagePath + "/US" + id + ".jpg";
        File file = new File(potentialPath);

        if (file.exists() && !file.isDirectory()) {
            // si la imagen existe, asignar su ruta relativa
            fullPhotoPath = request.getContextPath() + "/resources/photos/US" + id + ".jpg";
        } else {
            // Si no existe, usar una imagen predeterminada
            fullPhotoPath = request.getContextPath() + "/resources/images/AvatarPesa.jpg";
        }
    } else {
        fullPhotoPath = request.getContextPath() + "/resources/images/AvatarPesa.jpg";
    }
%>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Modificar Cuenta de Usuario</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/general1.css">
        <script>
            // codigo para cambiar la previsualizacion de la imagen
            function previewImage(event) {
                const input = event.target; // vemos el elemento que genero el evento
                const preview = document.getElementById("profilePreview"); //Lugar donde vamos a visualizar la foto

                if (input.files && input.files[0]) { //Verificamos si se ha seleccionado al menos un archivo
                    const reader = new FileReader();

                    reader.onload = function (e) { // asignamos el valor de la nueva imagen a src para cambiar la foto
                        preview.src = e.target.result; // el src es de la imagen
                    };

                    reader.readAsDataURL(input.files[0]);
                }
            }
        </script>
    </head>
    <body style="background-image: url('${pageContext.request.contextPath}/resources/images/GymGeneral.jpg'); background-size: cover; background-position: center; background-attachment: fixed;">
        <div class="main-container registration-container">

            <h1>Modificar Cuenta de Usuario</h1>

            <!-- Mostrar mensaje de éxito o error -->
            <c:if test="${not empty msg}">
                <div style="color: green; font-weight: bold;">${msg}</div>
            </c:if>

            <c:if test="${usuario != null}">
                <!-- Formulario para mostrar y editar la información del usuario -->
                <form action="${pageContext.request.contextPath}/modificarUsuario" method="POST" enctype="multipart/form-data" onsubmit="return checkData(event)">

                    <!-- Contenedor de imagen de perfil y carga -->
                    <div class="profile-container">
                        <img 
                            id="profilePreview" 
                            src="<%= fullPhotoPath%>" 
                            alt="Previsualización de la foto de perfil">
                        <label for="photo">Cambiar Foto</label>
                        <input type="file" id="photo" name="photo" accept="image/*" onchange="previewImage(event)">
                    </div>

                    <input type="hidden" id="id" name="id" value="${usuario.id}" ><br><br>

                    <label for="name">Nombre:</label>
                    <input type="text" id="name" name="name" value="${usuario.name}" required><br><br>

                    <label for="dni">DNI:</label>
                    <input type="text" id="dni" name="dni" value="${usuario.DNI}" required><br><br>
                    <span id="dniError" class="text-danger"></span>

                    <label for="email">Correo electrónico:</label>
                    <input type="email" id="email" name="email" value="${usuario.email}" required><br><br>
                    <span id="emailError" class="text-danger"></span>

                    <label for="phone">Teléfono:</label>
                    <input type="text" id="phone" name="phone" value="${usuario.telefono}" required><br><br>
                    <span id="phoneError" class="text-danger"></span>

                    <label for="fechanacimiento">Fecha de Nacimiento:</label>
                    <input type="date" id="fechanacimiento" name="fechanacimiento" value="<%= fechaNacimientoFormatted%>" required><br><br>

                    <!-- Obligatorio el uso de la contraseña del administrador -->
                    <label for="adminPassword">Contraseña Personal:</label>
                    <input type="password" id="adminPassword" name="adminPassword" required><br><br>

                    <!-- Campos para la contraseña -->
                    <label for="newPassword">Nueva Contraseña:</label>
                    <input type="password" id="newPassword" name="newPassword"><br><br>

                    <label for="confirmPassword">Confirmar Nueva Contraseña:</label>
                    <input type="password" id="confirmPassword" name="confirmPassword"><br><br>

                    <input type="submit" value="Actualizar datos">
                </form>
            </c:if>
        </div>
        <script src="${pageContext.request.contextPath}/js/functions.js" charset="UTF-8"></script>
    </body>
</html>
