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
            wine.setName(jsonObject.getString("wineName"));
            wine.setCountry(jsonObject.getString("country"));
            wine.setYear(jsonObject.getLong("year"));
            wine.setColor(jsonObject.getString("color"));
            wine.setType(jsonObject.getString("type"));

            if(wineModel.findByName(wine.getName()).isPresent())
            {
                ret.success = false;
                ret.message = "Wine "+wine.getName()+" already is in database";
                return ret;
            }
            wineModel.save(wine);

            ret.success = true;
            ret.message = "Wine "+wine.getName()+" added to database";
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

}
