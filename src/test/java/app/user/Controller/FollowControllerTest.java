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

package app.user.Controller;

import app.user.Controller.helpers.FollowReturn;
import app.user.Controller.helpers.MultipleWinesReturn;
import app.user.Entity.Customer;
import app.user.Entity.User;
import app.user.Entity.Wine;
import app.user.Entity.WineGrade;
import app.user.Model.FollowModel;
import app.user.Model.SecurityModel;
import app.user.Model.User.UserModelImpl;
import app.user.Model.WineGradeModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class FollowControllerTest {

    @Mock
    private FollowModel mockFollowModel;
    @Mock
    private SecurityModel mockSecurityModel;
    @Mock
    private UserModelImpl mockUserModel;
    @Mock
    private WineGradeModel mockWineGradeModel;

    private FollowController followControllerUnderTest;

    @Before
    public void setUp() {
        initMocks(this);
        followControllerUnderTest = new FollowController(mockFollowModel, mockSecurityModel, mockUserModel, mockWineGradeModel);
    }

    @Test
    public void testFollow() throws Exception {
        // Setup
        when(mockUserModel.findByUsername("username")).thenReturn(new User("username", "password", "role"));
        when(mockSecurityModel.findLoggedInUsername()).thenReturn("username");
        when(mockFollowModel.follow(new User("username", "password", "role"), new User("username", "password", "role"))).thenReturn(true);

        String followJSON = "{'params': {'username': \"username\"}}";
        // Run the test
        final FollowReturn result = followControllerUnderTest.follow(followJSON);
        assertTrue(result.success);

        // Verify the results
    }

    @Test
    public void testFollow_FollowModelThrowsException() throws Exception {
        // Setup
        when(mockUserModel.findByUsername("username")).thenReturn(new User("username", "password", "role"));
        when(mockSecurityModel.findLoggedInUsername()).thenReturn("result");
        when(mockFollowModel.follow(new User("username", "password", "role"), new User("username", "password", "role"))).thenThrow(Exception.class);

        String followJSON = "{'params': {'username': \"username\"}}";
        // Run the test
        final FollowReturn result = followControllerUnderTest.follow(followJSON);

        // Verify the results
    }

    @Test
    public void testUnfollow() throws Exception {
        // Setup
        when(mockUserModel.findByUsername("username")).thenReturn(new User("username", "password", "role"));
        when(mockSecurityModel.findLoggedInUsername()).thenReturn("result");
        when(mockFollowModel.unfollow(new User("username", "password", "role"), new User("username", "password", "role"))).thenReturn(false);

        // Run the test
        final FollowReturn result = followControllerUnderTest.unfollow("unfollowJSON");

        // Verify the results
    }

    @Test
    public void testUnfollow_FollowModelThrowsException() throws Exception {
        // Setup
        when(mockUserModel.findByUsername("username")).thenReturn(new User("username", "password", "role"));
        when(mockSecurityModel.findLoggedInUsername()).thenReturn("result");
        when(mockFollowModel.unfollow(new User("username", "password", "role"), new User("username", "password", "role"))).thenThrow(Exception.class);

        // Run the test
        final FollowReturn result = followControllerUnderTest.unfollow("unfollowJSON");

        // Verify the results
    }

    @Test
    public void testRecommendations() {
        // Setup
        when(mockUserModel.findByUsername("username")).thenReturn(new User("username", "password", "role"));
        when(mockSecurityModel.findLoggedInUsername()).thenReturn("result");

        // Configure FollowModel.getAllUsersFollowedBy(...).
        final List<Customer> customers = Arrays.asList(new Customer(new User("username", "password", "role")));
        when(mockFollowModel.getAllUsersFollowedBy(new User("username", "password", "role"), Customer.class)).thenReturn(customers);

        // Configure WineGradeModel.findByUser(...).
        final List<WineGrade> grades = Arrays.asList(new WineGrade(new User("username", "password", "role"), new Wine("wineName", "country", 0L, "color", "type"), 0L, "description"));
        when(mockWineGradeModel.findByUser(new User("username", "password", "role"))).thenReturn(grades);

        // Configure WineGradeModel.findAll(...).
        final List<WineGrade> grades1 = Arrays.asList(new WineGrade(new User("username", "password", "role"), new Wine("wineName", "country", 0L, "color", "type"), 0L, "description"));
        when(mockWineGradeModel.findAll()).thenReturn(grades1);

        when(mockWineGradeModel.averageGrade(Arrays.asList(new WineGrade(new User("username", "password", "role"), new Wine("wineName", "country", 0L, "color", "type"), 0L, "description")))).thenReturn(0.0);

        // Run the test
        final MultipleWinesReturn result = followControllerUnderTest.Recommendations("wineJSON");

        // Verify the results
    }
}
