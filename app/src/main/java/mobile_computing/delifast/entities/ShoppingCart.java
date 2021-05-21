package mobile_computing.delifast.entities;

import java.util.List;

public class ShoppingCart extends DelifastEntity {


    private Order order;
    private List<CartPosition> positions;

    /**
     * Empty firebase constructor, don't remove.
     */
    public ShoppingCart() {

    }

    public ShoppingCart(Order order, List<CartPosition> positions) {
        this.order = order;
        this.positions = positions;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<CartPosition> getPositions() {
        return positions;
    }

    public void setPositions(List<CartPosition> positions) {
        this.positions = positions;
    }
}
