package app.user.Controller;

import app.user.Entity.User;
import app.user.Model.RoleModel;
import app.user.Model.SecurityModel;
import app.user.Model.UserModel;
import app.user.validator.UserValidator;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController
{
    public UserController(UserModel userModel, SecurityModel securityModel,
                          UserValidator userValidator, RoleModel roleModel) {
        this.userModel = userModel;
        this.securityModel = securityModel;
        this.userValidator = userValidator;
        this.roleModel = roleModel;
    }

    private UserModel userModel;
    private SecurityModel securityModel;
    private UserValidator userValidator;
    private RoleModel roleModel;

    class Status
    {
        String user;

        String role;
        String message;
        Boolean success;

        public String getUser() {
            return user;
        }
        public void setUser(String user) {
            this.user = user;
        }

        public String getRole() {
            return role;
        }
        public void setRole(String role) {
            this.role = role;
        }

        public String getMessage() {
            return message;
        }
        public void setMessage(String message) {
            this.message = message;
        }

        public Boolean getSuccess() {
            return success;
        }
        public void setSuccess(Boolean success) {
            this.success = success;
        }
    }

    /*
    EXPECTS JSON LIKE:
    {
        "username" : "username",
        "password" : "PASSWORD",
        "passwordConfirm" : "PASSWORD"
        "role": user/shop
    }

    RETURNS JSON LIKE:
    {
    "user": user on whom action was performed
    "role": user/shop
    "message": message
    "success": true/false
    }

     */
    @PostMapping("/users")
    public String forbidden()
    {
        return "Access is forbidden";
    }

    @PostMapping("/register")
    public Status registration(@RequestBody String userJson) {

        User userForm = new User();
        Status ret = new Status();
        try {
            JSONObject jsonObject = new JSONObject(userJson);
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

            userModel.save(userForm);

            securityModel.Login(userForm.getUsername(), userForm.getPasswordConfirm());
            ret.user = userForm.getUsername();
            ret.role = userForm.getRole().getName();
            ret.message = "Registration successful";
            ret.success = true;
            return ret;


        } catch (Exception e) {
            e.printStackTrace();
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
    "user": user on whom action was performed
    "role": regular/shop
    "message": message
    "success": true/false
    }
     */
    @PostMapping("/login")
    public Status login(@RequestBody String userJson) {
        User userForm = new User();
        Status ret = new Status();

        try {
            JSONObject jsonObject = new JSONObject(userJson);
            String username = jsonObject.get("username").toString();
            String password = jsonObject.get("password").toString();

            userForm.setPassword(password);
            userForm.setUsername(username);

            boolean logged = securityModel.Login(userForm.getUsername(), userForm.getPassword());

            ret.success = logged;
            ret.message = "Logged successfully";
            ret.user = userForm.getUsername();
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
        "user": user on whom action was performed
        "message": message
        "success": true/false
    }
     */
    @PostMapping("/logout")
    public Status logout()
    {
        Status ret = new Status();
        try
        {
            securityModel.Logout();

            ret.message ="Successfully logged out";
            ret.success = true;
            return ret;

        }
        catch (Exception e)
        {
            ret.message = e.getMessage();
            ret.success = false;
            return ret;
        }

    }

    /*
    RETURNS JSON LIKE:
    {
        "user": user on whom action was performed
        "message": message
        "success": true/false
    }
     */
    @GetMapping({"/welcome"})
    public Status welcome() {
        Status ret = new Status();
        String user = securityModel.findLoggedInUsername();
        if (user!=null)
        {
            ret.user = user;
            ret.success = true;
            ret.role = userModel.findByUsername(user).getRole().toString();
        }
        else
        {

            ret.success = false;
        }
        return ret;
    }
}
