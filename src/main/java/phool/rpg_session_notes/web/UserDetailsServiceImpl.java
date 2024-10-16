package phool.rpg_session_notes.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import phool.rpg_session_notes.domain.AppUser;
import phool.rpg_session_notes.repository.AppUserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    AppUserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser currentUser = repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username"));
        return currentUser;
        // returns an AppUser instead of User
    }
}
