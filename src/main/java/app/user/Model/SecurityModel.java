package app.user.Model;

public interface SecurityModel {
    String findLoggedInUsername();

    boolean Login(String username, String password) throws Exception;
    void Logout();

}
