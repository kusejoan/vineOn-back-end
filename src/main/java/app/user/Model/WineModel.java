package app.user.Model;

import app.user.Entity.Wine;

import java.util.List;
import java.util.Optional;

public interface WineModel {
    List<Wine> findAll();
    Optional<Wine> findByName(String name);

    void save(Wine wine);

}
