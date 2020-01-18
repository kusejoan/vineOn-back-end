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

import app.user.Controller.helpers.MultipleStoresReturn;
import app.user.Controller.helpers.MultipleWinesReturn;
import app.user.Controller.helpers.StoreReturn;
import app.user.Controller.helpers.WineReturn;
import app.user.Entity.Store;
import app.user.Entity.Wine;
import app.user.Model.SecurityModel;
import app.user.Model.User.StoreModel;
import app.user.Model.WineModel;
import org.junit.Test;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class StoreControllerTest {
    private StoreModel storeModel = mock(StoreModel.class);
    private SecurityModel securityModel = mock(SecurityModel.class);;
    private WineModel wineModel = mock(WineModel.class);;

    private StoreController storeController = new StoreController(storeModel,securityModel,wineModel);

    @Test
    public void AddWineIfInDatabase()
    {
        String wineName = "testwine";
        String storeName = "teststore";
        Store test = new Store();
        Wine testWine = new Wine();
        test.setStoreName(storeName);
        testWine.setWineName(wineName);
        when(storeModel.findByUsername(anyString())).thenReturn(test);
        when(securityModel.findLoggedInUsername()).thenReturn("username");
        when(wineModel.findByName(anyString())).thenReturn(Optional.of(testWine));
        String wineJSON = "{ params: {\n" +
                "    \"wineName\": \"wino1\"\n" +
                "} }";

        WineReturn wineReturn = storeController.addWine(wineJSON);

        assertEquals(wineReturn.success,true);
        assertEquals(wineReturn.message,wineName+" added to "+storeName);
    }

    @Test
    public void DontAddWineIfNotInDatabase()
    {
        String wineName = "testwine";
        String storeName = "teststore";
        Store test = new Store();
        Wine testWine = new Wine();
        test.setStoreName(storeName);
        testWine.setWineName(wineName);
        when(storeModel.findByUsername(anyString())).thenReturn(test);
        when(securityModel.findLoggedInUsername()).thenReturn("username");
        when(wineModel.findByName(anyString())).thenReturn(Optional.empty());
        String wineJSON = "{ params: {\n" +
                "    \"wineName\": \"wino1\"\n" +
                "} } ";

        WineReturn wineReturn = storeController.addWine(wineJSON);

        assertEquals(wineReturn.success,false);
        assertEquals(wineReturn.message,"Wine not found in database");
    }

    @Test
    public void DontRemoveWineFromStoreIfItDidntExistBefore()
    {
        String wineName = "testwine";
        String storeName = "teststore";
        Store test = new Store();
        Wine testWine = new Wine();
        test.setStoreName(storeName);
        testWine.setWineName(wineName);
        when(storeModel.findByUsername(anyString())).thenReturn(test);
        when(securityModel.findLoggedInUsername()).thenReturn("username");
        when(wineModel.findByName(anyString())).thenReturn(Optional.of(testWine));
        String wineJSON = "{\n params:\n {\n" +
                "    \"wineName\": \"wino1\"\n" +
                "} }";
        WineReturn wineReturn = storeController.removeWine(wineJSON);

        assertEquals(wineReturn.success,false);
        assertEquals(wineReturn.message,"Couldn't remove "+wineName+" from "+storeName);
    }

    @Test
    public void RemoveWineFromStoreIfItExistedThereBefore()
    {
        String wineName = "testwine";
        String storeName = "teststore";
        Store test = new Store();
        Wine testWine = new Wine();
        test.setStoreName(storeName);
        testWine.setWineName(wineName);
        test.addWine(testWine);
        when(storeModel.findByUsername(anyString())).thenReturn(test);
        when(securityModel.findLoggedInUsername()).thenReturn("username");
        when(wineModel.findByName(anyString())).thenReturn(Optional.of(testWine));
        String wineJSON = "{ params: {\n" +
                "    \"wineName\": \"wino1\"\n" +
                "} }";
        WineReturn wineReturn = storeController.removeWine(wineJSON);

        assertEquals(wineReturn.success,true);
        assertEquals(wineReturn.message,wineName+" removed from "+storeName);
    }
    @Test
    public void updateProfileTest()
    {
        Store test = new Store();
        when(securityModel.findLoggedInUsername()).thenReturn("username");
        when(storeModel.findByUsername(anyString())).thenReturn(test);
        String address ="ul. Zielona 1";
        String city = "Krakow";
        String country = "Polska";
        String website = "www.sklep.pl";
        String profileJSON = "{ params: {\n" +
                "    \"storeName\": \"sklep\",\n" +
                "    \"address\": \""+address+"\",\n" +
                "    \"city\": \""+city+"\",\n" +
                "    \"country\": \""+country+"\",\n" +
                "    \"website\": \""+website+"\"\n" +
                "} }";
        StoreReturn storeReturn = storeController.UpdateProfile(profileJSON);

        assertEquals(storeReturn.address,address);
        assertEquals(storeReturn.city,city);
        assertEquals(storeReturn.country,country);
    }

    @Test
    public void testGetStoresOfCity() {
        // Setup
        Store s = new Store();
        s.setStoreName("name");
        s.setCity("city");
        Store s2 = new Store();
        s2.setStoreName("name2");
        s2.setCity("city");


        when(storeModel.findStoresOfCity("city")).thenReturn(Arrays.asList(s,s2));
        // Run the test
        String JSON = "{'params': {'city': \"city\"}}";
        final MultipleStoresReturn result = storeController.getStoresOfCity(JSON);

        assertTrue(result.success);
        assertEquals(result.stores.size(),2);

        // Verify the results
    }

    @Test
    public void testImportCSV() {
        // Setup

        // Run the test
        final MultipleWinesReturn result = storeController.importCSV("imported");

        // Verify the results
    }

    @Test
    public void testWinesOfStore() {
        // Setup
        Store s = new Store();
        Wine w1 = new Wine();
        Wine w2 = new Wine();
        s.setStoreName("store");
        s.addWine(w1);
        s.addWine(w2);
        when(storeModel.findByStorename("store")).thenReturn(Optional.of(s));


        String JSON = "{'params': {'storeName': \"store\"}}";
        // Run the test
        final MultipleWinesReturn result = storeController.winesOfStore(JSON);

        assertTrue(result.success);
        assertEquals(result.wines.size(),2);

        // Verify the results
    }
}
