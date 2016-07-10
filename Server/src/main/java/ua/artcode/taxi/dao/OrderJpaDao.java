package ua.artcode.taxi.dao;

import ua.artcode.taxi.model.Order;
import ua.artcode.taxi.model.OrderStatus;
import ua.artcode.taxi.model.User;
import ua.artcode.taxi.utils.ConnectionFactory;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class OrderJpaDao implements OrderDao {

    private EntityManager manager = ConnectionFactory.createEntityManager();

    private UserDao userDao;

    public OrderJpaDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Order create(User user, Order order) {

        manager.getTransaction().begin();

        order.setOrderStatus(OrderStatus.NEW);
        order.setPassenger(user);
        manager.persist(order);

        user.getOrdersPassenger().add(order);
        manager.merge(user);

        manager.getTransaction().commit();

        return order;
    }

    @Override
    public Collection<Order> getAllOrders() {

        manager.getTransaction().begin();
        Query query = manager.createNamedQuery("getAllOrders");
        Collection<Order> orders = query.getResultList();
        manager.getTransaction().commit();

        return orders;
    }

    @Override
    public Order update(Order newOrder) {

        manager.getTransaction().begin();

        Order foundOrder = manager.find(Order.class, newOrder.getId());

        OrderStatus status = newOrder.getOrderStatus();

        foundOrder.setOrderStatus(status);
        foundOrder.setFrom(newOrder.getFrom());
        foundOrder.setTo(newOrder.getTo());
        foundOrder.setPassenger(newOrder.getPassenger());
        foundOrder.setDriver(newOrder.getDriver());
        foundOrder.setDistance(newOrder.getDistance());
        foundOrder.setPrice(newOrder.getPrice());
        foundOrder.setMessage(newOrder.getMessage());

        manager.merge(foundOrder);
        manager.getTransaction().commit();

        return foundOrder;
    }

    @Override
    public Order delete(long id) {

        manager.getTransaction().begin();
        Order delOrder = manager.find(Order.class, id);
        manager.remove(delOrder);
        manager.getTransaction().commit();

        return delOrder;
    }

    @Override
    public Order findById(long id) {

        manager.getTransaction().begin();
        Order order = manager.find(Order.class, id);
        manager.getTransaction().commit();

        return order;
    }

    @Override
    public List<Order> getOrdersByStatus(OrderStatus status) {

        List<Order> ordersRes = new ArrayList<>();

        Collection<Order> orders = getAllOrders();
        for (Order order : orders) {
            if(order.getOrderStatus().equals(status)) {
                ordersRes.add(order);
            }
        }
        return ordersRes;
    }

    @Override
    public List<Order> getOrdersOfUser(User user) {

        return userDao.getAllOrdersOfUser(user);
    }

    @Override
    public Order addToDriver(User user, Order order) {

        order.setDriver(user);
        update(order);

        user.getOrdersDriver().add(order);
        userDao.updateUser(user);

        return order;
    }

    @Override
    public OrderStatus getOrderStatusById(long id) {

        OrderStatus status = null;

        Collection<Order> orders = getAllOrders();
        for (Order order : orders) {
            if(order.getId() == id) {
                status = order.getOrderStatus();
            }
        }
        return status;
    }
}
