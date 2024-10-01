package phool.rpg_session_notes.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import phool.rpg_session_notes.domain.AppUser;
import phool.rpg_session_notes.domain.Campaign;
import phool.rpg_session_notes.domain.CampaignUser;

public interface CampaignUserRepository extends CrudRepository<CampaignUser, Long> {

    Optional<CampaignUser> findByAppUserAndCampaign(AppUser appUser, Campaign campaign);

    List<CampaignUser> findAllByAppUser(AppUser appUser);

}
