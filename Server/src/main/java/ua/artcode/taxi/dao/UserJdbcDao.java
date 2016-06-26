package ua.artcode.taxi.dao;

import ua.artcode.taxi.model.Address;
import ua.artcode.taxi.model.Order;
import ua.artcode.taxi.model.User;
import ua.artcode.taxi.utils.ConnectionFactory;

import java.sql.*;
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

            connection.setAutoCommit(false);
            Address homeAddress = user.getHomeAddress();
            statement.execute(String.format("INSERT INTO cities (city_name) VALUES ('%s')", homeAddress.getCity()));

            ResultSet resultSet = statement.executeQuery(String.format("SELECT id FROM cities c WHERE c.city_name = '%s' LIMIT 1", homeAddress.getCity()));

            resultSet.next();
            int cityId = resultSet.getInt("id");

            String sqlInsertAddress = String.format("INSERT INTO addresses (city_id, street, num) VALUES (%d,'%s','%s')",
                    cityId, homeAddress.getStreet(), homeAddress.getHouseNum());

            statement.execute(sqlInsertAddress);

            ResultSet resultSet2 = statement.executeQuery("SELECT id FROM addresses s ORDER BY id DESC LIMIT 1;");

            resultSet2.next();
            int addressId = resultSet2.getInt("id");

            String sqlInsert = "INSERT INTO clients(client_name, phone, address_id) " +
                    "VALUES ('" + user.getName() + "', '" + user.getPhone() + "', " + addressId + ");";
            statement.execute(sqlInsert);

            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }


        return user;
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

        try(Connection connection =
                    ConnectionFactory.createConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM clients c WHERE c.id = ?;")){

            preparedStatement.setInt((int) 1, id);
            preparedStatement.execute();


        } catch (SQLException e) {
            e.printStackTrace();
        }
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
