package ufanet.test_task.coffee_order_system.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ufanet.test_task.coffee_order_system.models.Order;
import ufanet.test_task.coffee_order_system.models.OrderCanceledEvent;
import ufanet.test_task.coffee_order_system.models.OrderEvent;
import ufanet.test_task.coffee_order_system.models.OrderRegisteredEvent;
import ufanet.test_task.coffee_order_system.repositories.OrderCanceledEventRepository;
import ufanet.test_task.coffee_order_system.repositories.OrderEventRepository;
import ufanet.test_task.coffee_order_system.repositories.OrderIssuedEventRepository;
import ufanet.test_task.coffee_order_system.repositories.OrderRegisteredEventRepository;

import java.util.List;

@Service
@Transactional
public class BaseOrderService implements OrderService{
    private final OrderEventRepository orderEventRepository;
    private final OrderRegisteredEventRepository orderRegisteredEventRepository;
    private final OrderCanceledEventRepository orderCanceledEventRepository;
    private final OrderIssuedEventRepository orderIssuedEventRepository;

    @Autowired
    public BaseOrderService(OrderEventRepository orderEventRepository,
                            OrderRegisteredEventRepository orderRegisteredEventRepository,
                            OrderCanceledEventRepository orderCanceledEventRepository,
                            OrderIssuedEventRepository orderIssuedEventRepository) {
        this.orderEventRepository = orderEventRepository;
        this.orderRegisteredEventRepository = orderRegisteredEventRepository;
        this.orderCanceledEventRepository = orderCanceledEventRepository;
        this.orderIssuedEventRepository = orderIssuedEventRepository;
    }


    @Override
    public void publishEvent(OrderEvent event) {
        int orderId = event.getOrderId();
        if(orderRegisteredEventRepository.existsById(orderId)) {
            if(!orderCanceledEventRepository.existsById(orderId) && !orderIssuedEventRepository.existsById(orderId)) {
                orderEventRepository.save(event);
            }
        } else if(event instanceof OrderRegisteredEvent) {
            orderEventRepository.save(event);
        }
    }

    @Override
    public Order findOrder(int id) {
        List<OrderEvent> events = orderEventRepository.findAllByOrderIdOrderByEventDateTime(id);
        return new Order(id, events);
    }
}
