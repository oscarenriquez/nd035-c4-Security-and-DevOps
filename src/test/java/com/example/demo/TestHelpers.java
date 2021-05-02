package com.example.demo;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.IntStream;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;


public class TestHelpers {

    public static Optional<User> generateUser(long id) {
        return Optional.of(User.builder().id(id).cart(Cart.builder().build()).username("username").password(
                "password").build());
    }

    public static Optional<User> generateUserWithCart(long id, long itemId, int quantity) {
        List<Item> itemList = new ArrayList<>();

        IntStream.range(0, quantity)
                .forEach(i -> itemList.add(generateItem(itemId).get()));

        Cart cart = Cart.builder().items(itemList).build();

        return Optional.of(User.builder().id(id).cart(cart).username("username").password(
                "password").build());
    }

    public static Optional<Item> generateItem(long id) {
        return Optional.of(Item.builder().id(id).description("Item Description").name("Item Name").price(new BigDecimal(
                "1.99")).build());
    }

    public static List<Item> generateItems(int quantity) {
        List<Item> items = new ArrayList<>();
        IntStream.range(0, quantity).forEach((i) -> items.add(generateItem(i).get()));
        return items;
    }

    public static Optional<List<UserOrder>> generateUserOrderList(int quantity, User user) {
        List<UserOrder> userOrderList = new ArrayList<>();
        IntStream.range(0, quantity).forEach((i) ->
                        userOrderList.add(UserOrder.builder().user(user).total(new BigDecimal("100")).items(generateItems(5)).id(Long.valueOf(i)).build())
                );
        return Optional.of(userOrderList);
    }
}
