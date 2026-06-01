<%@page import="java.io.File"%>
<%@page import="daw.model.Usuario"%>
<%@page import="java.util.List"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Cuentas de Usuarios</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/general1.css">
    </head>
    <body style="background-image: url('${pageContext.request.contextPath}/resources/images/GymGeneral.jpg'); background-size: cover; background-position: center; background-attachment: fixed;">
        <div class="main-container"> 

            <%
                List<Usuario> usuarios = (List<Usuario>) request.getAttribute("usuario");
                
                // Creamos un SimpleDateFormat para formatear las fechas
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                String msg = (String) request.getAttribute("msg");
            %>

            <h1 style="text-align: center;">Cuentas de Usuarios</h1>

            <% if (msg != null) { %>
            <div class="alert" style="color: green; font-weight: bold; text-align: center;">
                <%= msg %>
            </div>
            <% } %>

            <% if (usuarios != null && !usuarios.isEmpty()) { %>
            <div class="table-responsive">
                <table>
                    <thead>
                        <tr>
                            <th>Foto</th> 
                            <th>Nombre</th>
                            <th>DNI</th>
                            <th>Fecha de Nacimiento</th>
                            <th>Teléfono</th>
                            <th>Email</th>
                            <th>Fecha de Entrada</th>
                            <th>Tipo</th>
                            <th>Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (Usuario usuario : usuarios) { 
                            // Ruta base de las imagenes de perfil
                            String baseImagePath = application.getRealPath("/resources/photos/");
                            String id = usuario.getId().toString();
                            String fullPhotoPath = null;

                            String potentialPath = baseImagePath + "/US" + id + ".jpg";
                            File file = new File(potentialPath);

                            if (file.exists() && !file.isDirectory()) {
                                fullPhotoPath = request.getContextPath() + "/resources/photos/US" + id + ".jpg";
                            } else {
                                fullPhotoPath = request.getContextPath() + "/resources/images/AvatarPesa.jpg"; // Imagen por defecto
                            }
                        %>
                        <tr>
                            <!-- Columna para la foto de perfil -->
                            <td><img src="<%= fullPhotoPath %>" alt="Foto de perfil" style="width: 50px; height: 50px; border-radius: 50%;"></td>

                            <td><%= usuario.getName() %></td>
                            <td><%= usuario.getDNI() %></td>
                            <td>
                                <% if (usuario.getFechaNacimiento() != null) { %>
                                <%= dateFormat.format(usuario.getFechaNacimiento()) %>
                                <% } else { %>
                                N/A
                                <% } %>
                            </td>
                            <td><%= usuario.getTelefono() %></td>
                            <td><%= usuario.getEmail() %></td>
                            <td>
                                <% if (usuario.getFechaEntrada() != null) { %>
                                <%= dateFormat.format(usuario.getFechaEntrada()) %>
                                <% } else { %>
                                N/A
                                <% } %>
                            </td>
                            <td><%= usuario.getTipo() %></td>
                            <td>
                                <!<<!-- Boton para modificar -->
                                <form action="${pageContext.request.contextPath}/modificarUsuario" method="GET" style="display:inline;">
                                    <input type="hidden" name="id" value="<%= usuario.getId() %>">
                                    <button type="submit" class="modify-button">Modificar</button>
                                </form>

                                <!-- Boton para borrar -->
                                <form action="${pageContext.request.contextPath}/confirmarBorrarUsuario" method="GET" style="display:inline;">
                                    <input type="hidden" name="id" value="<%= usuario.getId() %>">
                                    <input type="hidden" name="nombre" value="<%= usuario.getName() %>">
                                    <input type="hidden" name="email" value="<%= usuario.getEmail() %>">
                                    <button type="submit" class="delete-button">Borrar</button>
                                </form>

                            </td>
                        </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
            <% } else { %>
            <p style="text-align: center;">No hay usuarios disponibles.</p>
            <% } %>
        </div>
    </body>
</html>
