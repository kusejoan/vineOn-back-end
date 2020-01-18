package app.user.Controller;

import app.user.Controller.helpers.*;
import app.user.Entity.Store;
import app.user.Entity.Wine;
import app.user.Model.WineModel;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class WineController {
    private WineModel wineModel;

    public WineController(WineModel wineModel)
    {
        this.wineModel = wineModel;
    }

    /*
    REQUESTS JSON LIKE
   {
   "wineName": name of wine
   "country":
   "year":
   "color"
   "type"

   }

   RETURNS JSON LIKE
   {
   success: true/false
   message: some information
   }

     */
    @PostMapping("/user/addwine")
    public WineReturn add(@RequestBody String wineJSON)
    {
        WineReturn ret = new WineReturn();
        try
        {
            JSONObject jsonObject = JSONGetter.getParams(wineJSON);
            Wine wine = new Wine();
            wine.setWineName(jsonObject.getString("wineName"));
            wine.setCountry(jsonObject.getString("country"));
            wine.setYear(jsonObject.getLong("year"));
            wine.setColor(jsonObject.getString("color"));
            wine.setType(jsonObject.getString("type"));

            if(wineModel.findByName(wine.getWineName()).isPresent())
            {
                ret.success = false;
                ret.message = "Wine "+wine.getWineName()+" already is in database";
                return ret;
            }
            wineModel.save(wine);

            ret.success = true;
            ret.message = "Wine "+wine.getWineName()+" added to database";
            return ret;

        }
        catch (Exception e)
        {
            ret.success = false;
            ret.message = e.getMessage();
            return ret;
        }
    }

    /*
    REQUESTS NOTHING


    RETURNS LIST OF WINES

     */
    @PostMapping("/user/getAllWines")
    public MultipleWinesReturn getAllWines()
    {

        List<Wine> wines =  new ArrayList<>(wineModel.findAll());

        for(Wine w: wines)
        {
            w.setStore(null); //prevent infinite recursion
        }

        return new MultipleWinesReturn(wines);
    }

    /*
    REQUESTS JSON LIKE
    {
    "wineName": wineName
    }

    RETURNS JSON LIKE

   wines: [
        {
        "storeName": value
        "address": value,
        "city":  value,
        "country": value,
        "website": value,
        "success": true/false
        }
        ...

    ]
    status:

     */

    /**
     * Ta funkcja zwraca wszystkie sklepy, w których znajdziemy wino, którego nazwa jest przesłana w zapytaniu.
     * Jeśli wino o danej nazwie nie istnieje, zwracana jest pusta lista oraz flaga success = false
     * @param wineJSON
     * @return
     */
    @PostMapping("/user/storesofwine")
    public MultipleStoresReturn getStoresOfWine(@RequestBody String wineJSON)
    {
        MultipleStoresReturn ret = new MultipleStoresReturn();

        try
        {
            List<StoreReturn> tmp = new ArrayList<>();
            String wineName;
            Wine wine;
            JSONObject jsonObject = JSONGetter.getParams(wineJSON);
            if(jsonObject.has("wineName"))
            {
                wineName = jsonObject.getString("wineName");
            }
            else
            {
                throw new JSONException("JSON must contain field wineName");
            }
            if(wineModel.findByName(wineName).isPresent())
            {
                wine = wineModel.findByName(wineName).get();
            }
            else
            {
                throw new Exception("Wine "+ wineName +"not found in database");
            }

            List<Store> stores =  new ArrayList<>(wine.getStore());

            for(Store s: stores)
            {
                tmp.add(new StoreReturn(s));
            }
            ret.setStores(tmp);
            ret.setSuccess(true);

            return ret;

        }
        catch(Exception e)
        {
            ret.setSuccess(false);
            return ret;
        }
    }

    @PostMapping("user/searchwine")
    public MultipleWinesReturn searchWine(@RequestBody String jsonSearch)
    {
        MultipleWinesReturn ret = new MultipleWinesReturn();
        try
        {
            JSONObject json = JSONGetter.getParams(jsonSearch);
            if(json.has("wineName"))
            {
                String wineName = json.getString("wineName");
                Optional<Wine> wine = wineModel.findByName(wineName);
                if(wine.isPresent())
                {
                    ret.addWine(wine.get());
                    ret.success = true;
                    ret.message = "Found "+ret.wines.size()+" wine";
                }
                else
                {
                    ret.wines = null;
                    ret.success = false;
                    ret.message = "No wines found";
                }
            }
            else
            {
                if(json.has("color") && json.has("type"))
                {
                    String color = json.getString("color");
                    String type = json.getString("type");

                    ret.wines = wineModel.findByColorAndType(color,type);
                    ret.success = true;
                    ret.message = "Found "+ret.wines.size()+" wines";
                }
                else if(json.has("color") && !json.has("type"))
                {
                    String color = json.getString("color");

                    ret.wines = wineModel.findByColor(color);
                    ret.success = true;
                    ret.message = "Found "+ret.wines.size()+" wines";
                }
                else if(!json.has("color") && json.has("type"))
                {
                    String type = json.getString("type");

                    ret.wines = wineModel.findByType(type);
                    ret.success = true;
                    ret.message = "Found "+ret.wines.size()+" wines";
                }
                else
                {
                    ret.wines = null;
                    ret.success = false;
                    ret.message = "No parameters were given";
                }

            }
        }
        catch (Exception ex)
        {
            ret.wines = null;
            ret.success = false;
            return ret;
        }
        return ret;
    }

}
