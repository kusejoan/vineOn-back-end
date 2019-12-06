package app.user.Controller;

import app.user.Entity.User;
import app.user.Model.SecurityModel;
import app.user.Model.UserModel;
import app.user.validator.UserValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController
{
    Logger logger = LoggerFactory.getLogger(UserController.class);
    public UserController(UserModel userModel, SecurityModel securityModel, UserValidator userValidator) {
        this.userModel = userModel;
        this.securityModel = securityModel;
        this.userValidator = userValidator;
    }

    private UserModel userModel;
    private SecurityModel securityModel;
    private UserValidator userValidator;

    @GetMapping("/registration")
    public User registration(Model model) {
        model.addAttribute("userForm", new User());

        return new User();
    }
    @GetMapping("/list")
    public List list() {

        return userModel.findAll();
    }

    class Status
    {
        User user;
        String output;
        Boolean success;

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public String getOutput() {
            return output;
        }

        public void setOutput(String output) {
            this.output = output;
        }
        public Boolean getSuccess() {
            return success;
        }

        public void setSuccess(Boolean success) {
            this.success = success;
        }
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
            userForm.setPassword(password);
            userForm.setUsername(username);
            userForm.setPasswordConfirm(passwordConfirm);

            Errors bind =  new BindException(userForm,"userForm");
            userValidator.validate(userForm,bind);

            if(bind.hasErrors())
            {
                ret.output = bind.getAllErrors().get(0).getDefaultMessage();
                ret.success = false;
                return ret;
            }

            userModel.save(userForm);

            securityModel.autoLogin(userForm.getUsername(), userForm.getPasswordConfirm());
            ret.user = userForm;
            ret.output = "Registration successful";
            ret.success = true;
            return ret;


        } catch (Exception e) {
            ret.output = e.toString();
            return ret;
        }
    }

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

            boolean logged = securityModel.autoLogin(userForm.getUsername(), userForm.getPassword());

            ret.success = logged;
            ret.output = "Logged succesfully";
            ret.user = userForm;
            return ret;
        } catch (Exception e) {
            ret.output = e.getMessage();
            ret.success = false;
            return ret;
        }

    }

    @GetMapping({"/welcome"})
    public Model welcome(Model model) {
        return model;
    }
}
