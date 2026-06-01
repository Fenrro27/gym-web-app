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
        <div class="main-container"> <!-- Contenedor principal con fondo semitransparente -->

        <%
            // Recuperamos el objeto Actividad que se pasó desde el controlador
            Actividad actividad = (Actividad) request.getAttribute("actividad");
            // Verificamos si el usuario está inscrito a la actividad
            Boolean inscrito = (Boolean) request.getAttribute("inscrito");
        %>

        <h1><%= actividad.getNombre()%></h1>

        <% if (actividad != null) {%>
        <div class="actividad-details">
            <p><strong>Descripción:</strong> <%= actividad.getDescripcion()%></p>
            <p><strong>Monitor:</strong> <%= actividad.getMonitor().getName()%></p>
            <p><strong>Precio:</strong> <%= actividad.getPrecio()%> euros</p>
        </div>

        <c:if test="${not empty mensaje}">
            <p style="color: red;"><strong>${mensaje}</strong></p>
        </c:if>

        <div class="acciones">
            <% if (inscrito != null && inscrito) { %> <!<!-- Vemos a que boton enviamos, si a desapuntarse o a añadir -->
                <form action="${pageContext.request.contextPath}/desapuntarseActividad" method="post">
                    <input type="hidden" name="idActividad" value="<%= actividad.getId()%>">
                    <button type="submit" class="dynamic-button">Desapuntarse</button>
                </form>
            <% } else { %>
                <form action="${pageContext.request.contextPath}/agregarCarrito" method="post">
                    <input type="hidden" name="idActividad" value="<%= actividad.getId()%>">
                    <button type="submit" class="green-button">Añadir a la cesta</button>
                </form>
            <% } %>
        </div>

        <% } else { %>
        <p>No se encontraron detalles de la actividad.</p>
        <% } %>

        </div> 

    </body>
</html>
