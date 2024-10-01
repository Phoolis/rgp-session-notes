package phool.rpg_session_notes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCrypt;

import phool.rpg_session_notes.domain.AppUser;
import phool.rpg_session_notes.domain.Campaign;
import phool.rpg_session_notes.domain.CampaignUser;
import phool.rpg_session_notes.repository.AppUserRepository;
import phool.rpg_session_notes.repository.CampaignRepository;
import phool.rpg_session_notes.repository.CampaignUserRepository;

@SpringBootApplication
public class RpgSessionNotesApplication {

    private static final Logger log = LoggerFactory.getLogger(RpgSessionNotesApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(RpgSessionNotesApplication.class, args);
    }

    @Bean
    public CommandLineRunner demoData(CampaignRepository campaignRepository, AppUserRepository appUserRepository, CampaignUserRepository campaignUserRepository) {
        return (args) -> {

            log.info("Creating a few Campaign test entries");
            campaignRepository.save(new Campaign("SnarkyDM's Campaign", "The Fellowship"));
            campaignRepository.save(new Campaign("Tucker's Campaign", "Kobolds on Level One"));

            log.info("Create users: admin/admin, user/user");
            String adminPass = BCrypt.hashpw("admin", BCrypt.gensalt());
            String userPass = BCrypt.hashpw("user", BCrypt.gensalt());
            AppUser admin1 = new AppUser("admin", adminPass, "ADMIN");
            AppUser user1 = new AppUser("user", userPass, "USER");
            appUserRepository.save(admin1);
            appUserRepository.save(user1);

            log.info("Create more users: GM1/GM1, GM2/GM2, player1/player1");
            String gm1Pass = BCrypt.hashpw("GM1", BCrypt.gensalt());
            String gm2Pass = BCrypt.hashpw("GM2", BCrypt.gensalt());
            String player1Pass = BCrypt.hashpw("player1", BCrypt.gensalt());
            AppUser gm1 = new AppUser("GM1", gm1Pass, "USER");
            AppUser gm2 = new AppUser("GM2", gm2Pass, "USER");
            AppUser player1 = new AppUser("player1", player1Pass, "USER");
            appUserRepository.save(gm1);
            appUserRepository.save(gm2);
            appUserRepository.save(player1);

            log.info("Set some CampaignUser relations:");
            campaignUserRepository.save(new CampaignUser(appUserRepository.findById(3L).get(), campaignRepository.findById(1L).get(), "GM", "SnarkyGM"));
            campaignUserRepository.save(new CampaignUser(appUserRepository.findById(4L).get(), campaignRepository.findById(2L).get(), "GM", "TuckerGM"));
            campaignUserRepository.save(new CampaignUser(appUserRepository.findById(5L).get(), campaignRepository.findById(1L).get(), "PLAYER", "Aragorn"));
            campaignUserRepository.save(new CampaignUser(appUserRepository.findById(5L).get(), campaignRepository.findById(2L).get(), "PLAYER", "Poor Sod"));
        };
    }

}
