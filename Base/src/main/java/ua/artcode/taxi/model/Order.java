package ua.artcode.taxi.model;

import java.time.LocalDateTime;

public class Order {

    private long id;
    private OrderStatus orderStatus;
    private Address from;
    private Address to;
    private User passenger;
    private User driver;
    private int distance;
    private int price;
    private String message;

    private LocalDateTime makeOrderTime;

    public Order() {
    }

    public Order(Address from, Address to, User passenger,
                                            int distance, int price, String message) {
        this.from = from;
        this.to = to;
        this.passenger = passenger;
        this.distance = distance;
        this.price = price;
        this.message = message;
    }

    public Order(Address from, Address to) {
        this.from = from;
        this.to = to;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Address getFrom() {
        return from;
    }

    public void setFrom(Address from) {
        this.from = from;
    }

    public Address getTo() {
        return to;
    }

    public void setTo(Address to) {
        this.to = to;
    }

    public User getPassenger() {
        return passenger;
    }

    public void setPassenger(User passenger) {
        this.passenger = passenger;
    }

    public User getDriver() {
        return driver;
    }

    public void setDriver(User driver) {
        this.driver = driver;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getMakeOrderTime() {
        return makeOrderTime;
    }

    public void setMakeOrderTime(LocalDateTime makeOrderTime) {
        this.makeOrderTime = makeOrderTime;
    }

    public String toStringForView() {
        return "id " + id +
                ", status " + orderStatus +
                ", from " + from.getCity() + " " + from.getStreet() + " " + from.getHouseNum() +
                ", to " + to.getCity() + " " + to.getStreet() + " " + to.getHouseNum() +
                ", distance " + distance +
                "km, price " + price + "uah";
    }

    public String toStringForViewShort() {
        return "id " + id +
                ", price " + price + "uah";
    }
}
