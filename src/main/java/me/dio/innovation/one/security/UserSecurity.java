package me.dio.innovation.one.security;

import me.dio.innovation.one.domain.model.User;
import me.dio.innovation.one.domain.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("userSecurity")
public class UserSecurity {

    private static final Logger logger = LoggerFactory.getLogger(UserSecurity.class);

    @Autowired
    private UserRepository userRepository;

    public boolean hasAccessToUpdateOrDelete(Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        logger.info("Authenticated user: {}", userPrincipal.getUsername());

        Optional<User> userOptional = Optional.ofNullable(userRepository.findByUsername(userPrincipal.getUsername()));
        User user = userOptional.get();

        logger.info("User ID: {}", user.getId());
        logger.info("Requested user ID: {}", userId);

        return user.getId().equals(userId) || userPrincipal.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_MANAGER"));
     }
}
