package phool.rpg_session_notes.web;

import java.time.LocalDateTime;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import phool.rpg_session_notes.domain.AppUser;
import phool.rpg_session_notes.domain.Campaign;
import phool.rpg_session_notes.domain.Invitation;
import phool.rpg_session_notes.domain.Status;
import phool.rpg_session_notes.service.AppUserService;
import phool.rpg_session_notes.service.CampaignUserService;
import phool.rpg_session_notes.service.InvitationService;

@Controller
public class InvitationController {

    private final InvitationService invitationService;
    private final CampaignUserService campaignUserService;
    private final AppUserService appUserService;

    public InvitationController(InvitationService invitationService, CampaignUserService campaignUserService, AppUserService appUserService) {
        this.invitationService = invitationService;
        this.campaignUserService = campaignUserService;
        this.appUserService = appUserService;
    }

    @GetMapping("/join/{token}")
    public String joinCampaign(@PathVariable String token, Model model) {
        // TODO this seems too elaborate for a controller, put it in Service instead
        try {
            Invitation invitation = invitationService.findByToken(token);
            if (invitation.getStatus() == Status.EXPIRED
                    || invitation.getStatus() == Status.DEACTIVATED
                    || invitation.getExpiresAt().isBefore(LocalDateTime.now())) {
                model.addAttribute("errorMessage, Invitation has expired. Ask your GM for a new Invitation link.");
                return "error";
            }
            Campaign campaign = invitation.getCampaign();
            model.addAttribute("campaign", campaign);
            model.addAttribute("token", token);
            return "joincampaign";

        } catch (Exception e) {
            model.addAttribute("errorMessage", "Invalid invitation");
            return "error";
        }

    }

    @PostMapping("/acceptCampaignInvitation")
    public String acceptCampaignInvitation(
            @RequestParam("token") String token,
            @RequestParam("screenName") String screenName,
            Model model) {
        Invitation invitation = invitationService.findByToken(token);
        Campaign campaign = invitation.getCampaign();
        AppUser appUser = appUserService.getCurrentUser();
        String campaignRole = "PLAYER";
        try {
            campaignUserService.addUserToCampaign(appUser, campaign, campaignRole);
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }

        return "redirect:/campaignlist";
    }

}
