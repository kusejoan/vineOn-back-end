package app.user.Model;

import app.user.Entity.Wine;

import java.util.List;
import java.util.Optional;

public interface WineModel {
    List<Wine> findAll();
    Optional<Wine> findByName(String name);
    List<Wine> findByColor(String color);
    List<Wine> findByType(String type);
    List<Wine> findByColorAndType(String color, String type);

    void save(Wine wine);

}
