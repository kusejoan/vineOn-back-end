package app.user.Controller;

import app.user.Controller.helpers.WineReturn;
import app.user.Controller.helpers.StoreReturn;
import app.user.Entity.Store;
import app.user.Entity.Wine;
import app.user.Model.SecurityModel;
import app.user.Model.User.StoreModel;
import app.user.Model.WineModel;
import org.junit.Test;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

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
        testWine.setName(wineName);
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
        testWine.setName(wineName);
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
        testWine.setName(wineName);
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
        testWine.setName(wineName);
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

}
