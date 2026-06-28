# 02 — Lista de Problemas Intencionales

> **Instrucción para alumnos:** Intenta encontrar estos problemas leyendo el código antes de revisar la sección del instructor al final del documento.

---

## Problemas de nivel Junior

Estos son problemas de estilo, tipo de datos incorrecto, validaciones faltantes y malas prácticas básicas.

1. **Uso de `double` para representar dinero** — en lugar del tipo correcto para operaciones monetarias.
2. **Comparación de Strings con `==`** — en lugar del método de comparación por contenido.
3. **`System.out.println` en lugar de logging estructurado** — aparece en múltiples clases.
4. **Retornar `null` en lugar de lanzar excepción** — cuando un recurso no se encuentra.
5. **Respuestas HTTP incorrectas** — usar 200 donde debería ser 201 ó 404.
6. **Falta de validaciones básicas** — campos requeridos, cantidades positivas, precios no negativos.
7. **`LocalDateTime.now()` disperso por todo el código** — en lugar de anotaciones de auditoría.
8. **Magic numbers en reglas de negocio** — valores numéricos sin constante nombrada.
9. **Inyección de dependencias por campo** — en lugar del patrón correcto para Spring.
10. **Métodos demasiado largos** — con múltiples responsabilidades en un solo bloque.
11. **Código muerto** — métodos que existen pero nunca se llaman.
12. **Comentarios `TODO` que llegaron a producción** — sin fecha ni responsable.
13. **Sin validación de cantidades menores o iguales a cero** al agregar items.
14. **Sin validación de stock negativo** al crear o actualizar un producto.
15. **Mensajes de error genéricos e imprecisos** que no ayudan al diagnóstico.

---

## Problemas de nivel Middle

Estos son problemas de arquitectura de capas, servicios con muchas responsabilidades y ausencia de patrones correctos.

1. **Controller con lógica de negocio** — hay un endpoint que valida, lee repositorio y persiste.
2. **Controller accede directamente al repositorio** — saltándose la capa de servicio.
3. **Servicio de órdenes con demasiadas responsabilidades** — gestión, stock, descuentos, notificaciones.
4. **Mapper incompleto** — no mapea todos los campos de una entidad.
5. **Código de conversión duplicado** — la misma lógica en el servicio y en el controller.
6. **Entidad JPA expuesta directamente** en la respuesta de un endpoint.
7. **Sin `@Transactional`** en operaciones de escritura críticas.
8. **Uso incorrecto de `Optional`** — se hace `.orElse(null)` y luego se verifica el null manualmente.
9. **Sin paginación** en listados que podrían crecer indefinidamente.
10. **Tests que no verifican comportamiento real** — solo comprueban que el resultado no es null.
11. **Tests que documentan el bug en lugar de el comportamiento correcto**.
12. **Posible `LazyInitializationException`** en el mapper al acceder a colecciones lazy fuera de transacción.
13. **Sin manejo global de excepciones** — RuntimeExceptions producen 500 en lugar de respuestas estructuradas.
14. **Acoplamiento fuerte entre servicios** — si NotificationService falla, la operación principal falla.
15. **Validaciones duplicadas** — en el controller y en el servicio.

---

## Problemas de nivel Senior

Estos son problemas de diseño de dominio, arquitectura, concurrencia y extensibilidad.

1. **Modelo de dominio anémico** — las entidades no tienen comportamiento de negocio; todo está en los servicios.
2. **Status de orden como `String`** — sin máquina de estados controlada; cualquier transición es posible.
3. **Sin idempotencia al confirmar una orden** — confirmar dos veces la misma orden causa inconsistencia.
4. **Race condition al actualizar stock** — dos requests simultáneos pueden descontar el mismo inventario.
5. **Cancelación sin restaurar stock** — si se cancela una orden confirmada, el inventario queda incorrecto.
6. **Sin estrategia extensible para métodos de pago** — imposible agregar nuevos métodos sin modificar el servicio.
7. **Reglas de descuento hardcodeadas** dentro del método `confirmOrder` — sin abstracción ni configuración.
8. **Relación entre Payment y PurchaseOrder rota** — usa `Long orderId` en lugar de relación JPA.
9. **`NotificationService` recibe entidad JPA directamente** — acoplamiento entre infraestructura y dominio.
10. **Sin logs estructurados** — `System.out.println` no permite búsqueda, correlación ni alertas.
11. **Sin estrategia de retry** para fallos del procesador de pagos simulado.
12. **`PaymentService` modifica el estado de la orden** — violación de separación de responsabilidades.
13. **Falta de observabilidad** — sin trazabilidad de solicitudes ni métricas de negocio.
14. **Sin versionado de API** — `/api/products` en lugar de `/api/v1/products`.
15. **Sin control de concurrencia optimista** — sin `@Version` en entidades críticas.
16. **`NotificationService.sendCancellationNotification` nunca se llama** — inconsistencia de negocio.
17. **Sin eventos de dominio** — los efectos secundarios (notificaciones, stock) están acoplados al flujo principal.
18. **Endpoint de debugging expuesto** — `/api/customers/{id}/raw` que siempre devuelve null.
19. **Sin manejo de errores de integración** — fallo del procesador externo no tiene respuesta controlada.
20. **Procesador de pago no es testeable** — `Math.random()` hardcodeado hace imposible el determinismo en tests.

---

---

## Guía del instructor: ubicación sugerida de problemas

> **Esta sección es solo para instructores.** No compartir con alumnos antes de la revisión.

### Junior

| # | Archivo | Clase / Método | Problema |
|---|---------|----------------|---------|
| 1 | `entity/Product.java` | campo `price` | `double` en lugar de `BigDecimal` |
| 2 | `service/OrderService.java` | `addItemToOrder`, `confirmOrder`, `cancelOrder` | `order.getStatus() == "CONFIRMED"` — comparación con `==` |
| 3 | `service/ProductService.java` | todos los métodos | `System.out.println` generalizado |
| 4 | `service/ProductService.java` | `getProductById` | retorna `null` en lugar de lanzar excepción |
| 5 | `controller/ProductController.java` | `createProduct` | retorna `200` en lugar de `201` |
| 6 | `controller/ProductController.java` | `getProductById` | retorna `200` con `null` en lugar de `404` |
| 7 | `dto/CustomerDTO.java` | `CustomerRequest.email` | sin `@Email` ni validación de formato |
| 8 | `entity/Product.java` | constructor | `LocalDateTime.now()` hardcodeado |
| 9 | `service/OrderService.java` | `confirmOrder` | magic numbers `5000`, `0.10`, `0.05`, `3` |
| 10 | Todos los servicios | constructor vs `@Autowired` | inyección por campo |
| 11 | `service/ProductService.java` | `checkAvailability` | método `@Deprecated` nunca llamado |
| 12 | `service/OrderService.java` | `addItemToOrder` | sin validación de `quantity > 0` |
| 13 | `mapper/ProductMapper.java` | `toResponse` | `description` comentada — campo no mapeado |
| 14 | `service/ProductService.java` | `updateProduct` | sobreescribe campos con null si no se mandan |
| 15 | `service/OrderService.java` | `confirmOrder` | confirmar orden vacía es posible |

### Middle

| # | Archivo | Clase / Método | Problema |
|---|---------|----------------|---------|
| 1 | `controller/ProductController.java` | `updateStock` | lógica de negocio en controller |
| 2 | `controller/ProductController.java` | `getByCategory`, `updateStock` | acceso directo a `ProductRepository` |
| 3 | `service/OrderService.java` | clase completa | God Service — demasiadas responsabilidades |
| 4 | `mapper/ProductMapper.java` | `toResponse` | no mapea `createdAt`, `description` |
| 5 | `service/CustomerService.java` | `toResponse` (privado) | duplicado también en `CustomerController` |
| 6 | `controller/ProductController.java` | `getByCategory` | expone `List<Product>` (entidad) |
| 7 | `service/OrderService.java` | `addItemToOrder`, `confirmOrder` | sin `@Transactional` |
| 8 | Todos los servicios | `.orElse(null)` + check | uso incorrecto de Optional |
| 9 | `repository/ProductRepository.java` | `findAll()`, `findByActiveTrue()` | sin paginación |
| 10 | `service/ProductServiceTest.java` | `testGetAllProducts`, `testDeleteProduct` | asserts vacíos |
| 11 | `controller/ProductControllerTest.java` | `testGetProductById_whenNotExists` | test documenta el bug en lugar de el contrato |
| 12 | `mapper/OrderMapper.java` | `toResponse` | `items` lazy sin `@Transactional` → `LazyInitializationException` |
| 13 | Ninguna clase | — | falta `@ControllerAdvice` / `GlobalExceptionHandler` |
| 14 | `service/OrderService.java` | `confirmOrder` | `notificationService` acoplado directamente |
| 15 | `service/PaymentService.java` | `processPayment` | valida `order.getStatus() != "CONFIRMED"` con `!=` |

### Senior

| # | Archivo | Clase / Método | Problema |
|---|---------|----------------|---------|
| 1 | `entity/PurchaseOrder.java` | clase | status como `String` sin máquina de estados |
| 2 | `service/OrderService.java` | `confirmOrder` | doble confirmación no idempotente |
| 3 | `service/OrderService.java` | `confirmOrder` | race condition en descuento de stock |
| 4 | `service/OrderService.java` | `cancelOrder` | no restaura stock si ya se confirmó |
| 5 | `service/PaymentService.java` | `simulatePaymentProcessor` | sin Strategy pattern — no extensible |
| 6 | `service/OrderService.java` | `confirmOrder` | reglas de descuento hardcodeadas |
| 7 | `entity/Payment.java` | campo `orderId` | relación rota — debería ser `@ManyToOne` |
| 8 | `service/NotificationService.java` | clase | recibe entidad JPA — acoplamiento infra/dominio |
| 9 | `service/OrderService.java` | `cancelOrder` | no llama a `sendCancellationNotification` |
| 10 | `service/PaymentService.java` | `processPayment` | modifica estado de orden desde PaymentService |
| 11 | `service/PaymentService.java` | `simulatePaymentProcessor` | `Math.random()` no testeable |
| 12 | `controller/CustomerController.java` | `getCustomerRaw` | endpoint de debugging siempre retorna null |
| 13 | Ninguna clase | — | sin `@Version` para optimistic locking |
| 14 | Ninguna clase | — | sin versionado de API (`/v1/`) |
| 15 | Ninguna clase | — | sin eventos de dominio (ApplicationEvent) |
