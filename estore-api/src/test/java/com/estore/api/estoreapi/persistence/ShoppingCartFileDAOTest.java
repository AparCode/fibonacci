package com.estore.api.estoreapi.persistence;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.estore.api.estoreapi.model.Product;
import com.estore.api.estoreapi.model.ShoppingCart;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ShoppingCartFileDAOTest {
    ShoppingCartDAO shoppingCartDAO;
    ShoppingCart[] testCarts;
    Product[] testProducts;
    ObjectMapper mockObjectMapper;

    @BeforeEach
    @JsonProperty
    public void setupShoppingCartFileDAO() throws IOException{
        mockObjectMapper = mock(ObjectMapper.class);
        Product productA = new Product(13, "A", "Gucci", null, 45, null, 50, null, 1, null);
        Product productB = new Product(17, "B", "Gucci", null, 45, null, 50, null, 1, null);
        Product productC = new Product(101, "Red Watch", "Watch watch", null, 45, null, 50, null, 1, null);

        testProducts = new Product[] {productA, productB, productC};

        ShoppingCart cartA = new ShoppingCart("joe");
        cartA.addProduct(productA);
        cartA.addProduct(productB);

        ShoppingCart cartB = new ShoppingCart("bob");
        cartB.addProduct(productC);

        ShoppingCart cartC = new ShoppingCart("sally");
        cartC.addProduct(productB);
        cartC.addProduct(productC);

        testCarts = new ShoppingCart[] {cartA, cartB, cartC};
        
        when(mockObjectMapper
            .readValue(new File("fake_carts.txt"),ShoppingCart[].class))
                .thenReturn(testCarts);
        shoppingCartDAO = new ShoppingCartFileDAO("fake_carts.txt",mockObjectMapper);
    
    }

    // getShoppingCart
    @Test
    public void testGetShoppingCart() {
        for(ShoppingCart cart : testCarts) {
            assertEquals(cart, shoppingCartDAO.getShoppingCart(cart.getUser()));
        }
    }

    // addToCart
    @Test
    public void testAddItemToCart() {
        ShoppingCart result = assertDoesNotThrow(() -> shoppingCartDAO.addToCart(testCarts[0].getUser(), testProducts[2]));
        assertNotNull(result);

        ShoppingCart expected = new ShoppingCart(testCarts[0].getUser());
        for(Product testProd : testProducts) {
            expected.addProduct(testProd);
        }

        assertEquals(expected, result);
    }
    @Test
    public void testAddItemToEmptyCart() {
        ShoppingCart result = assertDoesNotThrow(() -> shoppingCartDAO.addToCart("new_user", testProducts[2]));
        assertNotNull(result);

        ShoppingCart expected = new ShoppingCart("new_user");
        expected.addProduct(testProducts[2]);
        
        assertEquals(expected, result);
    }

    // removeFromCart
    @Test
    public void testRemoveItemFromCart() {
        ShoppingCart result = assertDoesNotThrow(() -> shoppingCartDAO.removeFromCart(testCarts[1].getUser(), testProducts[1]));
        assertNotNull(result);

        ShoppingCart expected = testCarts[1];

        assertEquals(expected, result);
    }
    @Test
    public void testRemoveItemFromCartNoUser() {
        ShoppingCart result = assertDoesNotThrow(() -> shoppingCartDAO.removeFromCart("doesnt_exist", testProducts[1]));

        assertNull(result);
    }

    @Test
    public void testReserve() throws IOException {
        ShoppingCart test = testCarts[0];
        shoppingCartDAO.reserve(test.getUser(), testProducts[0]);

        ShoppingCart expected = new ShoppingCart(test.getUser());
        expected.addProduct(testProducts[0]);
        expected.addProduct(testProducts[1]);
        expected.reserveProduct(testProducts[0]);

        assertEquals(expected, test);
    }

    @Test
    public void testUnreserve() throws IOException {
        ShoppingCart expected = testCarts[0];
        expected.removeProduct(testProducts[0]);

        ShoppingCart test = new ShoppingCart(expected.getUser());
        test.addProduct(testProducts[0]);
        test.addProduct(testProducts[1]);
        test.reserveProduct(testProducts[0]);

        shoppingCartDAO.unreserve(test.getUser());

        assertEquals(expected, test);
    }
}
