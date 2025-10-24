package com.human.graduateproject.repository;

import com.human.graduateproject.entity.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PointRepository extends JpaRepository<Point,Long> {
    Optional<Point> findByCustomerId(Long customerId);
}
