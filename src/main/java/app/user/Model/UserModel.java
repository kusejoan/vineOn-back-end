package app.user.Model;

import app.user.Entity.User;

import java.util.List;


public interface UserModel {

    void save(User user);

    User findByUsername(String username);

    List findAll();

}
