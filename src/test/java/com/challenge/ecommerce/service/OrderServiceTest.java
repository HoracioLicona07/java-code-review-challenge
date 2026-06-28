package com.challenge.ecommerce.service;

import com.challenge.ecommerce.dto.OrderDTO;
import com.challenge.ecommerce.entity.Customer;
import com.challenge.ecommerce.entity.PurchaseOrder;
import com.challenge.ecommerce.mapper.OrderMapper;
import com.challenge.ecommerce.repository.CustomerRepository;
import com.challenge.ecommerce.repository.OrderRepository;
import com.challenge.ecommerce.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private OrderService orderService;

    private Customer sampleCustomer;
    private PurchaseOrder sampleOrder;

    @BeforeEach
    void setUp() {
        sampleCustomer = new Customer("Carlos", "carlos@test.com", "5512345678");
        sampleCustomer.setId(1L);

        sampleOrder = new PurchaseOrder(sampleCustomer);
        sampleOrder.setId(1L);
    }

    @Test
    void testCreateOrder_whenCustomerExists() {
        OrderDTO.CreateOrderRequest request = new OrderDTO.CreateOrderRequest();
        request.setCustomerId(1L);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(sampleCustomer));
        when(orderRepository.save(any())).thenReturn(sampleOrder);

        OrderDTO.OrderResponse mockResponse = new OrderDTO.OrderResponse();
        mockResponse.setId(1L);
        when(orderMapper.toResponse(any())).thenReturn(mockResponse);

        OrderDTO.OrderResponse result = orderService.createOrder(request);

        // PROBLEMA Junior: no verifica el status inicial de la orden
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    // PROBLEMA Junior: no hay test para createOrder cuando el cliente NO existe
    // Debería verificar que lanza excepción apropiada

    // PROBLEMA Junior: no hay test para confirmOrder
    // El flujo más crítico del sistema no tiene prueba

    // PROBLEMA Junior: no hay test para verificar que el stock se descuenta al confirmar

    // PROBLEMA Junior: test mal escrito — no verifica comportamiento real
    @Test
    void testCancelOrder() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(sampleOrder));
        when(orderRepository.save(any())).thenReturn(sampleOrder);
        when(orderMapper.toResponse(any())).thenReturn(new OrderDTO.OrderResponse());

        OrderDTO.OrderResponse result = orderService.cancelOrder(1L);

        // PROBLEMA Junior: no verifica que el status cambió a CANCELLED
        assertNotNull(result);
    }

    // Test que sí está bien — punto de comparación
    @Test
    void testCreateOrder_whenCustomerNotExists_shouldThrowException() {
        OrderDTO.CreateOrderRequest request = new OrderDTO.CreateOrderRequest();
        request.setCustomerId(99L);

        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> orderService.createOrder(request));

        assertEquals("Cliente no encontrado", ex.getMessage());
        verify(orderRepository, never()).save(any());
    }
}
