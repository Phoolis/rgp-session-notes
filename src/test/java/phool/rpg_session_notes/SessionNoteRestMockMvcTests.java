package phool.rpg_session_notes;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.MediaType;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import phool.rpg_session_notes.domain.AppUser;
import phool.rpg_session_notes.domain.Campaign;
import phool.rpg_session_notes.domain.Note;
import phool.rpg_session_notes.domain.Session;
import phool.rpg_session_notes.repository.NoteRepository;
import phool.rpg_session_notes.service.CampaignService;
import phool.rpg_session_notes.service.CampaignUserService;
import phool.rpg_session_notes.service.NoteService;
import phool.rpg_session_notes.service.SessionService;
import phool.rpg_session_notes.web.SessionNoteRestController;

@ExtendWith(SpringExtension.class)
@Import(WebSecurityConfig.class)
@WebMvcTest(SessionNoteRestController.class)
public class SessionNoteRestMockMvcTests {

    @MockBean
    private CommandLineRunner demoData; //Prevent running demodata when main App context is loaded for the tests

    @Autowired
    private WebApplicationContext webAppContext;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NoteService noteService;

    @MockBean
    private SessionService sessionService;

    @MockBean
    private CampaignService campaignService;

    @MockBean
    private CampaignUserService campaignUserService;

    @MockBean
    private NoteRepository noteRepository;

    private AppUser currentUser;
    private Note note1;
    private Note note2;
    private Session session;
    private Campaign campaign;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webAppContext)
                .apply(springSecurity())
                .build();

        currentUser = new AppUser();
        currentUser.setId(1L);
        currentUser.setUsername("currentUser");

        campaign = new Campaign();
        campaign.setId(1L);

        session = new Session();
        session.setId(1L);
        session.setCampaign(campaign);

        note1 = new Note();
        note1.setId(1L);
        note1.setText("First note");
        note1.setUserScreenName("User1");
        note1.setSession(session);

        note2 = new Note();
        note2.setId(2L);
        note2.setText("Second note");
        note2.setUserScreenName("User2");
        note2.setSession(session);

        session.setNotes(new ArrayList<>(List.of(note1, note2)));
    }

    @Test
    public void testGetAllNotesForSession_Success() throws Exception {

        Long sessionId = session.getId();

        when(sessionService.findById(sessionId)).thenReturn(session);
        when(campaignUserService.isUserInCampaign(any(AppUser.class), any(Campaign.class))).thenReturn(true);

        mockMvc.perform(get("/api/sessions/{sessionId}/notes", sessionId)
                .with(user(currentUser))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].text").value("First note"))
                .andExpect(jsonPath("$[1].text").value("Second note"));
    }

    @Test
    public void testGetAllNotesForSession_SessionNotFound() throws Exception {

        Long sessionId = session.getId();
        when(sessionService.findById(sessionId)).thenThrow(new ResourceNotFoundException("Session not found"));

        mockMvc.perform(get("/api/sessions/{sessionId}/notes", sessionId)
                .with(user(currentUser))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Session not found"));
    }

}
