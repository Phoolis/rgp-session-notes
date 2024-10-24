package phool.rpg_session_notes.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import phool.rpg_session_notes.domain.Campaign;
import phool.rpg_session_notes.domain.Session;

public interface SessionRepository extends CrudRepository<Session, Long> {

    int countByCampaign(Campaign campaign);

    List<Session> findByCampaign(Campaign campaign);

}
