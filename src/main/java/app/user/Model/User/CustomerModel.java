package app.user.Model.User;

import app.user.Entity.Customer;

public interface CustomerModel extends UserModel<Customer> {
    Customer findByUsername(String username);

}
