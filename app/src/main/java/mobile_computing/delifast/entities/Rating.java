package mobile_computing.delifast.entities;

public class Rating extends DelifastEntity {

    private User user;
    private int stars;
    private String description;

    /**
     * Empty firebase constructor, don't remove.
     */
    public Rating() {

    }

    public Rating(User user, int stars, String description) {
        this.user = user;
        this.stars = stars;
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

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
