package com.himanshu.stackoverflow.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JdbcUserDetailsManager userDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);
        manager.setUsersByUsernameQuery("SELECT email, password, 'true' FROM users WHERE email=?");
        manager.setAuthoritiesByUsernameQuery("SELECT email, 'ROLE_USER' FROM users WHERE email=?");
        return manager;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(configurer ->
                        configurer
                                .requestMatchers("/comments/**", "/answers/**", "/questions/create", "/questions/vote","/users/profile").authenticated()
                                .requestMatchers("/api/analyze-text").permitAll()
                                .anyRequest().permitAll()
                )
                .formLogin(form ->
                        form.loginPage("/users/login")
                                .loginProcessingUrl("/authenticateTheUser")
                                .successHandler(successHandler())
                                .permitAll()
                )
                .logout(LogoutConfigurer::permitAll);

        return http.build();
    }

    private AuthenticationSuccessHandler successHandler() {
        SimpleUrlAuthenticationSuccessHandler handler = new SimpleUrlAuthenticationSuccessHandler();
        handler.setDefaultTargetUrl("/questions/home");
        return handler;
    }
}
