package com.challenge.ecommerce.service;

import com.challenge.ecommerce.dto.OrderDTO;
import com.challenge.ecommerce.entity.Customer;
import com.challenge.ecommerce.entity.OrderItem;
import com.challenge.ecommerce.entity.Product;
import com.challenge.ecommerce.entity.PurchaseOrder;
import com.challenge.ecommerce.mapper.OrderMapper;
import com.challenge.ecommerce.repository.CustomerRepository;
import com.challenge.ecommerce.repository.OrderRepository;
import com.challenge.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

// PROBLEMA Senior: God Service — hace demasiado:
//   maneja órdenes, valida stock, aplica descuentos, actualiza inventario, notifica
// PROBLEMA Middle: sin @Transactional en operaciones críticas
// PROBLEMA Senior: acoplamiento directo con repositorios de otras entidades
// PROBLEMA Junior: inyección por campo en todos lados
@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private NotificationService notificationService;

    public List<OrderDTO.OrderResponse> getAllOrders() {
        // PROBLEMA Middle: sin paginación — potencialmente miles de órdenes en memoria
        List<PurchaseOrder> orders = orderRepository.findAll();
        return orderMapper.toResponseList(orders);
    }

    public OrderDTO.OrderResponse getOrderById(Long id) {
        PurchaseOrder order = orderRepository.findById(id).orElse(null);
        // PROBLEMA Junior: null sin excepción adecuada
        if (order == null) return null;
        return orderMapper.toResponse(order);
    }

    public List<OrderDTO.OrderResponse> getOrdersByCustomer(Long customerId) {
        List<PurchaseOrder> orders = orderRepository.findByCustomerId(customerId);
        return orderMapper.toResponseList(orders);
    }

    public OrderDTO.OrderResponse createOrder(OrderDTO.CreateOrderRequest request) {
        System.out.println("Creando orden para cliente: " + request.getCustomerId());

        // PROBLEMA Junior: sin validación de que el cliente exista antes
        Customer customer = customerRepository.findById(request.getCustomerId()).orElse(null);
        if (customer == null) {
            // PROBLEMA Junior: RuntimeException genérico sin contexto
            throw new RuntimeException("Cliente no encontrado");
        }

        // PROBLEMA Junior: no verifica si el cliente está activo
        PurchaseOrder order = new PurchaseOrder(customer);
        PurchaseOrder saved = orderRepository.save(order);
        System.out.println("Orden creada con id: " + saved.getId());
        return orderMapper.toResponse(saved);
    }

    // PROBLEMA Senior: método largo, múltiples responsabilidades
    // PROBLEMA Middle: sin @Transactional — si falla a mitad puede quedar stock inconsistente
    // PROBLEMA Senior: problema de concurrencia — dos hilos pueden pasar el check de stock
    //                  al mismo tiempo y ambos descontar (race condition)
    public OrderDTO.OrderResponse addItemToOrder(Long orderId, OrderDTO.AddItemRequest request) {
        System.out.println("Agregando item a orden: " + orderId);

        PurchaseOrder order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            throw new RuntimeException("Orden no encontrada");
        }

        // PROBLEMA Junior: comparación de String con == en lugar de .equals()
        if (order.getStatus() == "CONFIRMED" || order.getStatus() == "CANCELLED") {
            throw new RuntimeException("No se puede modificar una orden confirmada o cancelada");
        }

        Product product = productRepository.findById(request.getProductId()).orElse(null);
        if (product == null) {
            throw new RuntimeException("Producto no encontrado");
        }

        // PROBLEMA Junior: sin validación de cantidad > 0
        // PROBLEMA Junior: sin validación de stock disponible al agregar item
        if (request.getQuantity() <= 0) {
            // PROBLEMA Junior: mensaje de error poco útil
            throw new RuntimeException("Error en cantidad");
        }

        // PROBLEMA Middle: no verifica si el producto ya está en la orden
        // (podrían existir dos items con el mismo producto)
        OrderItem item = new OrderItem(product, request.getQuantity());
        item.setOrder(order);
        order.getItems().add(item);

        // PROBLEMA Senior: recalculate sin transacción — datos pueden quedar sucios
        order.recalculateTotal();
        order.setUpdatedAt(LocalDateTime.now());

        PurchaseOrder saved = orderRepository.save(order);
        return orderMapper.toResponse(saved);
    }

    // PROBLEMA Senior: confirmOrder sin idempotencia — se puede confirmar varias veces
    // PROBLEMA Senior: problema de concurrencia en descuento de stock
    // PROBLEMA Middle: sin @Transactional
    // PROBLEMA Junior: método de 60+ líneas — demasiado largo
    public OrderDTO.OrderResponse confirmOrder(Long orderId) {
        System.out.println("Confirmando orden: " + orderId);

        PurchaseOrder order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            throw new RuntimeException("Orden no encontrada");
        }

        // PROBLEMA Junior: comparación con == en lugar de .equals()
        if (order.getStatus() == "CONFIRMED") {
            // PROBLEMA Senior: NO hay idempotencia real — lanza error pero debería retornar la orden ya confirmada
            throw new RuntimeException("La orden ya está confirmada");
        }

        // PROBLEMA Junior: comparación con == de nuevo
        if (order.getStatus() == "CANCELLED") {
            throw new RuntimeException("No se puede confirmar una orden cancelada");
        }

        // PROBLEMA Junior: si la orden no tiene items, se confirma vacía
        // No hay validación de que la orden tenga al menos un item

        // PROBLEMA Senior: race condition — otro hilo puede leer el mismo stock
        // antes de que este hilo lo actualice
        for (OrderItem item : order.getItems()) {
            Product product = item.getProduct();
            // PROBLEMA Junior: sin validación de stock en el momento de confirmar
            int newStock = product.getStock() - item.getQuantity();
            if (newStock < 0) {
                // PROBLEMA Junior: mensaje no indica qué producto falló
                throw new RuntimeException("Stock insuficiente");
            }
            product.setStock(newStock);
            product.setUpdatedAt(LocalDateTime.now());
            productRepository.save(product);
        }

        // PROBLEMA Junior: descuento hardcodeado con magic number
        // PROBLEMA Senior: regla de negocio mezclada dentro del método de confirmación
        double descuento = 0;
        if (order.getSubtotal() > 5000) {
            // 10% de descuento para compras mayores a 5000
            descuento = order.getSubtotal() * 0.10;
        } else if (order.getItems().size() >= 3) {
            // 5% si tiene 3 o más items — magic number
            descuento = order.getSubtotal() * 0.05;
        }

        order.setDiscount(descuento);
        order.setTotal(order.getSubtotal() - descuento);
        order.setStatus("CONFIRMED");
        order.setUpdatedAt(LocalDateTime.now());

        PurchaseOrder saved = orderRepository.save(order);

        // PROBLEMA Middle: acoplamiento fuerte con NotificationService
        // Si la notificación falla, toda la operación falla
        try {
            notificationService.sendOrderConfirmation(saved);
        } catch (Exception e) {
            // PROBLEMA Junior: catch que oculta el error silenciosamente
            System.out.println("Error al enviar notificación: " + e.getMessage());
        }

        System.out.println("Orden confirmada: " + saved.getId());
        return orderMapper.toResponse(saved);
    }

    // PROBLEMA Senior: no hay regla de negocio que impida cancelar una orden ya enviada
    // PROBLEMA Middle: cancelación no restaura el stock si ya fue confirmada
    public OrderDTO.OrderResponse cancelOrder(Long orderId) {
        PurchaseOrder order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            throw new RuntimeException("Orden no encontrada");
        }

        // PROBLEMA Middle: se puede cancelar una orden CONFIRMED sin devolver stock
        // PROBLEMA Senior: no hay máquina de estados — cualquier transición es posible
        order.setStatus("CANCELLED");
        order.setUpdatedAt(LocalDateTime.now());

        PurchaseOrder saved = orderRepository.save(order);

        // PROBLEMA Junior: notificación de cancelación nunca se envía
        // (se olvidaron de llamar a notificationService aquí)

        return orderMapper.toResponse(saved);
    }
}
