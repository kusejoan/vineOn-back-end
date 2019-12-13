package app.user.Model;

import app.user.Entity.Wine;
import app.user.Repo.WineRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WineModelImpl implements WineModel {
    private WineRepository wineRepository;

    public WineModelImpl(WineRepository wineRepository)
    {
        this.wineRepository = wineRepository;
    }


    @Override
    public Optional<Wine> findByName(String name) {
        return wineRepository.findByName(name);
    }

    @Override
    public void save(Wine wine)
    {
        wineRepository.save(wine);
    }

    @Override
    public List<Wine> findAll()
    {
        return wineRepository.findAll();
    }
}
