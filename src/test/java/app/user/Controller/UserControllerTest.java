package app.user.Controller;

import app.user.Controller.helpers.UserReturn;
import app.user.Entity.Role;
import app.user.Entity.Store;
import app.user.Entity.Customer;
import app.user.Entity.User;
import app.user.Model.RoleModel;
import app.user.Model.SecurityModel;
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

    private UserController userController = new UserController(userModel, securityModel, userValidator, roleModel);


    // REGISTRATION
    @Test
    public void checkIfUserdataIsSentToRegister()
    {

        when(roleModel.getRoleByName("store")).thenReturn(new Role("store"));
        when(roleModel.getRoleByName("customer")).thenReturn(new Role("customer"));
        String registerJsonStore =
                "{\n" +
                "\t\"username\": \"user1234\",\n" +
                "\t\"password\": \"password\",\n" +
                "\t\"passwordConfirm\": \"password\",\n" +
                "\t\"role\": \"store\"\n" +
                "}";

        String registerJsonCustomer =
                "{\n" +
                        "\t\"username\": \"user123\",\n" +
                        "\t\"password\": \"password\",\n" +
                        "\t\"passwordConfirm\": \"password\",\n" +
                        "\t\"role\": \"customer\"\n" +
                        "}";
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
                "{\n" +
                        "\t\"username\": \"user1234\",\n" +
                        "\t\"password\": \"password\",\n" +
                        "\t\"passwordConfirm\": \"password\",\n" +
                        "\t\"role\": \"invalid\"\n" +
                        "}";
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
                "{\n" +
                        "\t\"username\": \"user1234\"\n" +
                        "}";

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
                "{\n" +
                        "\t\"username\": \"user1234\",\n" +
                        "\t\"password\": \"password\"\n" +
                        "}";
        UserReturn userReturn = userController.login(loginJson);

        assertEquals(userReturn.message,"Logged successfully");
        assertEquals(userReturn.success,true);
        assertEquals(userReturn.role,"test");
    }

    @Test
    public void checkIfDataIsntSentOnException()
    {
        String loginJson =
                "{\n" +
                        "\t\"username\": \"user1234\",\n" +
                        "\t\"passwordd\": \"password\"\n" +
                        "}";

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


    //WELCOME

}
