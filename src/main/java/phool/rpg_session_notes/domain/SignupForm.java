package phool.rpg_session_notes.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SignupForm {

    @NotBlank(message = "Username is required")
    @Size(min = 5, max = 30, message = "Username must be between 5 and 30 characters")
    private String username = "";

    @NotBlank(message = "Password is required")
    @Size(min = 7, max = 30, message = "Password must be at least 7 characters")
    private String password = "";

    @NotBlank(message = "Please confirm your password")
    @Size(min = 7, max = 30)
    private String passwordCheck = "";

    private String role = "USER";

    public SignupForm() {
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

    public String getPasswordCheck() {
        return passwordCheck;
    }

    public void setPasswordCheck(String passwordCheck) {
        this.passwordCheck = passwordCheck;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
