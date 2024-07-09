package ufanet.test_task.coffee_order_system.models;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@AllArgsConstructor
public class Order {
    private int id;
    private List<OrderEvent> orderEvents;

    public String getCurrentStatus(){
        if(orderEvents.isEmpty()){
            return "Not Ordered";
        } else{
            OrderEvent lastEvent = orderEvents.get(orderEvents.size()-1);
            return lastEvent.getClass().getSimpleName()
                    .replace("Event", "")
                    .replace("Order", "");
        }
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderEvents=" + orderEvents +
                '}';
    }

    public List<String> getEventsSummary(){
        return orderEvents.stream()
                .map(el -> String.format("%s at %s", el.getClass().getSimpleName(),el.getEventDateTime().toString()))
                .toList();
    }
}
