package ua.artcode.taxi.dao;

import ua.artcode.taxi.model.Order;
import ua.artcode.taxi.model.OrderStatus;

import java.util.Collection;
import java.util.List;

public interface OrderDao {

    Order create(Order order);
    Collection<Order> getAllOrders();
    Order update(Order newOrder);
    Order delete(long id);

    Order findById(long id);
    List<Order> getOrdersByStatus(OrderStatus status);
    Order getLastOrderOfUser(int userId);
}
