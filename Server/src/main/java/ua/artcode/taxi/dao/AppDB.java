package ua.artcode.taxi.dao;

import ua.artcode.taxi.model.Order;
import ua.artcode.taxi.model.User;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class AppDB {

    private AtomicInteger userIdCounter = new AtomicInteger(1);
    private AtomicInteger orderIdCounter = new AtomicInteger(1);
    private Map<User, List<Order>> users;
    private Collection<Order> orders;

    public AppDB() {
        users = new ConcurrentHashMap<>();
        orders = new CopyOnWriteArrayList<>();
    }

    public AppDB(Map<User, List<Order>> users, List<Order> orders) {
        this.users = users;
        this.orders = orders;
    }

    public Map<User, List<Order>> getUsers() {
        return users;
    }

    public void setUsers(Map<User, List<Order>> users) {
        this.users = users;
    }

    public Collection<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    @Override
    public String toString() {
        return "AppDB{" +
                "users=" + users.toString() +
                ", orders=" + orders.toString() +
                '}';
    }

    public User addUser(User user){

        user.setId(userIdCounter.getAndIncrement());
        users.put(user, new ArrayList<>());

        return user;
    }

    public Order addOrder(User user, Order order){

        order.setId(orderIdCounter.getAndIncrement());
        orders.add(order);

        List<Order> newList = users.get(user);
        newList.add(order);
        users.replace(user, newList);

        return order;
    }

    public Order findOrder(long id){
        for (Order order : orders) {
            if (order.getId() == id) {
                return order;
            }
        }
        return null;
    }

    public User findUser(String phone){
        for (User user : users.keySet()) {
            if (user.getPhone().equals(phone)) {
                return user;
            }
        }
        return null;
    }

    public Order addOrderToDriver(User user, Order order) {

        List<Order> newList = users.get(user);
        newList.add(order);
        users.replace(user, newList);

        return order;
    }
}
