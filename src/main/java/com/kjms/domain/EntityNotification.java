package com.kjms.domain;

import javax.persistence.*;
import java.time.OffsetDateTime;


@Entity
@Table(name = "jm_notification")
public class EntityNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "title_key", length = 250, nullable = false)
    private String titleKey;

    @Column(name = "desc_key", length = 1200, nullable = false)
    private String descKey;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "notify_to", foreignKey = @ForeignKey(name = "fk_jm_notification_notify_to"), updatable = false, nullable = false)
    private EntityUser notifyTo;
    // TODO: 24-Oct-23 @vargesh no foreign key mapping
    @Column(name = "created_by", updatable = false, nullable = false)
    private String createdBy;

    @Column(name = "created_at", updatable = false, nullable = false)
    private OffsetDateTime createdAt;
    // TODO: 24-Oct-23 @vargesh check length
    @Column(name = "title_values")
    private String titleValues;
    // TODO: 24-Oct-23 @vargesh check length
    @Column(name = "desc_values")
    private String descValues;

    @Column(name = "is_read", nullable = false)
    private Boolean isRead;

    public Boolean getRead() {
        return isRead;
    }

    public void setRead(Boolean read) {
        isRead = read;
    }

    public String getTitleValues() {
        return titleValues;
    }

    public void setTitleValues(String titleValues) {
        this.titleValues = titleValues;
    }

    public String getDescValues() {
        return descValues;
    }

    public void setDescValues(String descValues) {
        this.descValues = descValues;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitleKey() {
        return titleKey;
    }

    public void setTitleKey(String titleKey) {
        this.titleKey = titleKey;
    }

    public String getDescKey() {
        return descKey;
    }

    public void setDescKey(String descKey) {
        this.descKey = descKey;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public EntityUser getNotifyTo() {
        return notifyTo;
    }

    public void setNotifyTo(EntityUser notifyTo) {
        this.notifyTo = notifyTo;
    }
}
