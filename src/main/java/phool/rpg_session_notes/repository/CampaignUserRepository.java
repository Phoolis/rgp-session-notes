package phool.rpg_session_notes.repository;

import org.springframework.data.repository.CrudRepository;

import phool.rpg_session_notes.domain.CampaignUser;

public interface CampaignUserRepository extends CrudRepository<CampaignUser, Long> {

}
