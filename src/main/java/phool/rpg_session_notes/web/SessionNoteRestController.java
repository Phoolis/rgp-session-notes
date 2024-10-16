package phool.rpg_session_notes.web;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import phool.rpg_session_notes.domain.AppUser;
import phool.rpg_session_notes.domain.Campaign;
import phool.rpg_session_notes.domain.Note;
import phool.rpg_session_notes.domain.NoteDTO;
import phool.rpg_session_notes.domain.Session;
import phool.rpg_session_notes.domain.SessionDTO;
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

    public SessionNoteRestController(SessionService sessionService,
            NoteService noteService,
            CampaignUserService campaignUserService,
            CampaignService campaignService) {
        this.sessionService = sessionService;
        this.noteService = noteService;
        this.campaignUserService = campaignUserService;
        this.campaignService = campaignService;
    }

    @GetMapping("/campaigns/{campaignId}/sessions")
    public ResponseEntity<?> getAllSessionsForCampaign(
            @PathVariable("campaignId") Long campaignId,
            @AuthenticationPrincipal AppUser currentUser
    ) {

        Campaign campaign = null;
        try {
            campaign = campaignService.findById(campaignId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Campaign not found");
        }

        if (!campaignUserService.isUserInCampaign(currentUser, campaign)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not part of this campaign, " + currentUser.getUsername());
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
                        note.getUserScreenName()
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

        Session session = null;
        try {
            session = sessionService.findById(sessionId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Session not found");
        }
        if (!campaignUserService.isUserInCampaign(currentUser, session.getCampaign())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not part of this campaign, " + currentUser.getUsername());
        }

        List<Note> notes = session.getNotes();
        List<NoteDTO> noteDTOs = notes.stream()
                .map(note -> new NoteDTO(
                note.getId(),
                note.getText(),
                note.getCreatedAt(),
                note.getUserScreenName())
                )
                .collect(Collectors.toList());

        return ResponseEntity.ok(noteDTOs);
    }

    @PostMapping("/sessions/{sessionId}")
    public ResponseEntity<?> createNote(@PathVariable("sessionId") Long sessionId,
            @Valid @RequestBody NoteDTO noteDTO,
            @AuthenticationPrincipal AppUser currentUser) {

        Session session = null;
        try {
            session = sessionService.findById(sessionId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Session not found");
        }
        if (!campaignUserService.isUserInCampaign(currentUser, session.getCampaign())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not part of this campaign, " + currentUser.getUsername());
        }

        Note newNote = new Note();
        newNote.setText(noteDTO.text());
        newNote.setUserScreenName(noteDTO.userScreenName());
        Note createdNote = noteService.createNoteForSession(newNote, session);

        NoteDTO responseDTO = new NoteDTO(createdNote.getId(),
                createdNote.getText(), createdNote.getCreatedAt(), createdNote.getUserScreenName());

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

}
