package app.user.Model.User;

import app.user.Entity.Store;

import java.util.List;
import java.util.Optional;

public interface StoreModel extends UserModel<Store> {
    List findAll();

    List<Store> findStoresOfCity(String city);

    Optional<Store> findById(Long id);

    Store findByUsername(String username);
}
