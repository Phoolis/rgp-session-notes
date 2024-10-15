package phool.rpg_session_notes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import phool.rpg_session_notes.domain.AppUser;
import phool.rpg_session_notes.domain.SignupForm;
import phool.rpg_session_notes.repository.AppUserRepository;

@Service
public class AppUserService {

    @Autowired
    private AppUserRepository appUserRepository;

    public AppUser findByUsername(String username) {
        return appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with the username:" + username));
    }

    public AppUser findById(Long id) {
        return appUserRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with the id: " + id));
    }

    public AppUser getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return findByUsername(username);
    }

    public void registerAppUser(SignupForm signupForm) throws Exception {
        if (!signupForm.getPassword().equals(signupForm.getPasswordCheck())) {
            throw new Exception("Passwords do not match!");
        }
        if (appUserRepository.findByUsername(signupForm.getUsername()).isPresent()) {
            throw new Exception("Username is already taken!");
        }

        AppUser newUser = new AppUser();
        newUser.setUsername(signupForm.getUsername());
        newUser.setPassword(hashPassword(signupForm.getPassword()));
        newUser.setRole("USER");

        appUserRepository.save(newUser);
    }

    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}
