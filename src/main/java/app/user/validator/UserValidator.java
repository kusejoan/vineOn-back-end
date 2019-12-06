package app.user.validator;

import app.user.Entity.User;
import app.user.Model.UserModel;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

    private final UserModel userModel;

    public UserValidator(UserModel userModel)
    {
        this.userModel = userModel;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        User user = (User) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "Username can't be empty");
        if (user.getUsername().length() < 6 || user.getUsername().length() > 32) {
            errors.reject("username", "Username must be between 6 and 32 characters");
        }
        if (userModel.findByUsername(user.getUsername()) != null) {
            errors.reject("username", "User already exists");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "Password can't be empty");
        if (user.getPassword().length() < 8 || user.getPassword().length() > 32) {
            errors.reject("password", "Password must be between 8 and 32 characters");
        }

        if (!user.getPasswordConfirm().equals(user.getPassword())) {
            errors.reject("passwordConfirm", "Passwords are different");
        }
    }
}
