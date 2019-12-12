package app.user.Model.User;

import app.user.Entity.User;

import java.util.List;

public interface UserModel<T extends User>{

    void save(T user);

    T findByUsername(String username);

    List findAll();

}
