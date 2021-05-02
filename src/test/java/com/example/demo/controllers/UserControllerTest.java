package com.example.demo.controllers;

import java.util.Optional;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.example.demo.TestHelpers;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;


public class UserControllerTest {

    private static final String ENCODED_PASSWORD = "encodedpassword";

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private UserController userController;

    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createUser_returnsOk() {
        CreateUserRequest userRequest = newRequest();

        when(bCryptPasswordEncoder.encode(userRequest.getPassword())).thenReturn(ENCODED_PASSWORD);

        final ResponseEntity<User> response = userController.createUser(userRequest);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User user = response.getBody();
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals(userRequest.getUsername(), user.getUsername());
        assertEquals(ENCODED_PASSWORD, user.getPassword());
    }

    @Test
    public void createUser_returnsInternalError_whenPasswordNotMeetsRequirements() {
        final ResponseEntity<User> response = userController.createUser(newRequestWithBadPassword("bad", "bad"));
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());


        final ResponseEntity<User> response2 = userController.createUser(newRequestWithBadPassword("bad",
                "passDoesNotMatch"));
        assertNotNull(response2);
        assertEquals(400, response2.getStatusCodeValue());
    }

    @Test
    public void findById_returnsOk() {
        Optional<User> user = TestHelpers.generateUser(1L);
        when(userRepository.findById(1L)).thenReturn(user);

        ResponseEntity<User> response = userController.findById(1L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User userReturned = response.getBody();
        assertNotNull(userReturned);
        assertEquals(1L, userReturned.getId());
        assertEquals(user.map(User::getUsername).orElse(null), userReturned.getUsername());
    }

    @Test
    public void findByUserName_returnsOk() {
        Optional<User> user = TestHelpers.generateUser(1L);
        user.ifPresent(value -> when(userRepository.findByUsername("username")).thenReturn(value));


        ResponseEntity<User> response = userController.findByUserName("username");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User userReturned = response.getBody();
        assertNotNull(userReturned);
        assertEquals(1L, userReturned.getId());
        assertEquals(user.map(User::getUsername).orElse(null), userReturned.getUsername());
    }

    @Test
    public void findByUserName_returnsNoContent() {
        when(userRepository.findByUsername("userName")).thenReturn(null);

        ResponseEntity<User> response = userController.findByUserName("userName");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    private CreateUserRequest newRequest() {
        return CreateUserRequest.builder().username("username").password("A#skj#988").confirmPassword("A#skj#988").build();
    }
    private CreateUserRequest newRequestWithBadPassword(String password, String confirmPassword) {
        return CreateUserRequest.builder().username("username").password(password).confirmPassword(confirmPassword).build();
    }
}
