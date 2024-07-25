package me.dio.innovation.one.controller.dto;

import me.dio.innovation.one.domain.model.User;

public record UserDto(
        Long id,
        String name,
        String phone_number,
        String cep,
        String address,
        String neighborhood,
        String address_complement,
        String city,
        String email,
        String username,
        String password) {

    public UserDto(User model) {
        this(
                model.getId(),
                model.getName(),
                model.getPhone_number(),
                model.getCep(),
                model.getAddress(),
                model.getNeighborhood(),
                model.getAddress_complement(),
                model.getCity(),
                model.getEmail(),
                model.getUsername(),
                model.getPassword()
        );
    }

    public User toModel() {
        User model = new User();

        if (this.id != null) {
            model.setId(this.id);
        }
        model.setName(this.name);
        model.setPhone_number(this.phone_number);
        model.setCep(this.cep);
        model.setAddress(this.address);
        model.setNeighborhood(this.neighborhood);
        model.setAddress_complement(this.address_complement);
        model.setCity(this.city);
        model.setEmail(this.email);
        model.setUsername(this.username);
        model.setPassword(this.password);

        return model;
    }
}
