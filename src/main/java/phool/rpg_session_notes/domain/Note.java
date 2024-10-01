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

@Entity
@Table(name = "notes")
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private AppUser appUser;

    @ManyToOne
    @JoinColumn(name = "session_id", nullable = true)
    private Session session;

    private String text;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "parent_note_id", referencedColumnName = "id", nullable = true)
    private Note parentNote;

    @OneToMany(mappedBy = "parentNote", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Note> childNotes = new ArrayList<>();

    private String userRole;
    private String userScreenName;
}
