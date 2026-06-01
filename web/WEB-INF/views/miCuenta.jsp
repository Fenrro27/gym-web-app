<%@page import="java.io.File"%>
<%@page import="daw.model.Usuario"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>

<%
    Usuario user = (Usuario) request.getAttribute("usuario");

    String fechaNacimientoFormatted = "";
    if (user != null && user.getFechaNacimiento() != null) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        fechaNacimientoFormatted = sdf.format(user.getFechaNacimiento());
    }

    String baseImagePath = application.getRealPath("/resources/photos/");
    String id = user != null ? user.getId().toString() : null;
    String fullPhotoPath = null;

    if (id != null) {
        String potentialPath = baseImagePath + "/US" + id + ".jpg";
        File file = new File(potentialPath);

        if (file.exists() && !file.isDirectory()) {
            // Si la imagen existe, asignar su ruta relativa
            fullPhotoPath = request.getContextPath() + "/resources/photos/US" + id + ".jpg";
        } else {
            // Si no existe, usar una imagen predeterminada
            fullPhotoPath = request.getContextPath() + "/resources/images/AvatarPesa.jpg";
        }
        // System.out.println("path: " + fullPhotoPath);
    }
%>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Mi Cuenta</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/general1.css">
        <script>
            // codigo para cambiar la previsualizacion de la imagen
            function previewImage(event) {
                const input = event.target;// elemento que genro el evento
                const preview = document.getElementById("profilePreview"); //lugar donde visualizamos la foto

                if (input.files && input.files[0]) {//Verificamos si se ha seleccionado al menos un archivo
                    const reader = new FileReader();

                    reader.onload = function (e) {
                        preview.src = e.target.result;// asignamos el valor de la nueva imagen a src para cambiar la foto
                        // el src es de la imagen
                    };

                    reader.readAsDataURL(input.files[0]);
                }
            }
        </script>
    </head>
    <body style="background-image: url('${pageContext.request.contextPath}/resources/images/GymGeneral.jpg'); background-size: cover; background-position: center; background-attachment: fixed;">
        <div class="main-container registration-container">

            <h1>Mi Cuenta</h1>

            <!-- Mostrar mensaje de éxito o error -->
            <c:if test="${not empty msg}">
                <div style="color: green; font-weight: bold;">${msg}</div>
            </c:if>

            <c:if test="${usuario != null}">
                <form action="${pageContext.request.contextPath}/miCuenta" method="POST" enctype="multipart/form-data" onsubmit="return checkData(event)">

                    <!-- Campo para subir la foto -->
                    <label for="photo">Foto de Perfil:</label>
                    <div class="profile-container">
                        <img id="profilePreview" src="<%= fullPhotoPath%>" alt="Previsualización de la foto de perfil">
                        <label for="photo">Cambiar Foto</label>
                        <input type="file" id="photo" name="photo" accept="image/*" onchange="previewImage(event)">
                    </div>

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

                    <label for="currentPassword">Contraseña Actual:</label>
                    <input type="password" id="currentPassword" name="currentPassword" required><br><br>

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
