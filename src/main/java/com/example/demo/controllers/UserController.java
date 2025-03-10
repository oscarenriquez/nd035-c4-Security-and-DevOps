package com.example.demo.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.exception.SecurityDomainException;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@GetMapping("/id/{id}")
	public ResponseEntity<User> findById(@PathVariable Long id) {
		return ResponseEntity.of(userRepository.findById(id));
	}
	
	@GetMapping("/{username}")
	public ResponseEntity<User> findByUserName(@PathVariable String username) {
		log.info("Getting user by name {}", username);
		User user = userRepository.findByUsername(username);
		return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
	}
	
	@PostMapping("/create")
	public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
		try {
			log.info("Create user by name {}", createUserRequest.getUsername());
			User user = User.builder().build();
			validateCreateUserRequest(createUserRequest, user);
			user.setUsername(createUserRequest.getUsername());
			Cart cart = Cart.builder().build();
			cartRepository.save(cart);
			user.setCart(cart);
			userRepository.save(user);
			return ResponseEntity.ok(user);
		} catch (Exception e) {
			log.error("Error by creating a new user", e);
			return ResponseEntity.badRequest().build();
		}
	}

	private void validateCreateUserRequest(final CreateUserRequest createUserRequest, User user)
			throws SecurityDomainException {

		if(createUserRequest.getPassword().length()<7 ||
				!createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())){
			throw new SecurityDomainException("Error - Either length is less than 7 or pass and conf pass do not "
					+ "match. Unable to create "+ createUserRequest.getUsername());
		}
		user.setPassword(bCryptPasswordEncoder.encode(createUserRequest.getPassword()));
	}

}
