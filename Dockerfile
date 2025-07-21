# Etapa de construcción
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

# Copia pom.xml y descarga dependencias primero (para aprovechar cache)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia el resto del código y compila
COPY . .
RUN mvn clean package -DskipTests

# Etapa final de ejecución
FROM amazoncorretto:17.0.15

WORKDIR /app

# Copiar el jar desde la etapa de construcción
COPY --from=builder /app/target/product-api-0.0.1-SNAPSHOT.jar app.jar

# Puerto de la app
EXPOSE 8080

# Comando para ejecutar Spring Boot con perfil dev y habilitar consola H2 para acceso remoto. Ojo. Solo en Dev
ENTRYPOINT ["java", "-Dspring.h2.console.settings.web-allow-others=true", "-jar", "app.jar", "--spring.profiles.active=dev"]
