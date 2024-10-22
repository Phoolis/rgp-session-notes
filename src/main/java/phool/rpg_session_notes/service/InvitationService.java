package phool.rpg_session_notes.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import phool.rpg_session_notes.domain.Campaign;
import phool.rpg_session_notes.domain.Invitation;
import phool.rpg_session_notes.domain.Status;
import phool.rpg_session_notes.repository.InvitationRepository;

@Service
public class InvitationService {

    @Autowired
    private InvitationRepository invitationRepository;

    // read baseUrl from application.properties
    @Value("${app.base-url}")
    private String baseUrl;

    public Invitation createInvitation(Campaign campaign) {
        Invitation invitation = new Invitation();
        invitation.setCampaign(campaign);
        // Generate a 36 character string token
        invitation.setToken(UUID.randomUUID().toString());
        invitation.setCreatedAt(LocalDateTime.now());
        // Set invitations to last for 1 Week
        invitation.setExpiresAt(LocalDateTime.now().plusDays(7));
        invitation.setStatus(Status.ACTIVE);

        return invitationRepository.save(invitation);
    }

    public String generateInviteLink(Invitation invitation) {
        // TODO: change baseUrl for production
        return baseUrl + "/join/" + invitation.getToken();
    }

    public Invitation findByToken(String token) {
        Invitation invitation = invitationRepository.findByToken(token).
                orElseThrow(() -> new ResourceNotFoundException("Invitation not found"));
        if (invitation.getExpiresAt().isBefore(LocalDateTime.now())) {
            invitation.setStatus(Status.EXPIRED);
            invitationRepository.save(invitation);
        }
        return invitation;
    }

    public boolean isActive(String token) {
        Invitation invitation = this.findByToken(token);
        return invitation.getStatus().equals(Status.ACTIVE);
    }

    public void deactivate(String token) {
        Invitation invitation = this.findByToken(token);
        if (invitation.getStatus().equals(Status.ACTIVE)) {
            invitation.setStatus(Status.DEACTIVATED);
        }
    }

    public Optional<Invitation> findMostRecent(Long campaignId) {
        return invitationRepository.findTopByCampaignIdOrderByCreatedAtDesc(campaignId);
    }

    public String getActiveLinkForCampaignOrEmpty(Long campaignId) {
        Optional<Invitation> invitation = findMostRecent(campaignId);
        if (invitation.isPresent()) {
            this.findByToken(invitation.get().getToken()); // check expiration Status
        }
        if (invitation.isEmpty() || !invitation.get().getStatus().equals(Status.ACTIVE)) {
            return "";
        }
        return generateInviteLink(invitation.get());
    }
}
