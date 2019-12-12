package app.user;

import app.user.Model.SecurityModel;
import app.user.Model.SecurityModelImpl;
import app.user.Model.User.UserDetailsImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    public WebSecurityConfig(UserDetailsImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    private UserDetailsImpl userDetailsService;
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityModel securityModel() throws Exception
    {
        return new SecurityModelImpl(customAuthenticationManager(), userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/api/user/customer/**").hasAuthority("customer")
                .antMatchers("/api/user/store/**").hasAuthority("store")
                .antMatchers("/api/user/**").authenticated()
                .antMatchers("/api/resources/**", "/api/register", "/api/login", "/api/welcome","/api/").permitAll()
                .antMatchers("/api/**").denyAll()
                .anyRequest().permitAll()
                .and()
                .csrf().disable();
    }

    @Bean
    public AuthenticationManager customAuthenticationManager() throws Exception {
        return authenticationManager();
    }
}
