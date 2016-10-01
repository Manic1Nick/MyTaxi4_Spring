package ua.artcode.taxi.model;

public enum OrderStatus {

    // new OrderStatus("NEW")
    NEW(), IN_PROGRESS(), CANCELLED(), CLOSED();
    //0         1             2          3
    // "NEW" , "IN_PROGRESS", ...

    OrderStatus() {
    }


    // see Enum method
}
