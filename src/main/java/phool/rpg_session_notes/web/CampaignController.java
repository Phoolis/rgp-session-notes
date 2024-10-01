package phool.rpg_session_notes.web;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import phool.rpg_session_notes.domain.AppUser;
import phool.rpg_session_notes.domain.Campaign;
import phool.rpg_session_notes.service.AppUserService;
import phool.rpg_session_notes.service.CampaignService;

@Controller
public class CampaignController {

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private CampaignService campaignService;

    @GetMapping("/campaignlist")
    public String getCampaignsForUser(Model model, Principal principal) {
        AppUser appUser = appUserService.findByUsername(principal.getName());
        List<Campaign> campaigns = campaignService.findAllCampaignsForUser(appUser);
        model.addAttribute("campaigns", campaigns);
        return "campaignlist";
    }

}
