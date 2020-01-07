package app.user.Model;

import app.user.Entity.Follow;
import app.user.Entity.User;
import app.user.Repo.FollowRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
@Service
public class FollowModelImpl implements FollowModel {
    public FollowModelImpl(FollowRepository followRepository) {
        this.followRepository = followRepository;
    }

    private FollowRepository followRepository;
    @Override
    public List<User> getAllFollowers(User user) {
        List<Follow> l = followRepository.findByFollowing(user);
        List<User> ret = new LinkedList<>();

        for (Follow f: l)
        {
            ret.add(f.getFollower());
        }
        return ret;
    }

    @Override
    public List<User> getAllFollowedBy(User user) {
        List<Follow> l = followRepository.findByFollower(user);
        List<User> ret = new LinkedList<>();

        for (Follow f: l)
        {
            ret.add(f.getFollowing());
        }
        return ret;
    }

    @Override
    public boolean follow(User follower, User following) {
        if(follower.equals(following))
        {
            return false;
        }
        if(followRepository.findByFollowerAndFollowing(follower,following).isPresent())
        {
            return false;
        }
        else
        {
            Follow follow = new Follow(follower,following);
            followRepository.save(follow);
            return true;
        }
    }
    public boolean unfollow(User follower, User following)
    {
        if(follower.equals(following))
        {
            return false;
        }
        if(followRepository.findByFollowerAndFollowing(follower,following).isPresent())
        {
            return false;
        }
        else
        {
            Long count = followRepository.deleteByFollowerAndFollowing(follower,following);
            return count == 1L;
        }
    }
}
