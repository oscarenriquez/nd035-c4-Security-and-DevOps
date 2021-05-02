package com.example.demo.controllers;

import java.util.Optional;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import com.example.demo.TestHelpers;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;


public class CartControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private CartController cartController;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @After
    public void destroyMocks() {
        Mockito.reset(userRepository);
        Mockito.reset(cartRepository);
        Mockito.reset(itemRepository);
    }

    @Test
    public void addToCart_returnsOk() {
        ModifyCartRequest request = newRequest();
        Optional<User> user = TestHelpers.generateUser(1L);
        Optional<Item> item = TestHelpers.generateItem(1L);
        when(userRepository.findByUsername(request.getUsername())).thenReturn(user.get());
        when(itemRepository.findById(request.getItemId())).thenReturn(item);
        ResponseEntity<Cart> response = cartController.addToCart(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        Cart cart = response.getBody();
        assertNotNull(cart);
        assertEquals(request.getQuantity(), cart.getItems().size());
    }

    @Test
    public void addToCart_returnsNoContent_whenUserIsMissing() {
        ModifyCartRequest request = newRequest();
        when(userRepository.findByUsername(request.getUsername())).thenReturn(null);
        ResponseEntity<Cart> response = cartController.addToCart(request);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void addToCart_returnsNoContent_whenItemIsMissing() {
        ModifyCartRequest request = newRequest();
        Optional<User> user = TestHelpers.generateUser(1L);
        when(userRepository.findByUsername(request.getUsername())).thenReturn(user.get());
        doReturn(Optional.ofNullable(null)).when(itemRepository).findById(request.getItemId());
        ResponseEntity<Cart> response = cartController.addToCart(request);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void removeFromCart_returnsOk() {
        ModifyCartRequest request = newRequest();
        Optional<User> user = TestHelpers.generateUserWithCart(1L, 1L, 10);
        Optional<Item> item = TestHelpers.generateItem(1L);
        when(userRepository.findByUsername(request.getUsername())).thenReturn(user.get());
        when(itemRepository.findById(request.getItemId())).thenReturn(item);
        ResponseEntity<Cart> response = cartController.removeFromCart(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        Cart cart = response.getBody();
        assertNotNull(cart);
        assertEquals(9, cart.getItems().size());
    }

    @Test
    public void removeFromCart_returnsNoContent_whenUserIsMissing() {
        ModifyCartRequest request = newRequest();
        when(userRepository.findByUsername(request.getUsername())).thenReturn(null);
        ResponseEntity<Cart> response = cartController.removeFromCart(request);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void removeFromCart_returnsNoContent_whenItemIsMissing() {
        ModifyCartRequest request = newRequest();
        Optional<User> user = TestHelpers.generateUser(1L);
        when(userRepository.findByUsername(request.getUsername())).thenReturn(user.get());
        when(itemRepository.findById(request.getItemId())).thenReturn(Optional.ofNullable(null));
        ResponseEntity<Cart> response = cartController.removeFromCart(request);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    private ModifyCartRequest newRequest() {
        return ModifyCartRequest.builder().username("username").itemId(1L).quantity(1).build();
    }
}
