package app.user.Repo;

import app.user.Entity.Store;
import app.user.Entity.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface StoreRepository extends UserBaseRepository<Store>  {
    List<Store> findByCity(String city);

    Store findByAddressContaining(String address);
}
