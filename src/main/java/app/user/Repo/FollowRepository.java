package app.user.Repo;

import app.user.Entity.Follow;
import app.user.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@Transactional
public interface FollowRepository extends JpaRepository<Follow, Long> {
    List<Follow> findByFollower(User follower);
    List<Follow> findByFollowing(User following);

    Optional<Follow> findByFollowerAndFollowing(User follower, User following);

    long deleteByFollowerAndFollowing(User follower, User following);

}
