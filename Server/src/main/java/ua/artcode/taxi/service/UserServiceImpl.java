package ua.artcode.taxi.service;

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

    @Autowired
    private UserDao userDao;

    @Autowired
    private OrderDao orderDao;

    private double pricePerKilometer;
    private GoogleMapsAPI googleMapsAPI;
    private Map<String, User> accessKeys;

    public UserServiceImpl() {
        pricePerKilometer = Constants.PRICE_PER_KILOMETER_UAH;
        googleMapsAPI = new GoogleMapsAPIImpl();
        accessKeys = new ConcurrentHashMap<>();
    }

    public UserServiceImpl(UserDao userDao, OrderDao orderDao) {
        this.userDao = userDao;
        this.orderDao = orderDao;
        pricePerKilometer = Constants.PRICE_PER_KILOMETER_UAH;
        googleMapsAPI = new GoogleMapsAPIImpl();
        accessKeys = new ConcurrentHashMap<>();
    }

    @Override
    public User registerPassenger(Map<String, String> map)
            throws RegisterException, InputDataWrongException {

        if (userDao.findByPhone(map.get("phone")) != null) {
            throw new RegisterException("This phone using already");
        }

        User newUser = null;

        if (validateRegisterData(map)) {
            newUser = new User(
                    UserIdentifier.P,
                    map.get("phone"),
                    map.get("pass"),
                    map.get("name"),
                    new Address(map.get("country"),
                            map.get("city"),
                            map.get("street"),
                            map.get("houseNum")));
        }

        return userDao.createUser(newUser);
    }

    @Override
    public User registerDriver(Map<String, String> map)
            throws RegisterException, InputDataWrongException {

        if (userDao.findByPhone(map.get("phone")) != null) {
            throw new RegisterException("This phone using already");
        }

        User newUser = null;

        if (validateRegisterData(map)) {
            newUser = new User(
                    UserIdentifier.D,
                    map.get("phone"),
                    map.get("pass"),
                    map.get("name"),
                    new Car(map.get("carType"),
                            map.get("carModel"),
                            map.get("carNumber")));
        }

        return userDao.createUser(newUser);
    }

    @Override
    public String login(String phone, String pass) throws LoginException {

        User found = userDao.findByPhone(phone);

        if (found == null) {
            throw new LoginException("User not found or incorrect password");
        }

        String accessKey = UUID.randomUUID().toString();
        accessKeys.put(accessKey, found);

        return accessKey;
    }

    @Override
    public Order makeOrder(String accessToken, String lineFrom, String lineTo, String message)
            throws OrderMakeException, UserNotFoundException,
                    InputDataWrongException, UnknownHostException {

        Order createdOrder = null;

        if (validateAddressFromLine(lineFrom) && validateAddressFromLine(lineTo)) {

            User user = accessKeys.get(accessToken);

            List<Order> ordersNew = orderDao.getOrdersByStatus(OrderStatus.NEW);
            for (Order order : ordersNew) {
                if (user.getId() == order.getIdPassenger()) {
                    throw new OrderMakeException("User has orders NEW already");
                }
            }

            List<Order> ordersInProgress = orderDao.getOrdersByStatus(OrderStatus.IN_PROGRESS);
            for (Order order : ordersInProgress) {
                if (user.getId() == order.getIdPassenger()) {
                    throw new OrderMakeException("User has orders IN_PROGRESS already");
                }
            }

            try {
                Address from = new Address(lineFrom);
                Address to = new Address(lineTo);

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

            } catch (InputDataWrongException | IndexOutOfBoundsException e) {
                throw new InputDataWrongException("Wrong calculation in Google API");
            }
        }
        return createdOrder;
    }

    @Override
    public Order makeOrderAnonymous(String phone, String name, String lineFrom, String lineTo, String message)
            throws OrderMakeException, InputDataWrongException {

        Order createdOrder = null;

        if (validateAddressFromLine(lineFrom) && validateAddressFromLine(lineTo)) {

            User user = userDao.findByPhone(phone);

            List<Order> ordersNew = orderDao.getOrdersByStatus(OrderStatus.NEW);
            for (Order order : ordersNew) {
                if (user.getId() == order.getIdPassenger()) {
                    throw new OrderMakeException("User has orders NEW already");
                }
            }

            List<Order> ordersInProgress = orderDao.getOrdersByStatus(OrderStatus.IN_PROGRESS);
            for (Order order : ordersInProgress) {
                if (user.getId() == order.getIdPassenger()) {
                    throw new OrderMakeException("User has orders IN_PROGRESS already");
                }
            }

            try {
                Address from = new Address(lineFrom);
                Address to = new Address(lineTo);

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

            } catch (InputDataWrongException | IndexOutOfBoundsException e) {
                throw new InputDataWrongException("Wrong calculation in Google API");
            }
        }

        return createdOrder;
    }

    @Override
    public Map<String, Object> calculateOrder(String lineFrom, String lineTo) throws
            InputDataWrongException, UnknownHostException {

        Map<String, Object> map = new HashMap<>();

        if (validateAddressFromLine(lineFrom) && validateAddressFromLine(lineTo)) {
            try {
                Address from = new Address(lineFrom);
                Address to = new Address(lineTo);

                Location location = googleMapsAPI.findLocation(from.getCountry(), from.getCity(),
                        from.getStreet(), from.getHouseNum());
                Location location1 = googleMapsAPI.findLocation(to.getCountry(), to.getCity(),
                        to.getStreet(), to.getHouseNum());
                int distance = ((int) googleMapsAPI.getDistance(location, location1) / 1000);
                int price = (int) pricePerKilometer * distance + Constants.FEE_FOR_FILING_TAXI_UAH;

                map.put("distance", String.valueOf(distance));
                map.put("price", String.valueOf(price));

            } catch (InputDataWrongException | IndexOutOfBoundsException e) {
                throw new InputDataWrongException("Wrong calculation in Google API");
            }
        }

        return map;
    }

    @Override
    public Order getOrderInfo(long orderId) throws OrderNotFoundException {

        Order found = orderDao.findById(orderId);

        if (found == null) {
            throw new OrderNotFoundException("Order not found in data base");
        }
        return found;
    }

    @Override
    public Order getLastOrderInfo(String accessToken)
            throws UserNotFoundException, OrderNotFoundException {

        if (accessToken == null) {
            throw new UserNotFoundException("User not found");
        }

        User user = accessKeys.get(accessToken);
        Order lastOrder = orderDao.getLastOrderOfUser(user.getId());

        if (lastOrder == null) {
            throw new OrderNotFoundException("You don't have any orders");
        }
        return lastOrder;
    }

    @Override
    public Order cancelOrder(long orderId) throws OrderNotFoundException, WrongStatusOrderException {

        Order foundOrder = orderDao.findById(orderId);

        if (foundOrder == null) {
            throw new OrderNotFoundException("Order not found in data base");

        } else if (foundOrder.getOrderStatus().equals(OrderStatus.CLOSED) ||
                    foundOrder.getOrderStatus().equals(OrderStatus.CANCELLED)) {
            throw new WrongStatusOrderException("This order has been CLOSED or CANCELLED already");
        }
        foundOrder.setOrderStatus(OrderStatus.CANCELLED);
        foundOrder.setTimeCancelled(new Date());

        return orderDao.update(foundOrder);
    }

    @Override
    public Order closeOrder(String accessToken, long orderId) throws OrderNotFoundException,
            WrongStatusOrderException, DriverOrderActionException {

        User user = accessKeys.get(accessToken);
        Order foundOrder = orderDao.findById(orderId);

        if (foundOrder == null) {
            throw new OrderNotFoundException("Order not found in data base");

        } else if (user.getIdentifier() == UserIdentifier.D && foundOrder.getIdDriver() <= 0) {
            throw new DriverOrderActionException("This order is not your order");

        } else if (foundOrder.getOrderStatus() != OrderStatus.IN_PROGRESS) {
            throw new WrongStatusOrderException("This order has wrong status (not IN_PROGRESS)");
        }

        foundOrder.setOrderStatus(OrderStatus.CLOSED);
        foundOrder.setTimeClosed(new Date());

        return orderDao.update(foundOrder);
    }

    @Override
    public Order takeOrder(String accessToken, long orderId) throws OrderNotFoundException,
            WrongStatusOrderException, DriverOrderActionException {

        User user = accessKeys.get(accessToken);
        Order inProgress = orderDao.findById(orderId);

        List<Order> ordersInProgress = orderDao.getOrdersByStatus(OrderStatus.IN_PROGRESS);

        for (Order order : ordersInProgress) {
            if (user.getId() == order.getIdDriver()) {
                throw new DriverOrderActionException("Driver has orders IN_PROGRESS already");
            }
        }

        if (inProgress == null) {
            throw new OrderNotFoundException("Order not found in data base");

        } else if (inProgress.getOrderStatus() != OrderStatus.NEW) {
            throw new WrongStatusOrderException("This order has wrong status (not NEW)");
        }

        inProgress.setIdDriver(user.getId());
        inProgress.setOrderStatus(OrderStatus.IN_PROGRESS);
        inProgress.setTimeTaken(new Date());

        return orderDao.update(inProgress);
    }

    @Override
    public User getUser(String accessToken) {

        return accessKeys.get(accessToken);
    }

    @Override
    public Order[] createArrayOrdersForDriver(OrderStatus orderStatus, User driver)
            throws InputDataWrongException {

        //find all orders with status
        List<Order> ordersByStatus = getAllOrdersByStatus(orderStatus);
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

        //create array orders and sorting by distance to driver
        Order[] sortingOrders = new Order[ordersByStatus.size()];
        Arrays.sort(distances);

        for (int i = 0; i < distances.length; i++) {
            sortingOrders[i] = mapOfDistances.get(distances[i]);
        }

        return sortingOrders;
    }

    @Override
    public User updateUser(Map<String, String> map, String accessToken) throws RegisterException {

        User currentUser = accessKeys.get(accessToken);
        int currentUserId = currentUser.getId();

        User testUser = userDao.findByPhone(map.get("phone"));

        if (testUser != null && currentUserId != testUser.getId()) {
            throw new RegisterException("This phone is already in use by another user");

        } else {

            //create user for update
            User newUser = new User(currentUser.getIdentifier(), map.get("phone"), map.get("name"));
            newUser.setId(currentUserId);
            newUser.setPass(map.get("pass"));
            if (currentUser.getIdentifier().equals(UserIdentifier.P)) {
                newUser.setHomeAddress(new Address(
                        map.get("country"), map.get("city"), map.get("street"), map.get("houseNum")));
            } else if (currentUser.getIdentifier().equals(UserIdentifier.D)) {
                newUser.setCar(new Car(
                        map.get("carType"), map.get("carModel"), map.get("carNumber")));
            }

            User updatedUser = userDao.updateUser(newUser);
            accessKeys.put(accessToken, updatedUser);

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
                throw new WrongStatusOrderException
                        ("Can't delete. You have orders with status NEW");
            }
        }

        for (Order order : ordersInProgress) {
            if (user.getId() == order.getIdPassenger() ||
                                    user.getId() == order.getIdDriver()) {
                throw new WrongStatusOrderException
                        ("Can't delete. You have orders with status IN_PROGRESS");
            }
        }

        User deleteUser = userDao.deleteUser(user.getId());
        accessKeys.remove(accessToken);

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

        return sortingMapDistances;
    }

    @Override
    public User findById(int id) {

        return userDao.findById(id);
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

        return orderDao.getOrdersByStatus(status);
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

    private boolean validateRegisterData(Map<String, String> map)
            throws InputDataWrongException {

        if(!map.get("phone").matches(".*\\d+.*") ||
                map.get("phone").matches("(?i).*[a-zA-Z].*") ||
                map.get("phone").length() != 12)

            throw new InputDataWrongException
                    ("Wrong data \"phone\".\nPlease input your phone in format 380XXXXXXXXX");

        if(!map.get("pass").matches("(?i).*[a-zA-Z].*") ||
                !map.get("pass").matches(".*\\d+.*"))

            throw new InputDataWrongException("Wrong data \"password\". " +
                    "Password must has length > 3 \nand contains any letters and digits");

        if(!map.get("name").matches("(?i).*[a-zA-Z].*"))

            throw new InputDataWrongException("Wrong data \"name\"");

        return ((map.get("country") != null && validateAddressData(map)) ||
                map.get("carType") != null && validateCarData(map));
    }


    private boolean validateAddressData(Map<String, String> map) throws InputDataWrongException {

        if (!map.get("country").matches("(?i).*[a-zA-Z].*"))
            throw new InputDataWrongException("Wrong data \"country\"");

        else if (!map.get("city").matches("(?i).*[a-zA-Z].*"))
            throw new InputDataWrongException("Wrong data \"city\"");

        else if (!map.get("street").matches("(?i).*[a-zA-Z].*"))
            throw new InputDataWrongException("Wrong data \"street\"");

        else if (!map.get("houseNum").matches(".*\\d+.*"))
            throw new InputDataWrongException("Wrong data \"houseNum\"");

        return true;
    }

    private boolean validateCarData(Map<String, String> map) throws InputDataWrongException {

        if (!map.get("carType").matches("(?i).*[a-zA-Z].*"))
            throw new InputDataWrongException("Wrong data \"car type\"");

        else if (!map.get("carModel").matches("(?i).*[a-zA-Z].*"))
            throw new InputDataWrongException("Wrong data \"car model\"");

        else if (!map.get("carNumber").matches("(?i).*[a-zA-Z].*") ||
                !map.get("carNumber").matches(".*\\d+.*"))
            throw new InputDataWrongException("Wrong data \"car number\". " +
                    "Please input your car number with letters and digits");

        return true;
    }

    private boolean validateAddressFromLine(String line) throws InputDataWrongException {

        String[] fields = line.split(",", 4);

        if (!fields[0].matches("(?i).*[a-zA-Z].*")) {
            throw new InputDataWrongException("Wrong data \"country\"");

        } else if (!fields[1].matches("(?i).*[a-zA-Z].*")) {
            throw new InputDataWrongException("Wrong data \"city\"");

        } else if (!fields[2].matches("(?i).*[a-zA-Z].*")) {
            throw new InputDataWrongException("Wrong data \"street\"");

        } else if (!fields[3].matches(".*\\d+.*")) {
            throw new InputDataWrongException("Wrong data \"houseNum\"");
        }

        return true;
    }
}
