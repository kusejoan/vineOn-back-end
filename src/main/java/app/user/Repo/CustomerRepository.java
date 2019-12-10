package app.user.Repo;

import app.user.Entity.Customer;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface CustomerRepository extends UserBaseRepository<Customer>  {}
