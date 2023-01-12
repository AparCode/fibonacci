package com.estore.api.estoreapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.estore.api.estoreapi.persistence.ProductDAO;
import com.estore.api.estoreapi.model.Product;

@RestController
@RequestMapping("products")
public class ProductController {
    private static Logger LOG = Logger.getLogger(ProductController.class.getName());
    private ProductDAO productDAO;

    public ProductController(ProductDAO productDAO){
        this.productDAO = productDAO;
    }

    
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable int id) {
        LOG.info("GET /products/" + id);
        try {
            Product product = productDAO.getProduct(id);
            if (product != null)
                return new ResponseEntity<Product>(product,HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("")
    public ResponseEntity<Product[]> getProducts() {
        LOG.info("GET /products");
        
        // Replace below with your implementation
        try {
            Product[] products = productDAO.getProducts();
            if (products != null)
                return new ResponseEntity<Product[]>(products,HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
        }
        
    }
    @GetMapping("/")
    public ResponseEntity<Product[]> searchProducts(@RequestParam(required = false) String name) throws IOException {
        LOG.info("GET /products/?name=" + name);

        try {

	    Product[] products;
	    if (name == null || name.isEmpty()){
	        products = productDAO.getInventory();
	    } else {
		products = productDAO.findProducts(name);
	    }

	    if (products != null){
                return new ResponseEntity<Product[]>(products,HttpStatus.OK);
	    } else {
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable int id) {
        LOG.info("DELETE /products/" + id);
        try {
            
            Boolean product = productDAO.deleteProduct(id);
            if (product)
                return new ResponseEntity<Product>(HttpStatus.OK);
            else
                return new ResponseEntity<Product>(HttpStatus.NOT_FOUND);

        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<Product>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Creates a {@linkplain Product product} with the provided product
     * 
     * @param product - The {@link Product product} to create
     * 
     * @return ResponseEntity with created {@link Product hero} and HTTP status of CREATED
     * ResponseEntity with HTTP status of CONFLICT if {@link Product product} already exists
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PostMapping("")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        LOG.info("POST /products" + product);

        try {
            Product createdProduct = productDAO.createProduct(product);

            
            if(createdProduct != null) {
                // no conflict
                return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
            }

            // name conflict
            return new ResponseEntity<>(HttpStatus.CONFLICT);

        } 
        catch(IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    
    /**
     * Updates the watch product
     * @param watch
     * @return new ResponseEntity of the watch product
     * @throws IOException
     */
    @PutMapping("")
    public ResponseEntity<Product> updateProduct(@RequestBody Product watch){
        LOG.info("PUT /products " + watch);
        try{
            Product newWatch = productDAO.updateProduct(watch);
            if(newWatch != null)
                return new ResponseEntity<Product>(newWatch, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e){
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
