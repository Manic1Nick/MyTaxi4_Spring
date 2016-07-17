package ua.artcode.taxi.dao;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ua.artcode.taxi.model.Order;
import ua.artcode.taxi.model.User;
import ua.artcode.taxi.model.UserIdentifier;
import ua.artcode.taxi.utils.ConnectionFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component(value = "userDao")
public class UserJpaDao implements UserDao {

    @PersistenceContext
    private EntityManager manager;

    public UserJpaDao() {
    }

    @Override
    @Transactional
    public User createUser(User user) {

        manager.persist(user);

        return user;
    }

    @Override
    @Transactional
    public Collection<User> getAllUsers() {

        Query query = manager.createNamedQuery("getAllUsers");
        Collection<User> users = query.getResultList();

        return users;
    }

    @Override
    @Transactional
    public User updateUser(User newUser) {

        User foundUser = manager.find(User.class, newUser.getId());

        UserIdentifier identifier = newUser.getIdentifier();

        foundUser.setIdentifier(identifier);
        foundUser.setPhone(newUser.getPhone());
        foundUser.setPass(newUser.getPass());
        foundUser.setName(newUser.getName());

        if (identifier.equals(UserIdentifier.P)) {
            foundUser.getHomeAddress().setCountry(newUser.getHomeAddress().getCountry());
            foundUser.getHomeAddress().setCity(newUser.getHomeAddress().getCity());
            foundUser.getHomeAddress().setStreet(newUser.getHomeAddress().getStreet());
            foundUser.getHomeAddress().setHouseNum(newUser.getHomeAddress().getHouseNum());

        } else if (identifier.equals(UserIdentifier.D)) {
            foundUser.getCar().setType(newUser.getCar().getType());
            foundUser.getCar().setModel(newUser.getCar().getModel());
            foundUser.getCar().setNumber(newUser.getCar().getNumber());
        }

        foundUser.setOrdersDriver(newUser.getOrdersDriver());
        foundUser.setOrdersPassenger(newUser.getOrdersPassenger());

        manager.merge(foundUser);

        return foundUser;
    }

    @Override
    @Transactional
    public User deleteUser(int id) {

        User delUser = findById(id);
        manager.remove(delUser);

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
    @Transactional
    public User findById(int id) {

        User user = manager.find(User.class, id);

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

        /*if (user.getIdentifier().equals(UserIdentifier.P)) {
            orders = manager.createQuery("SELECT c FROM orders c WHERE c.passenger_id=:id")
                    .setParameter("id",user.getId()).getResultList();

        } else if (user.getIdentifier().equals(UserIdentifier.D)) {
            orders = manager.createQuery("SELECT c FROM orders c WHERE c.driver_id=:id")
                    .setParameter("id",user.getId()).getResultList();
        }

        for (Order order : orders) {
            System.out.println(order);
        }*/

        if (user.getIdentifier().equals(UserIdentifier.P)) {
            orders = user.getOrdersPassenger();
        }

        else if (user.getIdentifier().equals(UserIdentifier.D)) {
            orders = user.getOrdersDriver();
        }
        return orders;
    }


}

