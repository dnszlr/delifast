package mobile_computing.delifast.entities;

public class CartPosition extends DelifastEntity{

    private Product product;
    private int amount;

    /**
     * Empty firebase constructor, don't remove.
     */
    public CartPosition() {

    }

    public CartPosition(Product product, int amount) {
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
}
