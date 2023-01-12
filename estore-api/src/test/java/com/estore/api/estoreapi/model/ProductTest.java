package com.estore.api.estoreapi.model;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Model-tier")
public class ProductTest {
    @Test
    public void testCtor() {
        int expected_id = 0;
        String expected_name = "Captain";
        String expected_brand = "ZENITH";
        String expected_caseMaterial = "rose gold";
        double expected_caseDiameter = 45.45;
        String expected_condition = "UNWORN";
        double expected_cost = 12310.0;
        String expected_description = "original box";
        int expected_inventoryAmt = 100;
        String expected_image = "https://cdn-s3.touchofmodern.com/products/001/262/040/a37fadfc47cb09dc6b8708280b43679b_large.jpg?1540322406";
         

        
        Product product = new Product(expected_id, expected_name, expected_brand, expected_caseMaterial, expected_caseDiameter, expected_condition, expected_cost, expected_description, expected_inventoryAmt, expected_image);


        assertEquals(expected_id,product.getId());
        assertEquals(expected_name,product.getName());
        assertEquals(expected_brand,product.getBrand());
        assertEquals(expected_caseMaterial,product.getCaseMaterial());
        assertEquals(expected_caseDiameter,product.getCaseDiameter());
        assertEquals(expected_condition,product.getCondition());
        assertEquals(expected_cost,product.getCost());
        assertEquals(expected_description,product.getDescription());
        assertEquals(expected_inventoryAmt,product.getInventoryAmt());
        assertEquals(expected_image,product.getImage());
    }

    @Test
    public void testName() {
        // Setup
        int id = 0;
        String name = "Captain";
        String brand = "ZENITH";
        String caseMaterial = "rose gold";
        double caseDiameter = 45.45;
        String condition = "UNWORN";
        double cost = 12310.0;
        String description = "original box";
        int inventoryAmt = 100;
        String image = "https://cdn-s3.touchofmodern.com/products/001/262/040/a37fadfc47cb09dc6b8708280b43679b_large.jpg?1540322406";

        
        Product product = new Product(id, name, brand, caseMaterial, caseDiameter, condition, cost, description, inventoryAmt, image);


        String expected_name = "Oyster Perpetual Day-Date 36";

        
        product.setName(expected_name);

        
        assertEquals(expected_name,product.getName());
    }

    /*@Test
    public void testToString() {

        int id = 0;
        String name = "Captain";
        String brand = "ZENITH";
        String caseMaterial = "rose gold";
        double caseDiameter = 45.45;
        String condition = "UNWORN";
        double cost = 12310.0;
        String[] attributes =  {"original box"};
        int inventoryAmt = 100;
        String image="https://cdn-s3.touchofmodern.com/products/001/262/040/a37fadfc47cb09dc6b8708280b43679b_large.jpg?1540322406"
        
        
        String expected_string = String.format(Product.STRING_FORMAT, id, name, brand, caseMaterial, caseDiameter, condition, cost, attributes, inventoryAmt, image);
       
        
        Product product = new Product(id, name, brand, caseMaterial, caseDiameter, condition, cost,attributes, inventoryAmt, image);


        
        String actual_string = product.toString();

        
        assertEquals(expected_string,actual_string);
    }
*/
}
