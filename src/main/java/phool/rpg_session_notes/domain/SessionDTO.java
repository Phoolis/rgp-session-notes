package phool.rpg_session_notes.domain;

import java.util.List;

import jakarta.validation.constraints.NotNull;

public record SessionDTO(
        Long id,
        @NotNull(message = "campaignId cannot be null")
        Long campaignId,
        Integer sessionNumber,
        List<NoteDTO> notes) {

}
