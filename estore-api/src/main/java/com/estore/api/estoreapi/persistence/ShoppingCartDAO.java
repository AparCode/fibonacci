package com.estore.api.estoreapi.persistence;

import java.io.IOException;

import com.estore.api.estoreapi.model.Product;
import com.estore.api.estoreapi.model.ShoppingCart;

public interface ShoppingCartDAO {

    // get shopping cart by username
    ShoppingCart getShoppingCart(String user);

    // add to a user's shopping cart
    ShoppingCart addToCart(String user, Product product) throws IOException;

    // remove an item from the user's shopping cart
    ShoppingCart removeFromCart(String user, Product product) throws IOException;

    ShoppingCart reserve(String user, Product product) throws IOException;

    ShoppingCart unreserve(String user) throws IOException;

}
