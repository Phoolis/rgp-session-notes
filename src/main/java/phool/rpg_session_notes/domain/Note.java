package phool.rpg_session_notes.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "notes")
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "note_id", nullable = false, updatable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private AppUser appUser;

    @ManyToOne
    @JoinColumn(name = "session_id", nullable = true)
    private Session session;

    @NotBlank(message = "Note cannot be empty")
    private String text;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "parent_note_id", referencedColumnName = "note_id", nullable = true)
    private Note parentNote;

    @OneToMany(mappedBy = "parentNote", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Note> childNotes = new ArrayList<>();

    private String userRole;
    private String userScreenName;

    public Note() {
    }

    public Note(AppUser appUser, Session session, String text, String userRole) {
        this.appUser = appUser;
        this.session = session;
        this.text = text;
        this.userRole = userRole;
    }

    public Note(AppUser appUser, Session session, String text, String userRole, String userScreenName) {
        this.appUser = appUser;
        this.session = session;
        this.text = text;
        this.userRole = userRole;
        this.userScreenName = userScreenName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Note getParentNote() {
        return parentNote;
    }

    public void setParentNote(Note parentNote) {
        this.parentNote = parentNote;
    }

    public List<Note> getChildNotes() {
        return childNotes;
    }

    public void setChildNotes(List<Note> childNotes) {
        this.childNotes = childNotes;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getUserScreenName() {
        return userScreenName;
    }

    public void setUserScreenName(String userScreenName) {
        this.userScreenName = userScreenName;
    }

}
