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

import app.user.Controller.helpers.MultipleUsersReturn;
import app.user.Controller.helpers.UserReturn;
import app.user.Entity.Customer;
import app.user.Entity.Role;
import app.user.Entity.Store;
import app.user.Entity.User;
import app.user.Model.RoleModel;
import app.user.Model.SecurityModel;
import app.user.Model.User.CustomerModel;
import app.user.Model.User.StoreModel;
import app.user.Model.User.UserModel;
import app.user.validator.UserValidator;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


public class UserControllerTest {
    private UserModel userModel = mock(UserModel.class);
    private SecurityModel securityModel = mock(SecurityModel.class);
    private UserValidator userValidator = new UserValidator(userModel);
    private RoleModel roleModel = mock(RoleModel.class);
    private CustomerModel customerModel = mock(CustomerModel.class);
    private StoreModel storeModel = mock(StoreModel.class);

    private UserController userController = new UserController(userModel, securityModel, userValidator, roleModel, customerModel, storeModel);


    // REGISTRATION
    @Test
    public void checkIfUserdataIsSentToRegister()
    {

        when(roleModel.getRoleByName("store")).thenReturn(new Role("store"));
        when(roleModel.getRoleByName("customer")).thenReturn(new Role("customer"));
        String registerJsonStore =
                "{ params: {\n" +
                "\t\"username\": \"user1234\",\n" +
                "\t\"password\": \"password\",\n" +
                "\t\"passwordConfirm\": \"password\",\n" +
                "\t\"role\": \"store\"\n" +
                "} } ";

        String registerJsonCustomer =
                "{ params: {\n" +
                        "\t\"username\": \"user123\",\n" +
                        "\t\"password\": \"password\",\n" +
                        "\t\"passwordConfirm\": \"password\",\n" +
                        "\t\"role\": \"customer\"\n" +
                        "} }";
        UserReturn retStore = userController.registration(registerJsonStore);

        assertEquals(retStore.username,"user1234");
        assertEquals(retStore.success,true);
        assertEquals(retStore.message,"Registration successful");
        assertEquals(retStore.role, "store");

        UserReturn retCustomer = userController.registration(registerJsonCustomer);
        assertEquals(retCustomer.username,"user123");
        assertEquals(retCustomer.success,true);
        assertEquals(retCustomer.message,"Registration successful");
        assertEquals(retCustomer.role, "customer");


        verify(userModel,times(1)).save(any(Store.class));
        verify(userModel,times(1)).save(any(Customer.class));
        verify(userModel,times(2)).save(any());
    }
    @Test
    public void checkIfUserdataIsNotSentToRegisterIfInvalid()
    {
        when(userModel.findByUsername(anyString())).thenReturn(null);
        when(roleModel.getRoleByName("invalid")).thenReturn(new Role("invalid"));

        String registerJsonStore =
                "{ params: {\n" +
                        "\t\"username\": \"user1234\",\n" +
                        "\t\"password\": \"password\",\n" +
                        "\t\"passwordConfirm\": \"password\",\n" +
                        "\t\"role\": \"invalid\"\n" +
                        "} } ";
        UserReturn userReturn = userController.registration(registerJsonStore);

        assertEquals(userReturn.success, false);
        assertNull(userReturn.username);
        assertNull(userReturn.role);
        assertEquals(userReturn.message,"Incorrect role name");
    }
    @Test
    public void CheckIfExceptionIsProperlyHeldWhenJSONIsIncorrect()
    {
        String registerJson =
                "{ params: {\n" +
                        "\t\"username\": \"user1234\"\n" +
                        "} }";

        UserReturn userReturn = userController.registration(registerJson);

        assertEquals(userReturn.success, false);
        assertNull(userReturn.username);
        assertNull(userReturn.role);
    }

    //LOGIN

    @Test
    public void checkIfDataIsSentFurtherOnProperLogin() throws Exception {

        when(roleModel.getRoleByName("store")).thenReturn(new Role("store"));
        when(securityModel.Login(anyString(),anyString())).thenReturn(true);
        when(userModel.findByUsername(anyString())).thenReturn(new User("test","test","test"));

        String loginJson =
                "{ params: {\n" +
                        "\t\"username\": \"user1234\",\n" +
                        "\t\"password\": \"password\"\n" +
                        "} } ";
        UserReturn userReturn = userController.login(loginJson);

        assertEquals(userReturn.message,"Logged successfully");
        assertEquals(userReturn.success,true);
        assertEquals(userReturn.role,"test");
    }

    @Test
    public void checkIfDataIsntSentOnException()
    {
        String loginJson =
                "{ params: {\n" +
                        "\t\"username\": \"user1234\",\n" +
                        "\t\"passwordd\": \"password\"\n" +
                        "} } ";

        UserReturn userReturn = userController.login(loginJson);

        assertEquals(userReturn.message,"No value for password");
        assertEquals(userReturn.success,false);
        assertNull(userReturn.role);
        assertNull(userReturn.username);
    }

    //WELCOME

    @Test
    public void welcomesUserIfUserIsLoggedIn()
    {
        when(securityModel.findLoggedInUsername()).thenReturn("test_user");
        when(userModel.findByUsername("test_user")).thenReturn(new User("test_user","pass","role"));
        UserReturn userReturn = userController.welcome();

        assertEquals(userReturn.username, "test_user");
        assertEquals(userReturn.success, true);
        assertEquals(userReturn.role,"role");
    }


    //LOGOUT
    @Test
    public void LogoutsProperly()
    {
        UserReturn userReturn = userController.logout();

        assertEquals(userReturn.success,true);
        assertEquals(userReturn.message, "Successfully logged out");
    }

    @Test
    public void testGetAllUsers() {
        // Setup

        // Run the test
        final MultipleUsersReturn result = userController.getAllUsers();
        verify(userModel,times(1)).findAll();
        assertTrue(result.success);
    }

    @Test
    public void testWelcome() {
        // Setup

        when(securityModel.findLoggedInUsername()).thenReturn("username");
        when(userModel.findByUsername("username")).thenReturn(new User("username","pass","role"));
        // Run the test
        final UserReturn result = userController.welcome();

        assertEquals(result.username,"username");
        assertEquals(result.role,"role");


        // Verify the results
    }
}
