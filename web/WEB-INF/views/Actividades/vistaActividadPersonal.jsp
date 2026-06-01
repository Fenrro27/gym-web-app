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
                Boolean inscrito = (Boolean) request.getAttribute("inscrito");
            %>

            <h1><%= actividad.getNombre()%></h1>

            <% if (actividad != null) {%>
            <div class="actividad-details">
                <p><strong>Descripción:</strong> <%= actividad.getDescripcion()%></p>
                <p><strong>Monitor:</strong> <%= actividad.getMonitor().getName()%></p>
                <p><strong>Precio:</strong> <%= actividad.getPrecio()%> euros</p>
            </div>

            <div class="acciones">
                <% if (inscrito != null && inscrito) {%> <!<!-- Vemos que boton ponemos dependiendo si esta inscrito -->
                <form action="${pageContext.request.contextPath}/desapuntarseActividad" method="post">
                    <input type="hidden" name="idActividad" value="<%= actividad.getId()%>">
                    <button type="submit" class="dynamic-button">Desapuntarse</button>
                </form>
                <% } else {%>
                <form action="${pageContext.request.contextPath}/agregarCarrito" method="post">
                    <input type="hidden" name="idActividad" value="<%= actividad.getId()%>">
                    <button type="submit" class="green-button">Añadir a la cesta</button>

                </form>
                <% }%>

                <form action="${pageContext.request.contextPath}/modificarActividad" method="GET">
                    <input type="hidden" name="id" value="<%= actividad.getId()%>">
                    <button type="submit" class="modify-button">Modificar</button>
                </form>

                <form action="${pageContext.request.contextPath}/eliminarActividad" method="POST" onsubmit="return confirm('¿Estás seguro de que deseas eliminar esta actividad?');">
                    <input type="hidden" name="id" value="<%= actividad.getId()%>">
                    <button type="submit" class="delete-button">Eliminar</button>
                </form>
            </div>

            <% } else { %>
            <p>No se encontraron detalles de la actividad.</p>
            <% }%>

        </div>
        <br></br> 
        <div class="main-container"> 
            <h1>Usuarios Apuntados</h1>

            <!-- Mostrar la lista de usuarios apuntados -->
            <c:if test="${not empty usuarios}">
                <div class="table-responsive">
                    <table>
                        <thead>
                            <tr>
                                <th>Nombre</th>
                                <th>DNI</th>
                                <th>Email</th>
                            </tr>
                        </thead>
                        <tbody>
                            <!-- Recorrer la lista de usuarios -->
                            <c:forEach var="usuario" items="${usuarios}">
                                <tr>
                                    <td>${usuario.name}</td>
                                    <td>${usuario.DNI}</td>
                                    <td>${usuario.email}</td>
                                  
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:if>

            <c:if test="${empty usuarios}">
                <p>No hay usuarios apuntados a esta actividad.</p>
            </c:if>

        </div>

    </body>
</html>
