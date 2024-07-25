package me.dio.innovation.one.service;

import me.dio.innovation.one.domain.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {

    User findUserById(Long id);

    List<User> findAllUsers();

    User createUser(User userToCreate);

    User updateUser(Long id, User userToUpdate);

    void deleteUser(Long id);

    User getAuthenticatedUser();

    User getUserById(Long id);

    User findByUsername(String username);

    Optional<User> findOptionalByUsername(String username);
}
