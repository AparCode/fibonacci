package com.estore.api.estoreapi.model;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a user's shopping cart
 * @author Luke Wyland
 */
public class ShoppingCart {

    static final Logger LOG = Logger.getLogger(ShoppingCart.class.getName());

    static final String STRING_FORMAT = "{name=%s, shoppingCart=";

    @JsonProperty("user") private String user;
    @JsonProperty("items") private List<Product> items;
    @JsonProperty("reservation") private Product reserved;

    public ShoppingCart(@JsonProperty("user") String user) {
        this.user = user;
        this.items = new ArrayList<Product>();
    }

    public String getUser() {
        return user;
    }

    public List<Product> getItems() {
        return this.items;
    }
    
    public void addProduct(Product product) {
        items.add(product);
    }

    public boolean removeProduct(Product product) {
        return items.remove(product);
    }

    public Product getReservation() {
        return this.reserved;
    }

    public void reserveProduct(Product product) {
        items.remove(product);
        this.reserved = product;
    }

    public void clearReserved() {
        if(this.reserved == null) {
            return;
        }
        items.add(reserved);
        this.reserved = null;
    }

    @Override
    public String toString() {
        String s = String.format(STRING_FORMAT, user);
        s += items.toString() + "}";
        return s;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof ShoppingCart) {
            ShoppingCart other = (ShoppingCart) o;
            return this.items.equals(other.items) && this.getUser().equals(other.getUser());
        }
        return false;
    }
}
