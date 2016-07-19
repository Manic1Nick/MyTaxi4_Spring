package ua.artcode.taxi.utils;

import com.google.gson.Gson;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import ua.artcode.taxi.model.Order;
import ua.artcode.taxi.model.User;
import ua.artcode.taxi.model.UserIdentifier;
import ua.artcode.taxi.to.Message;
import ua.artcode.taxi.to.MessageBody;

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

        Map<String, Object> map = new HashMap<>();

        map.put("id", order.getId() + "");

        if (order.getOrderStatus() != null) {
            map.put("orderStatus", order.getOrderStatus().toString());
        }

        map.put("addressFrom", new Gson().toJson(order.getFrom()));
        map.put("addressTo", new Gson().toJson(order.getTo()));

        Message messagePassenger = new Message();
        messagePassenger.setMessageBody(new MessageBody(userToJsonMap(order.getPassenger())));
        map.put("passenger", new Gson().toJson(messagePassenger));

        if (order.getDriver() != null) {
            Message messageDriver = new Message();

            messageDriver.setMessageBody(new MessageBody(userToJsonMap(order.getDriver())));
            map.put("driver", new Gson().toJson(messageDriver));
        }

        map.put("distance", order.getDistance() + "");
        map.put("price", order.getPrice() + "");
        map.put("message", order.getMessage() + "");

        return map;
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