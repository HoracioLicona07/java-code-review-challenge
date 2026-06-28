# 03 — Guía para Alumnos

## Paso 1 — Levantar el proyecto

```bash
git clone https://github.com/TU_USUARIO/java-code-review-challenge.git
cd java-code-review-challenge
mvn spring-boot:run
```

Verifica que el servidor levante en `http://localhost:8080`.  
Abre `http://localhost:8080/swagger-ui.html` para explorar los endpoints.

---

## Paso 2 — Probar los endpoints

Antes de leer el código, **prueba el sistema** para entender su comportamiento:

### Flujo completo sugerido

1. **Crear un cliente:**
```bash
curl -X POST http://localhost:8080/api/customers \
  -H "Content-Type: application/json" \
  -d '{"name":"Juan Pérez","email":"juan@test.com","phone":"5512345678"}'
```

2. **Crear una orden:**
```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{"customerId": 1}'
```

3. **Agregar un producto a la orden:**
```bash
curl -X POST http://localhost:8080/api/orders/1/items \
  -H "Content-Type: application/json" \
  -d '{"productId": 1, "quantity": 2}'
```

4. **Confirmar la orden:**
```bash
curl -X POST http://localhost:8080/api/orders/1/confirm
```

5. **Procesar el pago:**
```bash
curl -X POST http://localhost:8080/api/payments/process \
  -H "Content-Type: application/json" \
  -d '{"orderId": 1, "amount": 25999.98, "paymentMethod": "CARD"}'
```

### Casos a probar deliberadamente

- ¿Qué pasa si confirmas la misma orden dos veces?
- ¿Qué pasa si agregas cantidad negativa?
- ¿Qué pasa si el producto no existe?
- ¿Qué pasa si el cliente no existe?
- ¿Qué status HTTP recibes cuando algo falla?
- ¿Qué pasa si cancelas una orden ya confirmada?

---

## Paso 3 — Revisar el código

Lee el código en este orden para entender el flujo:

1. `entity/` — Entidades JPA (modelo de datos)
2. `repository/` — Acceso a datos
3. `dto/` — Objetos de transferencia
4. `mapper/` — Conversión entre entidades y DTOs
5. `service/` — Lógica de negocio
6. `controller/` — Endpoints REST

---

## Paso 4 — Identificar problemas

Para cada problema que encuentres, documenta:

```
Archivo: service/OrderService.java
Línea: ~85
Nivel: Junior
Problema: Comparación de String con == en lugar de .equals()
Por qué es incorrecto: == compara referencias en Java; dos Strings con el mismo contenido
                       pueden tener referencias diferentes y la comparación fallaría.
Impacto: La validación de estado puede no funcionar correctamente en algunos contextos.
Solución propuesta: Cambiar a "CONFIRMED".equals(order.getStatus())
                    o mejor aún, usar un Enum OrderStatus.
```

---

## Paso 5 — Crear issues en GitHub

Para cada problema identificado, crea un issue en tu fork con:

- **Título:** `[Junior/Middle/Senior] Descripción corta del problema`
- **Body:** Archivo, línea, explicación, impacto y solución propuesta.
- **Label:** `bug`, `refactor`, `enhancement` según corresponda.

---

## Paso 6 — Proponer refactor

Antes de escribir código, documenta tu propuesta:

- ¿Qué cambiarías primero? ¿Por qué?
- ¿Qué cambios tienen mayor impacto en estabilidad?
- ¿Qué cambios son más urgentes para una revisión de seguridad?
- ¿Qué cambios requieren modificar tests?

---

## Paso 7 — Agregar pruebas

Los tests existentes tienen problemas intencionales. Tu tarea:

1. Identifica los tests que no validan comportamiento real.
2. Corrige los asserts vacíos o insuficientes.
3. Agrega tests para los flujos críticos que no tienen cobertura:
   - Confirmar orden con stock insuficiente.
   - Confirmar orden dos veces (idempotencia).
   - Cancelar orden confirmada (¿se restaura stock?).
   - Agregar item con cantidad negativa.
   - Crear cliente con email inválido.

---

## Paso 8 — Corregir errores

Trabaja en tu rama `student-solution`:

```bash
git checkout -b student-solution
```

Prioriza por impacto:
1. Errores que causan comportamiento incorrecto (bugs).
2. Errores de seguridad o consistencia de datos.
3. Errores de manejo de errores.
4. Mejoras de diseño.
5. Mejoras de estilo y nomenclatura.

---

## Paso 9 — Explicar decisiones técnicas

Prepárate para responder preguntas como:

- "¿Por qué usarías `BigDecimal` en lugar de `double` para dinero?"
- "¿Qué problema específico resuelve `@Transactional`?"
- "¿Cómo evitarías la race condition al actualizar el stock?"
- "¿Cuál es la diferencia entre `synchronized` y `@Lock(LockModeType.OPTIMISTIC)`?"
- "¿Cómo diseñarías la lógica de descuentos para que sea extensible?"
- "¿Qué es idempotencia y por qué importa al confirmar una orden?"

---

## Checklist de entrega

- [ ] Documento de hallazgos con todos los problemas identificados.
- [ ] Al menos 5 problemas Junior encontrados y corregidos.
- [ ] Al menos 3 problemas Middle encontrados y corregidos.
- [ ] Al menos 1 problema Senior identificado y propuesta de solución documentada.
- [ ] Tests mejorados con asserts reales.
- [ ] Al menos 3 nuevos tests para flujos no cubiertos.
- [ ] Rama `student-solution` con commits limpios y mensajes descriptivos.
