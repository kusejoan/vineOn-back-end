package app.user.Repo;

import app.user.Entity.Store;
import app.user.Entity.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
public interface StoreRepository extends UserBaseRepository<Store>  {
    List<Store> findByCity(String city);
    Optional<Store> findByStoreName(String storeName);

    Store findByAddressContaining(String address);
}
