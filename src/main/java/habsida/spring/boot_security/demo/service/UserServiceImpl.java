package habsida.spring.boot_security.demo.service;

import habsida.spring.boot_security.demo.model.Role;
import habsida.spring.boot_security.demo.model.User;
import habsida.spring.boot_security.demo.repository.RoleRepository;
import habsida.spring.boot_security.demo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

//    private Set<Role> loadRoles(List<Long> roleIds) {
//        if (roleIds == null)
//    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public void save(User user, List<Long> roleIds) {
        User existingUser = findByUsername(user.getUsername());

        if (existingUser != null && (user.getId() == null || !existingUser.getId().equals(user.getId()))) {
            throw new RuntimeException("User already exists");
        }

        if (user.getId() == null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        userRepository.save(user);
    }


    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow();
    }

    @Override
    public void update(User user, String newPassword, List<Long> roleIds) {
        User existing = userRepository.findById(user.getId()).orElseThrow();

        existing.setUsername(user.getUsername());
        existing.setFirstName(user.getFirstName());
        existing.setLastName(user.getLastName());
        existing.setAge(user.getAge());


        if (newPassword != null && !newPassword.isEmpty()) {
            existing.setPassword(newPassword);
        }

        userRepository.save(existing);
    }


    public void delete(Long id) {
        User user = userRepository.findById(id).orElseThrow();
        user.getRoles().clear();
        userRepository.delete(user);
    }
}
