package app.user.Model;

public interface SecurityModel {
    String findLoggedInUsername();

    void autoLogin(String username, String password);
}
