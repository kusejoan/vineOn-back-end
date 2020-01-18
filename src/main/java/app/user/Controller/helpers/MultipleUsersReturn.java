package app.user.Controller.helpers;

import java.util.List;

public class MultipleUsersReturn {
    public List<UserReturn> users;
    public boolean success;

    public MultipleUsersReturn(List<UserReturn> users) {
        this.users = users;
        this.success = true;
    }

    public MultipleUsersReturn() {}

    public List<UserReturn> getUsers() {
        return users;
    }

    public void setUsers(List<UserReturn> users) {
        this.users = users;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
