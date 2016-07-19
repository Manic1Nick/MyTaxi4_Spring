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

        return manager.createNamedQuery("getAllUsers").getResultList();
    }

    @Override
    @Transactional
    public User updateUser(User newUser) {

        User foundUser = findById(newUser.getId());

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
    @Transactional
    public User findByPhone(String checkPhone) {

        User user = null;

        List<User> users = manager.createQuery(
                "SELECT c FROM User c WHERE c.phone=:phone")
                .setParameter("phone", checkPhone).getResultList();

        if (users.size() != 0) {
            user = users.get(0);
        }

        return user;
    }

    @Override
    @Transactional
    public User findById(int id) {

        return manager.find(User.class, id);
    }

    @Override
    @Transactional
    public List<Order> getAllOrdersOfUser(User user) {

        List<Order> orders = manager.createQuery(
                "SELECT c FROM Order c WHERE c.passenger=:user OR c.driver=:user")
                .setParameter("user",user).getResultList();

        return orders;
    }


}

