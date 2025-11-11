# Usa Java 21 runtime leggero
FROM eclipse-temurin:21-jre-alpine

# Crea directory app
WORKDIR /app

# Copia il JAR buildato
COPY target/*.jar app.jar

# Imposta variabile JVM ottimizzata
ENV JAVA_TOOL_OPTIONS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Espone la porta 8080 (Railway inietta $PORT)
EXPOSE 8080

# Comando di avvio
ENTRYPOINT ["sh", "-c", "java $JAVA_TOOL_OPTIONS -jar /app/app.jar"]
