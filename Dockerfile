# =========================================================================
# Etapa 1: Compilación de la aplicación Web
# =========================================================================
# Usamos la misma imagen de GlassFish como builder porque ya contiene 
# el compilador de Java (javac) y todas las librerías de Jakarta EE.
FROM omnifish/glassfish:7.0.14 AS builder

USER root
WORKDIR /build

# Copiar el código fuente, la estructura web y configuraciones
COPY src ./src
COPY web ./web
COPY setup ./setup

# Copiamos la configuración de los recursos JDBC dentro de la propia aplicación
# Esto evita problemas y bloqueos del servidor al intentar autodesplegar dos cosas a la vez
RUN cp setup/glassfish-resources.xml web/WEB-INF/glassfish-resources.xml

# Compilar los archivos Java y colocarlos en la carpeta WEB-INF/classes
# Utilizamos todas las librerías propias de GlassFish para la compilación
RUN mkdir -p web/WEB-INF/classes && \
    find src/java -name "*.java" > sources.txt && \
    javac -cp "/opt/glassfish7/glassfish/modules/*" -d web/WEB-INF/classes @sources.txt

# Copiar recursos no-Java (como runSQL_Init.sql) a WEB-INF/classes
RUN cd src/java && find . -type f -not -name "*.java" -exec cp --parents \{\} ../../web/WEB-INF/classes/ \;

# Copiar persistence.xml
RUN mkdir -p web/WEB-INF/classes/META-INF && \
    cp src/conf/persistence.xml web/WEB-INF/classes/META-INF/

# Empaquetar todo en un archivo .war
RUN cd web && jar -cvf /app.war *

# =========================================================================
# Etapa 2: Entorno de ejecución (GlassFish + Derby)
# =========================================================================
FROM omnifish/glassfish:7.0.14

USER root

# Copiamos la configuración de los recursos JDBC a una ubicación temporal
COPY setup/glassfish-resources.xml /opt/glassfish7/glassfish-resources.xml

# Creamos un script de inicio secuencial y robusto
RUN echo '#!/bin/bash' > /start.sh && \
    echo 'echo "1. Iniciando base de datos Derby (JavaDB)..."' >> /start.sh && \
    echo '/opt/glassfish7/bin/asadmin start-database' >> /start.sh && \
    echo 'echo "2. Iniciando GlassFish en segundo plano..."' >> /start.sh && \
    echo '/opt/glassfish7/bin/asadmin start-domain domain1' >> /start.sh && \
    echo 'echo "3. Añadiendo recursos JDBC al servidor..."' >> /start.sh && \
    echo '/opt/glassfish7/bin/asadmin add-resources /opt/glassfish7/glassfish-resources.xml' >> /start.sh && \
    echo 'echo "4. Desplegando la aplicación Web..."' >> /start.sh && \
    echo '/opt/glassfish7/bin/asadmin deploy --contextroot /gym /app.war' >> /start.sh && \
    echo 'echo "✅ ¡Todo listo! Aplicación corriendo en http://localhost:8080/gym"' >> /start.sh && \
    echo 'tail -f /opt/glassfish7/glassfish/domains/domain1/logs/server.log' >> /start.sh && \
    chmod +x /start.sh

USER glassfish

# Exponemos los puertos: 8080 (Web), 8181 (HTTPS), 4848 (Administración) y 1527 (Derby)
EXPOSE 8080 8181 4848 1527

# Copiamos el WAR compilado a una ruta temporal (lo desplegamos explícitamente en el script)
COPY --from=builder --chown=glassfish:glassfish /app.war /app.war

# Iniciamos usando el script personalizado
ENTRYPOINT ["/start.sh"]
