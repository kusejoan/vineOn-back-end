package app.user.Model;

public interface SecurityModel {
    String findLoggedInUsername();

    boolean autoLogin(String username, String password);

}
