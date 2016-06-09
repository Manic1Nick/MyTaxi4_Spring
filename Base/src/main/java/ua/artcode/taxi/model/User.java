package ua.artcode.taxi.model;

import java.util.ArrayList;
import java.util.List;

public class User implements PassengerActive, DriverActive {

    private int id;
    private UserIdentifier identifier;
    private String phone;
    private String pass;
    private String name;
    private Address homeAddress;
    private Car car;
    private List<Long> orderIds = new ArrayList<>();

    public User(UserIdentifier identifier, String phone, String pass, String name, Address homeAddress) {
        this.identifier = identifier;
        this.phone = phone;
        this.pass = pass;
        this.name = name;
        this.homeAddress = homeAddress;
    }

    public User(UserIdentifier identifier, String phone, String pass, String name, Car car) {
        this.identifier = identifier;
        this.phone = phone;
        this.pass = pass;
        this.name = name;
        this.car = car;
    }

    //for anonymous
    public User(UserIdentifier identifier, String phone, String name) {
        this.identifier = identifier;
        this.phone = phone;
        this.name = name;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public UserIdentifier getIdentifier() {
        return identifier;
    }

    @Override
    public void setIdentifier(UserIdentifier identifier) {
        this.identifier = identifier;
    }

    @Override
    public String getPhone() {
        return phone;
    }

    @Override
    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String getPass() {
        return pass;
    }

    @Override
    public void setPass(String pass) {
        this.pass = pass;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Address getHomeAddress() {
        return homeAddress;
    }

    @Override
    public void setHomeAddress(Address homeAddress) {
        this.homeAddress = homeAddress;
    }

    @Override
    public List<Long> getOrderIds() {
        return orderIds;
    }

    @Override
    public void setOrderIds(List<Long> orderIds) {
        this.orderIds = orderIds;
    }

    @Override
    public Car getCar() {
        return car;
    }

    @Override
    public void setCar(Car car) {
        this.car = car;
    }
}
