package ECommerceFlowApi.common;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseModel {
    // made this as an abstract class so that to prevent it from ever being
    // instantiated on its own.

    /**
     * The timestamp when this entity was created.
     * 
     * @CreationTimestamp automatically sets this field to the current time when the
     *                    entity is first saved.
     */
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * The timestamp when this entity was last updated.
     * 
     * @UpdateTimestamp automatically updates this field to the current time
     *                  whenever the entity is modified.
     */
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    /**
     * A boolean flag for soft-deletes.
     * 'false' (default) means the record is active.
     * 'true' means the record is "deleted".
     */
    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean deleted = false;
}
