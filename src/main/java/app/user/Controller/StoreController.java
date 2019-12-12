package app.user.Controller;

import app.user.Entity.Store;
import app.user.Model.SecurityModel;
import app.user.Model.User.StoreModel;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.StoredProcedureQuery;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@RestController
public class StoreController  {
    private StoreModel storeModel;
    private SecurityModel securityModel;

    public StoreController(StoreModel storeModel, SecurityModel securityModel) {
        this.storeModel = storeModel;
        this.securityModel = securityModel;
    }

    class StoreInfo
    {
        public StoreInfo()
        {
            success = false;
        }

        public StoreInfo(Store store)
        {
            storeName = store.getStore_name();
            address = store.getAddress();
            city = store.getCity();
            country = store.getCountry();
            website = store.getWebsite();
            success = true;
        }
        public String storeName;
        public String address;
        public String city;
        public String country;
        public String website;
        public boolean success;
    }
    /*
    EXPECTS JSON LIKE (ONLY FIELDS THAT NEED TO BE MODIFIED)

    {
    "address": address,
    "city":  city,
    "country": country,
    "website": website
    }

     */

    @PostMapping("/user/store/update")
    public StoreInfo UpdateProfile(@RequestBody String profileJSON)
    {
        String name = securityModel.findLoggedInUsername();
        Store profile = storeModel.findByUsername(name);
        try {

            JSONObject jsonObject = new JSONObject(profileJSON);
            if(jsonObject.has("address"))
            {
                profile.setAddress(jsonObject.getString("address"));
            }
            if(jsonObject.has("city"))
            {
                profile.setCity(jsonObject.getString("city"));
            }
            if(jsonObject.has("country"))
            {
                profile.setCountry(jsonObject.getString("country"));
            }
            if(jsonObject.has("website"))
            {
                profile.setWebsite(jsonObject.getString("website"));
            }


            storeModel.save(profile);
            return new StoreInfo(profile);
        }
        catch (Exception e)
        {
            return new StoreInfo();
        }
    }
    /*
    EXPECTS JSON LIKE
    {
        'city' : city
    }

    RETURNS


     */

    @PostMapping("/user/storesofcity")
    public List getStoresOfCity(@RequestBody String cityJSON)
    {
        List<StoreInfo> ret = new ArrayList<>();
        String city;
        try
        {
            JSONObject jsonObject = new JSONObject(cityJSON);
            if(jsonObject.has("city"))
            {
                city = jsonObject.getString("city");
            }
            else
            {
                throw new JSONException("Couldn't find field city");
            }
        }
        catch (Exception e)
        {
         StoreInfo fail = new StoreInfo();
         ret.add(fail);
         return ret;
        }
        List<Store> stores = storeModel.findStoresOfCity(city);
        for(Store s: stores)
        {
            ret.add(new StoreInfo(s));
        }
        return ret;
    }



}

