package ua.artcode.taxi.model;

import java.util.Collection;
import java.util.List;

public interface DriverActive {

    int getId();
    void setId(int id);

    UserIdentifier getIdentifier();
    void setIdentifier(UserIdentifier identifier);

    String getPhone();
    void setPhone(String phone);

    String getPass();
    void setPass(String pass);

    String getName();
    void setName(String name);

    Car getCar();
    void setCar(Car car);

    Collection<Long> getOrderIds();
    void setOrderIds(List<Long> orderIds);

    String toString();
}
