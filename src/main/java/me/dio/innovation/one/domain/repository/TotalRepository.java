package me.dio.innovation.one.domain.repository;

import me.dio.innovation.one.domain.model.Total;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TotalRepository extends JpaRepository<Total, Long> {
}
