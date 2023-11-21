package com.kjms.domain;

import javax.persistence.*;

@Entity
@Table(name = "jm_journal_checklist")
public class EntityJournalCheckList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "journal_id", foreignKey = @ForeignKey(name = "fk_jm_journal_checklist_journal_id"), updatable = false, nullable = false)
    private EntityJournal journal;

    @Column(name = "checklist_item", nullable = false)
    private String checkListItem;

    @Column(name = "display_position")
    private Integer displayPosition;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    public Integer getDisplayPosition() {
        return displayPosition;
    }

    public void setDisplayPosition(Integer displayPosition) {
        this.displayPosition = displayPosition;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EntityJournal getJournal() {
        return journal;
    }

    public void setJournal(EntityJournal journal) {
        this.journal = journal;
    }

    public String getCheckListItem() {
        return checkListItem;
    }

    public void setCheckListItem(String checkListItem) {
        this.checkListItem = checkListItem;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }
}
