# 📝 Tasks API – Spring Boot 3.5.5

Este proyecto implementa un **CRUD de Tareas y Subtareas** con:
- **Autenticación JWT**
- **Validaciones y paginación**
- **Documentación automática con Swagger**
- **Indicadores de salud (Actuator)**
- Base de datos **PostgreSQL** usando **Docker Compose**

Ideal como punto de partida para microservicios REST seguros en Spring Boot.

## 🏗️ Arquitectura
          ┌────────────────────┐
          │  Spring Boot API   │
          │   (Tasks API)      │
          │                    │
          │  ┌──────────────┐  │
          │  │ Swagger UI   │  │  → Documentación REST
          │  └──────────────┘  │
          │  ┌──────────────┐  │
          │  │ JWT Security │  │  → Login & Validación de Token
          │  └──────────────┘  │
          │  ┌──────────────┐  │
          │  │ Actuator     │  │  → Health, métricas
          │  └──────────────┘  │
          └─────────┬──────────┘
                    │
                    ▼
          ┌────────────────────┐
          │   PostgreSQL DB    │
          │   (tasksdb)        │
          └────────────────────┘

---

## 📦 Tecnologías y Versiones

| Componente              | Versión |
|------------------------|---------|
| **Java**              | 17 |
| **Spring Boot**       | 3.5.5 |
| **Spring Security**   | 6.x |
| **Spring Data JPA**   | 3.x |
| **PostgreSQL**        | 15 (puerto `5434`) |
| **Swagger**           | springdoc-openapi 2.6.0 |
| **JWT**               | io.jsonwebtoken 0.11.5 |

---

## 🚀 Instalación y Configuración

### 1️⃣ Clonar el repositorio

bash

git clone + url repo

cd tasks-api

---

2️⃣ Configuración de Base de Datos y JWT

Actualmente, toda la configuración se encuentra en src/main/resources/application.properties:

spring.datasource.url=jdbc:postgresql://localhost:5434/tasksdb
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

jwt.secret=super_secreto
jwt.expiration=60


Nota: Para producción se recomienda usar variables de entorno (.env) o un gestor de secretos en lugar de credenciales en texto plano.

---

3️⃣ Levantar Base de Datos (Docker)

El proyecto incluye un docker-compose.yml para levantar Postgres:

docker compose up -d

---

4️⃣ Ejecutar el Proyecto

Compilar y ejecutar con Maven:

./mvnw spring-boot:run

---
🌐 Acceso y Documentación

Servicio	URL

Swagger UI	http://localhost:8080/swagger-ui.html

OpenAPI JSON	http://localhost:8080/v3/api-docs

Actuator Health	http://localhost:8080/actuator/health


---
🔑 Autenticación JWT
1️⃣ Obtener Token

POST /api/login

Request Body:
{
  "username": "admin",
  "password": "1234"
}

Respuesta:

{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTY5NDk5OTI2OCwiZXhwIjoxNjk1MDAyODY4fQ.uZQ..."
}

---

📌 Enviar el token en cada request en el header:

Authorization: Bearer <token>

2️⃣ Acceder al Perfil

GET /api/profile
Devuelve la información del usuario autenticado.

---

📋 Endpoints CRUD
🟢 Tareas (/tasks)
Método	Endpoint	Descripción

POST	/tasks	Crear nueva tarea

GET	/tasks	Listar tareas (paginadas)

GET	/tasks/{id}	Obtener tarea por ID

PUT	/tasks/{id}	Actualizar tarea

DELETE	/tasks/{id}	Eliminar tarea


Ejemplo de creación:

{
  "title": "Preparar documentación",
  "description": "Escribir el README",
  "category": "docs"
}

Respuesta esperada:

{
  "id": 1,
  "title": "Preparar documentación",
  "description": "Escribir el README",
  "category": "docs",
  "createdAt": "2025-09-17T22:00:00"
}

---

🔵 Subtareas (/tasks/{id}/subtasks)
Método	Endpoint	Descripción

POST	/tasks/{id}/subtasks	Crear subtarea

GET	/tasks/{id}/subtasks	Listar subtareas (paginadas)

GET	/tasks/{id}/subtasks/{subId}	Obtener subtarea

PUT	/tasks/{id}/subtasks/{subId}	Actualizar subtarea

DELETE	/tasks/{id}/subtasks/{subId}	Eliminar subtarea

📄 Respuesta con Paginación

Todas las listas devuelven datos en este formato:

{
  "data": [
    { "id": 1, "title": "Tarea 1" },
    { "id": 2, "title": "Tarea 2" }
  ],
  "meta": {
    "total": 100,
    "page": 2,
    "totalPages": 10
  }
}

---

Query params soportados:

?page=1&limit=10&sortBy=title&order=asc&category=docs

---

🩺 Actuator Endpoints
Endpoint	Descripción

/actuator/health	Estado de la aplicación

/actuator/metrics	Métricas de la app

/actuator/info	Información de compilación

👥 Usuarios Simulados
Usuario	Password	Rol

admin	1234	ADMIN

user	1234	USER

👥 Usuarios Simulados (en memoria)

Los usuarios están definidos en duro en la clase `InMemoryUserDetailsService` ubicada en el paquete 
`com.example.demo.security`. Este servicio crea dos usuarios (admin y user) al iniciar la aplicación, 
con contraseñas codificadas con BCrypt. Se utilizan únicamente para pruebas y no se persisten en base de datos.





