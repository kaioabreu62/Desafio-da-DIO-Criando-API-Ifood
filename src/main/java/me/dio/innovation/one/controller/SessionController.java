package me.dio.innovation.one.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.dio.innovation.one.controller.dto.AuthenticationSession;
import me.dio.innovation.one.controller.dto.Session;
import me.dio.innovation.one.domain.model.User;
import me.dio.innovation.one.domain.repository.UserRepository;
import me.dio.innovation.one.security.JWTCreator;
import me.dio.innovation.one.security.JWTObject;
import me.dio.innovation.one.security.SecurityConfig;
import me.dio.innovation.one.service.exception.AuthenticationSessionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/sessions")
@Tag(name = "Session Controller", description = "RESTful API for user authentication and session management")
public class SessionController {

   @Autowired
   private PasswordEncoder passwordEncoder;

   @Autowired
   private SecurityConfig securityConfig;

   @Autowired
   private UserRepository userRepository;


    @PostMapping("/login")
    @Operation(summary = "Authenticate user", description = "Authenticate user by username and password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User authenticated successfully",
                        content = @Content(mediaType = "application/json",
                        examples = @ExampleObject(value = "{ \"login\": \"user\", \"token\": \"Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...\" }"))),
            @ApiResponse(responseCode = "400", description = "Invalid login request"),
            @ApiResponse(responseCode = "401", description = "Authentication failed"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<AuthenticationSession> authenticate(@RequestBody @io.swagger.v3.oas.annotations.media.Schema(example = "{ \"username\": \"user\", \"password\": \"pass\" }")
                                                                  Session sessionRequest) {
        // Verifique se o username é fornecido
        if (sessionRequest.getUsername() == null || sessionRequest.getPassword() == null) {
            throw new AuthenticationSessionException("Username and password must not be null");
        }
        try {
            Optional<User> userOptional = Optional.ofNullable(userRepository.findByUsername(sessionRequest.getUsername()));
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                boolean passwordOk = passwordEncoder.matches(sessionRequest.getPassword(), user.getPassword());
                if (passwordOk) {
                    AuthenticationSession authSession = new AuthenticationSession();
                    authSession.setLogin(user.getUsername());

                    JWTObject jwtObject = new JWTObject();
                    jwtObject.setSubject(user.getUsername());
                    jwtObject.setIssuedAt(new Date(System.currentTimeMillis()));
                    jwtObject.setExpiration(new Date(System.currentTimeMillis() + securityConfig.expiration));
                    ;
                    jwtObject.setRoles(user.getRoles());

                    authSession.setToken(JWTCreator.create(securityConfig.getPrefix(), securityConfig.getKey(), jwtObject));

                    return ResponseEntity.ok(authSession);
                } else {
                    throw new AuthenticationSessionException("Invalid password");
                }
            } else {
                throw new AuthenticationSessionException("User not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
}
