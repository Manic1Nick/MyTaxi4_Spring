package ua.artcode.taxi.utils;

import com.google.gson.Gson;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import ua.artcode.taxi.model.*;

import java.util.HashMap;
import java.util.Map;

public class ReflectionFormatter {

    private Gson gson;

    public static Map<String, Object> userToJsonMap(User user) {
        Gson gson = new Gson();

        Map<String, Object> map = new HashMap<>();

        map.put("id", user.getId() + "");
        map.put("identifier", user.getIdentifier().toString());
        map.put("phone", user.getPhone());
        map.put("pass", user.getPass());
        map.put("name", user.getName());
        map.put("homeAddress", gson.toJson(user.getHomeAddress()));
        map.put("car", gson.toJson(user.getCar()));

        return map;
    }

    public static Map<String, Object> orderToJsonMap(Order order) {
        Gson gson = new Gson();

        Map<String, Object> map = new HashMap<>();

        map.put("id", String.valueOf(order.getId()));

        if (order.getOrderStatus() != null) {
            map.put("orderStatus", order.getOrderStatus().toString());
        }

        map.put("addressFrom", gson.toJson(order.getFrom()));
        map.put("addressTo", gson.toJson(order.getTo()));

        map.put("passenger", order.getIdPassenger());

        if (order.getIdDriver() >= 0) {
            map.put("driver", order.getIdDriver());
        }

        map.put("distance", String.valueOf(order.getDistance()));
        map.put("price", String.valueOf(order.getPrice()));
        map.put("message", String.valueOf(order.getMessage()));

        map.put("distanceToDriver", String.valueOf(order.getDistanceToDriver()));

        return map;
    }

    public static User getUserFromJsonMap(Map<String, Object> map) {
        Gson gson = new Gson();

        //create User
        User user = new User(
                Enum.valueOf(UserIdentifier.class, map.get("identifier").toString()),
                map.get("phone").toString(),
                map.get("name").toString()
        );

        user.setId(Integer.parseInt(map.get("id").toString()));

        if (map.get("pass") != null) {
            user.setPass(map.get("pass").toString());
        }

        if (map.get("homeAddress") != null) {
            user.setHomeAddress(gson.fromJson(map.get("homeAddress").toString(), Address.class));
        }

        if (map.get("car") != null) {
            user.setCar(gson.fromJson(map.get("car").toString(), Car.class));
        }

        return user;
    }

    public static Order getOrderFromJsonMap(Map<String, Object> map) {
        Gson gson = new Gson();

        Order order = new Order();

        order.setId(Long.parseLong(map.get("id").toString()));
        order.setOrderStatus(Enum.valueOf(OrderStatus.class, map.get("orderStatus").toString()));
        order.setFrom(gson.fromJson(map.get("addressFrom").toString(), Address.class));
        order.setTo(gson.fromJson(map.get("addressTo").toString(), Address.class));

        order.setIdPassenger((int) map.get("passenger"));

        if (map.get("driver") != null) {
            order.setIdDriver((int) map.get("driver"));
        }

        order.setDistance(Integer.parseInt(map.get("distance").toString()));
        order.setPrice(Integer.parseInt(map.get("price").toString()));
        order.setMessage(map.get("message").toString());

        order.setDistanceToDriver(Integer.parseInt(map.get("distanceToDriver").toString()));

        return order;
    }


    public static String userToJSON (User user) {

        JSONObject jobj = new JSONObject();
        jobj.put("id", String.valueOf(user.getId()));
        jobj.put("ifentifier", user.getIdentifier().toString());
        jobj.put("phone", user.getPhone());
        jobj.put("name", user.getName());

        if (user.getIdentifier() == UserIdentifier.P) {
            JSONArray homeAddress = new JSONArray();

            homeAddress.add("country:" + user.getHomeAddress().getCountry());
            homeAddress.add("city:" + user.getHomeAddress().getCity());
            homeAddress.add("street:" + user.getHomeAddress().getStreet());
            homeAddress.add("houseNum:" + user.getHomeAddress().getHouseNum());

            jobj.put("homeAddress", homeAddress);

        }

        if (user.getIdentifier() == UserIdentifier.D) {
            JSONArray car = new JSONArray();

            car.add("type:" + user.getCar().getType());
            car.add("model:" + user.getCar().getModel());
            car.add("number:" + user.getCar().getNumber());

            jobj.put("car", car);
        }

        return jobj.toJSONString();
    }

    public static User userFromJSON (String userJson) {



        return null;
    }
}