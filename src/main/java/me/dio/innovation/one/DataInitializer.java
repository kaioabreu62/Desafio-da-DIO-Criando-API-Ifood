package me.dio.innovation.one;

import me.dio.innovation.one.domain.model.Role;
import me.dio.innovation.one.domain.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initializeData(RoleRepository roleRepository) {
        return args -> {
            if (!roleRepository.existsByName("MANAGER")) {
                roleRepository.save(new Role("MANAGER"));
                System.out.println("Saved role MANAGER");
            }
            if (!roleRepository.existsByName("USER")) {
                roleRepository.save(new Role("USER"));
                System.out.println("Saved role USER");
            }
        };
    }
}
