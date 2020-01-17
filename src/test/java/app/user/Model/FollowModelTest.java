package app.user.Model;

import app.user.Entity.Follow;
import app.user.Entity.User;
import app.user.Repo.FollowRepository;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FollowModelTest {
    private FollowRepository followRepository = mock(FollowRepository.class);
    private FollowModel followModel = new FollowModelImpl(followRepository);

    @Test
    public void getAllFollowersTest()
    {
        User dummy1 = new User();
        dummy1.setUsername("dummy1");

        User dummy2 = new User();
        dummy2.setUsername("dummy2");

        User dummy3 = new User();
        dummy3.setUsername("dummy3");

        List<Follow> follows = new ArrayList<>();
        follows.add(new Follow(dummy1, dummy2));
        follows.add(new Follow(dummy1, dummy3));
        follows.add(new Follow(dummy3, dummy2));
        follows.add(new Follow(dummy2, dummy1));

        when(followRepository.findByFollowing(dummy1)).
                thenReturn(follows.stream().filter(c -> c.getFollowing().equals(dummy1)).collect(Collectors.toList()));
        List<User> followersOf1 = followModel.getAllFollowers(dummy1);
        assertEquals(followersOf1.size(), follows.stream().filter(c -> c.getFollowing().equals(dummy1)).count());
    }
}
