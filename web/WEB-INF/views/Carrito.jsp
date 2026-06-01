<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.List" %>
<%@ page import="daw.model.Actividad" %>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Carrito de Actividades</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/general1.css">
    </head>
    <body style="background-image: url('${pageContext.request.contextPath}/resources/images/GymGeneral.jpg'); background-size: cover; background-position: center; background-attachment: fixed;">
        <div class="main-container"> 


            <h1>Carrito de Actividades</h1>

            <c:if test="${not empty sessionScope.carrito}">
                <ul class="carrito-lista">
                    <c:forEach var="actividad" items="${sessionScope.carrito}">
                        <li class="carrito-item">
                            <p><strong>Nombre:</strong> ${actividad.nombre}</p>
                            <p><strong>Descripción:</strong> ${actividad.descripcion}</p>
                            <p><strong>Precio:</strong> ${actividad.precio} euros</p>

                            <form action="${pageContext.request.contextPath}/eliminarCarrito" method="post">
                                <input type="hidden" name="idActividad" value="${actividad.id}">
                                <button type="submit" class="delete-button">
                                    <span>Eliminar del carrito</span>
                                </button>                            
                            </form>
                        </li>
                    </c:forEach>
                </ul>

                <form action="${pageContext.request.contextPath}/inscribirseCarrito" method="post">
                    <input type="submit" value="Inscribirse en las actividades seleccionadas">
                </form>

            </c:if>

            <c:if test="${empty sessionScope.carrito}">
                <p>No hay actividades en el carrito.</p>
            </c:if>
        </div>  
    </body>
</html>
