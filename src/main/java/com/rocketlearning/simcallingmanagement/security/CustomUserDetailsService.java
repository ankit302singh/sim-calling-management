package com.rocketlearning.simcallingmanagement.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.rocketlearning.simcallingmanagement.entity.User;
import com.rocketlearning.simcallingmanagement.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        System.out.println("=================================");
        System.out.println("LOGIN REQUEST");
        System.out.println("Email : " + username);

        User user = userRepository.findByEmail(username);

        if (user == null) {

            System.out.println("USER NOT FOUND");

            throw new UsernameNotFoundException(
                    "User not found : " + username);

        }

        System.out.println("USER FOUND");
        System.out.println("Name : " + user.getName());
        System.out.println("Email : " + user.getEmail());
        System.out.println("Password : " + user.getPassword());
        System.out.println("Role : " + user.getRole());

        return new CustomUserDetails(user);

    }
}