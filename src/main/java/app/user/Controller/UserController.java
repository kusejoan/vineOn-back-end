package app.user.Controller;

import app.user.Entity.User;
import app.user.Model.SecurityModel;
import app.user.Model.UserModel;
import app.user.validator.UserValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping("/register")
    public User registration(String userJson) {
        User userForm = new User();
        try {
            JSONObject jsonObject = new JSONObject(userJson);
            String username = jsonObject.get("username").toString();
            String password = jsonObject.get("password").toString();
            String passwordConfirm = jsonObject.get("passwordConfirm").toString();
            userForm.setPassword(password);
            userForm.setUsername(username);
            userForm.setPasswordConfirm(passwordConfirm);

            userModel.save(userForm);

            securityModel.autoLogin(userForm.getUsername(), userForm.getPasswordConfirm());
        } catch (Exception ignored) {}




        return userModel.findByUsername(userForm.getUsername());
    }

    @GetMapping("/login")
    public String login(Model model, String error, String logout) {
        if (error != null)
            model.addAttribute("error", "Your username and password is invalid.");

        if (logout != null)
            model.addAttribute("message", "You have been logged out successfully.");

        return "login";
    }

    @GetMapping({"/welcome"})
    public Model welcome(Model model) {
        return model;
    }
}
