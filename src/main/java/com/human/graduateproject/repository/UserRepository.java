package com.human.graduateproject.repository;

import com.human.graduateproject.entity.Users;
import com.human.graduateproject.enums.UserRole;
import com.human.graduateproject.payload.DeliveryStaffView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users,Long> {
    Optional<Users> findByEmail(String email);

    @Query("SELECT u.name FROM Users u WHERE u.id = :id")
    Optional<String> findNameById(@Param("id") Long id);


    @Query("SELECT u.id AS id, u.name AS name FROM Users u WHERE u.role = :role")
    List<DeliveryStaffView> findDeliveryStaffSummaryByRole(@Param("role") UserRole role);

    boolean existsByEmail(String email);

    boolean existsByRole(UserRole userRole);
}
