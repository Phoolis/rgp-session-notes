package phool.rpg_session_notes.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import phool.rpg_session_notes.domain.Campaign;
import phool.rpg_session_notes.domain.Note;
import phool.rpg_session_notes.domain.Session;
import phool.rpg_session_notes.service.NoteService;
import phool.rpg_session_notes.service.SessionService;

@Controller
public class NoteController {

    @Autowired
    private NoteService noteService;

    @Autowired
    private SessionService sessionService;

    @PostMapping("/addNewNote")
    public String addNewNote(@Valid @ModelAttribute("note") Note note,
            BindingResult bindingResult,
            @RequestParam("session.id") Long sessionId,
            Model model) {

        Session session = sessionService.findById(sessionId);
        Campaign campaign = session.getCampaign();
        if (bindingResult.hasErrors()) {
            model.addAttribute("campaign", campaign);
            //model.addAttribute("note", new Note());
            return "sessionlog";
        }

        noteService.createNoteForSession(note, session);
        return "redirect:/campaign/" + session.getCampaign().getId() + "/sessionlog";
    }

}
