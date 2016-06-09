package ua.artcode.taxi.to;

import java.util.HashMap;
import java.util.Map;

public class MessageBody {

    private Map<String, Object> map;

    public MessageBody() {
        map = new HashMap<>();
    }

    public MessageBody(Map<String, Object> map) {
        this.map = map;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }
}