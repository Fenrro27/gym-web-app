<%@ page import="daw.model.Actividad" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Detalles de la Actividad</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/general1.css">
    </head>
    <body style="background-image: url('${pageContext.request.contextPath}/resources/images/GymGeneral.jpg'); background-size: cover; background-position: center; background-attachment: fixed;">
        <div class="main-container"> 

            <%
                Actividad actividad = (Actividad) request.getAttribute("actividad");

            %>


            <% if (actividad != null) {%>
            <div class="actividad-details">
                <h1><%= actividad.getNombre()%></h1>
                <p><strong>Descripción:</strong> <%= actividad.getDescripcion()%></p>
                <p><strong>Monitor:</strong> <%= actividad.getMonitor().getName()%></p>
                <p><strong>Precio:</strong> <%= actividad.getPrecio()%> euros</p>
            </div>
        </div>

        <% } else { %>
        <p>No se encontraron detalles de la actividad.</p>
        <% }%>

    </div> 

</body>
</html>
