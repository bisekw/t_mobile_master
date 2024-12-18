package framework.steps;

import java.util.HashMap;
import java.util.Map;

public class ScenarioContext {
    private Map<String, Object> data;

    public ScenarioContext(){
        data = new HashMap<>();
    }

    public void setData(String key, Object value) {
        data.put(key, value);
    }

    public Object getData(String key) {
        return data.get(key);
    }

    public void clearData() {
        data.clear();
    }

}
