package ua.artcode.taxi.model;

/**
 * Created with IntelliJ IDEA.
 * User: Роман
 * Date: 23.04.16
 * Time: 17:03
 * To change this template use File | Settings | File Templates.
 */
public class Car {

    String type;
    String model;
    String number;

    public Car() {

    }

    public Car(String type, String model, String number) {
        this.number = number;
        this.model = model;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
