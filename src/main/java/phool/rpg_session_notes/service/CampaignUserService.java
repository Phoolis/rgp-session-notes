package phool.rpg_session_notes.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import phool.rpg_session_notes.domain.AppUser;
import phool.rpg_session_notes.domain.Campaign;
import phool.rpg_session_notes.domain.CampaignUser;
import phool.rpg_session_notes.repository.AppUserRepository;
import phool.rpg_session_notes.repository.CampaignRepository;
import phool.rpg_session_notes.repository.CampaignUserRepository;

@Service
public class CampaignUserService {

    @Autowired
    private CampaignUserRepository campaignUserRepository;

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    public CampaignUser findByUserAndCampaign(AppUser appUser, Campaign campaign) {
        return campaignUserRepository.findByAppUserAndCampaign(appUser, campaign)
                .orElseThrow(() -> new RuntimeException("User not found in this campaign"));
    }

    public boolean isUserInCampaign(AppUser appUser, Campaign campaign) {
        return campaignUserRepository.findByAppUserAndCampaign(appUser, campaign).isPresent();
    }

    public List<CampaignUser> findByUser(AppUser appUser) {
        return campaignUserRepository.findAllByAppUser(appUser);
    }

    public CampaignUser addUserToCampaign(AppUser appUser, Campaign campaign, String campaignRole, String screenName) {
        Optional<CampaignUser> existingCampaignUser = campaignUserRepository.findByAppUserAndCampaign(appUser, campaign);
        if (existingCampaignUser.isPresent()) {
            throw new IllegalArgumentException("User is already part of this Campaign");
        }
        CampaignUser campaignUser = new CampaignUser();
        campaignUser.setAppUser(appUser);
        campaignUser.setCampaign(campaign);
        campaignUser.setCampaignRole(campaignRole);
        campaignUser.setScreenName(screenName);
        campaignUserRepository.save(campaignUser);

        // Add user associations to both campaign and appUser
        campaign.addCampaignUser(campaignUser);
        campaignRepository.save(campaign);
        appUser.addCampaignUser(campaignUser);
        appUserRepository.save(appUser);
        return campaignUser;
    }

    // Method overloading to set Username as screenName if it's not given
    public void addUserToCampaign(AppUser appUser, Campaign campaign, String campaignRole) {
        addUserToCampaign(appUser, campaign, campaignRole, appUser.getUsername());
    }
}
