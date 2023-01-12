package com.estore.api.estoreapi.persistence;

import java.io.IOException;
import com.estore.api.estoreapi.model.Product;

/**
 * Defines the interface for Product persistence
 * 
 * @author Richard Wituszynski
 * @author Nate Mount
 * @author Aparnaa Senthilnathan
 * @author Luke Wyland
 * @author Bishop Oparaugo
 */
public interface ProductDAO {

    /**
     * Retrieves a {@linkplain Hero hero} with the given id
     * 
     * @param id The id of the {@link Hero hero} to get
     * 
     * @return a {@link Hero hero} object with the matching id
     * <br>
     * null if no {@link Hero hero} with a matching id is found
     * 
     * @throws IOException if an issue with underlying storage
     */
    Product getProduct(int id) throws IOException;
    
    /**
     * Retrieves all {@linkplain Hero heroes}
     * 
     * @return An array of {@link Hero hero} objects, may be empty
     * 
     * @throws IOException if an issue with underlying storage
     */
    Product[] getProducts() throws IOException;


    /**
     * Finds all the products containing the given text
     * 
     * @param containsText The text to search for in product names
     * @return An array of a {@link Product products} that contains the given text
     * @throws IOException For storage issue
     */
    Product[] findProducts(String containsText) throws IOException;

    /**
     * Updates the watch product
     * @param watch
     * @return new ResponseEntity of the watch product
     * @throws IOException
     */
    Product updateProduct(Product watch) throws IOException;

    /**
     * Deletes a {@linkplain Hero hero} with the given id
     * 
     * @param id The id of the {@link Hero hero}
     * 
     * @return true if the {@link Hero hero} was deleted
     * <br>
     * false if hero with the given id does not exist
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    boolean deleteProduct(int id) throws IOException;

    /**
     * Creates and saves a new {@linkplain Product product}
     * @param product Product to create
     * @return newly created product or null if there was a name conflict
     * @throws IOException
     */
    Product createProduct(Product product) throws IOException;

    /**
     * Returns all products in inventory
     *
     * @return Array of {@link Product products} 
     * @throws IOException for storage issue
     */
    Product[] getInventory() throws IOException;

    public boolean reserveProduct(int id) throws IOException;

    public boolean unreserveProduct(int id) throws IOException;
}
