package phool.rpg_session_notes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class WebSecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    private static final AntPathRequestMatcher[] WHITE_LIST_URLS = {
        new AntPathRequestMatcher("/h2-console/**"),
        new AntPathRequestMatcher("/css/**"),
        new AntPathRequestMatcher("/signup"),
        new AntPathRequestMatcher("/saveuser"),
        new AntPathRequestMatcher("/")
    };

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(
                authorize -> authorize
                        .requestMatchers(WHITE_LIST_URLS).permitAll()
                        .anyRequest().authenticated())
                .headers(
                        headers -> headers
                                .frameOptions(
                                        frameOptions -> frameOptions
                                                .disable())) //TODO: h2-console enabled for development
                .formLogin(
                        formlogin -> formlogin
                                .loginPage("/login")
                                .defaultSuccessUrl("/campaignlist", true)
                                .permitAll())
                .logout(logout -> logout.permitAll())
                .csrf(csrf -> csrf.disable()); // TODO: csrf disabled for development, enable for production

        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }
}
