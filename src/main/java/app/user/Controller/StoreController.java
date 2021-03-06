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

package app.user.Controller;

import app.user.Controller.helpers.*;
import app.user.Entity.Store;
import app.user.Entity.Wine;
import app.user.Model.SecurityModel;
import app.user.Model.User.StoreModel;
import app.user.Model.WineModel;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.jackson.JsonObjectDeserializer;
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

    /**
     * This method modifies shop's profile with information given in profileJSON. It returns current state of profile
     * if everything went OK or success = false if it did not
     * @param profileJSON
     *  {
     *     "address",
     *     "city",
     *     "country",
     *     "website",
     *     }
     * @return
     *  {
     *     "storeName"
     *     "address",
     *     "city",
     *     "country",
     *     "website",
     *     "success": true/false
     *     }
     */
    @PostMapping("/user/store/update")
    public StoreReturn UpdateProfile(@RequestBody String profileJSON)
    {
        String name = securityModel.findLoggedInUsername();
        Store profile = storeModel.findByUsername(name);
        try {

            JSONObject jsonObject = JSONGetter.getParams(profileJSON);
            if(jsonObject.has("storeName"))
            {
                profile.setStoreName(jsonObject.getString("storeName"));
            }
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
            return new StoreReturn(profile);
        }
        catch (Exception e)
        {
            return new StoreReturn();
        }
    }



    /**
     *  This method returns all shops of given city or empty list + success = false if something went wrong
     * @param cityJSON
     * {
     *    'city'
     * }
     * @return
     * stores: [
     *         {
     *         "storeName",
     *         "address",
     *         "city",
     *         "country",
     *         "website",
     *         "success": true/false
     *         },
     *         {
     *         ...
     *
     * ]
     * success: true/false
     */
    @PostMapping("/user/storesofcity")
    public MultipleStoresReturn getStoresOfCity(@RequestBody String cityJSON)
    {
        MultipleStoresReturn ret = new MultipleStoresReturn();
        List<StoreReturn> tmp = new ArrayList<>();
        String city;
        try
        {
            JSONObject jsonObject = JSONGetter.getParams(cityJSON);
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
         ret.setSuccess(false);
         return ret;
        }
        List<Store> stores = storeModel.findStoresOfCity(city);
        for(Store s: stores)
        {
            tmp.add(new StoreReturn(s));
        }
        ret.setStores(tmp);
        ret.setSuccess(true);
        return ret;
    }

    /**
     *  This method adds wine that already exists in database into a shop. If wine's not in database or any other
     *  problem occured it returns a message with description and success = false
     * @param wineJSON
     * {
     *     wineName: name of wine that already IS IN DATABASE
     * }
     * @return
     * {
     *     success: true/false,
     *     message:
     *     }
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
                ret.message = wine.getWineName()+" added to "+store.getStoreName();
            }
            else
            {
                ret.success = false;
                ret.message = "Couldn't add "+wine.getWineName()+" to "+store.getStoreName();
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

    /**
     * This method removes wine from a shop. If wine could not be removed (that can happen because of few reasons)
     * information about it is provided and success = false
     * @param wineJSON
     * {
     *    wineName: name of wine that already IS IN DATABASE
     * }
     * @return
     * {
     *    success: true/false
     *    message: some information
     * }
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
                ret.message = wine.getWineName()+" removed from "+store.getStoreName();
            }
            else
            {
                ret.success = false;
                ret.message = "Couldn't remove "+wine.getWineName()+" from "+store.getStoreName();
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

    RETURNS LIST OF WINES
    wines: [

    ]
    success:

     */

    /**
     * This method returns all wines that are present in given store. If store cannot be found or any other errors occur
     * empty list is returned and success = false
     * @param storeJSON
     * {
     *     "storeName"
     * }
     * @return
     * {
     *      wines: [
     *      ...
     *      ]
     *      success: true/false
     * }
     */
    @PostMapping("/user/winesofstore")
    public MultipleWinesReturn winesOfStore(@RequestBody String storeJSON)
    {
        MultipleWinesReturn ret = new MultipleWinesReturn();

        try
        {
            String storeName;
            Store store;
            JSONObject jsonObject = JSONGetter.getParams(storeJSON);
            if(jsonObject.has("storeName"))
            {
                storeName = jsonObject.getString("storeName");
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
            ret.setWines(wines);
            ret.setSuccess(true);

            return ret;

        }
        catch(Exception e)
        {
            ret.setSuccess(false);
            return ret;
        }

    }

    /**
     * Ta metoda służy do wgrywania seryjnie win przez sklep. Sprawdzane jest po kolei każde wino z wysłanej listy.
     * Jeżeli istnieje ono w bazie, jest tylko dodawanie do magazynu sklepu. Jeśli zaś nie istnieje, to najpierw musi
     * zostać dodane do bazy, a dopiero potem jest dodawane do sklepu.
     * @param imported
     * {
     *     wstępnie sparsowany plik .csv zawierający kolumny wineName, country, year, color oraz type
     * }
     * @return
     * {
     * wines:
     *    [
     *    {wineName, country, year, color, type},
     *     ...
     *    ]
     *    success: true/false
     *    message: how many wines were added
     * }
     */
    @PostMapping("/user/store/importcsv")
    public MultipleWinesReturn importCSV(@RequestBody String imported)
    {
        MultipleWinesReturn ret = new MultipleWinesReturn();
        String username = securityModel.findLoggedInUsername();
        Store store = storeModel.findByUsername(username);
        try {
            JSONObject json = JSONGetter.getParamsCSV(imported);
            JSONArray array = json.getJSONArray("wines");

            for(int i = 0; i<array.length(); i++)
            {
                JSONObject wine = array.getJSONObject(i);

                String wineName = wine.getString("wineName");
                if(wineModel.findByName(wineName).isPresent())
                {
                    Wine tmp = wineModel.findByName(wineName).get();
                    store.addWine(tmp);
                    storeModel.save(store);
                    tmp.setStore(null); //prevent infinite recursion
                    ret.addWine(wineModel.findByName(wineName).get());
                }
                else
                {
                    String country = wine.getString("country");
                    Long year = wine.getLong("year");
                    String color = wine.getString("color");
                    String type = wine.getString("type");
                    Wine newWine = new Wine(wineName,country,year,color,type);
                    wineModel.save(newWine);

                    store.addWine(newWine);
                    ret.addWine(newWine);
                }
            }
            ret.success = true;
            ret.message = "Successfully exported "+array.length()+" wines from CSV";

        } catch (Exception ex)
        {
            ret.message = ex.getMessage();
            ret.success = false;
        }

        return ret;
    }

    /**
     *
     * @param wineJSON
     * {
     *     "wineName"
     * }
     * @return Wine object got from database
     * @throws JSONException if no field "wineName" in JSON or Exception when we try to access wine that is not present
     * in the database
     */
    private Wine getWineFromJSON(String wineJSON) throws Exception
    {
        JSONObject jsonObject = JSONGetter.getParams(wineJSON);
        String wineName;
        Wine wine;

        if(jsonObject.has("wineName"))
        {
            wineName = jsonObject.getString("wineName");
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

