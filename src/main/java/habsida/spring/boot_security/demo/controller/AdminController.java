package habsida.spring.boot_security.demo.controller;

import habsida.spring.boot_security.demo.dto.UserForm;
import habsida.spring.boot_security.demo.model.User;
import habsida.spring.boot_security.demo.repository.RoleRepository;
import habsida.spring.boot_security.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService service;
    private final RoleRepository roleRepository;

    public AdminController(UserService service, RoleRepository roleRepository) {
        this.service = service;
        this.roleRepository = roleRepository;
    }

    @GetMapping
    public String adminPage(Model model, Principal principal) {
        model.addAttribute("users", service.findAll());
        model.addAttribute("currentUsername", principal.getName());
        return "admin";
    }

    @GetMapping("/new")
    public String newUser(Model model) {
        model.addAttribute("userForm", new UserForm());
        model.addAttribute("allRoles", roleRepository.findAll());
        return "create";
    }

    @GetMapping("/edit")
    public String editUser(@RequestParam Long id, Model model) {
        model.addAttribute("userForm", service.toForm(id));
        model.addAttribute("allRoles", roleRepository.findAll());
        return "edit";
    }

    @PostMapping("/save")
    public String saveUser(@Valid @ModelAttribute("userForm") UserForm userForm,
                           BindingResult bindingResult,
                           @RequestParam(required = false) String newPassword,
                           Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("allRoles", roleRepository.findAll());
            return userForm.getId() == null ? "create" : "edit";
        }

        if(userForm.getAge() == null) {
            throw new RuntimeException("Age is required");
        }

        if (userForm.getId() == null) {
            service.save(userForm);
        } else {
            service.update(userForm, newPassword);
        }
        return "redirect:/admin";
    }

    @GetMapping("/delete")
    public String deleteUser(@RequestParam Long id) {
        service.delete(id);
        return "redirect:/admin";
    }


}

