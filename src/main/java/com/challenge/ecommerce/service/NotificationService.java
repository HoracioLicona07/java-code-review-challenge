package com.challenge.ecommerce.service;

import com.challenge.ecommerce.entity.PurchaseOrder;
import org.springframework.stereotype.Service;

// PROBLEMA Senior: servicio de notificación sin abstracción
// PROBLEMA Senior: acoplado directamente al dominio — recibe entidad JPA
// PROBLEMA Senior: sin estrategia para múltiples canales (email, SMS, push)
// PROBLEMA Senior: sin manejo de fallos de envío
@Service
public class NotificationService {

    // PROBLEMA Junior: System.out.println simula envío real — sin log estructurado
    // PROBLEMA Senior: no hay abstracción NotificationChannel
    public void sendOrderConfirmation(PurchaseOrder order) {
        System.out.println("====================================");
        System.out.println("ENVIANDO NOTIFICACION DE CONFIRMACION");
        System.out.println("Orden: " + order.getId());
        System.out.println("Cliente: " + order.getCustomer().getName());
        System.out.println("Total: " + order.getTotal());
        System.out.println("====================================");

        // TODO: integrar con servicio real de email
        // TODO: integrar con SMS
        // TODO: manejar errores de envío
    }

    // PROBLEMA Junior: método que existe pero no se usa desde ningún flujo
    public void sendPaymentConfirmation(Long orderId, double amount) {
        System.out.println("Pago confirmado para orden " + orderId + " monto: " + amount);
    }

    // PROBLEMA Middle: recibe entidad JPA directamente — acoplamiento
    public void sendCancellationNotification(PurchaseOrder order) {
        // PROBLEMA Junior: este método existe pero NUNCA se llama desde OrderService.cancelOrder()
        System.out.println("Orden cancelada: " + order.getId());
    }
}
