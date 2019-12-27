package app.user.Controller;

import app.user.Controller.helpers.StoreReturn;
import app.user.Controller.helpers.WineReturn;
import app.user.Entity.Store;
import app.user.Entity.Wine;
import app.user.Model.SecurityModel;
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
public class StoreController  {
    private StoreModel storeModel;
    private SecurityModel securityModel;
    private WineModel wineModel;

    public StoreController(StoreModel storeModel, SecurityModel securityModel, WineModel wineModel) {
        this.storeModel = storeModel;
        this.securityModel = securityModel;
        this.wineModel = wineModel;
    }

    /*
    EXPECTS JSON LIKE (ONLY FIELDS THAT NEED TO BE MODIFIED)

    {
    "address": value,
    "city":  value,
    "country": value,
    "website": value,
    }

    RETURNS JSON LIKE
    {
    "storeName": value
    "address": value,
    "city":  value,
    "country": value,
    "website": value,
    "success": true/false
    }

     */

    @PostMapping("/user/store/update")
    public StoreReturn UpdateProfile(@RequestBody String profileJSON)
    {
        String name = securityModel.findLoggedInUsername();
        Store profile = storeModel.findByUsername(name);
        try {

            JSONObject jsonObject = new JSONObject(profileJSON);
            if(jsonObject.getJSONObject("params").has("storeName"))
            {
                profile.setStoreName(jsonObject.getJSONObject("params").getString("storeName"));
            }
            if(jsonObject.getJSONObject("params").has("address"))
            {
                profile.setAddress(jsonObject.getJSONObject("params").getString("address"));
            }
            if(jsonObject.getJSONObject("params").has("city"))
            {
                profile.setCity(jsonObject.getJSONObject("params").getString("city"));
            }
            if(jsonObject.getJSONObject("params").has("country"))
            {
                profile.setCountry(jsonObject.getJSONObject("params").getString("country"));
            }
            if(jsonObject.getJSONObject("params").has("website"))
            {
                profile.setWebsite(jsonObject.getJSONObject("params").getString("website"));
            }


            storeModel.save(profile);
            return new StoreReturn(profile);
        }
        catch (Exception e)
        {
            return new StoreReturn();
        }
    }


    /*
    EXPECTS JSON LIKE
    {
        'city' : city
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
    @PostMapping("/user/storesofcity")
    public List getStoresOfCity(@RequestBody String cityJSON)
    {
        List<StoreReturn> ret = new ArrayList<>();
        String city;
        try
        {
            JSONObject jsonObject = new JSONObject(cityJSON);
            if(jsonObject.getJSONObject("params").has("city"))
            {
                city = jsonObject.getJSONObject("params").getString("city");
            }
            else
            {
                throw new JSONException("Couldn't find field city");
            }
        }
        catch (Exception e)
        {
         StoreReturn fail = new StoreReturn();
         ret.add(fail);
         return ret;
        }
        List<Store> stores = storeModel.findStoresOfCity(city);
        for(Store s: stores)
        {
            ret.add(new StoreReturn(s));
        }
        return ret;
    }


    /*
    REQUESTS JSON LIKE
    {
    wineName: name of wine that already IS IN DATABASE
    }

    RETURNS JSON LIKE
    {
    success: true/false
    message: some information
    }

     */
    @PostMapping("/user/store/addwine")
    public WineReturn addWine(@RequestBody String wineJSON)
    {
        Wine wine;
        WineReturn ret = new WineReturn();
        try
        {
            wine = getWineFromJSON(wineJSON);
            String username = securityModel.findLoggedInUsername();
            Store store = storeModel.findByUsername(username);
            boolean result = store.addWine(wine);
            storeModel.save(store);
            if(result)
            {
                ret.success = true;
                ret.message = wine.getName()+" added to "+store.getStoreName();
            }
            else
            {
                ret.success = false;
                ret.message = "Couldn't add "+wine.getName()+" to "+store.getStoreName();
            }
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
   REQUESTS JSON LIKE
   {
   wineName: name of wine that already IS IN DATABASE
   }

   RETURNS JSON LIKE
   {
   success: true/false
   message: some information
   }

    */
    @PostMapping("/user/store/removewine")
    public WineReturn removeWine(@RequestBody String wineJSON)
    {
        Wine wine;
        WineReturn ret = new WineReturn();
        try
        {
            wine = getWineFromJSON(wineJSON);
            String username = securityModel.findLoggedInUsername();
            Store store = storeModel.findByUsername(username);

            Boolean result = store.removeWine(wine);
            storeModel.save(store);
            if(result)
            {
                ret.success = true;
                ret.message = wine.getName()+" removed from "+store.getStoreName();
            }
            else
            {
                ret.success = false;
                ret.message = "Couldn't remove "+wine.getName()+" from "+store.getStoreName();
            }
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
    REQUESTS JSON LIKE
    {
    "storeName": storeName
    }

    RETURNS LIST OF WINES on success / null on error

     */
    @PostMapping("/user/winesofstore")
    public List<Wine> winesOfStore(@RequestBody String storeJSON)
    {

        try
        {
            String storeName;
            Store store;
            JSONObject jsonObject = new JSONObject(storeJSON);
            if(jsonObject.getJSONObject("params").has("storeName"))
            {
                storeName = jsonObject.getJSONObject("params").getString("storeName");
            }
            else
            {
                throw new JSONException("JSON must contain field storeName");
            }
            if(storeModel.findByStorename(storeName).isPresent())
            {
                store = storeModel.findByStorename(storeName).get();
            }
            else
            {
                throw new Exception("Store "+ storeName +"not found in database");
            }

            List<Wine> wines =  new ArrayList<>(store.getWines());

            for(Wine w: wines)
            {
                w.setStore(null); //prevent infinite recursion
            }

            return wines;

        }
        catch(Exception e)
        {
            return null;
        }

    }

    private Wine getWineFromJSON(String wineJSON) throws Exception
    {
        JSONObject jsonObject = new JSONObject(wineJSON);
        String wineName;
        Wine wine;

        if(jsonObject.getJSONObject("params").has("wineName"))
        {
            wineName = jsonObject.getJSONObject("params").getString("wineName");
        }
        else
        {
            throw new JSONException("Request must contain field wineName");
        }
        if(wineModel.findByName(wineName).isPresent())
        {
            wine = wineModel.findByName(wineName).get();
        }
        else
        {
            throw new Exception("Wine not found in database");
        }
        return wine;
    }

}

