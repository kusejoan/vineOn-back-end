package app.user.validator;

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

        Errors bind =  new BindException(u,"u");
        validator.validate(u,bind);

        assertEquals(3,bind.getErrorCount());
    }

}
