package com.estore.api.estoreapi.model;

import java.util.Arrays;
import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a Product Entity
 * 
 * @author Richard Wituszynski
 * @author Nate Mount
 * @author Aparnaa Senthilnathan
 * @author Luke Wyland
 * @author Bishop Oparaugo
 */
public class Product {
    private static final Logger LOG = Logger.getLogger(Product.class.getName());

    // String format for toString
    static final String STRING_FORMAT = "Watch {id=%d, name=%s, brand=%s, caseMaterial=%s, caseDiameter=%f, condition=%s, cost=%f, attributes=";

    // Indicate external property names
    @JsonProperty("id") private int id;
    @JsonProperty("name") private String name;
    @JsonProperty("brand") private String brand;
    @JsonProperty("caseMaterial") private String caseMaterial;
    @JsonProperty("caseDiameter") private double caseDiameter;
    @JsonProperty("condition") private String condition;
    @JsonProperty("cost") private double cost;
    @JsonProperty("description") private String description;
    @JsonProperty("inventoryAmt") private int inventoryAmt;
    @JsonProperty("image") private String image;


    /**
     * Create a product with the given information:
     * 
     * @param id The id of the product
     * @param name The name of the product
     * @param brand The brand of watch
     * @param caseMaterial The material for the case of the watch
     * @param caseDiameter The diameter of the watch case
     * @param condition The condition of the watch
     * @param cost The cost of the watch
     * @param description Additional information regarding the watch
     * @param inventoryAmt The amount of watches in inventory
     */

    //changed the float values to double

    public Product(@JsonProperty("id") int id,
                   @JsonProperty("name") String name,
                   @JsonProperty("brand") String brand,
                   @JsonProperty("caseMaterial") String caseMaterial,
                   @JsonProperty("caseDiameter") double caseDiameter,
                   @JsonProperty("condition") String condition,
                   @JsonProperty("cost") double cost,
                   @JsonProperty("description") String description,
                   @JsonProperty("inventoryAmt") int inventoryAmt,
                   @JsonProperty("image") String image) {

        this.id = id;
        this.name = name;
        this.brand = brand;
        this.caseMaterial = caseMaterial;
        this.caseDiameter = caseDiameter;
        this.condition = condition;
        this.cost = cost;
        this.description = description;
        this.inventoryAmt = inventoryAmt;
        this.image = image;
    }
    
    public int getId() {return id;}

    public void setName(String name) {this.name = name;}

    public String getName() {return name;}

    public String getBrand() {return brand;}

    public String getCaseMaterial() {return caseMaterial;}

    public double getCaseDiameter() {return caseDiameter;}

    public String getCondition() {return condition;}

    public void setCost(float cost) {this.cost = cost;}

    public double getCost() {return cost;}

    public String getDescription() {return this.description;}

    public void setInventoryAmt(int inventoryAmt) {this.inventoryAmt = inventoryAmt;}

    public int getInventoryAmt() {return inventoryAmt;}

    public String getImage() {return image;}

    @Override
    public String toString() {
        String s =  String.format(STRING_FORMAT, id, name, brand, caseMaterial, caseDiameter, condition, cost);
        return s + description + ", inventoryAmt=" + inventoryAmt + ", image=" + image + "}";
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Product) {
            Product other = (Product) o;
            return other.getId() == this.getId();
        } 
        return false;
    }
}
