package app.user.Model.User;

import app.user.Entity.Store;
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
    public List findAll() {
        return storeRepository.findAll();
    }

    @Override
    public List findStoresOfCity(String city) {
        return storeRepository.findByCity(city);
    }

    public Optional<Store> findById(Long id)
    {
        return storeRepository.findById(id);
    }

    @Override
    public void save(Store user) {
        storeRepository.save(user);
    }

    public Store findByUsername(String username)
    {
        return storeRepository.findByUsername(username);
    }
}
