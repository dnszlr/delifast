package mobile_computing.delifast.entities;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.type.DateTime;

import java.time.LocalDateTime;
import java.util.Objects;

@IgnoreExtraProperties
public abstract class DelifastEntity {

    @DocumentId
    private String id;
    private DateTime lastChanged;

    public DelifastEntity() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DateTime getLastChanged() {
        return lastChanged;
    }

    public void setLastChanged(DateTime lastChanged) {
        this.lastChanged = lastChanged;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DelifastEntity)) return false;
        DelifastEntity that = (DelifastEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(lastChanged, that.lastChanged);
    }
}
