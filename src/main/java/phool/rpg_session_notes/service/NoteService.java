package phool.rpg_session_notes.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import phool.rpg_session_notes.domain.AppUser;
import phool.rpg_session_notes.domain.Note;
import phool.rpg_session_notes.domain.Session;
import phool.rpg_session_notes.repository.NoteRepository;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    AppUserService appUserService;

    public Note createNoteForSession(Note note, Session session) {
        Note newNote = new Note();
        AppUser currentUser = appUserService.getCurrentUser();
        newNote.setAppUser(currentUser);
        newNote.setCreatedAt(LocalDateTime.now());
        newNote.setText(note.getText());
        if (note.getUserScreenName() == null) {
            newNote.setUserScreenName(currentUser.getUsername());
        } else {
            newNote.setUserScreenName(note.getUserScreenName());
        }

        session.addNote(newNote);
        return noteRepository.save(newNote);

    }

}
