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

package app.user.Model.User;

import app.user.Entity.User;
import app.user.Model.User.UserModel;
import app.user.Model.User.UserModelImpl;
import app.user.Repo.UserRepository;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class UserModelTest {
    private UserRepository userRepository = mock(UserRepository.class);
    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    private UserModel userModel = new UserModelImpl(userRepository, bCryptPasswordEncoder);

    @Test
    public void checkIfPasswordGetsHashedAfterSavingUser()
    {
        User u = new User();
        String password = "RANDOMPASSWORD";
        u.setPassword(password);
        userModel.save(u);
        assertTrue(bCryptPasswordEncoder.matches(password,u.getPassword()));
        verify(userRepository,times(1)).save(u);
    }
    @Test
    public void checkThatRightMethodsFromRepositoryAreCalledOnFindAllCall()
    {
        userModel.findAll();
        verify(userRepository,times(1)).findAll();
    }
    @Test
    public void checkThatRightMethodsFromRepositoryAreCalledOnFindByUsernameCall()
    {
        userModel.findByUsername("username");
        userModel.findByUsername("othername");
        userModel.findByUsername("anothername");
        verify(userRepository,times(1)).findByUsername("username");
        verify(userRepository,times(1)).findByUsername("othername");
        verify(userRepository,times(3)).findByUsername(anyString());
    }

}
