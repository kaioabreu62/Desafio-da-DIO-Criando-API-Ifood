package me.dio.innovation.one.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.dio.innovation.one.controller.dto.UserDto;
import me.dio.innovation.one.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/users")
@Tag(name = "Users Controller", description = "RESTful API for managing users")
public record UserController(UserService userService) {

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/getAll")
    @Operation(summary = "Get all users", description = "Retrieve a list of all registered users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation successful")
    })
    public ResponseEntity<List<UserDto>> findAll() {
        var users = userService.findAllUsers();
        var usersDto = users.stream().map(UserDto::new).toList();
        return ResponseEntity.ok(usersDto);
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("get/{id}")
    @Operation(summary = "Get a user by ID", description = "Retrieve a specific user based on its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation successful"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserDto> findById(@PathVariable Long id) {
        var user = userService.findUserById(id);
        return ResponseEntity.ok(new UserDto(user));
    }

    @PreAuthorize("hasAnyRole('USER', 'MANAGER')")
    @PostMapping("/create")
    @Operation(summary = "Create a new user", description = "Create a new user and return the created user´s data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "422", description = "Invalid user data provided")
    })
    public ResponseEntity<UserDto> create(@RequestBody UserDto userDto) {
        System.out.println("Creating user with data: " + userDto);
        var user = userService.createUser(userDto.toModel());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("create/{id}")
                .buildAndExpand(user.getId())
                .toUri();
        return ResponseEntity.created(location).body(new UserDto(user));
    }

    @PreAuthorize("hasAuthority('MANAGER') or @userSecurity.hasAccessToUpdateOrDelete(#id)")
    @PutMapping("update/{id}")
    @Operation(summary = "Update a user", description = "Update the data of an existing user based on its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "422", description = "Invalid user data provided")
    })
    public ResponseEntity<UserDto> update(@PathVariable Long id, @RequestBody UserDto userDto) {
        var user = userService.updateUser(id, userDto.toModel());
        return ResponseEntity.ok(new UserDto(user));
    }

    @PreAuthorize("hasAuthority('MANAGER') or @userSecurity.hasAccessToUpdateOrDelete(#id)")
    @DeleteMapping("delete/{id}")
    @Operation(summary = "Delete a user", description = "Delete an existing user based on its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
