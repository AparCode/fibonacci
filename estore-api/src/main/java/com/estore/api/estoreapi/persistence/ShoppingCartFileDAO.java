package com.estore.api.estoreapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.estore.api.estoreapi.model.Product;
import com.estore.api.estoreapi.model.ShoppingCart;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ShoppingCartFileDAO implements ShoppingCartDAO {

    // local shopping cart cache
    Map<String, ShoppingCart> shoppingCarts;

    // handles serialization
    private ObjectMapper objectMapper;

    // where to write shopping carts to
    private String filename;

    public ShoppingCartFileDAO(@Value("${shoppingCarts.file}") String filename, ObjectMapper objectMapper) throws IOException{
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();
    }

    private boolean load() throws IOException{
        shoppingCarts = new TreeMap<String, ShoppingCart>();

        ShoppingCart[] shoppingCartArray = objectMapper.readValue(new File(filename), ShoppingCart[].class);

        for (ShoppingCart shoppingCart : shoppingCartArray){
            shoppingCarts.put(shoppingCart.getUser(), shoppingCart);
        }
        return true;
    }

    private boolean save() throws IOException{
        ShoppingCart[] shoppingCartArray = getShoppingCartsArray();
        // Serializes the Java Objects to JSON objects into the file
        // writeValue will thrown an IOException if there is an issue
        // with the file or reading from the file
        objectMapper.writeValue(new File(filename), shoppingCartArray);

        return true;
    }

    private ShoppingCart[] getShoppingCartsArray() {
        synchronized(shoppingCarts) {
            ArrayList<ShoppingCart> shoppingCartArrayList = new ArrayList<>();

            for (ShoppingCart shoppingCart : shoppingCarts.values()){
                shoppingCartArrayList.add(shoppingCart);
            }
            
            ShoppingCart[] shoppingCartArray = new ShoppingCart[shoppingCartArrayList.size()];
            shoppingCartArrayList.toArray(shoppingCartArray);
            return shoppingCartArray;
        }
    }

    @Override
    public ShoppingCart getShoppingCart(String user) {
        synchronized(shoppingCarts) {
            return shoppingCarts.get(user);
        }
    }

    @Override
    public ShoppingCart addToCart(String user, Product product) throws IOException {
        synchronized(shoppingCarts) {
            ShoppingCart thisCart = getShoppingCart(user);

            if(thisCart == null) {
                thisCart = new ShoppingCart(user);
            }
            thisCart.addProduct(product);
            
            shoppingCarts.put(user, thisCart);
            save();
            return thisCart;
        }
    }

    @Override
    public ShoppingCart removeFromCart(String user, Product product) throws IOException{
        synchronized(shoppingCarts) {
            ShoppingCart thisCart = getShoppingCart(user);

            if(thisCart == null) {
                return null;
            }
            thisCart.removeProduct(product);

            shoppingCarts.put(user, thisCart);
            save();
            
            return thisCart;
        }
    }

    @Override
    public ShoppingCart reserve(String user, Product product) throws IOException {
        synchronized(shoppingCarts) {
            ShoppingCart thisCart = getShoppingCart(user);
            thisCart.reserveProduct(product);
            shoppingCarts.put(user, thisCart);
            save();
            return thisCart;
        }
    }

    @Override
    public ShoppingCart unreserve(String user) throws IOException {
        synchronized(shoppingCarts) {
            ShoppingCart thisCart = getShoppingCart(user);
            thisCart.clearReserved();
            shoppingCarts.put(user, thisCart);
            save();
            return thisCart;
        }
    }
    
}
