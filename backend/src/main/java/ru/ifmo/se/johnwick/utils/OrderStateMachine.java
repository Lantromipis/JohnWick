package ru.ifmo.se.johnwick.utils;

import jakarta.enterprise.context.ApplicationScoped;
import ru.ifmo.se.johnwick.entity.OrderEntity;
import ru.ifmo.se.johnwick.model.OrderStatus;

@ApplicationScoped
public class OrderStateMachine {

    public void createStatusOrder(OrderEntity order) {
            order.setStatus(OrderStatus.CREATED);
    }


    public void processStatusOrder(OrderEntity order) {
        if (order.getStatus() == OrderStatus.CREATED) {
            order.setStatus(OrderStatus.PROCESSING);
        } else {
            throw new IllegalStateException("Cannot process order in status: " + order.getStatus());
        }
    }

    public void completeStatusOrder(OrderEntity order) {
        if (order.getStatus() == OrderStatus.PROCESSING) {
            order.setStatus(OrderStatus.COMPLETED);
        } else {
            throw new IllegalStateException("Cannot complete order in status: " + order.getStatus());
        }
    }

    public void cancelStatusOrder(OrderEntity order) {
        if (order.getStatus() == OrderStatus.CREATED || order.getStatus() == OrderStatus.PROCESSING) {
            order.setStatus(OrderStatus.CANCELLED);
        } else {
            throw new IllegalStateException("Cannot cancel order in status: " + order.getStatus());
        }
    }
}
