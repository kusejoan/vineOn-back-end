package app.user.Entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "FOLLOWERS")
public class Follow {
    public Follow() { }

    public Follow(User follower, User following) {
        this.follower = follower;
        this.following = following;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User follower;

    @ManyToOne
    private User following;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getFollower() {
        return follower;
    }

    public void setFollower(User follower) {
        this.follower = follower;
    }

    public User getFollowing() {
        return following;
    }

    public void setFollowing(User following) {
        this.following = following;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Follow follow = (Follow) o;
        return Objects.equals(follower, follow.follower) &&
                Objects.equals(following, follow.following);
    }

    @Override
    public int hashCode() {
        return Objects.hash(follower, following);
    }
}