package app.user.Model.User;

import app.user.Entity.Store;
import app.user.Entity.Wine;

import java.util.List;
import java.util.Optional;

public interface StoreModel extends UserModel<Store> {
    List<Store> findAll();

    List<Store> findStoresOfCity(String city);

    Optional<Store> findById(Long id);
    Optional<Store> findByStorename(String storeName);

    Store findByUsername(String username);
    void addWine(Store store, Wine wine);
}
