/*
 * Copyright (c) 2020.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *  1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *  2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *  3. Neither the name of Vineon nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 *  POSSIBILITY OF SUCH DAMAGE.
 */

package app.user.Model;

import app.user.Entity.Customer;
import app.user.Entity.Follow;
import app.user.Entity.User;
import app.user.Repo.FollowRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class FollowModelImplTest {

    @Mock
    private FollowRepository mockFollowRepository;

    private FollowModelImpl followModelImplUnderTest;

    @Before
    public void setUp() {
        initMocks(this);
        followModelImplUnderTest = new FollowModelImpl(mockFollowRepository);
    }

    @Test
    public void testGetAllFollowers() {
        // Setup
        final User user = new User("username", "password", "role");
        final List<User> expectedResult = Arrays.asList(new User("username", "password", "role"));

        // Configure FollowRepository.findByFollowing(...).
        final List<Follow> follows = Arrays.asList(new Follow(new User("username", "password", "role"), new User("username", "password", "role")));
        when(mockFollowRepository.findByFollowing(new User("username", "password", "role"))).thenReturn(follows);

        // Run the test
        final List<User> result = followModelImplUnderTest.getAllFollowers(user);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetAllFollowedBy() {
        // Setup
        final User user = new User("username", "password", "role");
        final List<User> expectedResult = Arrays.asList(new User("username", "password", "role"));

        // Configure FollowRepository.findByFollower(...).
        final List<Follow> follows = Arrays.asList(new Follow(new User("username", "password", "role"), new User("username", "password", "role")));
        when(mockFollowRepository.findByFollower(new User("username", "password", "role"))).thenReturn(follows);

        // Run the test
        final List<User> result = followModelImplUnderTest.getAllFollowedBy(user);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetAllUsersFollowedBy() {
        // Setup
        final User user = new User("username", "password", "role");

        // Configure FollowRepository.findByFollower(...).
        final List<Follow> follows = Arrays.asList(new Follow(new User("username", "password", "role"), new User("username", "password", "role")));
        when(mockFollowRepository.findByFollower(new User("username", "password", "role"))).thenReturn(follows);

        // Run the test
        final List<Customer> result = followModelImplUnderTest.getAllUsersFollowedBy(user, Customer.class);

        // Verify the results
    }

    @Test
    public void testFollow() throws Exception {
        // Setup
        final User follower = new User("username1", "password1", "role1");
        final User following = new User("username", "password", "role");

        // Configure FollowRepository.findByFollowerAndFollowing(...).
        final Optional<Follow> follow = Optional.of(new Follow(new User("username", "password", "role"), new User("username", "password", "role")));
        when(mockFollowRepository.findByFollowerAndFollowing(new User("username", "password", "role"), new User("username", "password", "role"))).thenReturn(follow);

        // Configure FollowRepository.save(...).
        final Follow follow1 = new Follow(new User("username", "password", "role"), new User("username", "password", "role"));
        when(mockFollowRepository.save(new Follow(new User("username", "password", "role"), new User("username", "password", "role")))).thenReturn(follow1);

        // Run the test
        final boolean result = followModelImplUnderTest.follow(follower, following);

        // Verify the results
        assertTrue(result);
    }

    @Test(expected = Exception.class)
    public void testFollow_ThrowsException() throws Exception {
        // Setup
        final User follower = new User("username", "password", "role");
        final User following = new User("username", "password", "role");

        // Configure FollowRepository.findByFollowerAndFollowing(...).
        final Optional<Follow> follow = Optional.of(new Follow(new User("username", "password", "role"), new User("username", "password", "role")));
        when(mockFollowRepository.findByFollowerAndFollowing(new User("username", "password", "role"), new User("username", "password", "role"))).thenReturn(follow);

        // Configure FollowRepository.save(...).
        final Follow follow1 = new Follow(new User("username", "password", "role"), new User("username", "password", "role"));
        when(mockFollowRepository.save(new Follow(new User("username", "password", "role"), new User("username", "password", "role")))).thenReturn(follow1);

        // Run the test
        followModelImplUnderTest.follow(follower, following);
    }

    @Test
    public void testUnfollow() throws Exception {
        // Setup
        final User follower = new User("username1", "password1", "role1");
        final User following = new User("username", "password", "role");

        // Configure FollowRepository.findByFollowerAndFollowing(...).
        final Optional<Follow> follow = Optional.of(new Follow(new User("username", "password", "role"), new User("username", "password", "role")));
        when(mockFollowRepository.findByFollowerAndFollowing(new User("username", "password", "role"), new User("username", "password", "role"))).thenReturn(follow);

        when(mockFollowRepository.deleteByFollowerAndFollowing(new User("username", "password", "role"), new User("username", "password", "role"))).thenReturn(0L);

        // Run the test
        final boolean result = followModelImplUnderTest.unfollow(follower, following);

        // Verify the results
        assertFalse(result);
    }

    @Test(expected = Exception.class)
    public void testUnfollow_ThrowsException() throws Exception {
        // Setup
        final User follower = new User("username", "password", "role");
        final User following = new User("username", "password", "role");

        // Configure FollowRepository.findByFollowerAndFollowing(...).
        final Optional<Follow> follow = Optional.of(new Follow(new User("username", "password", "role"), new User("username", "password", "role")));
        when(mockFollowRepository.findByFollowerAndFollowing(new User("username", "password", "role"), new User("username", "password", "role"))).thenReturn(follow);

        when(mockFollowRepository.deleteByFollowerAndFollowing(new User("username", "password", "role"), new User("username", "password", "role"))).thenReturn(0L);

        // Run the test
        followModelImplUnderTest.unfollow(follower, following);
    }
}
