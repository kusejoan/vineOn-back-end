/*
 * Copyright (c) 2020.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *  1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *  2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *  3. Neither the name of Vineon nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 *  POSSIBILITY OF SUCH DAMAGE.
 */

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
            webformdata = webformdata.replace('+', ' ');
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
