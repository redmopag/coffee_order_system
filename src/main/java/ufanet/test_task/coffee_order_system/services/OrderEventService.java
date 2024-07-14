package ufanet.test_task.coffee_order_system.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.RuntimeJsonMappingException;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ufanet.test_task.coffee_order_system.models.Order;
import ufanet.test_task.coffee_order_system.models.OrderStatus;
import ufanet.test_task.coffee_order_system.events.OrderEvent;
import ufanet.test_task.coffee_order_system.repositories.OrderEventRepository;

import java.util.*;

@Service
@Slf4j
public class OrderEventService implements OrderService{
    private final OrderEventRepository orderEventRepository;
    private final ObjectMapper objectMapper;
    private final List<OrderStatus> orderStatuses;

    @Autowired
    public OrderEventService(OrderEventRepository orderEventRepository) {
        this.orderEventRepository = orderEventRepository;

        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());

        this.orderStatuses = setOrderStatuses();
    }

    /*
    Думал на счёт паттерна цепочки ответсвенности, но отличия будут только в типе события,
    а проверка везде одна и та же, так что решил его не использовать
    */
    protected void checkEvent(Iterator<OrderEvent> it,
                            OrderEvent event,
                            Iterator<OrderStatus> statusIterator) throws IllegalArgumentException
    {
        OrderStatus status = statusIterator.next();
        if(!it.hasNext() && event.getEventType() != status){
            String errorMessage = "Событию " + event + " должно предшествовать событие регистрации заказа";
            log.error(errorMessage);
            throw new IllegalStateException(errorMessage);
        } else if(it.hasNext() && event.getEventType() == status){
            String errorMessage = "Событие: " + event + " уже произошло для указанного заказа";
            log.error(errorMessage);
            throw new IllegalStateException(errorMessage);
        } else if(it.hasNext() && event.getEventType() != OrderStatus.CANCELED){
            it.next();
            checkEvent(it, event, statusIterator);
        }
    }

    protected List<OrderStatus> setOrderStatuses(){
        List<OrderStatus> orderStatuses = new LinkedList<>();
        orderStatuses.add(OrderStatus.REGISTERED);
        orderStatuses.add(OrderStatus.TAKEN);
        orderStatuses.add(OrderStatus.READY);
        orderStatuses.add(OrderStatus.ISSUED);

        return orderStatuses;
    }

    @Override
    public void publishEvent(OrderEvent event) throws IllegalStateException, RuntimeJsonMappingException {
        log.info("Получено событие для публикации: {}", event.toString());

        List<OrderEvent> events = getEvents(event.getOrderId());
        checkEvent(events.iterator(), event, orderStatuses.iterator());

        try {
            saveEvent(event);
        } catch (JsonProcessingException e) {
            String errorMessage = "Событие " + event + "не удалось опубликовать из-за его неудачной сериализации:" +
                    e.getMessage();
            log.error(errorMessage);
            throw new RuntimeJsonMappingException(errorMessage);
        }
    }

    private void saveEvent(OrderEvent event) throws JsonProcessingException {
        event.setEventData(serializeEvent(event));
        log.info("Событие, которое будет сохранено в базе данных: {}", event);
        orderEventRepository.save(event);
    }

    @Override
    public Order findOrder(int id) throws NoSuchElementException, RuntimeJsonMappingException {
        log.info("Получение заказа по его id = {}", id);
        List<OrderEvent> events;

        try {
            events = getEvents(id);
        } catch (RuntimeJsonMappingException e) {
            log.error(e.getMessage());
            throw e;
        }

        if(events.isEmpty()){
            log.error("Не удалось найти элемент по его id: {}", id);
            throw new NoSuchElementException("Не удалось найти элемент по его id: " + id);
        }
        return new Order(id, events);
    }

    /*
    Оаставил public для тестов
     */
    private List<OrderEvent> getEvents(int orderId) throws RuntimeJsonMappingException {
        return orderEventRepository.findAllByOrderIdOrderByEventDateTime(orderId)
                .stream()
                .map(orderEvent -> {
                    try {
                        return deserializeEvent(orderEvent.getEventData(), orderEvent.getClass());
                    } catch (JsonProcessingException|IllegalArgumentException e) {
                        throw new RuntimeJsonMappingException("Неудачная попытка десериализации" +
                                " полученного из базы данных события: " +
                                orderEvent + ": " +
                                e.getMessage());
                    }
                })
                .toList();
    }

    private String serializeEvent(OrderEvent orderEvent) throws JsonProcessingException {
        return objectMapper.writeValueAsString(orderEvent);
    }


    private OrderEvent deserializeEvent(String eventData,
                                       Class<? extends OrderEvent> type) throws JsonProcessingException
    {
        return objectMapper.readValue(eventData, type);
    }
}
