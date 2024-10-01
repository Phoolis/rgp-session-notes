package phool.rpg_session_notes.repository;

import org.springframework.data.repository.CrudRepository;

import phool.rpg_session_notes.domain.Session;

public interface SessionRepository extends CrudRepository<Session, Long> {

}
