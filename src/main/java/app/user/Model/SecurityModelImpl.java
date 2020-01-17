package app.user.Model;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.http.HttpSession;

public class SecurityModelImpl implements SecurityModel{

    public SecurityModelImpl(AuthenticationManager authenticationManager, UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }

    private AuthenticationManager authenticationManager;
    private UserDetailsService userDetailsService;

    @Override
    public String findLoggedInUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @Override
    public boolean Login(String username, String password) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());

        authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        if (usernamePasswordAuthenticationToken.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            return true;
        }
        else
            return false;
    }

    @Override
    public void Logout()
    {
        SecurityContextHolder.clearContext();

    }
}
