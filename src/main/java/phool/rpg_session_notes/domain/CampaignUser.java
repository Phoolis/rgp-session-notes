package phool.rpg_session_notes.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "campaign_users")
public class CampaignUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "campaign_user_id", nullable = false, updatable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser appUser;

    @ManyToOne
    @JoinColumn(name = "campaign_id", nullable = false)
    private Campaign campaign;

    @NotBlank
    @Column(name = "campaign_role", nullable = false)
    private String campaignRole;

    @NotBlank
    @Column(name = "screen_name", nullable = false, unique = true)
    private String screenName;

    public CampaignUser() {
    }

    public CampaignUser(AppUser appUser, Campaign campaign, String campaignRole, String screenName) {
        this.appUser = appUser;
        this.campaign = campaign;
        this.campaignRole = campaignRole;
        this.screenName = screenName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public Campaign getCampaign() {
        return campaign;
    }

    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
    }

    public String getCampaignRole() {
        return campaignRole;
    }

    public void setCampaignRole(String campaignRole) {
        this.campaignRole = campaignRole;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CampaignUsers{");
        sb.append("id=").append(id);
        sb.append(", appUser=").append(appUser);
        sb.append(", campaign=").append(campaign);
        sb.append(", campaignRole=").append(campaignRole);
        sb.append(", screenName=").append(screenName);
        sb.append('}');
        return sb.toString();
    }

}
