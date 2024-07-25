package me.dio.innovation.one.domain.repository;

import me.dio.innovation.one.domain.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END " +
            "FROM Product p WHERE p.name = :name AND p.cup_size = :cup_size AND p.size = :size")
    boolean existsByNameAndSizeAndCupSize(
            @Param("name") String name,
            @Param("cup_size") String cup_size,
            @Param("size") String size
    );

}
