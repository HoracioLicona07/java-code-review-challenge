# 06 — Roadmap de Refactorización

Este roadmap propone un orden lógico de mejoras, de lo más urgente a lo más arquitectural.

---

## Fase 1 — Correcciones básicas (Junior)

**Objetivo:** Estabilizar el código con correcciones de impacto inmediato.

### Tareas
- [ ] Reemplazar `double` por `BigDecimal` en todas las entidades y DTOs (`price`, `amount`, `subtotal`, `discount`, `total`, `unitPrice`).
- [ ] Reemplazar `System.out.println` por `private static final Logger log = LoggerFactory.getLogger(...)`.
- [ ] Corregir comparaciones `String == "..."` por `.equals()` o migrar a Enum.
- [ ] Corregir respuestas HTTP: `201 Created` para POST, `404 Not Found` para recursos no encontrados, `204 No Content` para DELETE.
- [ ] Eliminar código muerto (`checkAvailability`, endpoint `/raw`).
- [ ] Agregar `@Column(nullable = false)` y longitudes máximas en entidades.

**Criterio de éxito:** Los endpoints retornan los status HTTP correctos. No hay `System.out.println` en ninguna clase.

---

## Fase 2 — Validaciones (Junior/Middle)

**Objetivo:** Garantizar que solo datos válidos entren al sistema.

### Tareas
- [ ] Agregar `@NotBlank`, `@NotNull`, `@Positive`, `@Min(1)` en todos los DTOs de request.
- [ ] Agregar `@Email` en `CustomerRequest.email`.
- [ ] Activar `@Valid` en todos los `@RequestBody` de los controllers.
- [ ] Agregar validación de stock no negativo en `ProductRequest`.
- [ ] Validar que `quantity > 0` al agregar items.
- [ ] Validar que el precio de un producto sea positivo.

**Criterio de éxito:** Enviar datos inválidos retorna `400 Bad Request` con mensaje descriptivo.

---

## Fase 3 — DTOs y Mappers (Middle)

**Objetivo:** Nunca exponer entidades JPA directamente.

### Tareas
- [ ] Completar `ProductMapper.toResponse` — mapear `description` y `createdAt`.
- [ ] Crear `CustomerMapper` (eliminar código duplicado entre `CustomerService` y `CustomerController`).
- [ ] Crear `PaymentMapper`.
- [ ] Eliminar el endpoint `GET /api/customers/{id}/raw` que expone entidad.
- [ ] Eliminar `GET /api/products/category/{category}` que retorna `List<Product>` (entidad).
- [ ] Reemplazar con versión que use DTO.

**Criterio de éxito:** Ningún endpoint retorna una entidad JPA directamente.

---

## Fase 4 — Manejo global de excepciones (Middle)

**Objetivo:** Respuestas de error consistentes y sin stack traces expuestos al cliente.

### Tareas
- [ ] Crear excepciones custom:
  - `ProductNotFoundException extends RuntimeException`
  - `CustomerNotFoundException extends RuntimeException`
  - `OrderNotFoundException extends RuntimeException`
  - `InsufficientStockException extends RuntimeException`
  - `InvalidOrderStateException extends RuntimeException`
- [ ] Crear `GlobalExceptionHandler` con `@ControllerAdvice`.
- [ ] Mapear cada excepción a su status HTTP correcto.
- [ ] Crear `ErrorResponse` DTO con `timestamp`, `status`, `error`, `message`, `path`.
- [ ] Remover todos los retornos de `null` de servicios — reemplazar por excepciones.

**Criterio de éxito:** Todos los errores de negocio retornan un JSON estructurado con el status HTTP adecuado.

---

## Fase 5 — Transacciones (Middle/Senior)

**Objetivo:** Garantizar consistencia de datos en operaciones críticas.

### Tareas
- [ ] Agregar `@Transactional` en `OrderService.addItemToOrder`.
- [ ] Agregar `@Transactional` en `OrderService.confirmOrder`.
- [ ] Agregar `@Transactional` en `OrderService.cancelOrder`.
- [ ] Agregar `@Transactional` en `PaymentService.processPayment`.
- [ ] Agregar `@Transactional(readOnly = true)` en métodos de solo lectura.
- [ ] Mover `@Transactional` de controllers a servicios si existe en algún controller.

**Criterio de éxito:** Si falla cualquier paso de `confirmOrder`, ningún cambio queda persistido.

---

## Fase 6 — Testing (Junior/Middle)

**Objetivo:** Tests que documenten y protejan el comportamiento correcto.

### Tareas
- [ ] Corregir `testGetAllProducts` — agregar aserciones de contenido.
- [ ] Corregir `testDeleteProduct` — agregar `verify(productRepository).deleteById(1L)`.
- [ ] Agregar test: confirmar orden con stock insuficiente → `InsufficientStockException`.
- [ ] Agregar test: confirmar orden ya confirmada → comportamiento idempotente o excepción.
- [ ] Agregar test: cancelar orden confirmada → verificar comportamiento de stock.
- [ ] Agregar test: agregar item con cantidad `0` → `400 Bad Request`.
- [ ] Agregar test: crear cliente con email inválido → `400 Bad Request`.
- [ ] Corregir `testGetProductById_whenNotExists` — esperar excepción, no null.
- [ ] Agregar test de integración con `@SpringBootTest` para el flujo completo.

**Criterio de éxito:** Cobertura de líneas > 80% en la capa de servicios. Todos los flujos críticos tienen al menos un test.

---

## Fase 7 — Separación de responsabilidades (Middle/Senior)

**Objetivo:** Cada clase hace una sola cosa.

### Tareas
- [ ] Extraer lógica de descuentos de `OrderService` a `DiscountCalculator` o `DiscountStrategy`.
- [ ] Extraer actualización de stock a `InventoryService`.
- [ ] Extraer la verificación de estado de orden a un componente de validación.
- [ ] Mover `order.setStatus("PAID")` de `PaymentService` a `OrderService` (u `OrderStatusManager`).
- [ ] Eliminar acceso directo al repositorio desde `ProductController`.

**Criterio de éxito:** `OrderService` tiene menos de 100 líneas. Cada servicio tiene una responsabilidad clara.

---

## Fase 8 — Mejoras Senior (Arquitectura y Dominio)

**Objetivo:** Diseño extensible, consistente y preparado para producción.

### Tareas
- [ ] Crear `OrderStatus` como Enum con transiciones válidas explícitas:
```java
public enum OrderStatus {
    PENDING, CONFIRMED, CANCELLED, PAID, SHIPPED;

    public boolean canTransitionTo(OrderStatus next) { ... }
}
```
- [ ] Agregar `@Version` en `Product` y `PurchaseOrder` para optimistic locking.
- [ ] Crear interfaz `PaymentProcessor` e implementaciones por método de pago (Strategy).
- [ ] Desacoplar `NotificationService` usando `ApplicationEventPublisher` de Spring.
- [ ] Corregir relación `Payment` → `PurchaseOrder` con `@ManyToOne` real.
- [ ] Agregar versionado: `/api/v1/...`.
- [ ] Agregar paginación con `Pageable` en listados.
- [ ] Agregar `@CreationTimestamp` y `@UpdateTimestamp` en entidades.
- [ ] Agregar idempotency: verificar si ya existe un pago para la orden antes de procesar.
- [ ] Restaurar stock al cancelar una orden que ya fue confirmada.
- [ ] Agregar logs estructurados con MDC para correlación de requests.

**Criterio de éxito:** El sistema puede manejar confirmaciones concurrentes sin inconsistencias. Agregar un nuevo método de pago no requiere modificar código existente.

---

## Resumen del roadmap

```
Fase 1 → Correcciones básicas         (1-2h)  🟢 Urgente
Fase 2 → Validaciones                 (1h)    🟢 Urgente
Fase 3 → DTOs y Mappers               (2h)    🟡 Importante
Fase 4 → Manejo de excepciones        (1.5h)  🟡 Importante
Fase 5 → Transacciones                (1h)    🔴 Crítico
Fase 6 → Testing                      (3h)    🟡 Importante
Fase 7 → Separación de responsabilidades (3h) 🟠 Técnico
Fase 8 → Arquitectura y Dominio       (5h+)   🔵 Senior
```
