package com.human.graduateproject.repository;

import com.human.graduateproject.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
    @Query("SELECT c.id FROM Category c WHERE LOWER(TRIM(c.name)) = :name")
    Optional<Long> findIdByNameIgnoreCase(@Param("name") String name);
}
