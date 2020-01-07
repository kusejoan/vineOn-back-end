package app.user.Model;

import app.user.Entity.User;

import java.util.List;

public interface FollowModel {
    List<User> getAllFollowers(User user);
    List<User> getAllFollowedBy(User user);

    boolean follow(User follower, User following);
    boolean unfollow(User follower, User following);
}
