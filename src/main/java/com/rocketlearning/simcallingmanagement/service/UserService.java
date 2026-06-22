package com.rocketlearning.simcallingmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rocketlearning.simcallingmanagement.repository.UserRepository;
import com.rocketlearning.simcallingmanagement.entity.Role;
import com.rocketlearning.simcallingmanagement.entity.User;
import jakarta.annotation.PostConstruct;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @PostConstruct
    public void createDefaultAdmin() {
    	User existingAdmin = userRepository.findByEmail("admin@rocketlearning.org");
    	if (existingAdmin == null) {
    		User admin = new User();
    		
    		 admin.setName("Admin");
    		    admin.setEmail("admin@rocketlearning.org");
    		    admin.setPassword("admin123");
    		    admin.setRole(Role.ADMIN);
    		    
    		    userRepository.save(admin);
        }
    	
    }
    public User login(String email, String password) {

        return userRepository.findByEmailAndPassword(email, password);

    }

}