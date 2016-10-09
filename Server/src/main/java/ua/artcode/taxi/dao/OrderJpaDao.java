package ua.artcode.taxi.dao;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ua.artcode.taxi.model.Address;
import ua.artcode.taxi.model.Order;
import ua.artcode.taxi.model.OrderStatus;
import ua.artcode.taxi.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Collection;
import java.util.List;

@Component(value = "orderDao")
public class OrderJpaDao implements OrderDao {

    @PersistenceContext
    private EntityManager manager;

    public OrderJpaDao() {
    }

    @Override
    @Transactional
    public Order create(Order order) {

        User passUser = manager.find(User.class, order.getIdPassenger());
        order.setIdPassenger(passUser.getId());
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

        foundOrder.setOrderStatus(newOrder.getOrderStatus());
        foundOrder.setTimeCreate(newOrder.getTimeCreate());
        foundOrder.setTimeCancelled(newOrder.getTimeCancelled());
        foundOrder.setTimeTaken(newOrder.getTimeTaken());
        foundOrder.setTimeClosed(newOrder.getTimeClosed());

        foundOrder = updateAddressFrom(foundOrder, newOrder.getFrom());
        foundOrder = updateAddressTo(foundOrder, newOrder.getTo());

        User passenger = manager.find(User.class, newOrder.getIdPassenger());
        foundOrder.setIdPassenger(passenger.getId());

        if (newOrder.getIdDriver() > 0) {
            User driver = manager.find(User.class, newOrder.getIdDriver());
            foundOrder.setIdDriver(driver.getId());
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
    @Transactional
    public Order getLastOrderOfUser(int userId) {

        List<Long> orderIds = manager.createQuery(
                "SELECT MAX(c.id) FROM Order c WHERE c.idPassenger=:userId OR c.idDriver=:userId")
                .setParameter("userId", userId).setMaxResults(1).getResultList();

        return orderIds.get(0) != null ? findById(orderIds.get(0)) : null ;
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
