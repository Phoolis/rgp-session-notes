package phool.rpg_session_notes.domain;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;

public record NoteDTO(
        Long id,
        @NotBlank(message = "Note text cannot be empty")
        String text,
        LocalDateTime createdAt,
        String userScreenName) {

}
