package app.user.Model;

import app.user.Entity.Customer;
import app.user.Entity.User;

import java.util.List;

public interface FollowModel {
    List<User> getAllFollowers(User user);
    List<User> getAllFollowedBy(User user);

    <T extends User> List<T> getAllUsersFollowedBy(User user, Class<T> type);

    boolean follow(User follower, User following) throws Exception;
    boolean unfollow(User follower, User following) throws Exception;
}
