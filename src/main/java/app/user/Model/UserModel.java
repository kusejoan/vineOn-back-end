package app.user.Model;

import app.user.Entity.User;



public interface UserModel {

    void save(User user);

    User findByUsername(String username);

}
