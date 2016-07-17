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

        foundOrder = updateAddressFrom(foundOrder, newOrder.getFrom());
        foundOrder = updateAddressTo(foundOrder, newOrder.getTo());

        foundOrder = updatePassenger(foundOrder, newOrder.getPassenger());
        if (newOrder.getDriver() != null) {
            foundOrder = updateDriver(foundOrder, newOrder.getDriver());
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

    private Order updatePassenger(Order foundOrder, User passenger) {

        foundOrder.getPassenger().setId(passenger.getId());
        foundOrder.getPassenger().setIdentifier(passenger.getIdentifier());
        foundOrder.getPassenger().setPhone(passenger.getPhone());
        foundOrder.getPassenger().setPass(passenger.getPass());
        foundOrder.getPassenger().setName(passenger.getName());

        foundOrder.getPassenger().getHomeAddress().setCountry(passenger.getHomeAddress().getCountry());
        foundOrder.getPassenger().getHomeAddress().setCity(passenger.getHomeAddress().getCity());
        foundOrder.getPassenger().getHomeAddress().setStreet(passenger.getHomeAddress().getStreet());
        foundOrder.getPassenger().getHomeAddress().setHouseNum(passenger.getHomeAddress().getHouseNum());

        for (Order order : passenger.getOrdersPassenger()) {
            if (foundOrder.getId() == order.getId()) {
                passenger.getOrdersPassenger().remove(order);
                passenger.getOrdersPassenger().add(foundOrder);
            }
        }
        //foundOrder.getPassenger().setOrdersPassenger(passenger.getOrdersPassenger());

        return foundOrder;
    }

    private Order updateDriver(Order foundOrder, User driver) {

        foundOrder.getDriver().setId(driver.getId());
        foundOrder.getDriver().setIdentifier(driver.getIdentifier());
        foundOrder.getDriver().setPhone(driver.getPhone());
        foundOrder.getDriver().setPass(driver.getPass());
        foundOrder.getDriver().setName(driver.getName());

        foundOrder.getDriver().getCar().setType(driver.getCar().getType());
        foundOrder.getDriver().getCar().setModel(driver.getCar().getModel());
        foundOrder.getDriver().getCar().setNumber(driver.getCar().getNumber());

        for (Order order : driver.getOrdersPassenger()) {
            if (foundOrder.getId() == order.getId()) {
                driver.getOrdersPassenger().remove(order);
                driver.getOrdersPassenger().add(foundOrder);
            }
        }
        //foundOrder.getDriver().setOrdersDriver(driver.getOrdersDriver());

        return foundOrder;
    }
}
