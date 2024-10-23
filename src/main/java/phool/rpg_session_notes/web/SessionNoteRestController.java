package phool.rpg_session_notes.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import phool.rpg_session_notes.domain.AppUser;
import phool.rpg_session_notes.domain.Campaign;
import phool.rpg_session_notes.domain.ErrorResponse;
import phool.rpg_session_notes.domain.Note;
import phool.rpg_session_notes.domain.NoteDTO;
import phool.rpg_session_notes.domain.Session;
import phool.rpg_session_notes.domain.SessionDTO;
import phool.rpg_session_notes.repository.NoteRepository;
import phool.rpg_session_notes.service.CampaignService;
import phool.rpg_session_notes.service.CampaignUserService;
import phool.rpg_session_notes.service.NoteService;
import phool.rpg_session_notes.service.SessionService;

@RestController
@RequestMapping("/api")
public class SessionNoteRestController {

    private final SessionService sessionService;
    private final NoteService noteService;
    private final CampaignUserService campaignUserService;
    private final CampaignService campaignService;
    private final NoteRepository noteRepository;

    public SessionNoteRestController(SessionService sessionService,
            NoteService noteService,
            CampaignUserService campaignUserService,
            CampaignService campaignService,
            NoteRepository noteRepository) {
        this.sessionService = sessionService;
        this.noteService = noteService;
        this.campaignUserService = campaignUserService;
        this.campaignService = campaignService;
        this.noteRepository = noteRepository;
    }

    @GetMapping("/campaigns/{campaignId}/sessions")
    public ResponseEntity<?> getAllSessionsForCampaign(
            @PathVariable("campaignId") Long campaignId,
            @AuthenticationPrincipal AppUser currentUser
    ) {

        Campaign campaign;
        try {
            campaign = campaignService.findById(campaignId);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Campaign not found", HttpStatus.NOT_FOUND.value());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }

        if (!campaignUserService.isUserInCampaign(currentUser, campaign)) {
            ErrorResponse errorResponse = new ErrorResponse("You are not part of this campaign, " + currentUser.getUsername(), HttpStatus.FORBIDDEN.value());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        }

        List<Session> sessions = new ArrayList<>();
        sessionService.findAllByCampaign(campaign).forEach(sessions::add);

        List<SessionDTO> sessionDTOs = sessions.stream()
                .map(session -> new SessionDTO(
                session.getId(),
                session.getCampaign().getId(),
                session.getSessionNumber(),
                session.getNotes().stream()
                        .map(note -> new NoteDTO(
                        note.getId(),
                        note.getText(),
                        note.getCreatedAt(),
                        note.getUserScreenName(),
                        note.getUserRole()
                ))
                        .collect(Collectors.toList()))
                )
                .collect(Collectors.toList());

        return ResponseEntity.ok(sessionDTOs);
    }

    @GetMapping("/sessions/{sessionId}/notes")
    public ResponseEntity<?> getAllNotesForSession(
            @PathVariable("sessionId") Long sessionId,
            @AuthenticationPrincipal AppUser currentUser) {

        Session session;
        try {
            session = sessionService.findById(sessionId);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Session not found", HttpStatus.NOT_FOUND.value());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
        if (!campaignUserService.isUserInCampaign(currentUser, session.getCampaign())) {
            ErrorResponse errorResponse = new ErrorResponse("You are not part of this campaign, " + currentUser.getUsername(), HttpStatus.FORBIDDEN.value());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        }

        List<Note> notes = session.getNotes();
        List<NoteDTO> noteDTOs = notes.stream()
                .map(note -> new NoteDTO(
                note.getId(),
                note.getText(),
                note.getCreatedAt(),
                note.getUserScreenName(),
                note.getUserRole())
                )
                .collect(Collectors.toList());

        return ResponseEntity.ok(noteDTOs);
    }

    @PostMapping("/sessions/{sessionId}")
    public ResponseEntity<?> createNote(@PathVariable("sessionId") Long sessionId,
            @Valid @RequestBody NoteDTO noteDTO,
            @AuthenticationPrincipal AppUser currentUser) {

        Session session;
        try {
            session = sessionService.findById(sessionId);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Session not found", HttpStatus.NOT_FOUND.value());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
        if (!campaignUserService.isUserInCampaign(currentUser, session.getCampaign())) {
            ErrorResponse errorResponse = new ErrorResponse("You are not part of this campaign, " + currentUser.getUsername(), HttpStatus.FORBIDDEN.value());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        }

        Note newNote = new Note();
        newNote.setText(noteDTO.text());
        Note createdNote = noteService.createNoteForSession(newNote, session);

        NoteDTO responseDTO = new NoteDTO(createdNote.getId(),
                createdNote.getText(), createdNote.getCreatedAt(), createdNote.getUserScreenName(), createdNote.getUserRole());

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PutMapping("/notes/{noteId}")
    public ResponseEntity<?> editNote(@PathVariable("noteId") Long noteId,
            @Valid @RequestBody NoteDTO noteDTO,
            @AuthenticationPrincipal AppUser currentUser) {

        Note note;
        Session session;

        try {
            note = noteService.findById(noteId);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Note not found", HttpStatus.NOT_FOUND.value());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
        try {
            session = sessionService.findById(note.getSession().getId());
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Session not found", HttpStatus.NOT_FOUND.value());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
        if (!campaignUserService.isUserInCampaign(currentUser, session.getCampaign())) {
            ErrorResponse errorResponse = new ErrorResponse("You are not part of this campaign, " + currentUser.getUsername(), HttpStatus.FORBIDDEN.value());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        }
        if (!note.getAppUser().getId().equals(currentUser.getId())) {
            ErrorResponse errorResponse = new ErrorResponse("You are not the owner of this note " + currentUser.getUsername(), HttpStatus.FORBIDDEN.value());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        }

        note.setText(noteDTO.text());
        Note updatedNote = noteService.updateNote(note);
        NoteDTO responseDTO = new NoteDTO(noteId, updatedNote.getText(), updatedNote.getCreatedAt(), updatedNote.getUserScreenName(), updatedNote.getUserRole());

        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/notes/{noteId}")
    public ResponseEntity<?> deleteNote(@PathVariable("noteId") Long noteId,
            @AuthenticationPrincipal AppUser currentUser) {
        Note note;
        Session session;
        try {
            note = noteService.findById(noteId);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Note not found", HttpStatus.NOT_FOUND.value());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
        try {
            session = sessionService.findById(note.getSession().getId());
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Session not found", HttpStatus.NOT_FOUND.value());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
        if (!campaignUserService.isUserInCampaign(currentUser, session.getCampaign())) {
            ErrorResponse errorResponse = new ErrorResponse("You are not part of this campaign, " + currentUser.getUsername(), HttpStatus.FORBIDDEN.value());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        }
        if (!note.getAppUser().getId().equals(currentUser.getId())) {
            ErrorResponse errorResponse = new ErrorResponse("You are not the owner of this note " + currentUser.getUsername(), HttpStatus.FORBIDDEN.value());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        }
        // remove note association from session before delete
        session.getNotes().remove(note);
        noteRepository.deleteById(noteId);

        return ResponseEntity.noContent().build();
    }

// Exception handler for validation errors
// If validation fails, a MethodArgumentNotValidException is thrown,
// which then returns the failed field(s) and the validation failure message(s)
// as a BAD_REQUEST response
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return errors;
    }
    // Source:
    // https://dev.to/shujaat34/exception-handling-and-validation-in-spring-boot-3of9
}
