package gift.repository.userRepository;

import gift.entity.User;
import gift.entity.UserRole;

import java.util.List;

public interface UserRepository {
    User save(String email, String password, UserRole role);

    List<User> getAllUsers();

    List<User> findUsersByEmail(String email);

    User findUserById(Long id);

    User updateUser(User findUser, String email, String password);

    void deleteUser(User findUser);

    User findUserByEmail(String email);

}
