package phool.rpg_session_notes.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import phool.rpg_session_notes.domain.AppUser;
import phool.rpg_session_notes.domain.Campaign;
import phool.rpg_session_notes.domain.CampaignUser;
import phool.rpg_session_notes.repository.CampaignRepository;
import phool.rpg_session_notes.repository.CampaignUserRepository;

@Service
public class CampaignService {

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private CampaignUserRepository campaignUserRepository;

    public List<Campaign> findAllCampaignsForUser(AppUser appUser) {
        // For admin, show all campaings in the database 
        if (appUser.getRole().equals("ADMIN")) {
            List<Campaign> allCampaigns
                    = StreamSupport.stream(campaignRepository.findAll().spliterator(), false)
                            .collect(Collectors.toList());
            return allCampaigns;
        } else {
            // for user, show all campaigns they are part of either as player or GM
            List<CampaignUser> campaignUsers = campaignUserRepository.findAllByAppUser(appUser);
            List<Campaign> userCampaigns = campaignUsers.stream()
                    .map(CampaignUser::getCampaign)
                    .collect(Collectors.toList());
            return userCampaigns;
        }
    }
}
