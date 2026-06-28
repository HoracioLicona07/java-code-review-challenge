# 05 — Rúbrica de Evaluación

## Escala de evaluación

| Nivel | Descripción |
|-------|-------------|
| **Básico** | Identifica el problema superficialmente pero no explica el impacto ni propone solución fundamentada. |
| **Intermedio** | Identifica el problema, explica el impacto y propone una solución funcional. |
| **Avanzado** | Identifica, explica, propone solución de calidad y considera casos borde. |
| **Excelente** | Todo lo anterior + propone mejoras adicionales, diseño extensible y documenta decisiones técnicas. |

---

## Criterios de evaluación

### 1. Identificación de errores

| Nivel | Descripción |
|-------|-------------|
| Básico | Encuentra al menos 5 problemas Junior. |
| Intermedio | Encuentra 10+ problemas entre Junior y Middle. |
| Avanzado | Encuentra 15+ problemas incluyendo al menos 3 Senior. |
| Excelente | Encuentra la mayoría de los problemas documentados y puede priorizar por impacto. |

---

### 2. Calidad de refactorización

| Nivel | Descripción |
|-------|-------------|
| Básico | Corrige errores superficiales (nombres, println, tipos). |
| Intermedio | Corrige tipos de datos, validaciones y separación de capas básica. |
| Avanzado | Refactoriza servicios, agrega mappers completos y manejo de excepciones. |
| Excelente | Propone diseño con patrones, extensibilidad y sin duplicación. |

---

### 3. Pruebas unitarias

| Nivel | Descripción |
|-------|-------------|
| Básico | Mejora asserts existentes con al menos un valor verificado. |
| Intermedio | Agrega 3+ tests con asserts completos y verifica interacciones con mocks. |
| Avanzado | Cubre flujos críticos: stock insuficiente, idempotencia, estado inválido. |
| Excelente | Prueba casos borde, usa `@ParameterizedTest`, verifica comportamiento negativo. |

---

### 4. Diseño orientado a objetos

| Nivel | Descripción |
|-------|-------------|
| Básico | Entiende herencia y encapsulamiento básico. |
| Intermedio | Aplica SRP, propone separar responsabilidades del God Service. |
| Avanzado | Propone enriquecer el dominio (OrderStatus Enum con transiciones). |
| Excelente | Propone eventos de dominio, Strategy para extensibilidad, dominio rico. |

---

### 5. Uso correcto de Java

| Nivel | Descripción |
|-------|-------------|
| Básico | Corrige `==` por `.equals()`, `double` por `BigDecimal`. |
| Intermedio | Usa Optional correctamente, streams idiomáticos, inyección por constructor. |
| Avanzado | Propone Enums, records (Java 16+), inmutabilidad donde aplica. |
| Excelente | Considera concurrencia, locking, tipos correctos en todos los contextos. |

---

### 6. Uso correcto de Spring Boot

| Nivel | Descripción |
|-------|-------------|
| Básico | Corrige inyección por campo a constructor. Agrega `@Valid`. |
| Intermedio | Agrega `@Transactional`, `@ControllerAdvice`, status HTTP correctos. |
| Avanzado | Separa capa de servicio y controller correctamente, usa perfiles. |
| Excelente | Considera observabilidad, Actuator, manejo de errores de integración. |

---

### 7. Manejo de errores

| Nivel | Descripción |
|-------|-------------|
| Básico | Identifica que se retorna null en lugar de 404. |
| Intermedio | Crea excepciones custom (`ProductNotFoundException`, etc.) con `@ResponseStatus`. |
| Avanzado | Implementa `GlobalExceptionHandler` con respuestas estructuradas. |
| Excelente | Considera diferentes tipos de error (negocio vs infraestructura), logging de errores. |

---

### 8. Manejo de transacciones

| Nivel | Descripción |
|-------|-------------|
| Básico | Identifica que falta `@Transactional` en métodos de escritura. |
| Intermedio | Agrega `@Transactional` en servicios y explica el comportamiento por defecto. |
| Avanzado | Diseña transacciones correctamente (propagación, rollback). |
| Excelente | Considera consistencia eventual, idempotencia, y manejo de errores transaccionales. |

---

### 9. Diseño de API REST

| Nivel | Descripción |
|-------|-------------|
| Básico | Identifica status HTTP incorrectos (200 en lugar de 404/201). |
| Intermedio | Propone estructura de respuesta consistente con DTOs. |
| Avanzado | Propone versionado de API, paginación, HATEOAS básico. |
| Excelente | Considera idempotency keys, rate limiting, errores descriptivos RFC 7807. |

---

### 10. Claridad de explicación técnica

| Nivel | Descripción |
|-------|-------------|
| Básico | Puede leer el código y describir qué hace. |
| Intermedio | Puede explicar por qué algo está mal y qué impacto tiene. |
| Avanzado | Puede justificar sus decisiones con principios (SOLID, Clean Code). |
| Excelente | Puede defender sus decisiones, considerar trade-offs y proponer alternativas. |

---

## Puntaje sugerido

| Criterio | Peso |
|----------|------|
| Identificación de errores | 20% |
| Calidad de refactorización | 20% |
| Pruebas unitarias | 15% |
| Diseño OO | 15% |
| Uso correcto de Java y Spring | 10% |
| Manejo de errores y transacciones | 10% |
| Diseño de API | 5% |
| Explicación técnica | 5% |
