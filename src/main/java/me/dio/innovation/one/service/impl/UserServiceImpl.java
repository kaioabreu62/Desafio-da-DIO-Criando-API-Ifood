package me.dio.innovation.one.service.impl;

import jakarta.transaction.Transactional;
import me.dio.innovation.one.domain.model.Role;
import me.dio.innovation.one.domain.model.User;
import me.dio.innovation.one.domain.repository.RoleRepository;
import me.dio.innovation.one.domain.repository.UserRepository;
import me.dio.innovation.one.security.UserPrincipal;
import me.dio.innovation.one.service.UserService;
import me.dio.innovation.one.service.exception.BusinessException;
import me.dio.innovation.one.service.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private static final Long UNCHANGEABLE_USER_ID = 1L;
    private static final String ROLE_MANAGER = "MANAGER";
    private static final String ROLE_USER = "USER";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Transactional
    @Override
    public List<User> findAllUsers() {
        return this.userRepository.findAll();
    }

    @Transactional
    @Override
    public User findUserById(Long id) {
        return this.userRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    @Transactional
    @Override
    public User createUser(User userToCreate) {
        Optional.ofNullable(userToCreate).orElseThrow(() -> new BusinessException("User to create must not be null"));
        Optional.ofNullable(userToCreate.getName()).orElseThrow(() -> new BusinessException("User to be created must not null name"));
        Optional.ofNullable(userToCreate.getPhone_number()).orElseThrow(() -> new BusinessException("User to be created must not null phone number"));
        Optional.ofNullable(userToCreate.getCep()).orElseThrow(() -> new BusinessException("User to be created must not null cep"));
        Optional.ofNullable(userToCreate.getAddress()).orElseThrow(() -> new BusinessException("User to be created must not null address"));
        Optional.ofNullable(userToCreate.getNeighborhood()).orElseThrow(() -> new BusinessException("User to be created must not null neighborhood"));
        Optional.ofNullable(userToCreate.getAddress_complement()).orElseThrow(() -> new BusinessException("User to be created must not null address complement"));
        Optional.ofNullable(userToCreate.getCity()).orElseThrow(() -> new BusinessException("User to be created must not null city"));
        Optional.ofNullable(userToCreate.getEmail()).orElseThrow(() -> new BusinessException("User to be created must not null email"));
        Optional.ofNullable(userToCreate.getUsername()).orElseThrow(() -> new BusinessException("User to be created must not null username"));
        Optional.ofNullable(userToCreate.getPassword()).orElseThrow(() -> new BusinessException("User to be created must not null password"));

        this.validateChangeableId(userToCreate.getId(), "created");
        if (userRepository.existsByEmail(userToCreate.getEmail())) {
            throw new BusinessException("This is email already exists");
        }
        if (userRepository.existsByUsername(userToCreate.getUsername())) {
            throw new BusinessException("This is username already exists");
        }

        userToCreate.setPassword(passwordEncoder.encode(userToCreate.getPassword()));

        Role userRole;
        long userCount = userRepository.count();
        if (userCount == 0) {
            userRole = roleRepository.findByName(ROLE_MANAGER).orElseThrow(() -> new BusinessException("Role MANAGER not found"));
        } else {
            userRole = roleRepository.findByName(ROLE_USER).orElseThrow(() -> new BusinessException("Role USER not found"));
        }

        userToCreate.setRoles(Collections.singletonList(userRole));

        System.out.println("Creating user: " + userToCreate);

        return this.userRepository.save(userToCreate);
    }

    @Transactional
    @Override
    public User updateUser(Long id, User userToUpdate) {
       this.validateChangeableId(id, "updated");
       User dbUser = this.findUserById(id);

       if (dbUser == null) {
           throw new NotFoundException();
       }

       if (!dbUser.getId().equals(userToUpdate.getId())) {
           throw new BusinessException("Update IDs must be the same");
       }
       dbUser.setName(userToUpdate.getName());
       dbUser.setPhone_number(userToUpdate.getPhone_number());
       dbUser.setCep(userToUpdate.getCep());
       dbUser.setAddress(userToUpdate.getAddress());
       dbUser.setNeighborhood(userToUpdate.getNeighborhood());
       dbUser.setAddress_complement(userToUpdate.getAddress_complement());
       dbUser.setCity(userToUpdate.getCity());
       dbUser.setEmail(userToUpdate.getEmail());
       dbUser.setUsername(userToUpdate.getUsername());

       if (userToUpdate.getPassword() != null && !userToUpdate.getPassword().isEmpty()) {
           dbUser.setPassword(passwordEncoder.encode(userToUpdate.getPassword()));
       }

        return this.userRepository.save(dbUser);
    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
        this.validateChangeableId(id, "deleted");
        User dbUser = this.findUserById(id);
        if (dbUser == null) {
            throw new NotFoundException();
        }
        this.userRepository.delete(dbUser);
    }

    @Override
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Authentication: " + authentication);
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            System.out.println("UserPrincipal: " + userPrincipal);
            User user = findByUsername(userPrincipal.getUsername());
            System.out.println("User retrieved: " + user);
            return user;
        }
        throw new UsernameNotFoundException("User not found");
    }

    @Override
    public User getUserById(Long id) {
        User user = new User();
        userRepository.findById(id);
        return user;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findOptionalByUsername(String username) {
        User user = new User();
        userRepository.findByUsername(username);
        return Optional.of(user);
    }

    private void validateChangeableId(Long id, String operation) {
        if (UNCHANGEABLE_USER_ID.equals(id)) {
            throw new BusinessException("User with ID %d can not be %s.".formatted(UNCHANGEABLE_USER_ID, operation));
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found by username: " + username);
        }
        return (UserDetails) user;
    }
}
