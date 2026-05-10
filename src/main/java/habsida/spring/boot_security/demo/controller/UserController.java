package habsida.spring.boot_security.demo.controller;

import habsida.spring.boot_security.demo.dto.UserForm;
import habsida.spring.boot_security.demo.dto.UserProfileForm;
import habsida.spring.boot_security.demo.model.User;
import habsida.spring.boot_security.demo.service.UserService;
import jakarta.servlet.ServletResponse;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/user")
    public String userPage(Model model, Principal principal) {
        User user = service.findByUsername(principal.getName());
        model.addAttribute("user", user);
        return "user";
    }

    @GetMapping("/user/edit")
    public String editMyProfile(Model model, Principal principal) {
        User currentUser = service.findByUsername(principal.getName());
        model.addAttribute("userProfileForm", service.toProfileForm(currentUser.getId()));
        return "user-edit";
    }

    @PostMapping("/user/edit")
    public String updateMyProfile(@Valid @ModelAttribute("userProfileForm") UserProfileForm form,
                                  BindingResult bindingResult,
                                  Principal principal) {
        if (bindingResult.hasErrors()) {
            return "user-edit";
        }

        User currentUser = service.findByUsername(principal.getName());
        service.updateMyProfile(currentUser.getId(), form);

        return "redirect:/user";
    }
}
