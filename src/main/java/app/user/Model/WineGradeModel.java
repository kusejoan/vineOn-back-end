package app.user.Model;

import app.user.Entity.User;
import app.user.Entity.Wine;
import app.user.Entity.WineGrade;

import java.util.List;
import java.util.Optional;

public interface WineGradeModel {
    void save(WineGrade wineGrade);

    List<WineGrade> findByWine(Wine wine);

    List<WineGrade> findByUser(User user);

    Optional<WineGrade> findByUserAndWine(User user, Wine wine);
}
