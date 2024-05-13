package leontev.webtasktracker.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import leontev.webtasktracker.enums.Role;
import leontev.webtasktracker.services.Impl.MyUserDetailServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration  {

    private final MyUserDetailServiceImpl userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authenticationProvider(authProvider())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("users/registration").permitAll()
                        .requestMatchers(HttpMethod.POST,"/users").permitAll()
                        .requestMatchers(HttpMethod.GET,"/users").hasAuthority(Role.ADMIN.getAuthority())
                        .requestMatchers("users/{user_id:\\d+}/updateByAdmin").hasAuthority(Role.ADMIN.getAuthority())
                        .requestMatchers("users/{user_id:\\d+}/delete").hasAuthority(Role.ADMIN.getAuthority())
                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .loginPage("/login") //Post method Spring реализует за нас
                        .usernameParameter("email")    //ОБЯЗАТЕЛЬНО, ИНАЧЕ НЕ БУДЕТ ВИДЕТЬ ПОЛЕ email и в бд будет (where email="")
                        .successHandler(savedRequestAwareAuthenticationSuccessHandler())
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout") //Post method - реализовывать не нужно
                        .logoutSuccessUrl("/login")
                        .deleteCookies("JSESSIONID"));
    return http.build();
        }

        @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
        }

        @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(encoder());
        return authProvider;
        }

        @Bean
    public SavedRequestAwareAuthenticationSuccessHandler savedRequestAwareAuthenticationSuccessHandler() {
        var handler = new SavedRequestAwareAuthenticationSuccessHandler() {
            @Override
            protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response) {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (authentication != null && authentication.getPrincipal() instanceof  UserDetails) {
                    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                    Long id = userDetailsService.getIdByEmail(userDetails.getUsername());
                            return "/users/" + id;
                }
                return super.determineTargetUrl(request, response);
            }
        };
        handler.setDefaultTargetUrl("/login");
        return handler;
        }

}
