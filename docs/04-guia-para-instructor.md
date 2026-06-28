# 04 — Guía para el Instructor

## Cómo presentar el reto

### Introducción sugerida (5 minutos)

> "Hoy van a revisar el código de un sistema real. Fue escrito por un equipo con distintos niveles de experiencia, bajo presión de tiempo. Compila y funciona — pero tiene problemas. Su trabajo es encontrarlos, explicarlos y mejorarlos. Esto es lo que hacen los ingenieros senior todos los días."

### Primera actividad (10 minutos)
1. Pide que levanten el proyecto.
2. Pide que ejecuten el flujo completo: crear cliente → orden → agregar items → confirmar → pagar.
3. Pide que observen los logs, los status HTTP y los datos que regresan.
4. Pregunta: _"¿Algo les parece extraño?"_

---

## División por sesiones sugerida

### Sesión 1 — Exploración (1.5h)
- Levantar proyecto y probar endpoints.
- Leer código por capas.
- Identificar problemas Junior.
- Discusión grupal.

### Sesión 2 — Correcciones Junior (1.5h)
- Corregir uso de `double` → `BigDecimal`.
- Corregir comparaciones con `==`.
- Corregir respuestas HTTP incorrectas.
- Agregar validaciones básicas (`@NotBlank`, `@Email`, `@Min`).
- Mejorar tests con asserts reales.

### Sesión 3 — Problemas Middle (1.5h)
- Extraer lógica del controller.
- Eliminar acceso directo al repositorio desde controller.
- Agregar `@Transactional`.
- Crear `GlobalExceptionHandler`.
- Completar mapper.

### Sesión 4 — Problemas Senior (2h)
- Refactorizar `OrderService` (dividir responsabilidades).
- Diseñar `OrderStatus` como Enum con transiciones controladas.
- Solucionar race condition (optimistic locking o `synchronized`).
- Diseñar Strategy para métodos de pago.
- Discusión de eventos de dominio.

---

## Preguntas para hacer durante la sesión

### Para alumnos Junior
- "¿Qué retorna este endpoint si el producto no existe? ¿Es correcto?"
- "¿Cuál es la diferencia entre `double` y `BigDecimal`? ¿Cuándo importa?"
- "¿Por qué `String status == 'CONFIRMED'` puede no funcionar en Java?"
- "Si yo te mando un precio de -500, ¿qué pasa?"
- "¿Dónde deberían ir los logs de esta aplicación?"

### Para alumnos Middle
- "¿Qué problema puede ocurrir si este método no tiene `@Transactional`?"
- "¿Por qué un controller no debería acceder directamente al repositorio?"
- "¿Qué pasa si intento obtener los items de una orden fuera de una transacción activa?"
- "¿Por qué hay código duplicado entre `CustomerService` y `CustomerController`?"
- "¿Qué hace un `GlobalExceptionHandler`? ¿Cómo lo diseñarías?"

### Para alumnos Senior
- "¿Qué es una race condition? ¿Dónde ocurre en este código?"
- "¿Cómo harías que confirmar una orden sea idempotente?"
- "¿Qué cambiarías en la arquitectura para que agregar un nuevo método de pago no requiera modificar `PaymentService`?"
- "¿Qué es un modelo de dominio anémico? ¿Cómo lo mejorarías?"
- "¿Cómo desacoplarías las notificaciones del flujo principal de confirmación?"

---

## Puntos que revisar al final

- [ ] ¿El alumno identificó comparaciones con `==` en Strings?
- [ ] ¿Explica por qué `double` no es adecuado para dinero?
- [ ] ¿Propuso una solución para la race condition?
- [ ] ¿Sus tests nuevos validan comportamiento real?
- [ ] ¿Puede explicar qué es idempotencia con un ejemplo?
- [ ] ¿Entiende la diferencia entre `@Transactional` en servicio vs controller?
- [ ] ¿Propone un diseño extensible para descuentos o pagos?

---

## Guía por nivel de alumno

### Alumno Junior
Enfócate en que comprenda:
- Tipos de datos correctos (`BigDecimal`, Enum).
- Validaciones básicas con Bean Validation.
- Status HTTP semánticos.
- Inyección de dependencias por constructor.
- Logging básico con SLF4J.

### Alumno Middle
Enfócate en que comprenda:
- Separación de capas y responsabilidades.
- Manejo transaccional.
- Manejo global de excepciones.
- Diferencia entre entidad y DTO.
- Cómo mejorar tests con Mockito.

### Alumno Senior
Enfócate en que comprenda:
- Diseño de dominio rico vs anémico.
- Patrones: Strategy, Observer (eventos), State.
- Consistencia de datos y transacciones distribuidas.
- Observabilidad: logs estructurados, métricas, trazabilidad.
- Diseño para extensibilidad (Open/Closed Principle).

---

## Respuestas esperadas clave

**"¿Por qué `==` falla con Strings?"**
> En Java, `==` compara referencias, no contenido. Dos objetos `String` con el mismo texto pueden estar en diferentes ubicaciones de memoria. Usar `.equals()` compara el contenido. Aún mejor: usar un `Enum` para el estado evita el problema por completo.

**"¿Cómo resuelves la race condition del stock?"**
> Opciones: (1) `@Lock(LockModeType.PESSIMISTIC_WRITE)` en el repositorio — bloqueo pesimista; (2) `@Version` en la entidad — bloqueo optimista con reintento; (3) reducir el stock con una query de update atómica: `UPDATE products SET stock = stock - :qty WHERE id = :id AND stock >= :qty`.

**"¿Cómo harías extensible el sistema de pagos?"**
> Definir interfaz `PaymentProcessor` con método `process(PaymentRequest)`. Implementaciones: `CardPaymentProcessor`, `CashPaymentProcessor`, `PayPalProcessor`. Un `PaymentProcessorFactory` o inyección por Spring selecciona el correcto según el `paymentMethod`.
