package phool.rpg_session_notes.repository;

import org.springframework.data.repository.CrudRepository;

import phool.rpg_session_notes.domain.AppUser;

public interface AppUserRepository extends CrudRepository<AppUser, Long> {

    AppUser findByUsername(String username);

}
