package com.estore.api.estoreapi.persistence;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.estore.api.estoreapi.model.Product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Persistence-tier")
public class ProductFileDAOTest {
    ProductFileDAO productFileDAO;
    Product[] testProducts;
    ObjectMapper mockObjectMapper;

    @BeforeEach
    @JsonProperty
    public void setupProductFileDAO() throws IOException{
        mockObjectMapper = mock(ObjectMapper.class);
        testProducts = new Product[5];
        testProducts[0] = new Product(13, "A", "Gucci", null, 45, null, 50, null, 1, null);
        testProducts[1] = new Product(17, "B", "Gucci", null, 45, null, 50, null, 1, null);
        testProducts[2] = new Product(101, "Red Watch", "Watch watch", null, 45, null, 50, null, 1, null);
        testProducts[3] = new Product(102, "Blue Watch", "Watch watch", null, 45, null, 50, null, 1, null);
        testProducts[4] = new Product(103, "Green Watch", "Watch watch", null, 45, null, 50, null, 0, null);

        
        when(mockObjectMapper
            .readValue(new File("doesnt_matter.txt"),Product[].class))
                .thenReturn(testProducts);
        productFileDAO = new ProductFileDAO("doesnt_matter.txt",mockObjectMapper);
    
    }

    @Test
    public void testDeleteProduct() throws IOException {
       assertTrue(productFileDAO.deleteProduct(101));
       assertNull(productFileDAO.getProduct(101));
    }
    @Test
    public void testDeleteProductNotFound() throws IOException {
        assertFalse(productFileDAO.deleteProduct(0));
    }

    @Test
    public void testGetProducts() throws IOException{
        Product[] products = productFileDAO.getProducts();
        
        
        assertEquals(products.length,testProducts.length);
        for (int i = 0; i < testProducts.length;++i)
            assertEquals(products[i],testProducts[i]);
    }

    @Test
    public void testGetProduct() throws IOException{
        Product product = productFileDAO.getProduct(13);

        assertEquals(product,testProducts[0]);
    }


    @Test
    public void testUpdateProduct() throws IOException {
         // Setup
         Product watch = new Product(13, "A", "Gucci", null, 45, null, 50, null, 1, null);

         // Invoke
         Product result = assertDoesNotThrow(() -> productFileDAO.updateProduct(watch),
                                 "Unexpected exception thrown");

         // Analyze
         assertNotNull(result);
         Product actual = productFileDAO.getProduct(watch.getId());
         assertEquals(actual,watch);
         
     }

     @Test 
     public void testUpdateProductNotFound(){
        //Setup
        Product watch = new Product(0, "C", "Z", "rose gold", 45.45, "UNWORN", 12310.0, null, 100, "https://cdn-s3.touchofmodern.com/products/001/262/040/a37fadfc47cb09dc6b8708280b43679b_large.jpg?1540322406");
        // Invoke
        Product result = assertDoesNotThrow(() -> productFileDAO.updateProduct(watch),
                                 "Unexpected exception thrown");
        // Analyze
        assertNull(result);
     }


    @Test
    public void testGetProductNotFound() throws IOException{
    
        Product product = productFileDAO.getProduct(16);

        
        assertEquals(product,null);
    }

    
    @Test
    public void testSaveException() throws IOException{
        doThrow(new IOException())
            .when(mockObjectMapper)
                .writeValue(any(File.class),any(Product[].class));

        Product product = new Product(104, "Frog Watch", "Froggy Watch Co.", "Frog Skin",
         10.23, "Great", 123.45, "Looks cool, Whispers you the time",
         10, "https://tse2.mm.bing.net/th?id=OIP.Kz2i9dlp1w0-bRkR1iaMmQAAAA&pid=Api&P=0");

        
        assertThrows(IOException.class,
                        () -> productFileDAO.createProduct(product),
                        "IOException not thrown");
    }
    
    @Test
    public void testConstructorException() throws IOException {
        
        ObjectMapper mockObjectMapper = mock(ObjectMapper.class);
        doThrow(new IOException())
            .when(mockObjectMapper)
                .readValue(new File("doesnt_matter.txt"),Product[].class);

        
        assertThrows(IOException.class,
                        () -> new ProductFileDAO("doesnt_matter.txt",mockObjectMapper),
                        "IOException not thrown");
    }

    @Test
    public void testSearchProductSameCase() throws IOException {
        // Invoke
        Product[] products = productFileDAO.findProducts("Wa");

        // Analyze
        assertEquals(products.length,3);
        assertEquals(products[0], testProducts[2]);
        assertEquals(products[1], testProducts[3]);
        assertEquals(products[2], testProducts[4]);
    }

    @Test
    public void testSearchProductDifferentCase() throws IOException {
        // Invoke
        Product[] products = productFileDAO.findProducts("wA");

        // Analyze
        assertEquals(products.length,3);
        assertEquals(products[0], testProducts[2]);
        assertEquals(products[1], testProducts[3]);
        assertEquals(products[2], testProducts[4]);
    }

    @Test
    public void testSearchProductDoesNotExist() throws IOException {
        // Invoke
        Product[] products = productFileDAO.findProducts("xyz");

        // Analyze
        assertEquals(products.length,0);
    }

    @Test
    public void testCreateProduct() throws IOException {
       // setup
        Product testProduct = new Product(104, "Frog Watch", "Froggy Watch Co.", "Frog Skin",
         10.23, "Great", 123.45, "Looks cool, Whispers you the time",
         10, "https://tse2.mm.bing.net/th?id=OIP.Kz2i9dlp1w0-bRkR1iaMmQAAAA&pid=Api&P=0");

        Product result = assertDoesNotThrow(() -> productFileDAO.createProduct(testProduct), "Unexpected Exception Thrown.");

        assertNotNull(result);
        Product actual = productFileDAO.getProduct(testProduct.getId());
        
        assertEquals(actual.getId(), testProduct.getId());
        assertEquals(actual.getName(), testProduct.getName());
        assertEquals(actual.getInventoryAmt(), testProduct.getInventoryAmt());
        assertEquals(actual.getCost(), testProduct.getCost());
        assertEquals(actual.getCondition(), testProduct.getCondition());
        assertEquals(actual.getCaseMaterial(), testProduct.getCaseMaterial());
        assertEquals(actual.getCaseDiameter(), testProduct.getCaseDiameter());
        assertEquals(actual.getBrand(), testProduct.getBrand());
        assertEquals(actual.getDescription(), testProduct.getDescription());
        assertEquals(actual.getImage(), testProduct.getImage());
    }
    @Test
    public void testCreateProductExists() throws IOException {
        assertNull(productFileDAO.createProduct(testProducts[0]));
    }

    @Test
    public void testGetInventory() {
        assertTrue(Arrays.equals(testProducts, productFileDAO.getInventory()));
    }

    @Test
    public void testReserveProduct() throws IOException {
        Product testProduct = testProducts[0];
        int amount = testProduct.getInventoryAmt();
        assertTrue(productFileDAO.reserveProduct(testProduct.getId()));
        assertEquals(amount - 1, productFileDAO.getProduct(testProduct.getId()).getInventoryAmt());
    }
    @Test
    public void testReserveProductOutOfStock() throws IOException {
        Product testProduct = testProducts[4];
        int amount = testProduct.getInventoryAmt();
        assertFalse(productFileDAO.reserveProduct(testProduct.getId()));
        assertEquals(amount, productFileDAO.getProduct(testProduct.getId()).getInventoryAmt());
    }
    @Test
    public void testReserveProductNotFound() throws IOException {
        Product testProduct = testProducts[4];
        int amount = testProduct.getInventoryAmt();
        assertFalse(productFileDAO.reserveProduct(testProduct.getId() + 999));
        assertEquals(amount, productFileDAO.getProduct(testProduct.getId()).getInventoryAmt());
    }

    @Test
    public void testUnreserveProduct() throws IOException {
        Product testProduct = testProducts[0];
        int amount = testProduct.getInventoryAmt();
        assertTrue(productFileDAO.unreserveProduct(testProduct.getId()));
        assertEquals(amount + 1, productFileDAO.getProduct(testProduct.getId()).getInventoryAmt());
    }
    @Test
    public void testUnreserveProductNotFound() throws IOException {
        Product testProduct = testProducts[0];
        assertFalse(productFileDAO.unreserveProduct(testProduct.getId() + 999));
    }
}
