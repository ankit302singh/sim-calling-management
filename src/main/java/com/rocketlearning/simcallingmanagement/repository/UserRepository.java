package com.rocketlearning.simcallingmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rocketlearning.simcallingmanagement.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
	User findByEmail(String email);
	  User findByEmailAndPassword(String email, String password);

}
