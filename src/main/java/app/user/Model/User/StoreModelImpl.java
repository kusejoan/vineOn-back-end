package app.user.Model.User;

import app.user.Entity.Store;
import app.user.Entity.Wine;
import app.user.Repo.StoreRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StoreModelImpl implements StoreModel {
    private StoreRepository storeRepository;

    public StoreModelImpl(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    @Override
    public List<Store> findAll() {
        return storeRepository.findAll();
    }

    @Override
    public List<Store> findStoresOfCity(String city) {
        return storeRepository.findByCity(city);
    }

    public Optional<Store> findById(Long id)
    {
        return storeRepository.findById(id);
    }
    public Optional<Store> findByStorename(String storeName)
    {
        return storeRepository.findByStoreName(storeName);
    }

    @Override
    public void save(Store user) {
        storeRepository.save(user);
    }

    public Store findByUsername(String username)
    {
        return storeRepository.findByUsername(username);
    }

    public void addWine(Store store, Wine wine)
    {
        store.addWine(wine);
    }
}
