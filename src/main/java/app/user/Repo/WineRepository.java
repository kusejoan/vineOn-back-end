package app.user.Repo;

import app.user.Entity.Wine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WineRepository extends JpaRepository<Wine,Long> {
    Optional<Wine> findByName(String name);
}
