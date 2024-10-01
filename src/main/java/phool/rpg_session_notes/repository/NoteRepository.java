package phool.rpg_session_notes.repository;

import org.springframework.data.repository.CrudRepository;

import phool.rpg_session_notes.domain.Note;

public interface NoteRepository extends CrudRepository<Note, Long> {

}
