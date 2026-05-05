package habsida.spring.boot_security.demo.service;

import habsida.spring.boot_security.demo.model.User;
import habsida.spring.boot_security.demo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository repo;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repo, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> findAll() {
        return repo.findAll();
    }

    public void save(User user) {

        User existingUser = findByUsername(user.getUsername());

        if (existingUser != null && (user.getId() == null || !existingUser.getId().equals(user.getId()))) {
            throw new RuntimeException("User already exists");
        }

        if (user.getId() == null) {
            user.setPassword(user.getPassword());
        }

        repo.save(user);
    }

    public User findByUsername(String username) {
        return repo.findByUsername(username).orElse(null);
    }

    public User findById(Long id) {
        return repo.findById(id).orElseThrow();
    }

    public void update (User user, String newPassword) {
        User existing = repo.findById(user.getId()).orElseThrow();

        existing.setUsername(user.getUsername());
        existing.setFirstName(user.getFirstName());
        existing.setLastName(user.getLastName());
        existing.setAge(user.getAge());
        existing.setRoles(user.getRoles());

        if (newPassword != null && !newPassword.isEmpty()) {
            existing.setPassword(newPassword);
        }

        repo.save(existing);
    }

    public void delete(Long id) {
        User user = repo.findById(id).orElseThrow();
        user.getRoles().clear();
        repo.delete(user);
    }
}
