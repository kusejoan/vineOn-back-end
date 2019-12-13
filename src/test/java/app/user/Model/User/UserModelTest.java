package app.user.Model.User;

import app.user.Entity.User;
import app.user.Model.User.UserModel;
import app.user.Model.User.UserModelImpl;
import app.user.Repo.UserRepository;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class UserModelTest {
    private UserRepository userRepository = mock(UserRepository.class);
    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    private UserModel userModel = new UserModelImpl(userRepository, bCryptPasswordEncoder);

    @Test
    public void checkIfPasswordGetsHashedAfterSavingUser()
    {
        User u = new User();
        String password = "RANDOMPASSWORD";
        u.setPassword(password);
        userModel.save(u);
        assertTrue(bCryptPasswordEncoder.matches(password,u.getPassword()));
        verify(userRepository,times(1)).save(u);
    }
    @Test
    public void checkThatRightMethodsFromRepositoryAreCalledOnFindAllCall()
    {
        userModel.findAll();
        verify(userRepository,times(1)).findAll();
    }
    @Test
    public void checkThatRightMethodsFromRepositoryAreCalledOnFindByUsernameCall()
    {
        userModel.findByUsername("username");
        userModel.findByUsername("othername");
        userModel.findByUsername("anothername");
        verify(userRepository,times(1)).findByUsername("username");
        verify(userRepository,times(1)).findByUsername("othername");
        verify(userRepository,times(3)).findByUsername(anyString());
    }

}
