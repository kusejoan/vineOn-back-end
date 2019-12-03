package app.user;

import app.user.Entity.User;
import app.user.validator.UserValidator;
import org.junit.Test;
import org.springframework.validation.BindingResult;

public class UserValidatorTest {
    User u;
    UserValidator validator;

    @Test
    public void getHello() throws Exception {

        u.setUsername("short");
    }


}
