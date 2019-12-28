package app.user.Controller.helpers;

import org.springframework.boot.configurationprocessor.json.JSONObject;

public class JSONGetter {
    public static JSONObject getParams(String json) throws Exception
    {
        return new JSONObject(json).getJSONObject("params");
    }
}
