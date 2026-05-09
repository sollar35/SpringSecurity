package habsida.spring.boot_security.demo.service;

import habsida.spring.boot_security.demo.model.User;

import org.springframework.stereotype.Service;

import java.util.List;


public interface UserService {

    List<User> findAll();

    void save(User user);

    void save(User user, List<Long> roleIds);

    User findByUsername(String username);

    User findById(Long id);

    void update (User user, String newPassword, List<Long> roleIds);

    void delete(Long id);
}
