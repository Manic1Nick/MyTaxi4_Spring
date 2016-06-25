package ua.artcode.taxi.dao;

import ua.artcode.taxi.model.Order;
import ua.artcode.taxi.model.User;
import ua.artcode.taxi.utils.ConnectionFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;

/**
 * Created by serhii on 25.06.16.
 */
public class UserJdbcDao implements UserDao {


    @Override
    public User createUser(User user) {
        try (Connection connection =
                     ConnectionFactory.createConnection();
             Statement statement = connection.createStatement()) {
            String sqlInsert = "INSERT INTO clients(client_name, phone) VALUES ('" + user.getName() + "', '" + user.getPhone() + "')";
            statement.execute(sqlInsert);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    public Collection<User> getAllUsers() {
        return null;
    }

    @Override
    public User updateUser(User newUser) {
        return null;
    }

    @Override
    public User deleteUser(int id) {
        return null;
    }

    @Override
    public User find(String phone) {
        return null;
    }

    @Override
    public List<User> getAllPassenger() {
        return null;
    }

    @Override
    public List<User> getAllDrivers() {
        return null;
    }

    @Override
    public List<Order> getOrdersOfUser(User user) {
        return null;
    }
}
