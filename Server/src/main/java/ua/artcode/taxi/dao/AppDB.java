package ua.artcode.taxi.dao;

import ua.artcode.taxi.model.Order;
import ua.artcode.taxi.model.User;

import java.util.*;

public class AppDB {

    private static int userIdCounter = 1;
    private static int orderIdCounter = 1;
    private Map<User, List<Order>> users;
    private Collection<Order> orders;

    public AppDB() {
        users = new HashMap<User, List<Order>>();
        orders = new ArrayList<Order>();
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

        user.setId(userIdCounter++);
        users.put(user, new ArrayList<>());

        return user;
    }

    public Order addOrder(User user, Order order){

        order.setId(orderIdCounter++);
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
