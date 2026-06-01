<%@page import="daw.model.Actividad"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Lista de Actividades</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/general1.css">
    </head>
    <body style="background-image: url('${pageContext.request.contextPath}/resources/images/GymGeneral.jpg'); background-size: cover; background-position: center; background-attachment: fixed;">
        <div class="main-container"> 

        <%
            List<Actividad> actividades = (List<Actividad>) request.getAttribute("actividades");
        %>

        <h1>Lista de Actividades Inscritas</h1>

        <% if (actividades != null && !actividades.isEmpty()) { %>
        <table>
            <thead>
                <tr>
                    <th>Nombre</th>
                    <th>Monitor</th>
                    <th>Precio</th>
                    <th>Acción</th> 
                </tr>
            </thead>
            <tbody>
                <% for (Actividad actividad : actividades) { %>
                <tr>
                    <td><%= actividad.getNombre() %></td>
                    <td><%= actividad.getMonitor() %></td>
                    <td><%= actividad.getPrecio() %></td>
                    <td>
                        <!-- Vemos los detalles de la actividad, redirigimos a vistaActividad pasando el id -->
                        <form action="${pageContext.request.contextPath}/vistaActividad" method="get">
                            <input type="hidden" name="idActividad" value="<%= actividad.getId() %>">
                            <input type="submit" value="Ver Detalles">
                        </form>
                    </td>
                </tr>
                <% } %>
            </tbody>
        </table>
        <% } else { %>
        <p>No se ha inscrito a ninguna actividad aún.</p>
        <% } %>
        </div>
    </body>
</html>
