package com.estore.api.estoreapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ShoppingCartTest {
    @Test
    public void testConstructor() {
        String name = "joe";
        ShoppingCart shoppingCart = new ShoppingCart(name);

        assertEquals(name, shoppingCart.getUser());
    }
    @Test
    public void testToString() {
        String expected = "{name=joe, shoppingCart=[Watch {id=13, name=A, brand=Gucci, caseMaterial=null, caseDiameter=45.000000, condition=null, cost=50.000000, attributes=null, inventoryAmt=1, image=null}, Watch {id=17, name=B, brand=Gucci, caseMaterial=null, caseDiameter=45.000000, condition=null, cost=50.000000, attributes=null, inventoryAmt=1, image=null}, Watch {id=101, name=Red Watch, brand=Watch watch, caseMaterial=null, caseDiameter=45.000000, condition=null, cost=50.000000, attributes=null, inventoryAmt=1, image=null}]}";
        
        ShoppingCart testCart = new ShoppingCart("joe");

        Product productA = new Product(13, "A", "Gucci", null, 45, null, 50, null, 1, null);
        Product productB = new Product(17, "B", "Gucci", null, 45, null, 50, null, 1, null);
        Product productC = new Product(101, "Red Watch", "Watch watch", null, 45, null, 50, null, 1, null);

        testCart.addProduct(productA);
        testCart.addProduct(productB);
        testCart.addProduct(productC);
        
        assertEquals(expected, testCart.toString());
    }
}
