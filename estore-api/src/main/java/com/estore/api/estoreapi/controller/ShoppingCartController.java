package com.estore.api.estoreapi.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.estore.api.estoreapi.model.Product;
import com.estore.api.estoreapi.model.ShoppingCart;
import com.estore.api.estoreapi.persistence.ProductDAO;
import com.estore.api.estoreapi.persistence.ShoppingCartDAO;

@RestController
@RequestMapping("shopping-carts")
public class ShoppingCartController {
    private static Logger LOG = Logger.getLogger(ShoppingCartController.class.getName());
    private ShoppingCartDAO shoppingCartDAO;
    private ProductDAO productDAO;

    public ShoppingCartController(ShoppingCartDAO shoppingCartDAO, ProductDAO productDAO) {
        this.shoppingCartDAO = shoppingCartDAO;
        this.productDAO = productDAO;
    }

    @GetMapping("/{user}/cart")
    public ResponseEntity<ShoppingCart> getShoppingCart(@PathVariable String user) {
        LOG.info("GET /shopping-carts/" + user + "/cart");

        ShoppingCart shoppingCart = shoppingCartDAO.getShoppingCart(user);

        if(shoppingCart != null) {
            return new ResponseEntity<ShoppingCart>(shoppingCart, HttpStatus.OK);
        }
        
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/{user}/cart")
    public ResponseEntity<ShoppingCart> addItemToCart(@PathVariable String user, @RequestBody Product product) {
        LOG.info("POST /shopping-carts/" + user + "/cart " + product);

        ShoppingCart shoppingCart;
        try {
            shoppingCart = shoppingCartDAO.addToCart(user, product);
            return new ResponseEntity<ShoppingCart>(shoppingCart, HttpStatus.OK);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{user}/cart/reserve")
    public ResponseEntity<ShoppingCart> reserveItem(@PathVariable String user, @RequestBody Product product) {
        LOG.info("POST /shopping-carts/" + user + "/cart/reserve" + product);

        ShoppingCart shoppingCart = shoppingCartDAO.getShoppingCart(user);

        if(shoppingCart == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        try {
            if(productDAO.reserveProduct(product.getId())) {
                shoppingCart = shoppingCartDAO.reserve(user, product);
                return new ResponseEntity<ShoppingCart>(shoppingCart, HttpStatus.OK);
            }
            LOG.info("cant reserve:" + shoppingCart);
            return new ResponseEntity<ShoppingCart>(shoppingCart, HttpStatus.OK);

        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping("{user}/cart/reserve")
    public ResponseEntity<ShoppingCart> clearReserve(@PathVariable String user) {
        LOG.info("DELETE /shopping-carts/" + user + "/cart/reserve");

        ShoppingCart shoppingCart = shoppingCartDAO.getShoppingCart(user);

        if(shoppingCart == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        try {
            if(productDAO.unreserveProduct(shoppingCart.getReservation().getId())) {
                shoppingCart = shoppingCartDAO.unreserve(user);
                return new ResponseEntity<ShoppingCart>(shoppingCart, HttpStatus.OK);
            }

            return new ResponseEntity<ShoppingCart>(shoppingCart, HttpStatus.CONFLICT);

        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{user}/cart")
    public ResponseEntity<ShoppingCart> removeItemFromCart(@PathVariable String user, @RequestBody Product product) {
        LOG.info("DELETE /shopping-carts/" + user + "/cart " + product);

        ShoppingCart shoppingCart;
        try {
            shoppingCart = shoppingCartDAO.removeFromCart(user, product);

            if(shoppingCart == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<ShoppingCart>(shoppingCart, HttpStatus.OK);
            
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{user}/cart/checkout")
    public ResponseEntity<ShoppingCart> checkoutCart(@PathVariable String user) {
        LOG.info("POST /shopping-carts/" + user + "cart/checkout");
        
        ShoppingCart shoppingCart = shoppingCartDAO.getShoppingCart(user);
        shoppingCart.clearReserved();

        for(int i = 0; i < shoppingCart.getItems().size(); i++) {
            try {
                Product item = shoppingCart.getItems().get(i);
                if(productDAO.reserveProduct(item.getId())) {
                    LOG.info("removing:" + item);
                    shoppingCart = shoppingCartDAO.removeFromCart(user, item);
                    i--;
                } else {
                    LOG.info("cant remove:" + item);
                }
            } catch (IOException e) {
                LOG.log(Level.SEVERE, e.getLocalizedMessage());
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        LOG.info(shoppingCart + "");
        return new ResponseEntity<ShoppingCart>(shoppingCart, HttpStatus.OK);
    }

}
