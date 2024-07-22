package com.studentgradetacker.sgt.config;

import com.studentgradetacker.sgt.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http

                .csrf(csrf -> csrf.disable())
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
//                                .requestMatchers("/sgt/students/**").permitAll()
//                                .requestMatchers("/sgt/courses/**").permitAll()
//                                .requestMatchers("/sgt/courses/addCourse").hasRole("ADMIN")
//                                .requestMatchers("/sgt/user/addUser").permitAll()
//                                .anyRequest().authenticated()
                                .anyRequest().permitAll()
                )
                .httpBasic(Customizer.withDefaults()); // Enable Basic Authentication

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
//        UserDetails admin = User.withUsername("admin")
//                .password(passwordEncoder.encode("mikan"))
//                .roles("ADMIN")
//                .build();
//
//        UserDetails teacher = User.withUsername("teacher")
//                .password(passwordEncoder.encode("teacherpassword"))
//                .roles("TEACHER")
//                .build();
//
//        return new InMemoryUserDetailsManager(admin, teacher);
        return userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/ignore1", "/ignore2");
    }
}
