package mobile_computing.delifast.entities;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;

import java.time.LocalDateTime;
import java.util.Objects;

@IgnoreExtraProperties
public abstract class DelifastEntity {

    @Exclude
    private String id;

    private LocalDateTime lastChanged;

    private String lastChangedBy;

    public DelifastEntity(){

    }
    @Exclude
    public String getId() {
        return id;
    }
    @Exclude
    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getLastChanged() {
        return lastChanged;
    }

    public void setLastChanged(LocalDateTime lastChanged) {
        this.lastChanged = lastChanged;
    }

    public String getLastChangedBy() {
        return lastChangedBy;
    }

    public void setLastChangedBy(String lastChangedBy) {
        this.lastChangedBy = lastChangedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DelifastEntity that = (DelifastEntity) o;
        return id.equals(that.id) &&
                Objects.equals(lastChanged, that.lastChanged) &&
                Objects.equals(lastChangedBy, that.lastChangedBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, lastChanged, lastChangedBy);
    }
}
