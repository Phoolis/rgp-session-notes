package phool.rpg_session_notes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import phool.rpg_session_notes.domain.Campaign;
import phool.rpg_session_notes.repository.CampaignRepository;

@SpringBootApplication
public class RpgSessionNotesApplication {

    private static final Logger log = LoggerFactory.getLogger(RpgSessionNotesApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(RpgSessionNotesApplication.class, args);
    }

    @Bean
    public CommandLineRunner demoData(CampaignRepository campaignRepository) {
        return (args) -> {

            log.info("Creating a few Campaign test entries");
            campaignRepository.save(new Campaign("SnarkyDM's Campaign", "The Fellowship"));
            campaignRepository.save(new Campaign("Tucker's Campaign", "Kobolds on Level One"));
        };
    }

}
