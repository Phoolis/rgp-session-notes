package phool.rpg_session_notes;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
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

    @Autowired
    private CampaignUserRepository campaignUserRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private CampaignRepository campaignRepository;

    @Test
    public void findByAppUserAndCampaignReturnsCampaignUser() {
        AppUser appUser = appUserRepository.findByUsername("player1").get();
        Campaign campaign = campaignRepository.findById(1L).get();
        CampaignUser campaignUser = campaignUserRepository.findByAppUserAndCampaign(appUser, campaign).get();

        assertThat(campaignUser).isNotNull();
        assertThat(campaign.getName()).isEqualTo(campaignUser.getCampaign().getName());
        assertThat(appUser.getusername()).isEqualTo(campaignUser.getAppUser().getusername());
    }
}
