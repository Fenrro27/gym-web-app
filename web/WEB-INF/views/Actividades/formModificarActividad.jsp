<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Modificar Actividad</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/general1.css">
    </head>
    <body style="background-image: url('${pageContext.request.contextPath}/resources/images/GymGeneral.jpg'); background-size: cover; background-position: center; background-attachment: fixed;">
        <div class="main-container"> 

        <h1>Modificar Actividad</h1>
        <form action="${pageContext.request.contextPath}/modificarActividad" method="post">
            <label for="nombre">Nombre:</label><br>
            <input type="text" id="nombre" name="nombre" value="${actividad.nombre}" required><br><br>

            <label for="descripcion">Descripción:</label><br>
            <textarea id="descripcion" name="descripcion" rows="4" cols="50" required>${actividad.descripcion}</textarea><br><br>

            <label for="monitor">Monitor:</label><br>
            <select id="monitor" name="monitor" required> <!-- Mostramos un deplegable con los monitores -->
                <option value="">Seleccionar un monitor</option>
                <!-- Iterar sobre la lista de monitores disponibles -->
                <c:forEach var="monitorDisponible" items="${monitores}">
                    <option value="${monitorDisponible.id}" <!-- Enviamos el id del monitor seleccionado -->
                            ${monitorDisponible.id == actividad.monitor.id ? 'selected' : ''}><!-- Si el id coincide marcamos el monitor como selected -->
                            ${monitorDisponible.name} (${monitorDisponible.email}) <!-- Mostramos el nombre del monitor y el correo entre parentesis -->
                    </option>
                </c:forEach>
            </select><br><br>

            <label for="precio">Precio:</label><br>
            <input type="number" id="precio" name="precio" value="${actividad.precio}" step="0.01" min="0" required><br><br>

            <input type="hidden" name="id" value="${actividad.id}">

            <input type="submit" value="Modificar Actividad">
        </form>

        </div>
    </body>
</html>
