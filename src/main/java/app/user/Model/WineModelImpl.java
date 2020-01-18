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
        return wineRepository.findByWineName(name);
    }

    @Override
    public List<Wine> findByColor(String color) {
        return wineRepository.findByColor(color);
    }

    @Override
    public List<Wine> findByType(String type) {
        return wineRepository.findByType(type);
    }


    @Override
    public List<Wine> findByColorAndType(String color, String type) {
        return wineRepository.findByColorAndType(color,type);
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
