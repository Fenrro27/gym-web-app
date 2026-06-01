<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Crear Actividad</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/general1.css">
    </head>
     <body style="background-image: url('${pageContext.request.contextPath}/resources/images/GymGeneral.jpg'); background-size: cover; background-position: center; background-attachment: fixed;">
        <div class="main-container"> 

        <h1>Dar de Alta una Actividad</h1>
        <form action="${pageContext.request.contextPath}/crearActividad" method="post">
            <label for="nombre">Nombre:</label><br>
            <input type="text" id="nombre" name="nombre" required><br><br>

            <label for="descripcion">Descripción:</label><br>
            <textarea id="descripcion" name="descripcion" rows="4" cols="50" required></textarea><br><br>

            <!-- Seleccionamos un monitor -->
            <label for="monitor">Monitor:</label><br>
            <select id="monitor" name="monitor" required>
                <option value="">Seleccionar un monitor</option>
                <c:forEach var="usuario" items="${usuarios}">
                    <!-- Mostramos los monitores con el nombre y el email entre parentesis -->
                    <option value="${usuario.id}">${usuario.name} (${usuario.email})</option> 
                </c:forEach>
            </select><br><br>

            <label for="precio">Precio:</label><br>
            <input type="number" id="precio" name="precio" step="0.01" min="0" required><br><br>

            <input type="submit" value="Crear Actividad">
        </form>
        </div>
    </body>
</html>
