package app.user.Repo;

import app.user.Entity.User;
import app.user.Entity.Wine;
import app.user.Entity.WineGrade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WineGradesRepository extends JpaRepository<WineGrade,Long> {

    List<WineGrade> findByUser(User user);
    List<WineGrade> findByWine(Wine wine);

    Optional<WineGrade> findByUserAndWine(User user, Wine wine);


}
