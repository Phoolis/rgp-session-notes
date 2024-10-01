package phool.rpg_session_notes.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import phool.rpg_session_notes.domain.AppUser;
import phool.rpg_session_notes.domain.Campaign;
import phool.rpg_session_notes.domain.CampaignUser;
import phool.rpg_session_notes.repository.CampaignUserRepository;

@Service
public class CampaignUserService {

    @Autowired
    private CampaignUserRepository campaignUserRepository;

    public CampaignUser findByUserAndCampaign(AppUser appUser, Campaign campaign) {
        return campaignUserRepository.findByAppUserAndCampaign(appUser, campaign)
                .orElseThrow(() -> new RuntimeException("User not found in this campaign"));
    }

    public List<CampaignUser> findByUser(AppUser appUser) {
        return campaignUserRepository.findAllByAppUser(appUser);
    }

    public CampaignUser addUserToCampaign(AppUser appUser, Campaign campaign, String role) {
        CampaignUser campaignUser = new CampaignUser();
        campaignUser.setAppUser(appUser);
        campaignUser.setCampaign(campaign);
        campaignUser.setCampaignRole(role);
        return campaignUserRepository.save(campaignUser);
    }

}
