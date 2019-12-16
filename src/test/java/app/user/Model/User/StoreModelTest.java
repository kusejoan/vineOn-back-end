package app.user.Model.User;

import app.user.Entity.Store;
import app.user.Entity.Wine;
import app.user.Model.User.StoreModel;
import app.user.Model.User.StoreModelImpl;
import app.user.Repo.StoreRepository;
import org.junit.Test;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class StoreModelTest {
    private StoreRepository storeRepository = mock(StoreRepository.class);
    private StoreModel storeModel = new StoreModelImpl(storeRepository);

    @Test
    public void checkIfModelCallsRightRepoMethods()
    {
        storeModel.findByStorename("ABC");
        verify(storeRepository,times(1)).findByStoreName("ABC");

        storeModel.findAll();
        verify(storeRepository,times(1)).findAll();

        storeModel.findStoresOfCity("CITY");
        verify(storeRepository,times(1)).findByCity("CITY");


        storeModel.findByUsername("USERNAME");
        verify(storeRepository,times(1)).findByUsername("USERNAME");

        storeModel.findById(1L);
        verify(storeRepository,times(1)).findById(1L);

        Store s = new Store();
        storeModel.save(s);
        verify(storeRepository,times(1)).save(s);

    }
    @Test
    public void checkIfWinesAreProperlyAddedToStore()
    {
        Store s = new Store();
        Wine w  = new Wine();
        w.setName("ABC");

        Wine w2 = new Wine();
        w.setName("CBA");

        storeModel.addWine(s,w);
        storeModel.addWine(s,w2);

        assertEquals(s.getWines().size(),2);
    }
}
