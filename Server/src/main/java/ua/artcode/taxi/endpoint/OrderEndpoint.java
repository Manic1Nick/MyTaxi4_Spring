package ua.artcode.taxi.endpoint;

import ua.artcode.taxi.model.Order;

import javax.ws.rs.*;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class OrderEndpoint {

    private final Map<Long,Order> orderMap = new ConcurrentHashMap<>();

    public OrderEndpoint() {
    }

    @Path(value = "/add")
    @POST
    @Consumes("application/json")
    public boolean addOrder(Order order) {
        orderMap.put(order.getId(), order);
        return true;
    }

    @Path("/controller")
    @GET
    public String hello(@QueryParam("name") String name){
        return "controller " + name + " " + new Date();
    }

    //
    @Path(value = "/get/{id}")
    @GET
    @Produces("application/json")
    public Order getById(@PathParam("id") int id) {
        return orderMap.get(id);
    }
}
