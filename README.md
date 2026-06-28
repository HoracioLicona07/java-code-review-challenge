# java-code-review-challenge

> **Ejercicio educativo de code review, refactorización y entrevistas técnicas en Java.**

---

## Descripción

Sistema de órdenes de compra e inventario construido con Java 17 + Spring Boot. Compila y funciona correctamente, pero contiene **errores intencionales y oportunidades de mejora** distribuidas por nivel de seniority.

El objetivo no es que el proyecto esté perfecto — el objetivo es que **tú lo mejores**.

---

## Objetivo educativo

Este proyecto está diseñado para que los alumnos practiquen:

- 🔍 Lectura de código existente (code reading)
- 🐛 Identificación de bugs y malas prácticas
- 🔧 Refactorización con buenas prácticas
- ☕ Java idiomático (BigDecimal, Optional, Enum, streams)
- 🏗️ Arquitectura por capas (controller → service → repository)
- ✅ Testing con JUnit 5 + Mockito
- 🌐 Diseño de APIs REST
- 🔐 Manejo de transacciones y consistencia de datos
- 🗣️ Preparación para entrevistas técnicas senior

---

## Stack técnico

| Tecnología | Versión |
|------------|---------|
| Java | 17 |
| Spring Boot | 3.2.x |
| Spring Data JPA | incluido |
| H2 Database | embebida |
| JUnit 5 | incluido |
| Mockito | incluido |
| Bean Validation | incluido |
| Springdoc OpenAPI | 2.5.0 |
| Maven | 3.8+ |

---

## Cómo ejecutar

### Prerrequisitos
- Java 17+
- Maven 3.8+

### Levantar el servidor

```bash
git clone https://github.com/TU_USUARIO/java-code-review-challenge.git
cd java-code-review-challenge
mvn spring-boot:run
```

El servidor levanta en: **`http://localhost:8080`**

### Interfaces disponibles

| URL | Descripción |
|-----|-------------|
| `http://localhost:8080/swagger-ui.html` | Documentación interactiva |
| `http://localhost:8080/h2-console` | Base de datos H2 |

**H2 Console:** JDBC URL `jdbc:h2:mem:ecommercedb`, usuario `sa`, contraseña vacía.

---

## Cómo ejecutar las pruebas

```bash
mvn test
```

> ⚠️ Algunos tests fallan intencionalmente. Parte del reto es identificar por qué.

---

## Endpoints principales

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/api/products` | Listar productos |
| `POST` | `/api/products` | Crear producto |
| `GET` | `/api/products/{id}` | Obtener producto |
| `PUT` | `/api/products/{id}` | Actualizar producto |
| `DELETE` | `/api/products/{id}` | Eliminar producto |
| `GET` | `/api/customers` | Listar clientes |
| `POST` | `/api/customers` | Crear cliente |
| `GET` | `/api/orders` | Listar órdenes |
| `POST` | `/api/orders` | Crear orden |
| `POST` | `/api/orders/{id}/items` | Agregar item |
| `POST` | `/api/orders/{id}/confirm` | Confirmar orden |
| `POST` | `/api/orders/{id}/cancel` | Cancelar orden |
| `POST` | `/api/payments/process` | Procesar pago |

---

## Estructura del proyecto

```
src/main/java/com/challenge/ecommerce/
├── JavaCodeReviewChallengeApplication.java
├── controller/     ← Endpoints REST
├── service/        ← Lógica de negocio
├── repository/     ← Acceso a datos
├── entity/         ← Entidades JPA
├── dto/            ← Objetos de transferencia
├── mapper/         ← Conversión entidad ↔ DTO
├── exception/      ← Excepciones (¡pendiente de crear!)
├── config/         ← Configuración
└── util/           ← Utilidades

docs/
├── 01-descripcion-del-reto.md
├── 02-lista-de-problemas-intencionales.md   ← ⚠️ Spoilers al final
├── 03-guia-para-alumnos.md
├── 04-guia-para-instructor.md
├── 05-rubrica-evaluacion.md
└── 06-roadmap-de-refactorizacion.md
```

---

## Qué se espera del alumno

1. **Leer** el código completo antes de modificar.
2. **Documentar** los problemas encontrados (archivo, línea, nivel, impacto).
3. **Corregir** los errores con buenas prácticas.
4. **Agregar** pruebas unitarias que validen el comportamiento correcto.
5. **Defender** las decisiones técnicas en una sesión de code review.

---

## Cómo entregar la solución

1. Haz un fork de este repositorio.
2. Crea una rama `student-solution`:
```bash
git checkout -b student-solution
```
3. Trabaja en esa rama con commits descriptivos.
4. Abre un Pull Request hacia `main` con:
   - Lista de cambios realizados.
   - Explicación de los problemas encontrados.
   - Tests agregados o mejorados.

---

## Ramas recomendadas

| Rama | Propósito |
|------|-----------|
| `main` | Versión con errores intencionales (este repo) |
| `student-solution` | Tu solución personal |
| `instructor-solution` | Solución de referencia del instructor |
| `refactor-clean-architecture` | Refactor avanzado con arquitectura hexagonal |

---

## Cómo subir a GitHub

```bash
git init
git add .
git commit -m "Initial commit: intentional Java code review challenge"
git branch -M main
git remote add origin https://github.com/TU_USUARIO/java-code-review-challenge.git
git push -u origin main
```

---

## Siguiente fase sugerida — Rama `instructor-solution`

Una vez que los alumnos hayan trabajado su solución, el instructor puede publicar la rama de referencia:

```bash
git checkout -b instructor-solution
# Aplicar las correcciones de las 8 fases del roadmap
git push origin instructor-solution
```

La rama `instructor-solution` debería incluir:
- `BigDecimal` para todo el dinero.
- `OrderStatus` como Enum con transiciones.
- `GlobalExceptionHandler` con excepciones custom.
- `@Transactional` en servicios críticos.
- Strategy pattern para métodos de pago.
- Desacoplamiento de notificaciones con eventos de Spring.
- Tests completos con cobertura > 80% en servicios.
- Paginación en todos los listados.

---

> 💡 **Consejo:** Antes de ver la solución del instructor, intenta encontrar al menos 10 problemas por tu cuenta. El aprendizaje ocurre en el proceso de búsqueda, no en la respuesta.
