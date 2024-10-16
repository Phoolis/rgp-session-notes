package phool.rpg_session_notes.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class AppUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = true)
    private String email;

    @Column(name = "role", nullable = false)
    private String role;

    @OneToMany(mappedBy = "appUser", cascade = CascadeType.ALL)
    private List<CampaignUser> campaignUsers = new ArrayList<>();

    public List<CampaignUser> getCampaignUsers() {
        return campaignUsers;
    }

    public void setCampaignUsers(List<CampaignUser> campaignUsers) {
        this.campaignUsers = campaignUsers;
    }

    public void addCampaignUser(CampaignUser campaignUser) {
        this.campaignUsers.add(campaignUser);
        campaignUser.setAppUser(this);
    }

    public AppUser() {
    }

    public AppUser(String username, String password, String email, String role) {
        this.password = password;
        this.username = username;
        this.email = email;
        this.role = role;
    }

    public AppUser(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
        // eg. hasAuthority("ROLE_ADMIN")
    }

    // Have to implement all UserDetails methods:
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
