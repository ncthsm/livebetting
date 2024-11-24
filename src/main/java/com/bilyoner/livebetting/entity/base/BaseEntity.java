package com.bilyoner.livebetting.entity.base;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Setter
@Getter
@MappedSuperclass
@ToString(of = {"id", "version"})
@EqualsAndHashCode(of = {"id", "version"})
public abstract class BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;
    @Version
    private Long version = 0L;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean deleted = false;

    @PrePersist
    protected void prePersist() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

}
