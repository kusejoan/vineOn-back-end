package app.user.Repo;

import app.user.Entity.Store;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface StoreRepository extends UserBaseRepository<Store>  {}
