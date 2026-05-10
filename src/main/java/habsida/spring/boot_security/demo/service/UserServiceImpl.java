package habsida.spring.boot_security.demo.service;

import habsida.spring.boot_security.demo.dto.UserForm;
import habsida.spring.boot_security.demo.model.Role;
import habsida.spring.boot_security.demo.model.User;
import habsida.spring.boot_security.demo.repository.RoleRepository;
import habsida.spring.boot_security.demo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    private Set<Role> loadRoles(List<Long> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            throw new RuntimeException("Choose at least one role");
        }
        return new HashSet<>(roleRepository.findAllById(roleIds));
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void save(UserForm userForm) {
        User existingUser = findByUsername(userForm.getUsername());

        if (existingUser != null && (userForm.getId() == null || !existingUser.getId().equals(userForm.getId()))) {
            throw new RuntimeException("User already exists");
        }

        User user = new User();
        user.setUsername(userForm.getUsername());
        user.setFirstName(userForm.getFirstName());
        user.setLastName(userForm.getLastName());
        user.setAge(userForm.getAge());

        if (userForm.getPassword() != null  && !userForm.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userForm.getPassword()));
        }

        user.setRoles(loadRoles(userForm.getRoleIds()));
        userRepository.save(user);
    }


    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow();
    }

    @Override
    public void update(UserForm userForm,String newPassword) {
        User existing = userRepository.findById(userForm.getId()).orElseThrow();

        existing.setUsername(userForm.getUsername());
        existing.setFirstName(userForm.getFirstName());
        existing.setLastName(userForm.getLastName());
        existing.setAge(userForm.getAge());

        if (userForm.getRoleIds() != null && !userForm.getRoleIds().isEmpty()) {
            existing.setRoles(loadRoles(userForm.getRoleIds()));
        }

        if (newPassword != null && !newPassword.isEmpty()) {
            existing.setPassword(passwordEncoder.encode(newPassword));
        }

        userRepository.save(existing);
    }


    public void delete(Long id) {
        User user = userRepository.findById(id).orElseThrow();
        user.getRoles().clear();
        userRepository.delete(user);
    }

    @Override
    public UserForm toForm(Long id) {
        User user = userRepository.findById(id).orElseThrow();
        UserForm form = new UserForm();
        form.setId(user.getId());
        form.setUsername(user.getUsername());
        form.setFirstName(user.getFirstName());
        form.setLastName(user.getLastName());
        form.setAge(user.getAge());
        form.setRoleIds(user.getRoles().stream().map(Role::getId).toList());
        return form;
    }
}
