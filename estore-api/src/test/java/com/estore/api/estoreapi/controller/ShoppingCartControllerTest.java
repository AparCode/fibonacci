package com.estore.api.estoreapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.estore.api.estoreapi.model.Product;
import com.estore.api.estoreapi.model.ShoppingCart;
import com.estore.api.estoreapi.persistence.ProductDAO;
import com.estore.api.estoreapi.persistence.ShoppingCartDAO;

@Tag("Controller-tier")
public class ShoppingCartControllerTest {
    private ShoppingCartController shoppingCartController;
    private ShoppingCartDAO mockShoppingCartDAO;
    private ProductDAO mockProductDAO;

    @BeforeEach
    public void setupShoppingCartController() {
        mockShoppingCartDAO = mock(ShoppingCartDAO.class);
        mockProductDAO = mock(ProductDAO.class);
        shoppingCartController = new ShoppingCartController(mockShoppingCartDAO, mockProductDAO);
    }

    // getShoppingCart
    @Test
    public void testGetShoppingCart() {
        ShoppingCart shoppingCart = new ShoppingCart("userABC123");
        when(mockShoppingCartDAO.getShoppingCart(shoppingCart.getUser())).thenReturn(shoppingCart);

        ResponseEntity<ShoppingCart> response = shoppingCartController.getShoppingCart(shoppingCart.getUser());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(shoppingCart, response.getBody());
    }
    @Test
    public void testGetShoppingCartNotFound() {
        ShoppingCart shoppingCart = new ShoppingCart("not_a_user");
        when(mockShoppingCartDAO.getShoppingCart(shoppingCart.getUser())).thenReturn(null);

        ResponseEntity<ShoppingCart> response = shoppingCartController.getShoppingCart(shoppingCart.getUser());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    }

    // addItemToCart
    @Test
    public void testAddItemToCart() throws IOException {
        ShoppingCart shoppingCart = new ShoppingCart("joe");
        Product itemA = new Product(0, "Watch A", "Brand", "Mat", 12, "good", 1000, null, 5, null);

        ShoppingCart expectedCart = new ShoppingCart(shoppingCart.getUser());
        expectedCart.addProduct(itemA);

        when(mockShoppingCartDAO.addToCart(shoppingCart.getUser(), itemA)).thenReturn(expectedCart);
        ResponseEntity<ShoppingCart> response = shoppingCartController.addItemToCart(shoppingCart.getUser(), itemA);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedCart, response.getBody());
        
    }
    @Test
    public void testAddItemToCartIOException() throws IOException {
        ShoppingCart shoppingCart = new ShoppingCart("joe");
        Product itemA = new Product(0, "Watch A", "Brand", "Mat", 12, "good", 1000, null, 5, null);

        when(mockShoppingCartDAO.addToCart(shoppingCart.getUser(), itemA)).thenThrow(new IOException());

        ResponseEntity<ShoppingCart> response = shoppingCartController.addItemToCart(shoppingCart.getUser(), itemA);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    // removeItemFromCart
    @Test
    public void testRemoveItemFromCart() throws IOException {
        ShoppingCart shoppingCart = new ShoppingCart("joe");
        Product itemA = new Product(0, "Watch A", "Brand", "Mat", 12, "good", 1000, null, 5, null);
        shoppingCart.addProduct(itemA);

        ShoppingCart expectedCart = new ShoppingCart(shoppingCart.getUser());

        when(mockShoppingCartDAO.removeFromCart(shoppingCart.getUser(), itemA)).thenReturn(expectedCart);

        ResponseEntity<ShoppingCart> response = shoppingCartController.removeItemFromCart(shoppingCart.getUser(), itemA);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedCart, response.getBody());

    }
    @Test
    public void testRemoveItemFromCartNotFound() throws IOException {
        ShoppingCart shoppingCart = new ShoppingCart("not_a_user");
        Product itemA = new Product(0, "Watch A", "Brand", "Mat", 12, "good", 1000, null, 5, null);
       
        when(mockShoppingCartDAO.removeFromCart(shoppingCart.getUser(), itemA)).thenReturn(null);

        ResponseEntity<ShoppingCart> response = shoppingCartController.removeItemFromCart(shoppingCart.getUser(), itemA);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    }
    @Test
    public void testRemoveItemFromCartIOException() throws IOException {
        ShoppingCart shoppingCart = new ShoppingCart("joe");
        Product itemA = new Product(0, "Watch A", "Brand", "Mat", 12, "good", 1000, null, 5, null);
       
        when(mockShoppingCartDAO.removeFromCart(shoppingCart.getUser(), itemA)).thenThrow(new IOException());

        ResponseEntity<ShoppingCart> response = shoppingCartController.removeItemFromCart(shoppingCart.getUser(), itemA);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        
    }

    @Test
    public void testCheckout() throws IOException {
        ShoppingCart shoppingCart = new ShoppingCart("joe");
        Product itemA = new Product(0, "Watch A", "Brand", "Mat", 12, "good", 1000, null, 5, null);
        shoppingCart.addProduct(itemA);
        shoppingCart.addProduct(itemA);
        shoppingCart.addProduct(itemA);

        when(mockShoppingCartDAO.getShoppingCart(shoppingCart.getUser())).thenReturn(shoppingCart);
        when(mockProductDAO.reserveProduct(itemA.getId())).thenReturn(true);
        when(mockShoppingCartDAO.removeFromCart(shoppingCart.getUser(), itemA)).thenReturn(new ShoppingCart(shoppingCart.getUser()));

        ResponseEntity<ShoppingCart> response = shoppingCartController.checkoutCart(shoppingCart.getUser());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(new ShoppingCart(shoppingCart.getUser()), response.getBody());

    }
    @Test
    public void testCheckoutIOException() throws IOException {
        ShoppingCart shoppingCart = new ShoppingCart("joe");
        Product itemA = new Product(0, "Watch A", "Brand", "Mat", 12, "good", 1000, null, 5, null);
        shoppingCart.addProduct(itemA);
        shoppingCart.addProduct(itemA);
        shoppingCart.addProduct(itemA);

        when(mockShoppingCartDAO.getShoppingCart(shoppingCart.getUser())).thenReturn(shoppingCart);
        when(mockProductDAO.reserveProduct(itemA.getId())).thenThrow(new IOException());

        ResponseEntity<ShoppingCart> response = shoppingCartController.checkoutCart(shoppingCart.getUser());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

    }

    @Test
    public void testReserveItem() throws IOException {
        ShoppingCart shoppingCart = new ShoppingCart("joe");
        Product itemA = new Product(0, "Watch A", "Brand", "Mat", 12, "good", 1000, null, 5, null);
        shoppingCart.addProduct(itemA);

        ShoppingCart expectedCart = new ShoppingCart("joe");
        expectedCart.addProduct(itemA);
        expectedCart.reserveProduct(itemA);

        when(mockShoppingCartDAO.getShoppingCart(shoppingCart.getUser())).thenReturn(shoppingCart);
        when(mockShoppingCartDAO.reserve(shoppingCart.getUser(), itemA)).thenReturn(expectedCart);
        when(mockProductDAO.reserveProduct(itemA.getId())).thenReturn(true);

        ResponseEntity<ShoppingCart> response = shoppingCartController.reserveItem(shoppingCart.getUser(), itemA);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(itemA, response.getBody().getReservation());

    }
    @Test
    public void testReserveItemOutOfStock() throws IOException {
        ShoppingCart shoppingCart = new ShoppingCart("joe");
        Product itemA = new Product(0, "Watch A", "Brand", "Mat", 12, "good", 1000, null, 5, null);
        shoppingCart.addProduct(itemA);

        when(mockShoppingCartDAO.getShoppingCart(shoppingCart.getUser())).thenReturn(shoppingCart);
        when(mockProductDAO.reserveProduct(itemA.getId())).thenReturn(false);

        ResponseEntity<ShoppingCart> response = shoppingCartController.reserveItem(shoppingCart.getUser(), itemA);

        assertEquals(HttpStatus.OK, response.getStatusCode());

    }
    @Test
    public void testReserveItemCartNotFound() throws IOException {
        ShoppingCart shoppingCart = new ShoppingCart("joe");
        Product itemA = new Product(0, "Watch A", "Brand", "Mat", 12, "good", 1000, null, 5, null);
        shoppingCart.addProduct(itemA);

        when(mockShoppingCartDAO.getShoppingCart(shoppingCart.getUser())).thenReturn(null);

        ResponseEntity<ShoppingCart> response = shoppingCartController.reserveItem(shoppingCart.getUser(), itemA);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    }
    @Test
    public void testReserveItemIOException() throws IOException {
        ShoppingCart shoppingCart = new ShoppingCart("joe");
        Product itemA = new Product(0, "Watch A", "Brand", "Mat", 12, "good", 1000, null, 5, null);
        shoppingCart.addProduct(itemA);

        when(mockShoppingCartDAO.getShoppingCart(shoppingCart.getUser())).thenReturn(shoppingCart);
        when(mockProductDAO.reserveProduct(itemA.getId())).thenThrow(new IOException());

        ResponseEntity<ShoppingCart> response = shoppingCartController.reserveItem(shoppingCart.getUser(), itemA);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

    }

    @Test public void testClearReserve() throws IOException {
        ShoppingCart shoppingCart = new ShoppingCart("joe");
        Product itemA = new Product(0, "Watch A", "Brand", "Mat", 12, "good", 1000, null, 5, null);
        shoppingCart.addProduct(itemA);
        shoppingCart.reserveProduct(itemA);

        ShoppingCart expectedCart = new ShoppingCart(shoppingCart.getUser());
        shoppingCart.addProduct(itemA);

        when(mockShoppingCartDAO.getShoppingCart(shoppingCart.getUser())).thenReturn(shoppingCart);
        when(mockProductDAO.unreserveProduct(itemA.getId())).thenReturn(true);
        when(mockShoppingCartDAO.unreserve(shoppingCart.getUser())).thenReturn(expectedCart);

        ResponseEntity<ShoppingCart> response = shoppingCartController.clearReserve(shoppingCart.getUser());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedCart, response.getBody());

    }
    @Test public void testClearReserveNotFound() throws IOException {
        ShoppingCart shoppingCart = new ShoppingCart("joe");
        Product itemA = new Product(0, "Watch A", "Brand", "Mat", 12, "good", 1000, null, 5, null);
        shoppingCart.addProduct(itemA);
        shoppingCart.reserveProduct(itemA);


        when(mockShoppingCartDAO.getShoppingCart(shoppingCart.getUser())).thenReturn(null);

        ResponseEntity<ShoppingCart> response = shoppingCartController.clearReserve(shoppingCart.getUser());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    }
    @Test public void testClearReserveOutOfStock() throws IOException {
        ShoppingCart shoppingCart = new ShoppingCart("joe");
        Product itemA = new Product(0, "Watch A", "Brand", "Mat", 12, "good", 1000, null, 5, null);
        shoppingCart.addProduct(itemA);
        shoppingCart.reserveProduct(itemA);

        when(mockShoppingCartDAO.getShoppingCart(shoppingCart.getUser())).thenReturn(shoppingCart);
        when(mockProductDAO.unreserveProduct(itemA.getId())).thenReturn(false);

        ResponseEntity<ShoppingCart> response = shoppingCartController.clearReserve(shoppingCart.getUser());

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());

    }
    @Test public void testClearReserveIOException() throws IOException {
        ShoppingCart shoppingCart = new ShoppingCart("joe");
        Product itemA = new Product(0, "Watch A", "Brand", "Mat", 12, "good", 1000, null, 5, null);
        shoppingCart.addProduct(itemA);
        shoppingCart.reserveProduct(itemA);

        when(mockShoppingCartDAO.getShoppingCart(shoppingCart.getUser())).thenReturn(shoppingCart);
        when(mockProductDAO.unreserveProduct(itemA.getId())).thenThrow(new IOException());

        ResponseEntity<ShoppingCart> response = shoppingCartController.clearReserve(shoppingCart.getUser());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

    }

}
