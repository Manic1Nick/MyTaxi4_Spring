package ua.artcode.taxi.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.artcode.taxi.dao.OrderDao;
import ua.artcode.taxi.dao.UserDao;
import ua.artcode.taxi.exception.*;
import ua.artcode.taxi.model.*;
import ua.artcode.taxi.utils.geolocation.GoogleMapsAPI;
import ua.artcode.taxi.utils.geolocation.GoogleMapsAPIImpl;
import ua.artcode.taxi.utils.geolocation.Location;

import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service(value = "service")
public class UserServiceImpl implements UserService {

    private final static Logger LOG = Logger.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDao userDao;

    @Autowired
    private OrderDao orderDao;

    private double pricePerKilometer;
    private GoogleMapsAPI googleMapsAPI;
    private Map<String, User> accessKeys;

    public UserServiceImpl() {
        pricePerKilometer = Constants.PRICE_PER_KILOMETER;
        googleMapsAPI = new GoogleMapsAPIImpl();
        accessKeys = new ConcurrentHashMap<>();
    }

    public UserServiceImpl(UserDao userDao, OrderDao orderDao) {
        this.userDao = userDao;
        this.orderDao = orderDao;
        pricePerKilometer = Constants.PRICE_PER_KILOMETER;
        googleMapsAPI = new GoogleMapsAPIImpl();
        accessKeys = new ConcurrentHashMap<>();
    }

    @Override
    public User registerPassenger(Map<String, String> map)
                                throws RegisterException, InputDataWrongException {

        if (!validateRegisterData(map)) {

            LOG.error("InputDataWrongException: wrong input register data");

            throw new InputDataWrongException("Wrong input register data");
        }

        if (userDao.findByPhone(map.get("phone")) != null) {

            LOG.error("RegisterException: failed attempt to register with phone " + map.get("phone"));

            throw new RegisterException("This phone using already");
        }

        Address newAddress = new Address(map.get("homeAddress"));

        User newUser = new User(
                UserIdentifier.P,
                map.get("phone"),
                map.get("pass"),
                map.get("name"),
                newAddress);

        User createdUser = userDao.createUser(newUser);

        LOG.info("New passenger ID=" + createdUser.getId() + " registered");

        return createdUser;
    }

    @Override
    public User registerDriver(Map<String, String> map)
                            throws RegisterException, InputDataWrongException {

        if (!validateRegisterData(map)) {

            LOG.error("InputDataWrongException: wrong input register data");

            throw new InputDataWrongException("Wrong input register data");
        }

        if (userDao.findByPhone(map.get("phone")) != null) {

            LOG.error("RegisterException: failed attempt to register with phone " + map.get("phone"));

            throw new RegisterException("This phone using already");
        }

        Car newCar = new Car(map.get("carType"), map.get("carModel"), map.get("carNumber"));

        User newUser = new User(
                UserIdentifier.D,
                map.get("phone"),
                map.get("pass"),
                map.get("name"),
                newCar);

        User createdUser = userDao.createUser(newUser);

        LOG.info("New driver ID=" + createdUser.getId() + " registered");

        return createdUser;
    }

    @Override
    public String login(String phone, String pass) throws LoginException {

        User found = userDao.findByPhone(phone);

        if (found == null) {

            LOG.error("LoginException: failed attempt to log in with phone=" + phone);

            throw new LoginException("User not found or incorrect password");
        }

        String accessKey = UUID.randomUUID().toString();
        accessKeys.put(accessKey, found);

        LOG.info("User ID=" + found.getId() + " logged in");

        return accessKey;
    }

    @Override
    public Order makeOrder(String accessToken, String lineFrom, String lineTo, String message) throws
            OrderMakeException, UserNotFoundException, InputDataWrongException, UnknownHostException {

        Address from = new Address(lineFrom);
        Address to = new Address(lineTo);
        Order createdOrder = null;

        if (!validateAddress(from) || !validateAddress(to)) {

            LOG.error("InputDataWrongException: wrong input data address");

            throw new InputDataWrongException("Wrong input data addresses. Can not make order");
        }

        User user = accessKeys.get(accessToken);

        if (user != null) {

            List<Order> ordersNew = orderDao.getOrdersByStatus(OrderStatus.NEW);
            List<Order> ordersInProgress = orderDao.getOrdersByStatus(OrderStatus.IN_PROGRESS);

            for (Order order : ordersNew) {
                if (user.getId() == order.getIdPassenger()) {

                    LOG.error("OrderMakeException: failed attempt to make order by user " + user.getPhone());

                    throw new OrderMakeException("User has orders NEW already");
                }
            }

            for (Order order : ordersInProgress) {
                if (user.getId() == order.getIdPassenger()) {

                    LOG.error("OrderMakeException: failed attempt to make order by user " + user.getPhone());

                    throw new OrderMakeException("User has orders IN_PROGRESS already");
                }
            }

            try {
                Location location = googleMapsAPI.findLocation(from.getCountry(), from.getCity(),
                        from.getStreet(), from.getHouseNum());
                Location location1 = googleMapsAPI.findLocation(to.getCountry(), to.getCity(),
                        to.getStreet(), to.getHouseNum());
                int distance = (int) (googleMapsAPI.getDistance(location, location1) / 1000);
                int price = (int) pricePerKilometer * distance + 30;

                message = message == null || message.equals("") ? "" : user.getName() + ": " + message;

                Order newOrder = new Order(from, to, user.getId(), distance, price, message);
                newOrder.setOrderStatus(OrderStatus.NEW);
                newOrder.setTimeCreate(new Date());
                createdOrder = orderDao.create(newOrder);

                LOG.info("User " + user.getPhone() + " makes new order " + createdOrder.getId());

            } catch (InputDataWrongException | IndexOutOfBoundsException e) {

                LOG.error("InputDataWrongException: errors in calculate locations in Google API");

                throw new InputDataWrongException("Wrong calculation in Google API");
            }
        }

        LOG.info("New order ID=" + createdOrder.getId() + "was created by user ID=" + user.getId());

        return createdOrder;
    }

    @Override
    public Order makeOrderAnonymous(String phone, String name, String lineFrom, String lineTo, String message)
            throws OrderMakeException, InputDataWrongException {

        Address from = new Address(lineFrom);
        Address to = new Address(lineTo);
        Order createdOrder = null;

        if (!validateAddress(from) && !validateAddress(to)) {

            LOG.error("InputDataWrongException: wrong input data address");

            throw new InputDataWrongException("Wrong input data addresses. Can not make order");
        }

        User user = userDao.findByPhone(phone);

        if (user != null) {

            List<Order> ordersNew = orderDao.getOrdersByStatus(OrderStatus.NEW);
            List<Order> ordersInProgress = orderDao.getOrdersByStatus(OrderStatus.IN_PROGRESS);

            for (Order order : ordersNew) {
                if (user.getId() == order.getIdPassenger()) {

                    LOG.error("OrderMakeException: failed attempt to make order by user " + user.getPhone());

                    throw new OrderMakeException("User has orders NEW already");
                }
            }

            for (Order order : ordersInProgress) {
                if (user.getId() == order.getIdPassenger()) {

                    LOG.error("OrderMakeException: failed attempt to make order by user " + user.getPhone());

                    throw new OrderMakeException("User has orders IN_PROGRESS already");
                }
            }

            try {
                Location location = googleMapsAPI.findLocation(from.getCountry(), from.getCity(),
                        from.getStreet(), from.getHouseNum());
                Location location1 = googleMapsAPI.findLocation(to.getCountry(), to.getCity(),
                        to.getStreet(), to.getHouseNum());
                int distance = (int) (googleMapsAPI.getDistance(location, location1) / 1000);
                int price = (int) pricePerKilometer * distance + 30;

                message = message == null || message.equals("") ? "" : user.getName() + ": " + message;

                User anonymousUser = userDao.createUser(new User(UserIdentifier.A, phone, name));
                Order newOrder = new Order(from, to, anonymousUser.getId(), distance, price, message);
                newOrder.setTimeCreate(new Date());
                createdOrder = orderDao.create(newOrder);

                LOG.info("User anonymous makes new order " + newOrder.getId());

            } catch (InputDataWrongException | IndexOutOfBoundsException e) {

                LOG.error("InputDataWrongException: errors in calculate locations in Google API");

                throw new InputDataWrongException("Wrong calculation in Google API");
            }
        }

        return createdOrder;
    }

    @Override
    public Map<String, Object> calculateOrder(String lineFrom, String lineTo) throws
            InputDataWrongException, UnknownHostException {

        Address from = new Address(lineFrom);
        Address to = new Address(lineTo);
        Map<String, Object> map = new HashMap<>();

        if (!validateAddress(from) || !validateAddress(to)) {

            LOG.error("InputDataWrongException: wrong input data address");

            throw new InputDataWrongException("Wrong input data. Can not make order");
        }

        try {
            Location location = googleMapsAPI.findLocation(from.getCountry(), from.getCity(),
                    from.getStreet(), from.getHouseNum());
            Location location1 = googleMapsAPI.findLocation(to.getCountry(), to.getCity(),
                    to.getStreet(), to.getHouseNum());
            int distance = ((int) googleMapsAPI.getDistance(location, location1) / 1000);
            int price = (int) pricePerKilometer * distance + Constants.FEE_FOR_FILING_TAXI;

            map.put("distance", distance + "");
            map.put("price", price + "");

        } catch (InputDataWrongException | IndexOutOfBoundsException e) {

            LOG.error("InputDataWrongException: errors in calculate locations in Google API");

            throw new InputDataWrongException("Wrong calculation in Google API");
        }

        LOG.info("Someone calculates order from " + lineFrom + " to " + lineTo);

        return map;
    }

    @Override
    public Order getOrderInfo(long orderId) throws OrderNotFoundException {

        Order found = orderDao.findById(orderId);

        if (found == null) {

            LOG.error("OrderNotFoundException: failed attempt to get info about order with ID " +
                    orderId + " (order not found in data base)");

            throw new OrderNotFoundException("Order not found in data base");
        }

        LOG.info("Information has been requested for order ID=" + found.getId());

        return found;
    }

    @Override
    public Order getLastOrderInfo(String accessToken) throws UserNotFoundException, OrderNotFoundException {

        if (accessToken == null) {

            LOG.error("UserNotFoundException: failed attempt to find user in data base");

            throw new UserNotFoundException("User not found");
        }

        User user = accessKeys.get(accessToken);
        Order lastOrder = orderDao.getLastOrderOfUser(user.getId());

        if (lastOrder == null) {

            LOG.error("OrderNotFoundException: failed attempt to get info about last order of user " +
                    user.getPhone());

            throw new OrderNotFoundException("You don't have any orders");
        }

        LOG.info("User " + user.getPhone() +
                " get information for his last order ID=" + lastOrder.getId());

        return lastOrder;
    }

    @Override
    public Order cancelOrder(long orderId) throws OrderNotFoundException, WrongStatusOrderException {

        Order foundOrder = orderDao.findById(orderId);

        if (foundOrder == null) {

            LOG.error("OrderNotFoundException: failed attempt to cancel order with ID " +
                    orderId + " (not found in data base)");

            throw new OrderNotFoundException("Order not found in data base");

        } else if (foundOrder.getOrderStatus().equals(OrderStatus.CLOSED) ||
                    foundOrder.getOrderStatus().equals(OrderStatus.CANCELLED)) {

            LOG.error("WrongStatusOrderException: failed attempt to close order ID=" +
                    orderId);

            throw new WrongStatusOrderException("This order has been CLOSED or CANCELLED already");
        }

        foundOrder.setOrderStatus(OrderStatus.CANCELLED);
        foundOrder.setTimeCancelled(new Date());
        Order cancelledOrder = orderDao.update(foundOrder);

        LOG.info("Order ID=" + cancelledOrder.getId() + " was cancelled");

        return cancelledOrder;
    }

    @Override
    public Order closeOrder(String accessToken, long orderId) throws OrderNotFoundException,
            WrongStatusOrderException, DriverOrderActionException {

        User user = accessKeys.get(accessToken);
        Order foundOrder = orderDao.findById(orderId);

        if (foundOrder == null) {

            LOG.error("OrderNotFoundException: failed attempt to close order with ID " +
                    orderId + " by user " + user.getPhone());

            throw new OrderNotFoundException("Order not found in data base");

        } else if (user.getIdentifier() == UserIdentifier.D && foundOrder.getIdDriver() <= 0) {

            LOG.error("DriverOrderActionException: failed attempt to close order ID=" +
                    orderId + " by user ID=" + user.getId());

            throw new DriverOrderActionException("Order not found in driver orders list");

        } else if (foundOrder.getOrderStatus() != OrderStatus.IN_PROGRESS) {

            LOG.error("WrongStatusOrderException: failed attempt to close order ID=" +
                    orderId + " by user ID=" + user.getId());

            throw new WrongStatusOrderException("This order has wrong status (not IN_PROGRESS)");
        }

        foundOrder.setOrderStatus(OrderStatus.CLOSED);
        foundOrder.setTimeClosed(new Date());
        Order closedOrder = orderDao.update(foundOrder);

        LOG.info("User ID=" + user.getId() + " closed his order ID=" + closedOrder.getId());

        return closedOrder;
    }

    @Override
    public Order takeOrder(String accessToken, long orderId) throws OrderNotFoundException,
            WrongStatusOrderException, DriverOrderActionException {

        User user = accessKeys.get(accessToken);
        Order inProgress = orderDao.findById(orderId);

        List<Order> ordersInProgress = orderDao.getOrdersByStatus(OrderStatus.IN_PROGRESS);

        for (Order order : ordersInProgress) {
            if (user.getId() == order.getIdDriver()) {

                LOG.error("DriverOrderActionException: failed attempt to take order with ID " +
                        orderId + " by user " + user.getPhone());

                throw new DriverOrderActionException("Driver has orders IN_PROGRESS already");
            }
        }

        if (inProgress == null) {

            LOG.error("OrderNotFoundException: failed attempt to take order with ID" +
                    orderId + " by user " + user.getPhone());

            throw new OrderNotFoundException("Order not found in data base");

        } else if (inProgress.getOrderStatus() != OrderStatus.NEW) {

            LOG.error("WrongStatusOrderException: failed attempt to take order ID=" +
                    orderId + " by user " + user.getPhone());

            throw new WrongStatusOrderException("This order has wrong status (not NEW)");
        }

        inProgress.setIdDriver(user.getId());
        inProgress.setOrderStatus(OrderStatus.IN_PROGRESS);
        inProgress.setTimeTaken(new Date());
        Order takenOrder = orderDao.update(inProgress);

        LOG.info("User ID=" + user.getId() + " was take order ID=" + takenOrder.getId() + " for execution");

        return takenOrder;
    }

    @Override
    public User getUser(String accessToken) {

        User foundUser = accessKeys.get(accessToken);

        LOG.info("Request user ID=" + foundUser.getId());

        return foundUser;
    }

    @Override
    public Order[] createArrayOrdersForDriver(OrderStatus orderStatus, User driver)
            throws InputDataWrongException {

        //find all orders with status
        List<Order> ordersByStatus = getAllOrdersByStatus(orderStatus);

        LOG.info("Found " + ordersByStatus.size() + " orders with status " + orderStatus);

        Address addressDriver = new Address(driver.getUserCurrentLocation());
        Location locationDriver = googleMapsAPI.findLocation(
                addressDriver.getCountry(),
                addressDriver.getCity(),
                addressDriver.getStreet(),
                addressDriver.getHouseNum());

        Map <Integer, Order> mapOfDistances = new HashMap<>();
        int[] distances = new int[ordersByStatus.size()];

        int increaseDistance = 1;
        for (Order order : ordersByStatus) {
            int distance = calculateDistanceFromPassengerToDriver(order.getFrom(), locationDriver);

            if (mapOfDistances.keySet().size() > 0 && mapOfDistances.keySet().contains(distance)) {
                distance += increaseDistance++;
            }

            order.setDistanceToDriver(distance/1000);
            mapOfDistances.put(distance, order);
            distances[ordersByStatus.indexOf(order)] = distance;
        }

        LOG.info("Create array orders with status " + orderStatus.toString() +
                                    " with distance to " + addressDriver.toLine());

        //create array orders and sorting by distance to driver
        Order[] sortingOrders = new Order[ordersByStatus.size()];
        Arrays.sort(distances);

        for (int i = 0; i < distances.length; i++) {
            sortingOrders[i] = mapOfDistances.get(distances[i]);
        }

        LOG.info("Add new array orders with status " + orderStatus.toString() +
                                                " for driver ID=" + driver.getId());

        return sortingOrders;
    }

    @Override
    public User updateUser(Map<String, String> map, String accessToken) throws RegisterException {

        User currentUser = accessKeys.get(accessToken);
        int currentUserId = currentUser.getId();

        User testUser = userDao.findByPhone(map.get("phone"));

        if (testUser != null && currentUserId != testUser.getId()) {

            LOG.error("RegisterException: failed attempt to update user " +
                    currentUser.getPhone() + " (phone " + map.get("phone") + " already in use by another user)");

            throw new RegisterException("This phone is already in use by another user");

        } else {

            //create user for update
            User newUser = new User(currentUser.getIdentifier(), map.get("phone"), map.get("name"));
            newUser.setId(currentUserId);
            newUser.setPass(map.get("pass"));
            if (currentUser.getIdentifier().equals(UserIdentifier.P)) {
                newUser.setHomeAddress(new Address(map.get("homeAddress")));
            } else if (currentUser.getIdentifier().equals(UserIdentifier.D)) {
                newUser.setCar(new Car(map.get("carType"), map.get("carModel"), map.get("carNumber")));
            }

            User updatedUser = userDao.updateUser(newUser);
            accessKeys.put(accessToken, updatedUser);

            LOG.info("Change registered data for user. User ID=" + updatedUser.getId() + " was updated");

            return updatedUser;
        }
    }

    @Override
    public User deleteUser(String accessToken) throws WrongStatusOrderException {

        User user = accessKeys.get(accessToken);

        //check open orders of user (NEW or IN_PROGRESS)
        List<Order> ordersNew = orderDao.getOrdersByStatus(OrderStatus.NEW);
        List<Order> ordersInProgress = orderDao.getOrdersByStatus(OrderStatus.IN_PROGRESS);

        for (Order order : ordersNew) {
            if (user.getId() == order.getIdPassenger()) {

                LOG.error("WrongStatusOrderException: failed attempt to delete user " + user.getPhone());

                throw new WrongStatusOrderException
                        ("Can't delete. You have orders with status NEW");
            }
        }

        for (Order order : ordersInProgress) {
            if (user.getId() == order.getIdPassenger() ||
                                    user.getId() == order.getIdDriver()) {

                LOG.error("WrongStatusOrderException: failed attempt to delete user " + user.getPhone());

                throw new WrongStatusOrderException
                        ("Can't delete. You have orders with status IN_PROGRESS");
            }
        }

        User deleteUser = userDao.deleteUser(user.getId());
        accessKeys.remove(accessToken);

        LOG.info("User ID=" + user.getId() + " was deleted");

        return deleteUser;
    }

    @Override
    public Address getUserLocation() {

        return new Address(Constants.USER_LOCATION_PATH);
    }

    @Override
    public Order updateOrder(Order order) {

        return orderDao.update(order);
    }

    @Override
    public List<Order> getOrdersOfUser(int userId, int from, int to) {

        return userDao.getOrdersOfUser(userId, from, to);
    }

    @Override
    public int getQuantityOrdersOfUser(int userId) {

        return userDao.getQuantityOrdersOfUser(userId);
    }

    @Override
    public Map<Integer, Order> getMapDistancesToDriver(String orderStatus, String lineAddressDriver)
            throws InputDataWrongException {

        //find all orders with status
        List<Order> orders = getAllOrdersByStatus(Enum.valueOf(OrderStatus.class, orderStatus));

        LOG.info("Found " + orders.size() + " orders with status " + orderStatus);

        //create list of int unique distances
        List<Integer> distancesList = getArrayDistancesToDriver(orders, new Address(lineAddressDriver));
        int[] distances = new int[distancesList.size()];

        //create map of distances
        Map<Integer, Order> mapDistances = new TreeMap<>();
        for (int i = 0; i < distancesList.size(); i++) {
            distances[i] = distancesList.get(i);
            mapDistances.put(distances[i], orders.get(i));
        }

        //sorting map by distances
        Arrays.sort(distances);
        Map<Integer, Order> sortingMapDistances = new TreeMap<>();
        for (int i = 0; i < distances.length; i++) {
            sortingMapDistances.put(distances[i], mapDistances.get(distances[i]));
        }

        LOG.info("Create map of distances from orders to driver address " + lineAddressDriver);

        return sortingMapDistances;
    }

    @Override
    public User findById(int id) {

        User foundUser = userDao.findById(id);

        LOG.info("Request user by ID=" + id);

        return foundUser;
    }

    public List<Integer> getArrayDistancesToDriver(List<Order> orders, Address addressDriver)
            throws InputDataWrongException {

        Location locationDriver = googleMapsAPI.findLocation
                (addressDriver.getCountry(), addressDriver.getCity(),
                        addressDriver.getStreet(), addressDriver.getHouseNum());

        //int[] distances = new int[orders.size()];
        List<Integer> distances = new ArrayList<>();

        //increasing distance by 1 or little more meters for unique distances to driver
        int increaseDistance = 1;

        for (Order order : orders) {
            int i = orders.indexOf(order);

            Location locationPassenger = googleMapsAPI.findLocation(
                    orders.get(i).getFrom().getCountry(),
                    orders.get(i).getFrom().getCity(),
                    orders.get(i).getFrom().getStreet(),
                    orders.get(i).getFrom().getHouseNum());

            int distance = new Distance(locationDriver, locationPassenger).calculateDistance();


            if (!distances.isEmpty() && distances.contains(distance)) {
                distances.add(distance + increaseDistance);
                increaseDistance++;

            } else {
                distances.add(distance);
            }
        }

        return distances;
    }


    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public OrderDao getOrderDao() {
        return orderDao;
    }

    public void setOrderDao(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    public double getPricePerKilometer() {
        return pricePerKilometer;
    }

    public void setPricePerKilometer(double pricePerKilometer) {
        this.pricePerKilometer = pricePerKilometer;
    }

    public GoogleMapsAPI getGoogleMapsAPI() {
        return googleMapsAPI;
    }

    public void setGoogleMapsAPI(GoogleMapsAPI googleMapsAPI) {
        this.googleMapsAPI = googleMapsAPI;
    }

    public Map<String, User> getAccessKeys() {
        return accessKeys;
    }

    public void setAccessKeys(Map<String, User> accessKeys) {
        this.accessKeys = accessKeys;
    }

    public int calculateDistanceFromPassengerToDriver(Address addressPassenger, Location locationDriver)
            throws InputDataWrongException {

        Location locationPassenger = googleMapsAPI.findLocation(
                addressPassenger.getCountry(),
                addressPassenger.getCity(),
                addressPassenger.getStreet(),
                addressPassenger.getHouseNum());

        return new Distance(locationDriver, locationPassenger).calculateDistance();
    }

    public List<Order> getAllOrdersByStatus(OrderStatus status) {

        List<Order> ordersByStatus = orderDao.getOrdersByStatus(status);

        LOG.info("Get all order by status " + status.toString());

        return ordersByStatus;
    }

    public class Distance implements Comparable {

        private Location fromLocation;
        private Location toLocation;
        private GoogleMapsAPI googleMapsAPI;

        private int averageSpeedKmH;
        private int timeInMin;

        public Distance() {
        }

        public Distance(Location fromLocation, Location toLocation) {
            this.fromLocation = fromLocation;
            this.toLocation = toLocation;
            googleMapsAPI = new GoogleMapsAPIImpl();
            averageSpeedKmH = Constants.AVERAGE_SPEED_KM_H;
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

        public int calculateDistance() throws InputDataWrongException {
            return (int) googleMapsAPI.getDistance(fromLocation, toLocation);
        }

        public void setSpeedKmH(int speedKmH) {
            this.averageSpeedKmH = speedKmH;
        }

        public int getTimeInMin() throws InputDataWrongException {
            return (this.calculateDistance() / 1000) / this.averageSpeedKmH;
        }

        @Override
        public int compareTo(Object o) {

            Distance tmp = (Distance)o;
            try {
                double distance1 = this.googleMapsAPI.getDistance(fromLocation, toLocation);
                double distance2 = tmp.googleMapsAPI.getDistance(fromLocation, toLocation);
                return distance1 - distance2 > 0 ? 1 : -1 ;

            } catch (InputDataWrongException e) {
                e.printStackTrace();
            }
            return 0;
        }
    }

    private boolean validateAddress(Address address) throws InputDataWrongException {

        if (!address.getCountry().matches("(?i).*[a-zA-Z].*")) {
            throw new InputDataWrongException("Wrong data \"country\"");
            //result = false;
        } else if (!address.getCity().matches("(?i).*[a-zA-Z].*")) {
            throw new InputDataWrongException("Wrong data \"city\"");
            //result = false;
        } else if (!address.getStreet().matches("(?i).*[a-zA-Z].*")) {
            throw new InputDataWrongException("Wrong data \"street\"");
            //result = false;
        } else if (!address.getHouseNum().matches(".*\\d+.*")) {
            throw new InputDataWrongException("Wrong data \"houseNum\"");
            //result = false;
        }

        return true;
    }

    private boolean validateRegisterData(Map<String, String> map) throws InputDataWrongException {

        if (!map.get("phone").matches(".*\\d+.*") ||
                map.get("phone").length() != 12) {
            throw new InputDataWrongException
                    ("Wrong data \"phone\". Please input your phone in format 380XXXXXXXXX");
            //result = false;
        } else if (!map.get("pass").matches("(?i).*[a-zA-Z].*") ||
                !map.get("pass").matches(".*\\d+.*")) {
            throw new InputDataWrongException("Wrong data \"password\". " +
                            "Password must has length > 3 \nand contains any letters and digits");
            //result = false;
        } else if (!map.get("name").matches("(?i).*[a-zA-Z].*")) {
            throw new InputDataWrongException("Wrong data \"name\"");
            //result = false;
        }

        return (map.get("homeAddress") != null &&
                        validateAddress(new Address(map.get("homeAddress")))) ||
                ((map.get("carType") != null ||
                        map.get("carModel") != null || map.get("carNumber") != null) &&
                        validateCar(map));
    }

    private boolean validateCar(Map<String, String> map) throws InputDataWrongException {

        if (!map.get("carType").matches("(?i).*[a-zA-Z].*")) {
            throw new InputDataWrongException("Wrong data \"cat type\"");
            //result = false;
        } else if (!map.get("carModel").matches("(?i).*[a-zA-Z].*")) {
            throw new InputDataWrongException("Wrong data \"car model\"");
            //result = false;
        } else if (!map.get("carNumber").matches("(?i).*[a-zA-Z].*") ||
                !map.get("carNumber").matches(".*\\d+.*")) {
            throw new InputDataWrongException
                    ("Wrong data \"car number\". Please input your car number with letters and digits");
            //result = false;
        }
        return true;
    }
}
