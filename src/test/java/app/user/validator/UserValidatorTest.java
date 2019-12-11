package app.user.validator;

import app.user.Entity.Role;
import app.user.Entity.User;
import app.user.Model.UserModel;
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
