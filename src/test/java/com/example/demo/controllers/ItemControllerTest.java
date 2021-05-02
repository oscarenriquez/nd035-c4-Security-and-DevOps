package com.example.demo.controllers;

import java.util.Collections;
import java.util.Optional;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import com.example.demo.TestHelpers;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;


public class ItemControllerTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemController itemController;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getItems_returnOk() {
        List<Item> items = TestHelpers.generateItems(10);
        when(itemRepository.findAll()).thenReturn(items);

        ResponseEntity<List<Item>> response = itemController.getItems();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<Item> itemsReturned = response.getBody();
        assertNotNull(itemsReturned);
        assertEquals(10, itemsReturned.size());
    }

    @Test
    public void getItemById_returnsOk() {
        Optional<Item> item = TestHelpers.generateItem(999L);
        when(itemRepository.findById(999L)).thenReturn(item);

        ResponseEntity<Item> response = itemController.getItemById(999L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Item itemReturned = response.getBody();
        assertNotNull(itemReturned);
    }

    @Test
    public void getItemsByName_returnsOk() {
        Optional<Item> item = TestHelpers.generateItem(999L);
        when(itemRepository.findByName("Item")).thenReturn(Collections.singletonList(item.get()));

        ResponseEntity<List<Item>> response = itemController.getItemsByName("Item");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void getItemsByName_returnsNoContent() {
        Optional<Item> item = TestHelpers.generateItem(999L);
        when(itemRepository.findByName("Item")).thenReturn(Collections.emptyList());

        ResponseEntity<List<Item>> response = itemController.getItemsByName("Item");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }
}
