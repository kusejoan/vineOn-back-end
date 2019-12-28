package app.user.Controller;

import app.user.Controller.helpers.JSONGetter;
import app.user.Controller.helpers.UserReturn;
import app.user.Entity.*;
import app.user.Model.RoleModel;
import app.user.Model.SecurityModel;
import app.user.Model.User.UserModel;
import app.user.validator.UserValidator;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;


@RestController
public class UserController
{
    public UserController(UserModel userModelImpl, SecurityModel securityModel,
                          UserValidator userValidator, RoleModel roleModel) {
        this.userModel = userModelImpl;
        this.securityModel = securityModel;
        this.userValidator = userValidator;
        this.roleModel = roleModel;
    }

    private UserModel userModel;
    private SecurityModel securityModel;
    private UserValidator userValidator;
    private RoleModel roleModel;


    /*
    EXPECTS JSON LIKE:
    {
        "username" : "username",
        "password" : "PASSWORD",
        "passwordConfirm" : "PASSWORD"
        "role": customer/store
    }

    RETURNS JSON LIKE:
    {
    "username": username on whom action was performed
    "role": customer/store
    "message": message
    "success": true/false
    }

     */

    @PostMapping("/register")
    public UserReturn registration(@RequestBody String userJson) {

        User userForm = new User();
        UserReturn ret = new UserReturn();
        try {
            JSONObject jsonObject = JSONGetter.getParams(userJson);
            String username = jsonObject.get("username").toString();
            String password = jsonObject.get("password").toString();
            String passwordConfirm = jsonObject.get("passwordConfirm").toString();
            String role = jsonObject.get("role").toString();
            userForm.setPassword(password);
            userForm.setUsername(username);
            userForm.setPasswordConfirm(passwordConfirm);
            userForm.setRole(roleModel.getRoleByName(role));

            Errors bind = new BindException(userForm,"userForm");
            userValidator.validate(userForm,bind);

            if(bind.hasErrors())
            {
                StringBuilder errorMessage = new StringBuilder();
                for (ObjectError errors : bind.getAllErrors()) {
                    errorMessage.append(errors.getDefaultMessage());
                }
                ret.message = errorMessage.toString();
                ret.success = false;
                return ret;
            }
            if(userForm.getRole().getName().equals("store"))
            {
                userForm = new Store(userForm);
            }
            else
            {
                userForm = new Customer(userForm);
            }
            userModel.save(userForm);

            securityModel.Login(userForm.getUsername(), userForm.getPasswordConfirm());
            ret.username = userForm.getUsername();
            ret.role = userForm.getRole().getName();
            ret.message = "Registration successful";
            ret.success = true;
            return ret;


        } catch (Exception e) {
            ret.success = false;
            ret.message = e.toString();
            return ret;
        }
    }


    /*
    EXPECTS JSON LIKE:
    {
        "username" : "username",
        "password" : "PASSWORD",
    }
    RETURNS JSON LIKE:
    {
    "username": username on whom action was performed
    "role": customer/store
    "message": message
    "success": true/false
    }
     */
    @PostMapping("/login")
    public UserReturn login(@RequestBody String userJson) {
        User userForm = new User();
        UserReturn ret = new UserReturn();

        try {
            JSONObject jsonObject = JSONGetter.getParams(userJson);
            String username = jsonObject.get("username").toString();
            String password = jsonObject.get("password").toString();

            userForm.setPassword(password);
            userForm.setUsername(username);

            boolean logged = securityModel.Login(userForm.getUsername(), userForm.getPassword());

            ret.success = logged;
            ret.message = "Logged successfully";
            ret.username = userForm.getUsername();
            ret.role = userModel.findByUsername(username).getRole().getName();
        } catch (Exception e) {
            ret.message = e.getMessage();
            ret.success = false;
        }

        return ret;
    }


    /*
    RETURNS JSON LIKE:
    {
        "username": username on whom action was performed
        "message": message
        "success": true/false
    }
     */
    @PostMapping("/user/logout")
    public UserReturn logout()
    {
        UserReturn ret = new UserReturn();
            securityModel.Logout();

            ret.message ="Successfully logged out";
            ret.success = true;
            return ret;
    }

    /*
    RETURNS JSON LIKE:
    {
        "username": username on whom action was performed
        "role": username's role
        "message": message
        "success": true/false
    }
     */
    @PostMapping({"/welcome","/"})
    public UserReturn welcome() {
        UserReturn ret = new UserReturn();
        String user = securityModel.findLoggedInUsername();
        if (!user.equals("anonymousUser"))
        {
            ret.username = user;
            ret.success = true;
            ret.role = userModel.findByUsername(user).getRole().getName();
        }
        else
        {
            ret.success = false;
        }
        return ret;
    }
}
