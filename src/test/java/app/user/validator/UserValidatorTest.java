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

package app.user.validator;

import app.user.Entity.Role;
import app.user.Entity.User;
import app.user.Model.User.UserModel;
import org.junit.Test;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class UserValidatorTest {
    UserModel userModel = mock(UserModel.class);
    private UserValidator validator = new UserValidator(userModel);

    @Test
    public void noErrorsWithGoodData()
    {
        User u = new User();
        u.setUsername("ProperUsername");
        u.setPassword("ProperPassword");
        u.setPasswordConfirm("ProperPassword");
        Role r = new Role();
        r.setName("customer");
        u.setRole(r);
        Errors bind =  new BindException(u,"u");
        validator.validate(u,bind);
        assertEquals(0,bind.getErrorCount());
    }
    @Test
    public void tooShortUsernameRaisesError()
    {
        User u = new User();
        u.setUsername("short");
        u.setPassword("ProperPassword");
        u.setPasswordConfirm("ProperPassword");
        Role r = new Role();
        r.setName("customer");
        u.setRole(r);

        Errors bind =  new BindException(u,"u");
        validator.validate(u,bind);


        assertEquals(1,bind.getErrorCount());
        assertEquals(bind.getAllErrors().get(0).getDefaultMessage(),"Username must be between 6 and 32 characters");
    }

    @Test
    public void tooShortPasswordRaisesError()
    {
        User u = new User();
        u.setUsername("ProperUsername");
        u.setPassword("short");
        u.setPasswordConfirm("short");
        Role r = new Role();
        r.setName("customer");
        u.setRole(r);

        Errors bind =  new BindException(u,"u");
        validator.validate(u,bind);

        assertEquals(1,bind.getErrorCount());
        assertEquals(bind.getAllErrors().get(0).getDefaultMessage(),"Password must be between 8 and 32 characters");
    }
    @Test
    public void differentPasswordsRaiseError()
    {
        User u = new User();
        u.setUsername("ProperUsername");
        u.setPassword("ProperPassword");
        u.setPasswordConfirm("InProperPassword");
        Role r = new Role();
        r.setName("customer");
        u.setRole(r);

        Errors bind =  new BindException(u,"u");
        validator.validate(u,bind);

        assertEquals(1,bind.getErrorCount());
        assertEquals(bind.getAllErrors().get(0).getDefaultMessage(),"Passwords are different");
    }
    @Test
    public void multipleErrorsCanBeRaised()
    {
        User u = new User();
        u.setUsername("short");
        u.setPassword("short");
        u.setPasswordConfirm("short1");
        Role r = new Role();
        r.setName("customer");
        u.setRole(r);

        Errors bind =  new BindException(u,"u");
        validator.validate(u,bind);

        assertEquals(3,bind.getErrorCount());
    }

    @Test
    public void invalidRolenameRaisesError()
    {
        User u = new User();
        u.setUsername("ProperUsername");
        u.setPassword("ProperPassword");
        u.setPasswordConfirm("ProperPassword");
        Role r = new Role();
        r.setName("random");
        u.setRole(r);

        Errors bind = new BindException(u,"u");
        validator.validate(u,bind);
        assertEquals(1,bind.getErrorCount());
        assertEquals("Incorrect role name",bind.getAllErrors().get(0).getDefaultMessage());
    }

    @Test
    public void supportsUserClass()
    {
        assertEquals(true, validator.supports(User.class));
    }

}
