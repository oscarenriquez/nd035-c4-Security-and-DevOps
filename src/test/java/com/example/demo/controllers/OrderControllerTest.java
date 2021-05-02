package com.example.demo.controllers;

import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import com.example.demo.TestHelpers;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;


public class OrderControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderController orderController;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void submit_returnsOk() {
        Optional<User> user = TestHelpers.generateUserWithCart(888L, 1L, 1);
        when(userRepository.findByUsername("username")).thenReturn(user.get());

        ResponseEntity<UserOrder> response = orderController.submit("username");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void submit_returnsNoContent() {
        when(userRepository.findByUsername("username")).thenReturn(null);

        ResponseEntity<UserOrder> response = orderController.submit("username");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void getOrdersForUser_returnsOk() {
        Optional<User> user = TestHelpers.generateUser(987L);
        Optional<List<UserOrder>> orderList = TestHelpers.generateUserOrderList(2, user.get());

        when(userRepository.findByUsername("username")).thenReturn(user.get());
        when(orderRepository.findByUser(user.get())).thenReturn(orderList.get());

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("username");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void getOrdersForUser_returnsNoContent() {
        when(userRepository.findByUsername("username")).thenReturn(null);

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("username");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

    }
}
