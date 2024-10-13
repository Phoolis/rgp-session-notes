package phool.rpg_session_notes.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;
import phool.rpg_session_notes.domain.SignupForm;
import phool.rpg_session_notes.service.AppUserService;

@Controller
public class UserController {

    private final AppUserService appUserService;

    public UserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping("/signup")
    public String addAppUser(Model model) {
        model.addAttribute("signupform", new SignupForm());
        return "signup";
    }

    @PostMapping("/saveuser")
    public String saveUser(@Valid @ModelAttribute("signupform") SignupForm signupForm, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "signup";
        }

        try {
            appUserService.registerAppUser(signupForm);
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "signup";
        }
    }
}
