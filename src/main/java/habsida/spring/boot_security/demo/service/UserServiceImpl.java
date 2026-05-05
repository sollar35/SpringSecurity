//package habsida.spring.boot_security.demo.service;
//
//import habsida.spring.boot_security.demo.model.User;
//import habsida.spring.boot_security.demo.repository.UserRepository;
//
//import java.util.List;
//
//public class UserServiceImpl implements UserService{
//
//    private final UserRepository userRepository;
//
//    public UserServiceImpl(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    @Override
//    public List<User> findAll() {
//        return userRepository.findAll();
//    }
//
//    @Override
//    public void save(User user) {
//        userRepository.save(user);
//    }
//
//    @Override
//    public void deleteById(Long id) {
//        userRepository.deleteById(id);
//    }
//
//    @Override
//    public User findById(Long id) {
//        return userRepository.findById(id).orElse(null);
//    }
//
//    @Override
//    public User findByUsername(String username) {
//        return userRepository.findByUsername(username).orElse(null);
//    }
//}
