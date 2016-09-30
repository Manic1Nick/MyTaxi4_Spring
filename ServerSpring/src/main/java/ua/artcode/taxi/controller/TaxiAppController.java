package ua.artcode.taxi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.artcode.taxi.exception.OrderNotFoundException;
import ua.artcode.taxi.model.Order;
import ua.artcode.taxi.service.UserService;

@RestController
public class TaxiAppController {

    @Autowired
    private UserService userService;

    @RequestMapping("/testorder")
    public Order getRandomOrder(@RequestParam(name = "id") Long id){
        try {
            return userService.getOrderInfo(id);
        } catch (OrderNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping("/quantityordersofuser")
    public int getQuantityOrdersOfUser(@RequestParam(name = "id") int id){
        return userService.getQuantityOrdersOfUser(id);
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
