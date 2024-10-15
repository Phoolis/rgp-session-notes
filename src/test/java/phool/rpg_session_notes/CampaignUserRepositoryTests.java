package phool.rpg_session_notes;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import phool.rpg_session_notes.domain.AppUser;
import phool.rpg_session_notes.domain.Campaign;
import phool.rpg_session_notes.domain.CampaignUser;
import phool.rpg_session_notes.repository.AppUserRepository;
import phool.rpg_session_notes.repository.CampaignRepository;
import phool.rpg_session_notes.repository.CampaignUserRepository;

@SpringBootTest(classes = RpgSessionNotesApplication.class)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CampaignUserRepositoryTests {

    @MockBean
    private CommandLineRunner demoData; //Prevent running demodata when main App context is loaded for the tests

    @Autowired
    private CampaignUserRepository campaignUserRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private CampaignRepository campaignRepository;

    @BeforeEach
    public void testSetup() {

        campaignRepository.save(new Campaign("SnarkyDM's Campaign", "The Fellowship"));
        campaignRepository.save(new Campaign("Tucker's Campaign", "Kobolds on Level One"));

        // add users admin/admin, user/user
        String adminPass = BCrypt.hashpw("admin", BCrypt.gensalt());
        String userPass = BCrypt.hashpw("user", BCrypt.gensalt());
        AppUser admin1 = new AppUser("admin", adminPass, "ADMIN");
        AppUser user1 = new AppUser("user", userPass, "USER");
        appUserRepository.save(admin1);
        appUserRepository.save(user1);

        // add users gm1/gm1, gm2/gm2, player1/player1
        String gm1Pass = BCrypt.hashpw("gm1", BCrypt.gensalt());
        String gm2Pass = BCrypt.hashpw("gm2", BCrypt.gensalt());
        String player1Pass = BCrypt.hashpw("player1", BCrypt.gensalt());
        AppUser gm1 = new AppUser("gm1", gm1Pass, "USER");
        AppUser gm2 = new AppUser("gm2", gm2Pass, "USER");
        AppUser player1 = new AppUser("player1", player1Pass, "USER");
        appUserRepository.save(gm1);
        appUserRepository.save(gm2);
        appUserRepository.save(player1);

        // set up campaign users
        campaignUserRepository.save(new CampaignUser(appUserRepository.findById(3L).get(), campaignRepository.findById(1L).get(), "GM", "SnarkyGM"));
        campaignUserRepository.save(new CampaignUser(appUserRepository.findById(4L).get(), campaignRepository.findById(2L).get(), "GM", "TuckerGM"));
        campaignUserRepository.save(new CampaignUser(appUserRepository.findById(5L).get(), campaignRepository.findById(1L).get(), "PLAYER", "Aragorn"));
        campaignUserRepository.save(new CampaignUser(appUserRepository.findById(5L).get(), campaignRepository.findById(2L).get(), "PLAYER", "Poor Sod"));
    }

    @Test
    public void findByAppUserAndCampaignReturnsCampaignUser() {
        AppUser appUser = appUserRepository.findByUsername("player1").get();
        Campaign campaign = campaignRepository.findById(1L).get();
        CampaignUser campaignUser = campaignUserRepository.findByAppUserAndCampaign(appUser, campaign).get();

        assertThat(campaignUser).isNotNull();
        assertThat(campaign.getName()).isEqualTo(campaignUser.getCampaign().getName());
        assertThat(appUser.getUsername()).isEqualTo(campaignUser.getAppUser().getUsername());
    }
}
