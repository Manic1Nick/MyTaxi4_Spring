package ua.artcode.taxi.service;

import org.apache.log4j.Logger;
import ua.artcode.taxi.dao.OrderDao;
import ua.artcode.taxi.dao.UserDao;
import ua.artcode.taxi.exception.*;
import ua.artcode.taxi.model.*;
import ua.artcode.taxi.utils.geolocation.GoogleMapsAPI;
import ua.artcode.taxi.utils.geolocation.GoogleMapsAPIImpl;
import ua.artcode.taxi.utils.geolocation.Location;

import javax.security.auth.login.LoginException;
import java.util.*;

public class UserServiceImpl implements UserService {

    private final static Logger LOG = Logger.getLogger(UserServiceImpl.class);

    private UserDao userDao;
    private OrderDao orderDao;
    private ValidatorImpl validator;
    private double pricePerKilometer = 5;
    private GoogleMapsAPI googleMapsAPI = new GoogleMapsAPIImpl();
    private Map<String, User> accessKeys = new HashMap<>();

    public UserServiceImpl(UserDao userDao, OrderDao orderDao, ValidatorImpl validator) {
        this.userDao = userDao;
        this.orderDao = orderDao;
        this.validator = validator;
    }

    @Override
    public User registerPassenger(Map<String, String> map) throws RegisterException {

        if (!validator.validateRegistration(map.get("phone"))) {

            LOG.error("RegisterException");

            throw new RegisterException("can not create exception");
        }

        User newUser = new User(
                UserIdentifier.P,
                map.get("phone"),
                map.get("pass"),
                map.get("name"),
                new Address(map.get("homeAddress")));

        User createdUser = userDao.createUser(newUser);

        LOG.info("new passenger " + createdUser.getName() + " registered");

        return createdUser;
    }

    @Override
    public User registerDriver(Map<String, String> map) throws RegisterException {

        if (!validator.validateRegistration(map.get("phone"))) {

            LOG.error("RegisterException");

            throw new RegisterException("can not create exception");
        }

        User newUser = new User(
                UserIdentifier.D,
                map.get("phone"),
                map.get("pass"),
                map.get("name"),
                new Car(map.get("carType"), map.get("carModel"), map.get("carNumber")));

        User createdUser = userDao.createUser(newUser);

        LOG.info("new driver " + createdUser.getName() + " registered");

        return createdUser;
    }

    @Override
    public String login(String phone, String pass) throws LoginException {

        User found = null;

        boolean valid = validator.validateLogin(phone, pass);

        if (valid) {
            Collection<User> users = userDao.getAllUsers();
            for (User user : users) {
                found = user.getPhone().equals(phone) ? user : found ;
            }

        } else {

            LOG.error("LoginException");

            throw new LoginException("User not found or incorrect password");
        }

        String accessKey = UUID.randomUUID().toString();
        accessKeys.put(accessKey, found);

        LOG.info("user " + phone + " logged in");

        return accessKey;
    }

    @Override
    public Order makeOrder(String accessToken, String lineFrom, String lineTo, String message)
                            throws OrderMakeException, UserNotFoundException, InputDataWrongException {

        Address from = new Address(lineFrom);
        Address to = new Address(lineTo);
        Order newOrder = null;

        if (!validator.validateAddress(from) && !validator.validateAddress(to)) {

            LOG.error("InputDataWrongException");

            throw  new InputDataWrongException("Wrong input data addresses. Can not make order");
        }

        if (accessKeys.get(accessToken) != null) {

            Location location = googleMapsAPI.findLocation(from.getCountry(), from.getCity(),
                    from.getStreet(), from.getHouseNum());
            Location location1 = googleMapsAPI.findLocation(to.getCountry(), to.getCity(),
                    to.getStreet(), to.getHouseNum());
            int distance = (int) (googleMapsAPI.getDistance(location, location1) / 1000);
            int price = (int) pricePerKilometer * distance + 30;
            message = message.equals("") ? "" : accessKeys.get(accessToken).getName() + ": " + message;

            newOrder = new Order(from, to, accessKeys.get(accessToken), distance, price, message);

            orderDao.create(accessKeys.get(accessToken), newOrder);
            accessKeys.get(accessToken).getOrderIds().add(newOrder.getId());

            LOG.info("User " + accessKeys.get(accessToken).getName() + " makes new order " + newOrder.getId());
        }

        return newOrder;
    }

    @Override
    public Order makeOrderAnonymous(String phone, String name, String lineFrom, String lineTo, String message)
                            throws OrderMakeException, InputDataWrongException {

        Address from = new Address(lineFrom);
        Address to = new Address(lineTo);

        if (!validator.validateAddress(from) && !validator.validateAddress(to)) {

            LOG.error("InputDataWrongException");

            throw new InputDataWrongException("Wrong input data. Can not make order");
        }

        Location location = googleMapsAPI.findLocation(from.getCountry(), from.getCity(),
                from.getStreet(), from.getHouseNum());
        Location location1 = googleMapsAPI.findLocation(to.getCountry(), to.getCity(),
                to.getStreet(), to.getHouseNum());
        int distance = (int) (googleMapsAPI.getDistance(location, location1) / 1000);
        int price = (int) pricePerKilometer * distance + 30;


        User anonymousUser = userDao.createUser(new User(UserIdentifier.A, phone, name));
        Order newOrder = new Order(from, to, anonymousUser, distance, price, message);
        orderDao.create(anonymousUser, newOrder);

        LOG.info("User anonymous makes new order " + newOrder.getId());

        return newOrder;
    }

    @Override
    public Map<String, Object> calculateOrder(String lineFrom, String lineTo) throws InputDataWrongException {

        Address from = new Address(lineFrom);
        Address to = new Address(lineTo);

        if (!validator.validateAddress(from) && !validator.validateAddress(to)) {

            LOG.error("InputDataWrongException");

            throw new InputDataWrongException("Wrong input data. Can not make order");
        }

        Location location = googleMapsAPI.findLocation(from.getCountry(), from.getCity(),
                        from.getStreet(), from.getHouseNum());
        Location location1 = googleMapsAPI.findLocation(to.getCountry(), to.getCity(),
                        to.getStreet(), to.getHouseNum());
        int distance = ((int) googleMapsAPI.getDistance(location, location1) / 1000);
        int price = (int) pricePerKilometer * distance + 30;

        LOG.info("Someone calculates order");

        Map<String, Object> map = new HashMap<>();
        map.put("distance", distance + "");
        map.put("price", price + "");

        return map;
    }

    @Override
    public Order getOrderInfo(long orderId) throws OrderNotFoundException {

        Order found = orderDao.find(orderId);

        if (found == null) {

            LOG.error("OrderNotFoundException");

            throw new OrderNotFoundException("Order not found in data base");
        }

        LOG.info("Information has been requested for order " + found.getId());

        return found;
    }

    @Override
    public Order getLastOrderInfo(String accessToken) throws UserNotFoundException, OrderNotFoundException {

        if (accessToken != null) {
            List<Order> allUserOrders = getAllOrdersUser(accessToken);

            if (allUserOrders == null) {

                LOG.error("OrderNotFoundException");

                throw new OrderNotFoundException("User don't have any orders");

            }

            Order lastOrder = allUserOrders.get(allUserOrders.size() - 1);

            LOG.info("User " + accessKeys.get(accessToken).getName() +
                        " get information for his last order " + lastOrder.getId());

            return lastOrder;

        } else {

            LOG.error("UserNotFoundException");

            throw new UserNotFoundException("wrong data user");
        }
    }

    @Override
    public Order cancelOrder(long orderId) throws OrderNotFoundException {

        Order cancelled = orderDao.find(orderId);

        if (cancelled != null) {
            cancelled.setOrderStatus(OrderStatus.CANCELLED);

        } else {

            LOG.error("OrderNotFoundException");

            throw new OrderNotFoundException("Order not found in data base");
        }

        LOG.info("Order " + cancelled.getId() + " was cancelled by user");

        return cancelled;
    }

    @Override
    public Order closeOrder(String accessToken, long orderId) throws OrderNotFoundException,
                                            WrongStatusOrderException, DriverOrderActionException {

        User user = accessKeys.get(accessToken);
        Order closed = orderDao.find(orderId);
        List<Order> ordersUser = userDao.getOrdersOfUser(user);
        Order result = null;

        for (Order order : ordersUser) {
            if (order.getId() == closed.getId()) {
                result = order;
            }
        }

        if (closed == null) {

            LOG.error("OrderNotFoundException");

            throw new OrderNotFoundException("Order not found in data base");

        } else if (result == null) {

            LOG.error("DriverOrderActionException");

            throw new DriverOrderActionException("Order not found in driver orders list");

        } else if (!result.getOrderStatus().equals(OrderStatus.IN_PROGRESS)) {

            LOG.error("WrongStatusOrderException");

            throw new WrongStatusOrderException("This order has wrong status (not IN_PROGRESS)");
        } else {
            closed.setOrderStatus(OrderStatus.DONE);
        }

        LOG.info("User " + accessKeys.get(accessToken).getName() + " was closed his order " + orderId);

        return closed;
    }

    @Override
    public Order takeOrder(String accessToken, long orderId) throws OrderNotFoundException,
                                        WrongStatusOrderException, DriverOrderActionException {

        User user = accessKeys.get(accessToken);
        Order inProgress = orderDao.find(orderId);
        List<Order> ordersUser = userDao.getOrdersOfUser(user);

        for (Order order : ordersUser) {
            if (order.getOrderStatus().equals(OrderStatus.IN_PROGRESS)) {

                LOG.error("DriverOrderActionException");

                throw new DriverOrderActionException("Driver has orders IN_PROGRESS already");
            }
        }

        if (inProgress == null) {

            LOG.error("OrderNotFoundException");

            throw new OrderNotFoundException("Order not found in data base");

        } else if (!inProgress.getOrderStatus().equals(OrderStatus.NEW)) {

            LOG.error("WrongStatusOrderException");

            throw new WrongStatusOrderException("This order has wrong status (not NEW)");
        }

        inProgress.setDriver(user);
        inProgress.setOrderStatus(OrderStatus.IN_PROGRESS);

        orderDao.addToDriver(user, inProgress);
        orderDao.update(inProgress);

        user.getOrderIds().add(inProgress.getId());

        LOG.info("User " + user.getName() + " was take order " + orderId + " for execution");

        return inProgress;
    }

    @Override
    public User getUser(String accessToken) {

        LOG.info("Request user " + accessKeys.get(accessToken).getName());

        return accessKeys.get(accessToken);
    }

    @Override
    public List<Order> getAllOrdersUser(String accessToken) {

        LOG.info("Get all order for user " + accessKeys.get(accessToken).getName());

        return userDao.getOrdersOfUser(accessKeys.get(accessToken));
    }

    @Override
    public Map<Integer, Order> getMapDistancesToDriver(String orderStatus, String lineAddressDriver) {

        //find all orders with status
        List<Order> ordersInProgress = getAllOrdersByStatus(Enum.valueOf(OrderStatus.class, orderStatus));

        //create array of int distances
        int[] distances = getArrayDistancesToDriver(ordersInProgress, new Address(lineAddressDriver));

        //create map of distances
        Map<Integer, Order> mapDistances = new HashMap<>(ordersInProgress.size());
        for (int i = 0; i < distances.length; i++) {
            mapDistances.put(distances[i], ordersInProgress.get(i));
        }

        //sorting map by distances
        Arrays.sort(distances);
        Map<Integer, Order> sortingMapDistances = new HashMap<>(ordersInProgress.size());
        for (int i = 0; i < distances.length; i++) {
            sortingMapDistances.put(distances[i], mapDistances.get(distances[i]));
        }

        return sortingMapDistances;
    }

    @Override
    public User updateUser(Map<String, String> map, String accessToken) throws RegisterException {

        UserIdentifier typeUser = accessKeys.get(accessToken).getIdentifier();
        int idUser = accessKeys.get(accessToken).getId();

        if (!validator.validateChangeRegistration(
                typeUser,
                idUser,
                map.get("phone"))) {

            LOG.error("RegisterException");

            throw new RegisterException("This phone is already in use by another user");

        } else {

            //create user for update
            User newUser = new User(typeUser, map.get("phone"), map.get("name"));
            newUser.setId(idUser);
            newUser.setPass(map.get("pass"));
            if (typeUser.equals(UserIdentifier.P)) {
                newUser.setHomeAddress(new Address(map.get("homeAddress")));
            } else if (typeUser.equals(UserIdentifier.D)) {
                newUser.setCar(new Car(map.get("carType"), map.get("carModel"), map.get("carNumber")));
            }
            newUser.setOrderIds(accessKeys.get(accessToken).getOrderIds());

            User updatedUser = userDao.updateUser(newUser);
            accessKeys.put(accessToken, updatedUser);

            LOG.info("User " + updatedUser.getName() + " was updated");

            return updatedUser;
        }
    }

    @Override
    public User deleteUser(String accessToken) {

        LOG.info("User " + accessKeys.get(accessToken).getName() + " was deleted");

        return userDao.deleteUser(accessKeys.get(accessToken).getId());
    }

    public int[] getArrayDistancesToDriver(List<Order> orders, Address addressDriver){

        Location locationDriver = googleMapsAPI.findLocation
                (addressDriver.getCountry(), addressDriver.getCity(),
                        addressDriver.getStreet(), addressDriver.getHouseNum());

        int[] distances = new int[orders.size()];

        for (int i = 0; i < orders.size(); i++) {
            Location locationPassenger = googleMapsAPI.findLocation(orders.get(i).getFrom().getCountry(),
                    orders.get(i).getFrom().getCity(), orders.get(i).getFrom().getStreet(),
                    orders.get(i).getFrom().getHouseNum());

            distances[i] = new Distance(locationDriver, locationPassenger).calculateDistance();
        }

        return distances;
    }

    public List<Order> getAllOrdersByStatus(OrderStatus status) {

        LOG.info("Get all order by status " + status.toString());

        return orderDao.getOrdersByStatus(status);
    }

    public class Distance implements Comparable {

        private Location fromLocation;
        private Location toLocation;
        private GoogleMapsAPI googleMapsAPI = new GoogleMapsAPIImpl();

        private int speedKmH = 60;
        private int timeInMin;

        public Distance() {
        }

        public Distance(Location fromLocation, Location toLocation) {
            this.fromLocation = fromLocation;
            this.toLocation = toLocation;
        }

        public Location getFromLocation() {
            return fromLocation;
        }

        public void setFromLocation(Location fromLocation) {
            this.fromLocation = fromLocation;
        }

        public Location getToLocation() {
            return toLocation;
        }

        public void setToLocation(Location toLocation) {
            this.toLocation = toLocation;
        }

        public int calculateDistance() {
            return (int) googleMapsAPI.getDistance(fromLocation, toLocation);
        }

        public void setSpeedKmH(int speedKmH) {
            this.speedKmH = speedKmH;
        }

        public int getTimeInMin() {
            return (this.calculateDistance() / 1000) / this.speedKmH;
        }

        @Override
        public int compareTo(Object o) {

            Distance tmp = (Distance)o;
            if(this.googleMapsAPI.getDistance(fromLocation, toLocation) <
                    tmp.googleMapsAPI.getDistance(fromLocation, toLocation)) {
                return -1;

            } else if (this.googleMapsAPI.getDistance(fromLocation, toLocation) >
                    tmp.googleMapsAPI.getDistance(fromLocation, toLocation)) {
                return 1;
            }
            return 0;
        }
    }
}
