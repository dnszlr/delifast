package mobile_computing.delifast.entities;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        if (!super.equals(o)) return false;
        Product product = (Product) o;
        return Double.compare(product.price, price) == 0 &&
                Objects.equals(name, product.name) &&
                category == product.category;
    }
}
