# 01 — Descripción del Reto

## Objetivo

Este proyecto es un ejercicio de **code review, refactorización y mejora de código** para desarrolladores que se están preparando para entrevistas técnicas en Java.

El sistema implementa un e-commerce básico de órdenes de compra. Funciona, compila y puede ejecutarse localmente — pero contiene errores intencionales, malas prácticas y oportunidades de mejora distribuidas por niveles de seniority.

Tu objetivo **no es solo corregir el código**, sino también:
- Identificar y explicar los problemas.
- Proponer soluciones fundamentadas.
- Refactorizar con buenas prácticas.
- Agregar pruebas que validen el comportamiento correcto.

---

## Contexto del sistema

El sistema permite:
- Registrar y consultar productos con inventario.
- Registrar clientes.
- Crear órdenes de compra y agregar productos.
- Confirmar o cancelar una orden.
- Calcular totales con descuentos.
- Simular el procesamiento de un pago.
- Simular el envío de notificaciones.

---

## Stack técnico

| Tecnología | Versión |
|------------|---------|
| Java | 17 |
| Spring Boot | 3.2.x |
| Spring Data JPA | incluido en Boot |
| H2 Database | embebida |
| JUnit 5 | incluido en Boot |
| Mockito | incluido en Boot |
| Bean Validation | incluido en Boot |
| Springdoc OpenAPI | 2.5.0 |

---

## Cómo ejecutar el proyecto

### Prerrequisitos
- Java 17+
- Maven 3.8+

### Levantar el servidor

```bash
cd java-code-review-challenge
mvn spring-boot:run
```

El servidor levanta en `http://localhost:8080`

### Interfaces disponibles

| URL | Descripción |
|-----|-------------|
| `http://localhost:8080/swagger-ui.html` | Documentación interactiva de la API |
| `http://localhost:8080/h2-console` | Consola de base de datos H2 |
| `http://localhost:8080/api-docs` | OpenAPI JSON |

**Credenciales H2:**
- JDBC URL: `jdbc:h2:mem:ecommercedb`
- Usuario: `sa`
- Contraseña: _(vacío)_

---

## Cómo ejecutar las pruebas

```bash
mvn test
```

> **Nota:** Algunos tests están intencionalmente incompletos o fallan. Parte del reto es identificar por qué fallan y corregirlos.

---

## Datos semilla

Al levantar la aplicación se cargan automáticamente:
- **8 productos** en diferentes categorías y estados.
- **4 clientes** (uno inactivo).

---

## Qué deben entregar los alumnos

1. **Documento de hallazgos** — lista de todos los problemas identificados con:
   - Archivo y línea aproximada.
   - Nivel (Junior / Middle / Senior).
   - Explicación del problema.
   - Propuesta de solución.

2. **Rama `student-solution`** — con el código corregido y refactorizado.

3. **Tests** — nuevos o mejorados que validen el comportamiento correcto.

4. **Explicación técnica** — prepararse para defender las decisiones técnicas en una sesión de code review simulada.

---

## Endpoints disponibles para explorar

```
GET  /api/products
POST /api/products
GET  /api/products/{id}
PUT  /api/products/{id}
DELETE /api/products/{id}
PATCH /api/products/{id}/stock

GET  /api/customers
POST /api/customers
GET  /api/customers/{id}

GET  /api/orders
POST /api/orders
GET  /api/orders/{id}
POST /api/orders/{id}/items
POST /api/orders/{id}/confirm
POST /api/orders/{id}/cancel

POST /api/payments/process
```
