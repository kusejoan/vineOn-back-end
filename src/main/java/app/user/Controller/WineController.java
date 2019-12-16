package app.user.Controller;

import app.user.Controller.helpers.StoreReturn;
import app.user.Controller.helpers.WineReturn;
import app.user.Entity.Store;
import app.user.Entity.Wine;
import app.user.Model.User.StoreModel;
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
    private StoreModel storeModel;

    public WineController(WineModel wineModel, StoreModel storeModel)
    {
        this.wineModel = wineModel;
        this.storeModel = storeModel;
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
            JSONObject jsonObject = new JSONObject(wineJSON);
            Wine wine = new Wine();
            wine.setName(jsonObject.getString("wineName"));
            wine.setCountry(jsonObject.getString("country"));
            wine.setYear(jsonObject.getLong("year"));
            wine.setColor(jsonObject.getString("color"));
            wine.setType(jsonObject.getString("type"));

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
    public List<Wine> getAllWines()
    {
        List<Wine> wines =  new ArrayList<>(wineModel.findAll());

        for(Wine w: wines)
        {
            w.setStore(null); //prevent infinite recursion
        }

        return wines;
    }

    /*
    REQUESTS JSON LIKE
    {
    "wineName": wineName
    }

    RETURNS JSON LIKE

    [
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

     */
    @PostMapping("/user/storesofwine")
    public List<StoreReturn> getStoresOfWine(@RequestBody String wineJSON)
    {
        List<StoreReturn> ret = new ArrayList<>();
        try
        {
            String wineName;
            Wine wine;
            JSONObject jsonObject = new JSONObject(wineJSON);
            if(jsonObject.has("storeName"))
            {
                wineName = jsonObject.getString("wineName");
            }
            else
            {
                throw new JSONException("JSON must contain field storeName");
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
                ret.add(new StoreReturn(s));
            }

            return ret;

        }
        catch(Exception e)
        {
            StoreReturn fail = new StoreReturn();
            ret.add(fail);
            return ret;
        }
    }

}
