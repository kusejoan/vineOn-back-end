package app.user.Controller;
import app.user.Controller.helpers.WineReturn;
import app.user.Model.User.StoreModel;
import app.user.Model.WineModel;
import org.junit.Test;


import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class WineControllerTest {
    private WineModel wineModel = mock(WineModel.class);
    private StoreModel storeModel = mock(StoreModel.class);

    private WineController wineController = new WineController(wineModel);

    @Test
    public void wineDataIsSentFurther()
    {
        String wineJSON = "{ params: {\n" +
                "    \"wineName\": \"wino1\",\n" +
                "    \"country\": Polska,\n" +
                "    \"year\": 1998,\n" +
                "    \"color\": \"red\",\n" +
                "    \"type\": \"sweet\"\n" +
                "} }";
        WineReturn wineReturn = wineController.add(wineJSON);

        assertEquals(wineReturn.success, true);
        assertEquals(wineReturn.message,"Wine wino1 added to database");
    }
    @Test
    public void wineDataIsNotSentFurtherIfIncorrect()
    {
        String wineJSON = "{ params: {\n" +
                "    \"wineName\": \"wino1\",\n" +
                "    \"country\": Polska,\n" +
                "    \"color\": \"red\",\n" +
                "    \"type\": \"sweet\"\n" +
                "} }";

        WineReturn wineReturn = wineController.add(wineJSON);

        assertEquals(wineReturn.success,false);
        assertEquals(wineReturn.message,"No value for year");
    }
}
