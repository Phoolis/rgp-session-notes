package phool.rpg_session_notes.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
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

    @Autowired
    private CampaignUserService campaignUserService;

    @Autowired
    private AppUserService appUserService;

    public Campaign findById(Long id) {
        return campaignRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Campaign not found"));
    }

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

    public boolean canEditCampaign(AppUser appUser, Campaign campaign) {
        if (appUser.getRole().equals("ADMIN")) {
            return true; // Admins can edit any campaign
        }
        CampaignUser campaignUser = campaignUserService.findByUserAndCampaign(appUser, campaign);
        return campaignUser != null && campaignUser.getCampaignRole().equals("GM");
    }

    public Campaign save(Campaign campaign) {
        //TODO Add validation to saving a campaign 
        return campaignRepository.save(campaign);
    }

    public Campaign updateCampaign(Campaign updatedCampaignData) {
        Campaign existingCampaign = this.findById(updatedCampaignData.getId());
        existingCampaign.setName(updatedCampaignData.getName());
        existingCampaign.setDescription(updatedCampaignData.getDescription());
        return this.save(existingCampaign);
    }

    public void delete(Long id) {
        Campaign campaign = this.findById(id);
        AppUser currentUser = appUserService.getCurrentUser();
        if (this.canEditCampaign(currentUser, campaign)) {
            campaignRepository.deleteById(id);
        } else {
            throw new BadCredentialsException("You do not have permission to delete this campaign");
        }

    }
}
