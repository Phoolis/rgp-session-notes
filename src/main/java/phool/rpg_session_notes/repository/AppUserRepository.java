package phool.rpg_session_notes.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import phool.rpg_session_notes.domain.AppUser;

public interface AppUserRepository extends CrudRepository<AppUser, Long> {

    Optional<AppUser> findByUsername(String username);

}
