package phool.rpg_session_notes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import phool.rpg_session_notes.domain.AppUser;
import phool.rpg_session_notes.domain.Campaign;
import phool.rpg_session_notes.domain.Note;
import phool.rpg_session_notes.domain.NoteDTO;
import phool.rpg_session_notes.domain.Session;
import phool.rpg_session_notes.service.CampaignUserService;
import phool.rpg_session_notes.service.NoteService;
import phool.rpg_session_notes.service.SessionService;
import phool.rpg_session_notes.web.SessionNoteRestController;

public class SessionNoteRestMockitoTests {

    //Prevent running demodata when main App context is loaded for the tests
    @MockBean
    private CommandLineRunner demoData;

    @Mock
    private NoteService noteService;

    @Mock
    private SessionService sessionService;

    @Mock
    private CampaignUserService campaignUserService;

    @InjectMocks
    private SessionNoteRestController sessionNoteRestController;

    private AppUser noteOwner;
    private AppUser otherUser;
    private Note note;
    private Session session;
    private Campaign campaign;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        noteOwner = new AppUser();
        noteOwner.setId(1L);
        noteOwner.setUsername("owner");

        otherUser = new AppUser();
        otherUser.setId(2L);
        otherUser.setUsername("otherUser");

        note = new Note();
        note.setId(1L);
        note.setText("Original note text");
        note.setAppUser(noteOwner);

        session = new Session();
        session.setId(1L);
        campaign = new Campaign();
        campaign.setId(1L);
        session.setCampaign(campaign);

        note.setSession(session);
    }

    @Test
    public void testEditNote_Success() {
        // GIVEN
        NoteDTO noteDTO = new NoteDTO(null, "Updated note text", null, null, null);

        when(noteService.findById(1L)).thenReturn(note);
        when(sessionService.findById(1L)).thenReturn(session);
        when(campaignUserService.isUserInCampaign(noteOwner, session.getCampaign())).thenReturn(true);

        Note updatedNote = new Note();
        updatedNote.setId(note.getId());
        updatedNote.setText(noteDTO.text());
        updatedNote.setAppUser(noteOwner);
        updatedNote.setSession(note.getSession());

        when(noteService.updateNote(note)).thenReturn(updatedNote);

        // WHEN
        ResponseEntity<?> response = sessionNoteRestController.editNote(1L, noteDTO, noteOwner);

        // THEN
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(noteService).updateNote(note);
        assertEquals("Updated note text", note.getText());
    }

    @Test
    public void testEditNote_Forbidden() {
        // GIVEN
        NoteDTO noteDTO = new NoteDTO(null, "Updated note text", null, null, null);

        when(noteService.findById(1L)).thenReturn(note);
        when(sessionService.findById(1L)).thenReturn(session);
        when(campaignUserService.isUserInCampaign(otherUser, session.getCampaign())).thenReturn(false);

        Note updatedNote = new Note();
        updatedNote.setId(note.getId());
        updatedNote.setText(noteDTO.text());
        updatedNote.setAppUser(noteOwner);
        updatedNote.setSession(note.getSession());

        when(noteService.updateNote(note)).thenReturn(updatedNote);

        // WHEN
        ResponseEntity<?> response = sessionNoteRestController.editNote(1L, noteDTO, noteOwner);

        // THEN
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        // verify that updateNote was not run
        verify(noteService, never()).updateNote(note);
        assertEquals("Original note text", note.getText());
    }

}
