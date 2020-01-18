package app.user.Controller;

import app.user.Controller.helpers.*;
import app.user.Entity.*;
import app.user.Model.RoleModel;
import app.user.Model.SecurityModel;
import app.user.Model.User.CustomerModel;
import app.user.Model.User.StoreModel;
import app.user.Model.User.UserModel;
import app.user.validator.UserValidator;
import org.hibernate.event.spi.SaveOrUpdateEvent;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;


@RestController
public class UserController
{
    public UserController(UserModel userModelImpl, SecurityModel securityModel,
                          UserValidator userValidator, RoleModel roleModel, CustomerModel customerModel, StoreModel storeModel) {
        this.userModel = userModelImpl;
        this.securityModel = securityModel;
        this.userValidator = userValidator;
        this.roleModel = roleModel;
        this.customerModel = customerModel;
        this.storeModel = storeModel;
    }

    private UserModel userModel;
    private SecurityModel securityModel;
    private UserValidator userValidator;
    private RoleModel roleModel;
    private CustomerModel customerModel;
    private StoreModel storeModel;


    /**
     * This method is responsible for user's registration. It checks if username and passwords
     * are of proper length, if password is properly confirmed and if this username is not taken.
     *
     * If all these conditions are met user is added to database and simultaneously logged in.
     *
     * Information whether registration was successful or not is returned
     * @param userJson
     * {
     *         "username",
     *         "password",
     *         "passwordConfirm"
     *         "role": customer/store
     * }
     * @return
     * {
     *     "username": username on whom action was performed
     *     "role": customer/store
     *     "message"
     *     "success": true/false
     *     }
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

    /**
     * This method is responsible for logging in user who was previously registered.
     * It checks if login and password are proper and if so logs user in.
     *
     * Information whether login was successful or not is returned
     * @param userJson
     * {
     *         "username",
     *         "password"
     * }
     * @return
     * {
     *     "username": username on whom action was performed,
     *     "role": customer/store,
     *     "message",
     *     "success": true/false
     * }
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

    /**
     * This method logs user out. It should never fail but for convenience it returns success flag and message with
     * information
     * @return
     * {
     *         "username": username on whom action was performed
     *         "message"
     *         "success": true/false
     * }
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

    /**
     *  This method returns information about currently logged user. If this user is anonymousUser (default user
     *  when no other user is logged) then flag is set to false
     * @return
     * {
     *         "username": username on whom action was performed
     *         "role": shop/customer/null
     *         "message"
     *         "success": true/false
     *}
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


    /**
     * This method modifies customer's profile with information given in profileJSON. It returns current state of profile
     * if everything went OK or success = false if it did not
     * @param profileJSON
     * {
     *     firstName,
     *     surname,
     *     birthdate,
     * }
     * @return
     * {
     *     username,
     *     firstName,
     *     surname,
     *     birthdate
     * }
     */

    @PostMapping("/user/customer/update")
    public CustomerReturn update(@RequestBody String profileJSON)
    {
        String name = securityModel.findLoggedInUsername();
        Customer profile = customerModel.findByUsername(name);
        try {

            JSONObject jsonObject = JSONGetter.getParams(profileJSON);
            if(jsonObject.has("firstName"))
            {
                profile.setFirstName(jsonObject.getString("firstName"));
            }
            if(jsonObject.has("surname"))
            {
                profile.setSurname(jsonObject.getString("surname"));
            }
            if(jsonObject.has("birthdate"))
            {
                profile.setBirthdate(jsonObject.getString("birthdate"));
            }
            customerModel.save(profile);
            return new CustomerReturn(profile);
        }
        catch (Exception e)
        {
            return new CustomerReturn();
        }
    }

    /**
     * Ta metoda pobiera z bazy danych wszystkich użytkowników, a następnie zwraca listę wypełnioną ich danymi
     * @return
     * {
     *     users:
     *     [
     *      {username, role}
     *      ...
     *     ]
     *     success
     * }
     */
    @PostMapping("/user/getallusers")
    public MultipleUsersReturn getAllUsers()
    {
        List<User> users = userModel.findAll();
        List<UserReturn> tmp = new ArrayList<>();

        for(User u: users)
        {
            tmp.add(new UserReturn(u));
        }
        return new MultipleUsersReturn(tmp);
    }

}
