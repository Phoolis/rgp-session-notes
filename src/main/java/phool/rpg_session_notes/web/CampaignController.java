package phool.rpg_session_notes.web;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import phool.rpg_session_notes.domain.AppUser;
import phool.rpg_session_notes.domain.Campaign;
import phool.rpg_session_notes.domain.CampaignUser;
import phool.rpg_session_notes.service.AppUserService;
import phool.rpg_session_notes.service.CampaignService;
import phool.rpg_session_notes.service.CampaignUserService;

@Controller
public class CampaignController {

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private CampaignService campaignService;

    @Autowired
    private CampaignUserService campaignUserService;

    @GetMapping("/campaignlist")
    public String getCampaignsForUser(Model model, Principal principal) {
        AppUser currentUser = appUserService.findByUsername(principal.getName());

        if (currentUser.getRole().equals("ADMIN")) {
            List<Campaign> allCampaigns = campaignService.findAllCampaignsForUser(currentUser);
            model.addAttribute("allcampaigns", allCampaigns);
        } else {
            List<CampaignUser> campaignUsers = campaignUserService.findByUser(currentUser);
            model.addAttribute("campaignusers", campaignUsers);
        }

        return "campaignlist";
    }

    @GetMapping("/createcampaign")
    public String createCampaign(Model model) {
        model.addAttribute("campaign", new Campaign());
        return "createcampaign";
    }

    @PostMapping("/saveNewCampaign")
    public String saveCampaign(Campaign campaign) {
        campaignService.saveNewCampaign(campaign);
        return "redirect:/campaignlist";
    }

    @GetMapping("/campaign/{id}/edit")
    public String editCampaign(@PathVariable("id") Long id, Principal principal, Model model) {
        AppUser currentUser = appUserService.findByUsername(principal.getName());
        Campaign campaign = campaignService.findById(id);

        if (campaignService.canEditCampaign(currentUser, campaign)) {
            model.addAttribute("campaign", campaign);
            return "editcampaign";
        } else {
            throw new AccessDeniedException("Unauthorised Access!");
        }
    }

    @PostMapping("/saveEditedCampaign")
    public String saveEditedCampaign(Campaign campaign, Model model) {
        campaignService.updateCampaign(campaign);
        return "redirect:/campaignlist";
    }

    @GetMapping("/campaign/{id}/delete")
    public String deleteCampaign(@PathVariable("id") Long id) {
        campaignService.delete(id);
        return "redirect:/campaignlist";
    }

    @GetMapping("/campaign/{id}/dashboard")
    public String getCampaignDashboard(@PathVariable("id") Long id, Model model) {
        Campaign campaign = campaignService.findById(id);
        model.addAttribute("campaign", campaign);
        return "dashboard";
    }

}
