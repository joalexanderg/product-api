# Product API

Esta es una API Rest diseñada para exponer información de productos, categorias,proveedores, clientes, empelados y
ordendes. Aunque están los CRUD para todas ellas, se hizo enfasis en los evaluados en la práctica. De igual forma,
cuentan con documentación básica de uso en swageer. Está construida con **Spring Boot**, **Java 17** y **base de datos H2**. Ofrece funcionalidades como CRUD de productos y categorías, búsqueda paginada con filtros, generación masiva asíncrona de productos, y está documentada con **Swagger**.

## 📄 Descripción

Esta API permite:

- Gestionar productos y categorías.
- Buscar productos con filtros por nombre, categoría, proveedor, ordenamiento y paginación.
- Crear productos aleatorios de forma masiva de manera asíncrona.
- Consultar el estado de carga mediante un `jobId`.

## 🛀 Arquitectura del Proyecto

El proyecto sigue una arquitectura **por capas**. Se opto por este camino de un arquitectura sencilla con foco el la gestión básica de productos.
Quizas en un dominio mas amplio, donde los subdominios de ordenes y clientes se gestionaran de forma indipendiente, lo recomendado
sería una arquitectura hexagonal donde esta funcionalidades se consumen desde otros endpoint:

- **Controller**: expone endpoints REST.
- **Service**: contiene la lógica de negocio.
- **Repository**: acceso a datos usando Spring Data JPA.
- **DTOs**: evitan exponer directamente las entidades.
- **Asíncrono**: servicio para generación masiva no bloqueante.
- **Job Registry**: seguimiento de cargas masivas.

Adicionalmente, se contruye de tal forma que pueda ser contenerizado y desplegado dentro de un grupo de auto escalamiento
que permita el crecimiento horizontal en picos de demanda teniando al frente un balanceador de cargar con un servicio como ECS-Fargate o
en caso de requerir latencias mas bajas en la comunicación con otros servicios alojarse en un cluster de Kubernetes por lo menos en dos zonas de disponibilidad.

## 📃 Especificaciones Técnicas

- **Java**: 17 (Corretto 17.0.15)
- **Spring Boot**: 3.5.3
- **Maven**: 3.9.6
- **JPA/Hibernate**: con H2 embebido
- **Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **H2 Console**: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
- **Base de datos**: H2 (modo memoria)
- **Pruebas unitarias**: con **JUnit 5** y **Mockito**

## ✅ Pruebas unitarias y de integración

Se han desarrollado pruebas unitarias haciendo enfasis en las clases relacionadas con los puntos evaluados
para garantizar el correcto funcionamiento de los servicios:

- Servicios probados con **Mockito**: `ProductsServiceImpl`, `CategoriesServiceImpl`, `AsyncProductsGeneratorService`
- Casos cubiertos: creación, actualización, eliminación, búsqueda, manejo de errores, y operaciones asíncronas.
- Prueba de integración destacada:
  - `AsyncProductsGeneratorService` usando `@SpringBootTest`
  - Carga masiva de productos con verificación del resultado en base de datos.
  - Uso de Awaitility para esperar el estado `COMPLETED` del `JobId`.

## 📖 Clonar y ejecutar localmente

```bash
# Clonar el proyecto
https://github.com/joalexanderg/product-api.git
cd product-api

# Compilar y ejecutar (perfil dev)
mvn clean spring-boot:run -Dspring-boot.run.profiles=dev
```

## 🚀 Docker: build & run

### 1. Construir la imagen

```bash
docker build -t product-api .
```

### 2. Ejecutar el contenedor

```bash
docker run -p 8080:8080 product-api
```
### 2.1 Disponible en el contedor
> Los datos de conexión de la bd estan expuestos en un archivo .yml En entorno productivo deben almacenarse en secretos bajo custioda y ser consumidos en tiempo de ejecución por al app.

> Estos link se dejan públicos por simplicidad en fase de desarrollo. En un entrono real deben estar protegidos con control de acceso. En la BD H2 el nombre de la bd es jdbc:h2:mem:testdb, entonces hay que cambiar el que sale por defecto.
> ✅ Swagger disponible en: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) ✅ Consola H2 en: [http://localhost:8080/h2-console](http://localhost:8080/h2-console) ⚠️ Se habilitó `-Dh2.console.settings.web-allow-others=true` para permitir acceso desde Docker.

## ⚖️ Estrategia de carga masiva
Se elige una estrategia de carga masiva por lotes con el fin de no saturar los recursos del entorno de ejecución y que se
ejecute de forma asincrona para no generar un bloqueo de hilo. Adicionalmente, se crea un EndPoint para visualizar
el código del job (se almacena en memoria. En un entorno productivo debe quedar registrado en una BD con persistencia)

- Endpoint: `POST /api/Products/Product`
- Recibe parámetro `count` (1-100000)
- Crea productos aleatorios y los inserta en lotes (batch insert) asíncronamente.
- Retorna un `JobId` para seguimiento:

```json
{
  "message": "Carga iniciada. Consulta el estado con JobId: 123e4567-e89b-12d3-a456-426614174000"
}
```

- Consultar estado: `GET /api/Products/Product/status/{jobId}`

## 📂 Ejemplos de payload y headers (JWT)
Para la seguridad en el consumo de los Endpoint se creo el path /auth/token el cual simula un servicio de autenticación y autorización.
En un entorno productivo éste puede ser suministrado por servicios como Cognito o ADP de Azure. Por facilidad, se crea un boton "Autorize"
en la doc de swagger en la que sin restricciones se deja libre acceso para obtener un token y agregarlo en la interfaz indicada.

Para complementar los temas de seguridad se pueden incluir politicas de CORS en el backend que limite las peticiones a origenes autorizados. Por ejemplo,
el front de la aplicación que haga uso de él.
### Headers comunes:

```http
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR...
Content-Type: application/json
```

### Crear producto (POST /api/Products)

```json
{
  "productName": "Servidor Dell",
  "supplierID": 1,
  "categoryID": 2,
  "quantityPerUnit": "10 unidades",
  "unitPrice": 999.99,
  "unitsInStock": 15,
  "unitsOnOrder": 5,
  "reorderLevel": 3,
  "discontinued": false
}
```

### Crear categoría (POST /api/category)

```json
{
  "categoryName": "Computadores",
  "description": "Línea de equipos de escritorio",
  "picture": "https://example.com/image.png"
}
```

## 📅 Endpoints destacados

- `GET /api/Products` - Búsqueda con paginación y filtros
- `POST /api/Products/Product` - Generación masiva asíncrona
- `GET /api/Products/Product/status/{jobId}` - Estado del job
- `POST /api/category` - Crear categoría
- `GET /api/category/{id}` - Obtener categoría por ID

## 👤 Autor

**Jorge Alexander García** [https://github.com/joalexanderg](https://github.com/joalexanderg)

## 🚀 Extensiones futuras sugeridas

- Autenticación real basada en usuarios
- Exportación de productos a Excel o CSV
- Paginación mejorada con HATEOAS o PagedModel
- Despliegue continuo con GitHub Actions y Docker Hub

---

> 🚀 Proyecto desarrollado como parte de una práctica de arquitectura y desarrollo de APIs modernas en Spring Boot.

