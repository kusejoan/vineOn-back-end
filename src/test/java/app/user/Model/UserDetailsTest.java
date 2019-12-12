package app.user.Model;

import app.user.Entity.User;
import app.user.Model.User.UserDetailsImpl;
import app.user.Repo.UserRepository;
import org.junit.Test;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class UserDetailsTest {
    UserRepository userRepository = mock(UserRepository.class);
    UserDetailsService userDetailsService = new UserDetailsImpl(userRepository);
    @Test
    public void CheckIfPermissionsAreGrantedProperlyForRightUser()
    {
        String role = "customer";
        String username = "ABC";
        String password = "PASS";
        User u = new User(username,password,role);

        when(userRepository.findByUsername(anyString())).thenReturn(u);
        org.springframework.security.core.userdetails.UserDetails user = userDetailsService.loadUserByUsername(username);

        assertEquals("["+role+"]",user.getAuthorities().toString());
        assertEquals(username,user.getUsername());
        assertEquals(password,user.getPassword());
        verify(userRepository,times(1)).findByUsername(username);
    }
    @Test(expected = UsernameNotFoundException.class)
    public void CheckIfUsernameNotFoundExceptionWillRaise()
    {
        when(userRepository.findByUsername(anyString())).thenReturn(null);
        userDetailsService.loadUserByUsername("username");

    }
}
