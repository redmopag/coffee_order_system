package ufanet.test_task.coffee_order_system.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ufanet.test_task.coffee_order_system.events.*;
import ufanet.test_task.coffee_order_system.models.Order;
import ufanet.test_task.coffee_order_system.services.OrderService;

@RestController
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/registered")
    public void pushOrderEvent(@RequestBody OrderRegisteredEvent orderEvent) {
        orderService.publishEvent(orderEvent);
    }

    @PostMapping("/issued")
    public void pushOrderEvent(@RequestBody OrderIssuedEvent orderEvent) {
        orderService.publishEvent(orderEvent);
    }

    @PostMapping("/ready")
    public void pushOrderEvent(@RequestBody OrderReadyEvent orderEvent) {
        orderService.publishEvent(orderEvent);
    }

    @PostMapping("/taken")
    public void pushOrderEvent(@RequestBody OrderTakenInWorkEvent orderEvent) {
        orderService.publishEvent(orderEvent);
    }

    @PostMapping("/canceled")
    public void pushOrderEvent(@RequestBody OrderCanceledEvent orderEvent) {
        orderService.publishEvent(orderEvent);
    }

    @GetMapping()
    public Order getOrder(@RequestParam int orderId) {
        return orderService.findOrder(orderId);
    }
}
