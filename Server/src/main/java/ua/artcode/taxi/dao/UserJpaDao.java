package ua.artcode.taxi.dao;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ua.artcode.taxi.model.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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

        foundUser.setIdentifier(newUser.getIdentifier());
        foundUser.setPhone(newUser.getPhone());
        foundUser.setPass(newUser.getPass());
        foundUser.setName(newUser.getName());

        if (identifier.equals(UserIdentifier.P)) {
            foundUser = updateHomeAddress(foundUser, newUser.getHomeAddress());

        } else if (identifier.equals(UserIdentifier.D)) {
            foundUser = updateCar(foundUser, newUser.getCar());
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

        List<User> users = manager.createQuery(
                "SELECT c FROM User c WHERE c.phone=:phone")
                .setParameter("phone", checkPhone).getResultList();

        return users.size() != 0 ? users.get(0) : null ;
    }

    @Override
    @Transactional
    public User findById(int id) {

        return manager.find(User.class, id);
    }

    @Override
    @Transactional
    public List<Order> getOrdersOfUser(int userId, int from, int to) {

        List<Order> orders = manager.createQuery(
                "SELECT c FROM Order c WHERE c.idPassenger=:userId OR c.idDriver=:userId")
                .setParameter("userId",userId).getResultList().subList(from, to);

        return orders;
    }

    @Override
    @Transactional
    public int getQuantityOrdersOfUser(int userId) {

        List<Long> orderIds = manager.createQuery(
                "SELECT c.id FROM Order c WHERE c.idPassenger=:userId OR c.idDriver=:userId")
                .setParameter("userId", userId).getResultList();

        return orderIds.size();
    }

    private User updateHomeAddress(User foundUser, Address address) {

        foundUser.getHomeAddress().setCountry(address.getCountry());
        foundUser.getHomeAddress().setCity(address.getCity());
        foundUser.getHomeAddress().setStreet(address.getStreet());
        foundUser.getHomeAddress().setHouseNum(address.getHouseNum());

        return foundUser;
    }

    private User updateCar(User foundUser, Car car) {

        foundUser.getCar().setType(car.getType());
        foundUser.getCar().setModel(car.getModel());
        foundUser.getCar().setNumber(car.getNumber());

        return foundUser;
    }
}

