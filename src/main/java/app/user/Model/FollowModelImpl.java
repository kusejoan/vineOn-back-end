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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    public <T extends User> List<T> getAllUsersFollowedBy(User user, Class<T> type) {
        List<User> all = getAllFollowedBy(user);
        List<T> ret = new ArrayList<>();

        for(User u: all)
        {
            if (type.isInstance(u))
            {
                ret.add((T) u);
            }
        }
        return ret;
    }

    @Override
    public boolean follow(User follower, User following) throws Exception {
        if(follower.equals(following))
        {
            throw new Exception("You can't follow yourself");
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
    public boolean unfollow(User follower, User following) throws Exception
    {
        if(follower.equals(following))
        {
            throw new Exception("You can't follow yourself");
        }
        if(followRepository.findByFollowerAndFollowing(follower,following).isPresent())
        {
            return false;
        }
        else
        {
            long count = followRepository.deleteByFollowerAndFollowing(follower,following);
            return count == 1L;
        }
    }
}
