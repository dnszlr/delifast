package mobile_computing.delifast.entities;

import mobile_computing.delifast.delifastEnum.ProductCategory;

public class Product extends DelifastEntity {

    private String name;
    private ProductCategory category;
    private double price;

    /**
     * Empty firebase constructor, don't remove.
     */
    public Product() {

    }

    public Product(String name, ProductCategory category, double price) {
        this.name = name;
        this.category = category;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
