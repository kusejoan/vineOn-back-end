package app.user.Controller.helpers;

import app.user.Entity.User;

public class UserReturn
{
    public UserReturn() {
    }
    public UserReturn(User u) {
        username = u.getUsername();
        role = u.getRole().getName();
        success = true;
    }

    public String username;

    public String role;
    public String message;
    public Boolean success;

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
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