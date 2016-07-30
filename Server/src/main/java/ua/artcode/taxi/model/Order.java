package ua.artcode.taxi.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "orders")
@NamedQueries({@NamedQuery(name = "getAllOrders", query = "SELECT c FROM Order c")})
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "time", nullable = false)
    private Date timeCreate;

    @Transient
    private Date timeTaken;

    @Transient
    private Date timeCancelled;

    @Transient
    private Date timeClosed;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(referencedColumnName = "id")
    private Address from;

    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(referencedColumnName = "id")
    private Address to;

    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(referencedColumnName = "id")
    private User passenger;

    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(referencedColumnName = "id")
    private User driver;

    @Column(name = "distance", nullable = false)
    private int distance;

    @Column(name = "price", nullable = false)
    private int price;

    private String message;

    @Transient
    private int distanceToDriver;

    public Order() {
    }

    public Order(Address from, Address to, User passenger, int distance, int price, String message) {
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

    public Date getTimeCreate() {
        return timeCreate;
    }

    public void setTimeCreate(Date timeCreate) {
        this.timeCreate = timeCreate;
    }

    public Date getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(Date timeTaken) {
        this.timeTaken = timeTaken;
    }

    public Date getTimeCancelled() {
        return timeCancelled;
    }

    public void setTimeCancelled(Date timeCancelled) {
        this.timeCancelled = timeCancelled;
    }

    public Date getTimeClosed() {
        return timeClosed;
    }

    public void setTimeClosed(Date timeClosed) {
        this.timeClosed = timeClosed;
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

    public int getDistanceToDriver() {
        return distanceToDriver;
    }

    public void setDistanceToDriver(int distanceToDriver) {
        this.distanceToDriver = distanceToDriver;
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

    @Override
    public boolean equals(Object obj) {

        if (obj instanceof Order) {

            if (((Order)obj).driver == null) {
                return  id == (((Order)obj).id) &&
                        timeCreate == (((Order)obj).timeCreate) &&
                        orderStatus.equals(((Order)obj).orderStatus) &&
                        from.equals(((Order)obj).from) &&
                        to.equals(((Order)obj).to) &&
                        passenger.equals(((Order)obj).passenger) &&
                        distance == (((Order)obj).distance) &&
                        price == (((Order)obj).price) &&
                        message.equals(((Order)obj).message);

            } else if (((Order)obj).driver != null) {
                return  id == (((Order)obj).id) &&
                        timeCreate == (((Order)obj).timeCreate) &&
                        orderStatus.equals(((Order)obj).orderStatus) &&
                        from.equals(((Order)obj).from) &&
                        to.equals(((Order)obj).to) &&
                        passenger.equals(((Order)obj).passenger) &&
                        driver.equals(((Order)obj).driver) &&
                        distance == (((Order)obj).distance) &&
                        price == (((Order)obj).price) &&
                        message.equals(((Order)obj).message);
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}

