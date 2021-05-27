package mobilecomputing.delifast.entities;

public class OrderPosition {

    private Product product;
    private int amount;

    /**
     * Empty firebase constructor, don't remove.
     */
    public OrderPosition() {
        super();
    }

    public OrderPosition(Product product, int amount) {
        this.product = product;
        this.amount = amount;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    /**
     * @return Multiplies the product price with the amount
     */
    public double getPrice() {

        return product.getPrice() * amount;
    }

    @Override
    public String toString() {
        return product.getName();
    }
}



