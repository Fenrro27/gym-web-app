<%@ page import="daw.model.Actividad" %>
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
        <h1>Lista de Actividades</h1>

        <table id="tablaActividades">
            <thead>
                <tr>
                    <th>Nombre</th>
                    <th>Monitor</th>
                    <th>Precio</th>
                    <th>Acción</th> 
                </tr>
            </thead>
            <tbody>
                <!-- Rellenamos mediante fetch -->
            </tbody>
        </table>
    </div>

    <script>
        // Funcion fetch para actualizar la tabla de actividades cada 5 segundos
        function cargarActividades() {
            fetch("${pageContext.request.contextPath}/catalogoActividades", {
                method: 'GET',
                headers: {
                    'X-Requested-With': 'XMLHttpRequest' // Indicamos que es una solicitud fetch
                }
            })
            .then(response => response.text()) // Parseamos la respuesta como texto HTML
            .then(html => {
                // Colocamos el HTML generado en el servidor dentro de la tabla
                let tablaActividades = document.getElementById("tablaActividades"); // Id de la tabla en el jsp
                tablaActividades.querySelector("tbody").innerHTML = html; // Insertamos el HTML de las filas
            })
            .catch(error => {
                console.error('Error al cargar las actividades:', error);
            });
        }

        // Llamamos a la funcion cargarActividades cuando la pagina cargue
        window.onload = function() {
            cargarActividades(); // Llamar la primera vez al cargar la pagina
            // Actualizamos la tabla cada 5 segundos (5000 ms)
            setInterval(cargarActividades, 5000);
        };
    </script>
</body>