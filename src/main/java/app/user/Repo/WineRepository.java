package app.user.Repo;

import app.user.Entity.Wine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WineRepository extends JpaRepository<Wine,Long> {
    Optional<Wine> findByWineName(String name);
    List<Wine> findByColor(String color);
    List<Wine> findByType(String type);
    List<Wine> findByColorAndType(String color, String type);
}
