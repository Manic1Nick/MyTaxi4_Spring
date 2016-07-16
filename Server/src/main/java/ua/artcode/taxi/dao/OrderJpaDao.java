package ua.artcode.taxi.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ua.artcode.taxi.model.Order;
import ua.artcode.taxi.model.OrderStatus;
import ua.artcode.taxi.model.User;
import ua.artcode.taxi.utils.ConnectionFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component(value = "orderDao")
public class OrderJpaDao implements OrderDao {

    @PersistenceContext
    private EntityManager manager;

    @Autowired
    private UserDao userDao;

    public OrderJpaDao() {
    }

    public OrderJpaDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    @Transactional
    public Order create(User user, Order order) {

        order.setOrderStatus(OrderStatus.NEW);
        order.setPassenger(user);
        manager.persist(order);

        user.getOrdersPassenger().add(order);
        manager.merge(user);

        return order;
    }

    @Override
    @Transactional
    public Collection<Order> getAllOrders() {

        Query query = manager.createNamedQuery("getAllOrders");
        Collection<Order> orders = query.getResultList();

        return orders;
    }

    @Override
    @Transactional
    public Order update(Order newOrder) {

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

        return foundOrder;
    }

    @Override
    @Transactional
    public Order delete(long id) {

        Order delOrder = manager.find(Order.class, id);
        manager.remove(delOrder);

        return delOrder;
    }

    @Override
    @Transactional
    public Order findById(long id) {

        Order order = manager.find(Order.class, id);

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

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
