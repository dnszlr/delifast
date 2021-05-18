package mobile_computing.delifast.entities;

import java.time.LocalDateTime;
import java.util.Objects;

public class User extends DelifastEntity {

    private String username;
    private String email;

    /**
     * Empty firebase constructor, don't remove.
     */
    public User() {
        super();
    }

    public User(String name , String email, String password) {
        super();
        this.username = name;
        this.email = email;
    }

    
    
    public String getName() {
        return username;
    }

    public void setName(String name) {
        this.username = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return this.username;
    }
}
