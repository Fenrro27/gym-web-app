
<<%@page import="daw.model.Usuario"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Confirmar Eliminación</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/general1.css">
    </head>
        <body style="background-image: url('${pageContext.request.contextPath}/resources/images/GymGeneral.jpg'); background-size: cover; background-position: center; background-attachment: fixed;">
        <div class="main-container"> 


        <%
            String nombre = request.getParameter("nombre");
            String email = request.getParameter("email");
            String id = request.getParameter("id");
        %>

        <h1 style="text-align: center;">Confirmar Eliminación de Usuario</h1>

        <p>¿Está seguro de que desea eliminar al siguiente usuario?</p>
        <table>
            <tr>
                <th>Nombre</th>
                <td><%= nombre %></td>
            </tr>
            <tr>
                <th>Email</th>
                <td><%= email %></td>
            </tr>
        </table>

        <form action="${pageContext.request.contextPath}/borrarUsuario" method="POST">
            <input type="hidden" name="id" value="<%= id %>">
            <button type="submit" class="delete-button">Sí, eliminar</button>
        </form>

        <form action="${pageContext.request.contextPath}/cuentasUsuarios" method="GET">
            <button type="submit" class="cancel-button">Cancelar</button>
        </form>
        </div>
    </body>
</html>
