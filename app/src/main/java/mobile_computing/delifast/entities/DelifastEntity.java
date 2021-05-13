package mobile_computing.delifast.entities;

import java.time.LocalDateTime;
import java.util.Objects;

public abstract class DelifastEntity {

    private long id;
    private LocalDateTime lastChanged;
    private String lastChangedBy;

    public DelifastEntity(long id, LocalDateTime lastChanged, String lastChangedBy) {
        this.id = id;
        this.lastChanged = lastChanged;
        this.lastChangedBy = lastChangedBy;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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
        return id == that.id &&
                Objects.equals(lastChanged, that.lastChanged) &&
                Objects.equals(lastChangedBy, that.lastChangedBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, lastChanged, lastChangedBy);
    }
}
