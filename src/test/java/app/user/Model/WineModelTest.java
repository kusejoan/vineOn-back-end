package app.user.Model;


import app.user.Entity.Wine;
import app.user.Repo.WineRepository;
import org.junit.Test;

import static org.mockito.Mockito.*;


public class WineModelTest {
    private WineRepository wineRepository = mock(WineRepository.class);
    private WineModel wineModel = new WineModelImpl(wineRepository);

    @Test
    public void checkIfRightMethodsFromWineRepositoryAreCalled()
    {
        wineModel.findByName("NAME");
        verify(wineRepository,times(1)).findByName("NAME");

        Wine w = new Wine();
        wineModel.save(w);
        verify(wineRepository,times(1)).save(w);

        wineModel.findAll();
        verify(wineRepository,times(1)).findAll();
    }

}
