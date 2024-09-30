package phool.rpg_session_notes.repository;

import org.springframework.data.repository.CrudRepository;

import phool.rpg_session_notes.domain.Campaign;

public interface CampaignRepository extends CrudRepository<Campaign, Long>{

}
