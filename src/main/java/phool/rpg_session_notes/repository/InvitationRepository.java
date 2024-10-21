package phool.rpg_session_notes.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import phool.rpg_session_notes.domain.Invitation;

public interface InvitationRepository extends CrudRepository<Invitation, Long> {

    Optional<Invitation> findByToken(String token);

    Optional<Invitation> findTopByCampaignIdOrderByCreatedAtDesc(Long campaignId);

}
