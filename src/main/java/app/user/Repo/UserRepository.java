package app.user.Repo;

import app.user.Entity.User;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface UserRepository extends UserBaseRepository<User>  {}
