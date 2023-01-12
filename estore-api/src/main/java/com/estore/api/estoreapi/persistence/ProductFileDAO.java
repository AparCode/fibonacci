package com.estore.api.estoreapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import org.apache.logging.log4j.message.ReusableMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.estore.api.estoreapi.model.Product;

/**
 * Implements JSON file persistence for products
 * 
 * {@literal @}Component Used for Spring to create a single instance of class
 * 
 * @author Richard Wituszynski
 * @author Nate Mount
 * @author Aparnaa Senthilnathan
 * @author Luke Wyland
 * @author Bishop Oparaugo
 */

@Component
public class ProductFileDAO implements ProductDAO {
    private static final Logger LOG = Logger.getLogger(ProductFileDAO.class.getName());
    // Local cache of Product information to avoid constant reading of JSON
    Map<Integer, Product> products;

    
    private ObjectMapper objectMapper; // Conversion of Product objects and JSON file

    private static int nextId; // The next ID to assign to a product
    private String filename; // File name to read and Write to

    /**
     * Creates a Product File Data Access Object
     * 
     * @param filename JSON file name
     * @param objectMapper Provides JSON object deserialization for mapping JSON file
     * @throws IOException For storage issue
     */
    public ProductFileDAO(@Value("${products.file}") String filename, ObjectMapper objectMapper) throws IOException{
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();
    }

    /**
     * Updates the ID upon new {@linkplain Product product}
     * 
     * @return The next id
     */
    private synchronized static int nextId() {
        int id = nextId;
        ++nextId;
        return id;
    }

    /**
     * Creates a product array based on the text given
     * 
     * @param containsText The text to search for in a {@link Product products}
     * @return Array of products with the given text 
     */
    
    private Product[] getProductsArray(String containsText){
        ArrayList<Product> productArrayList = new ArrayList<>();

        for (Product product : products.values()){
            if (containsText == null || product.getName().toLowerCase().contains(containsText)){
                productArrayList.add(product);
            }
        }
        
        Product[] productArray = new Product[productArrayList.size()];
        productArrayList.toArray(productArray);
        return productArray;
    }

    /**
     * Returns all of the available products
     * 
     * @return Array of every product in the products file
     */
    private Product[] getProductsArray() {
        return getProductsArray(null);
    }

    /**
     * Returns all products from inventory
     * 
     * @return Array of products found in inventory
     */
    public Product[] getInventory(){
	ArrayList<Product> inventoryArrayList = new ArrayList<>();

	for (Product p : products.values()){
            if (p != null) inventoryArrayList.add(p);
	}

	Product[] inventoryArray = new Product[inventoryArrayList.size()];
	inventoryArrayList.toArray(inventoryArray);
	return inventoryArray;
    }

    /**
     * Saves local product cache to file
     * @return true on success
     * @throws IOException
     */
    private boolean save() throws IOException{
        Product[] productArray = getProducts();
        // Serializes the Java Objects to JSON objects into the file
        // writeValue will thrown an IOException if there is an issue
        // with the file or reading from the file
        objectMapper.writeValue(new File(filename), productArray);

        return true;
    }

    /**
     * Loads {@link Product products} from JSON file into map
     * Along with setting the next product ID based on the last ID in JSON
     * 
     * @return true if file read correctly
     * @throws IOException For storage issue
     */
    private boolean load() throws IOException{
        products = new TreeMap<>();
        nextId = 0;

        Product[] productArray = objectMapper.readValue(new File(filename), Product[].class);

        for (Product product : productArray){
            products.put(product.getId(), product);
            if (product.getId() > nextId)
                nextId = product.getId();
        }
        ++nextId;
        return true;
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Product[] findProducts(String containsText){
        synchronized(products){
            return getProductsArray(containsText.toLowerCase());
        }
    }
    /**
    ** {@inheritDoc}
     */
    @Override
    public boolean deleteProduct(int id) throws IOException {
        synchronized(products) {
            if (products.containsKey(id)) {
                products.remove(id);
                return save();
            }
            else
                return false;
        }
    }

    @Override
    public Product getProduct(int id) throws IOException {
        synchronized(products) {
            if (products.containsKey(id))
                return products.get(id);
            else
                return null;
        }
    }
 
    /**
     * {@inheritDoc}}
     */
    @Override
    public Product createProduct(Product product) throws IOException {
        synchronized(products) {

        for(Product existing : getProducts()) {
            if(existing.getName().equals(product.getName())) {
                // there is a name conflict, return null
                return null;
            }
        }

        // no conflict, create and save new product
        Product newProduct = new Product(nextId(), product.getName(), product.getBrand(), product.getCaseMaterial(),
         product.getCaseDiameter(), product.getCondition(), product.getCost(), product.getDescription(), product.getInventoryAmt(), product.getImage());
        
         products.put(newProduct.getId(), newProduct);
         save();

         return newProduct;
        }
    }

    @Override
    public Product[] getProducts() throws IOException {
        synchronized(products) {
            return getProductsArray();
        }
    }

    @Override
    public Product updateProduct(Product watch) throws IOException {
        synchronized(products) {
            if(products.containsKey(watch.getId()) == false)
                return null;
            products.put(watch.getId(), watch);
            save();
            return watch;
        }
    }

    public boolean reserveProduct(int id) throws IOException {
        synchronized(products) {
            if(products.containsKey(id)) {

                Product toReserve = products.get(id);
                int stock = toReserve.getInventoryAmt();

                if(stock >= 1) {
                    toReserve.setInventoryAmt(stock - 1);
                    products.put(id, toReserve);
                    save();
                    return true;
                }
                return false;
            }
            return false;
        }
    }

    public boolean unreserveProduct(int id) throws IOException {
        synchronized(products) {
            if(products.containsKey(id)) {

                Product toUnreserve = products.get(id);
                int stock = toUnreserve.getInventoryAmt();

                toUnreserve.setInventoryAmt(stock + 1);
                products.put(id, toUnreserve);
                save();
                return true;
            }
            return false;
        }
    }
}
