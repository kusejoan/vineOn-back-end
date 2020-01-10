package app.user.Controller.helpers;

import app.user.Entity.Customer;

public class CustomerReturn extends UserBaseReturn{
    public String username;
    public String firstName;
    public String surname;
    public String birthdate;

    public boolean success;

    public CustomerReturn(Customer c) {
        super(true);
        username = c.getUsername();
        firstName = c.getFirstName();
        surname = c.getSurname();
        birthdate = c.getBirthdate();
    }

    public CustomerReturn() {
        super(false);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
