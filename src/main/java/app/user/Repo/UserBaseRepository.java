package app.user.Repo;
import app.user.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserBaseRepository<T extends User> extends JpaRepository<T, Long> {
    T findByUsername(String username);
}
