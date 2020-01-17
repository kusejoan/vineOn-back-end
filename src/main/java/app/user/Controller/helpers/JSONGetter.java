package app.user.Controller.helpers;

import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JSONGetter {
    public static JSONObject getParams(String webformdata) throws Exception
    {
        if(webformdata.contains("%"))
        {
            webformdata = webformdata.replaceAll("%5B", "[");
            webformdata = webformdata.replaceAll("%5D", "]");
            webformdata = webformdata.replaceAll("%2F", "/");
            webformdata = webformdata.replaceAll("%3B", ";");
            webformdata = webformdata.replaceAll("%3D", "=");
            JSONObject test = new JSONObject();
            String[] array = webformdata.split("&");
            for(String s: array)
            {
                if(s.contains("params"))
                {
                    Matcher key = Pattern.compile("\\[([^)]+)\\]").matcher(s);
                    Matcher value = Pattern.compile("=(.*)").matcher(s);
                    if(key.find() && value.find())
                    {
                        test.put(key.group(1),value.group(1));
                    }
                }
            }
            return test;

        }
        return new JSONObject(webformdata).getJSONObject("params");
    }
    public static JSONObject getParamsCSV(String webformdata) throws Exception
    {
        int column = 0;
        webformdata = webformdata.replaceAll("%5B", "[");
        webformdata = webformdata.replaceAll("%5D", "]");
        webformdata = webformdata.replaceAll("%2F", "/");
        webformdata = webformdata.replaceAll("%3B", ";");
        webformdata = webformdata.replaceAll("%3D", "=");
        webformdata = webformdata.replace('+', ' ');
        JSONObject test = new JSONObject();
        JSONArray wines = new JSONArray();
        JSONObject wine = new JSONObject();
        String[] array = webformdata.split("&");
        for(String s: array)
        {
            if(s.contains("params[data]"))
            {
                s = s.substring(12);

                Matcher key = Pattern.compile("\\[.*\\]\\[([^)]+)\\]").matcher(s);
                Matcher value = Pattern.compile("=(.*)").matcher(s);
                if(key.find() && value.find())
                {
                    wine.put(key.group(1),value.group(1));
                    if(column==4)
                    {
                        wines.put(wine);
                        wine = new JSONObject();
                    }
                    column = (column+1)%5;
                }
            }
        }
        test.put("wines",wines);
        return test;
    }
}
