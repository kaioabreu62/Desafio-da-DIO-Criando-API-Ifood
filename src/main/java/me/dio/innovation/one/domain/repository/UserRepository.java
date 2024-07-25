package me.dio.innovation.one.domain.repository;

import me.dio.innovation.one.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email); // Checar se o email existe

    boolean existsByUsername(String username); // Checar se o username existe

    Optional<User> findById(Long userId);

    @Query("SELECT e FROM User e JOIN FETCH e.roles WHERE e.username= (:username)")
    User findByUsername(@Param("username") String username); // Pesquisa pelo username para o login de sessão
}
