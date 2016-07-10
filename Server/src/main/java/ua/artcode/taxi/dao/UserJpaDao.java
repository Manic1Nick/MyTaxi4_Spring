package ua.artcode.taxi.dao;

import ua.artcode.taxi.model.Order;
import ua.artcode.taxi.model.User;
import ua.artcode.taxi.model.UserIdentifier;
import ua.artcode.taxi.utils.ConnectionFactory;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserJpaDao implements UserDao {

    private EntityManager manager = ConnectionFactory.createEntityManager();

    public UserJpaDao() {
    }

    @Override
    public User createUser(User user) {

        manager.getTransaction().begin();
        manager.persist(user);
        manager.getTransaction().commit();

        return user;
    }

    @Override
    public Collection<User> getAllUsers() {

        manager.getTransaction().begin();
        Query query = manager.createNamedQuery("getAllUsers");
        Collection<User> users = query.getResultList();
        manager.getTransaction().commit();

        return users;
    }

    @Override
    public User updateUser(User newUser) {

        manager.getTransaction().begin();
        User foundUser = manager.find(User.class, newUser.getId());

        UserIdentifier identifier = newUser.getIdentifier();

        foundUser.setIdentifier(identifier);
        foundUser.setPhone(newUser.getPhone());
        foundUser.setPass(newUser.getPass());
        foundUser.setName(newUser.getName());

        if (identifier.equals(UserIdentifier.P)) {
            foundUser.setHomeAddress(newUser.getHomeAddress());
        } else if (identifier.equals(UserIdentifier.D)) {
            foundUser.setCar(newUser.getCar());
        }
        foundUser.setOrdersDriver(newUser.getOrdersDriver());
        foundUser.setOrdersPassenger(newUser.getOrdersPassenger());

        manager.merge(foundUser);
        manager.getTransaction().commit();

        return foundUser;
    }

    @Override
    public User deleteUser(int id) {

        User delUser = findById(id);

        manager.getTransaction().begin();
        manager.remove(delUser);
        manager.getTransaction().commit();

        return delUser;
    }

    @Override
    public User findByPhone(String phone) {

        Collection<User> users = getAllUsers();
        for (User user : users) {
            if(user.getPhone().equals(phone)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public List<User> getAllUsersByIdentifier(UserIdentifier identifier) {

        List<User> usersRes = new ArrayList<>();

        Collection<User> users = getAllUsers();
        for (User user : users) {
            if(user.getIdentifier().equals(identifier)) {
                usersRes.add(user);
            }
        }
        return usersRes;
    }

    @Override
    public User findById(int id) {

        manager.getTransaction().begin();
        User user = manager.find(User.class, id);
        manager.getTransaction().commit();

        return user;
    }

    @Override
    public List<String> getAllRegisteredPhones() {

        List<String> phones = new ArrayList<>();

        Collection<User> users = getAllUsers();
        for (User user : users) {
            phones.add(user.getPhone());
        }

        return phones;
    }

    @Override
    public List<Order> getAllOrdersOfUser(User user) {

        List<Order> orders = new ArrayList<>();

        if (user.getIdentifier().equals(UserIdentifier.P)) {
            orders = user.getOrdersPassenger();
        }

        else if (user.getIdentifier().equals(UserIdentifier.D)) {
            orders = user.getOrdersDriver();
        }
        return orders;
    }
}

