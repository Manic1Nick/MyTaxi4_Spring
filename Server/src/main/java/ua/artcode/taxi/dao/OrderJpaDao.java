package ua.artcode.taxi.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ua.artcode.taxi.model.Address;
import ua.artcode.taxi.model.Order;
import ua.artcode.taxi.model.OrderStatus;
import ua.artcode.taxi.model.User;

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
    public Order create(Order order) {

        User passUser = manager.find(User.class,order.getPassenger().getId());
        order.setPassenger(passUser);
        manager.persist(order);

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

        foundOrder = updateAddressFrom(foundOrder, newOrder.getFrom());
        foundOrder = updateAddressTo(foundOrder, newOrder.getTo());

        User passenger = manager.find(User.class, newOrder.getPassenger().getId());
        foundOrder.setPassenger(passenger);

        if (newOrder.getDriver() != null) {
            User driver = manager.find(User.class, newOrder.getDriver().getId());
            foundOrder.setDriver(driver);
        }

        foundOrder.setDistance(newOrder.getDistance());
        foundOrder.setPrice(newOrder.getPrice());
        foundOrder.setMessage(newOrder.getMessage());

        manager.merge(foundOrder);

        return foundOrder;
    }

    @Override
    @Transactional
    public Order delete(long id) {

        Order delOrder = findById(id);
        manager.remove(delOrder);

        return delOrder;
    }

    @Override
    @Transactional
    public Order findById(long id) {

        return manager.find(Order.class, id);
    }

    @Override
    @Transactional
    public List<Order> getOrdersByStatus(OrderStatus status) {

        List<Order> orders = manager.createQuery(
                "SELECT c FROM Order c WHERE c.orderStatus=:orderStatus")
                .setParameter("orderStatus", status).getResultList();

        return orders;
    }

    @Override
    public List<Order> getOrdersOfUser(User user) {

        return userDao.getAllOrdersOfUser(user);
    }

    @Override
    public OrderStatus getOrderStatusById(long id) {

        Order foundOrder = findById(id);

        return foundOrder.getOrderStatus();
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    private Order updateAddressFrom(Order foundOrder, Address address) {

        foundOrder.getFrom().setCountry(address.getCountry());
        foundOrder.getFrom().setCity(address.getCity());
        foundOrder.getFrom().setStreet(address.getStreet());
        foundOrder.getFrom().setHouseNum(address.getHouseNum());

        return foundOrder;
    }

    private Order updateAddressTo(Order foundOrder, Address address) {

        foundOrder.getTo().setCountry(address.getCountry());
        foundOrder.getTo().setCity(address.getCity());
        foundOrder.getTo().setStreet(address.getStreet());
        foundOrder.getTo().setHouseNum(address.getHouseNum());

        return foundOrder;
    }
}
