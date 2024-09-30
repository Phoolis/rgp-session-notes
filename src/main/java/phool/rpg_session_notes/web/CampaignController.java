package phool.rpg_session_notes.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import phool.rpg_session_notes.repository.CampaignRepository;

@Controller
public class CampaignController {

    @Autowired
    private CampaignRepository campaignRepository;

    @GetMapping("/campaignlist")
    public String showCampaigns(Model model) {
        model.addAttribute("campaigns", campaignRepository.findAll());
        return "campaignlist";
    }

}
