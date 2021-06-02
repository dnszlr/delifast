package mobilecomputing.delifast.entities;

public class Rating extends DelifastEntity {

    private String userId;
    private String orderId;
    private int stars;
    private String description;

    /**
     * Empty firebase constructor, don't remove.
     */
    public Rating() {
        super();
    }

    public Rating(String userId, String orderId, int stars, String description) {
        this.userId = userId;
        this.stars = stars;
        this.description = description;
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrderId() { return orderId; }

    public void setOrderId(String orderId) { this.orderId = orderId; }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
