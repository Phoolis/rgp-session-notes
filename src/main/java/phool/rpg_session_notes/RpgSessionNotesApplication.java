package phool.rpg_session_notes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RpgSessionNotesApplication {

    private static final Logger log = LoggerFactory.getLogger(RpgSessionNotesApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(RpgSessionNotesApplication.class, args);
    }

    /*     @Bean
    public CommandLineRunner demoData(
            CampaignRepository campaignRepository,
            AppUserRepository appUserRepository,
            CampaignUserRepository campaignUserRepository,
            SessionRepository sessionRepository,
            NoteRepository noteRepository) {
        return (args) -> {

            log.info("Create a few Campaign test entries");
            campaignRepository.save(new Campaign("SnarkyDM's Campaign", "The Fellowship"));
            campaignRepository.save(new Campaign("Tucker's Campaign", "Kobolds on Level One"));

            log.info("Create users: admin/admin, user/user");
            String adminPass = BCrypt.hashpw("admin", BCrypt.gensalt());
            String userPass = BCrypt.hashpw("user", BCrypt.gensalt());
            AppUser admin1 = new AppUser("admin", adminPass, "ADMIN");
            AppUser user1 = new AppUser("user", userPass, "USER");
            appUserRepository.save(admin1);
            appUserRepository.save(user1);

            log.info("Create more users: gm1/gm1, gm2/gm2, player1/player1");
            String gm1Pass = BCrypt.hashpw("gm1", BCrypt.gensalt());
            String gm2Pass = BCrypt.hashpw("gm2", BCrypt.gensalt());
            String player1Pass = BCrypt.hashpw("player1", BCrypt.gensalt());
            AppUser gm1 = new AppUser("gm1", gm1Pass, "USER");
            AppUser gm2 = new AppUser("gm2", gm2Pass, "USER");
            AppUser player1 = new AppUser("player1", player1Pass, "USER");
            appUserRepository.save(gm1);
            appUserRepository.save(gm2);
            appUserRepository.save(player1);

            log.info("Set some CampaignUser relations:");
            campaignUserRepository.save(new CampaignUser(appUserRepository.findById(3L).get(), campaignRepository.findById(1L).get(), "GM", "SnarkyGM"));
            campaignUserRepository.save(new CampaignUser(appUserRepository.findById(4L).get(), campaignRepository.findById(2L).get(), "GM", "TuckerGM"));
            campaignUserRepository.save(new CampaignUser(appUserRepository.findById(5L).get(), campaignRepository.findById(1L).get(), "PLAYER", "Aragorn"));
            campaignUserRepository.save(new CampaignUser(appUserRepository.findById(5L).get(), campaignRepository.findById(2L).get(), "PLAYER", "Poor Sod"));

            log.info("Create some sessions");
            sessionRepository.save(new Session(campaignRepository.findById(1L).get(), LocalDate.now(), 1));
            sessionRepository.save(new Session(campaignRepository.findById(1L).get(), LocalDate.now(), 2));
            sessionRepository.save(new Session(campaignRepository.findById(2L).get(), LocalDate.now(), 1));
            sessionRepository.save(new Session(campaignRepository.findById(2L).get(), LocalDate.now(), 2));
            sessionRepository.save(new Session(campaignRepository.findById(2L).get(), LocalDate.now(), 3));

            log.info("Create some notes");
            noteRepository.save(new Note(appUserRepository.findById(3L).get(), sessionRepository.findById(1L).get(), "Very important note from GM!", "GM", "SnarkyGM"));
            noteRepository.save(new Note(appUserRepository.findById(5L).get(), sessionRepository.findById(1L).get(), "Player1 adds a note.", "PLAYER", "Aragorn"));
            noteRepository.save(new Note(appUserRepository.findById(3L).get(), sessionRepository.findById(2L).get(), "Session 2 note from GM!", "GM", "SnarkyGM"));
            noteRepository.save(new Note(appUserRepository.findById(5L).get(), sessionRepository.findById(2L).get(), "Player1 adds a note to session 2.", "PLAYER", "Aragorn"));
            noteRepository.save(new Note(appUserRepository.findById(5L).get(), sessionRepository.findById(2L).get(), "Player1 adds second note to session 2.", "PLAYER", "Aragorn"));
        };
    } */
}
