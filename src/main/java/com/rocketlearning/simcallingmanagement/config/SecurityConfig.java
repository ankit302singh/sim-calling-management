package com.rocketlearning.simcallingmanagement.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.rocketlearning.simcallingmanagement.security.CustomLoginSuccessHandler;
import com.rocketlearning.simcallingmanagement.security.CustomUserDetailsService;

@Configuration
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    
    @Autowired
    private CustomLoginSuccessHandler successHandler;
    
   
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
        .csrf(csrf -> csrf.disable())

            .userDetailsService(customUserDetailsService)

            .authorizeHttpRequests(auth -> auth

            	    .requestMatchers(
            	            "/login",
            	            "/css/**",
            	            "/js/**",
            	            "/images/**"
            	    ).permitAll()

            	    // ADMIN URLs
            	    .requestMatchers(
            	            "/dashboard",
            	            "/sims/**",
            	            "/employees/**",
            	            "/assignment-history/**"
            	    ).hasRole("ADMIN")

            	    // EMPLOYEE URLs
            	    .requestMatchers(
            	            "/employee/**"
            	    ).hasRole("EMPLOYEE")

            	    .anyRequest().authenticated()

            	)

            .formLogin(form -> form

            	    .loginPage("/login")

            	    .loginProcessingUrl("/login")

            	    .usernameParameter("email")

            	    .passwordParameter("password")

            	    .successHandler(successHandler)

            	    .failureUrl("/login?error=true")

            	    .permitAll()
            	)
            
            .exceptionHandling(exception -> exception

                    .accessDeniedPage("/403")

            )

            .logout(logout -> logout

                .logoutUrl("/logout")

                .logoutSuccessUrl("/login?logout")

                .permitAll()

            );

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {

        return NoOpPasswordEncoder.getInstance();

    }

    @Bean
    AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration)
            throws Exception {

        return configuration.getAuthenticationManager();

    }

}