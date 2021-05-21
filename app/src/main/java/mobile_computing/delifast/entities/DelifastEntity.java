package mobile_computing.delifast.entities;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.type.DateTime;

import java.time.LocalDateTime;
import java.util.Objects;

@IgnoreExtraProperties
public abstract class DelifastEntity {

    @Exclude
    private String id;
    private DateTime lastChanged;

    public DelifastEntity() {

    }

    @Exclude
    public String getId() {
        return id;
    }

    @Exclude
    public void setId(String id) {
        this.id = id;
    }

    public DateTime getLastChanged() {
        return lastChanged;
    }

    public void setLastChanged(DateTime lastChanged) {
        this.lastChanged = lastChanged;
    }
}
