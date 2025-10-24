package com.human.graduateproject.repository;

import com.human.graduateproject.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice,Long> {

    boolean existsByOrderId(Long orderId);

    // Tổng doanh thu theo khoảng thời gian
    @Query("SELECT SUM(i.totalAmount) FROM Invoice i WHERE i.issueDate BETWEEN :startDate AND :endDate AND i.isPaid = true")
    BigDecimal getTotalRevenueBetweenDates(@Param("startDate") LocalDateTime startDate,
                                           @Param("endDate") LocalDateTime endDate);

    // Doanh thu theo ngày
    @Query("SELECT DATE(i.issueDate) as date, SUM(i.totalAmount) as total " +
            "FROM Invoice i " +
            "WHERE i.issueDate BETWEEN :startDate AND :endDate AND i.isPaid = true " +
            "GROUP BY DATE(i.issueDate) " +
            "ORDER BY DATE(i.issueDate)")
    List<Object[]> getDailyRevenueBetweenDates(@Param("startDate") LocalDateTime startDate,
                                               @Param("endDate") LocalDateTime endDate);

    // Doanh thu theo tháng
    @Query("SELECT FUNCTION('YEAR', i.issueDate) as year, FUNCTION('MONTH', i.issueDate) as month, SUM(i.totalAmount) as total " +
            "FROM Invoice i " +
            "WHERE i.issueDate BETWEEN :startDate AND :endDate AND i.isPaid = true " +
            "GROUP BY FUNCTION('YEAR', i.issueDate), FUNCTION('MONTH', i.issueDate) " +
            "ORDER BY FUNCTION('YEAR', i.issueDate), FUNCTION('MONTH', i.issueDate)")
    List<Object[]> getMonthlyRevenueBetweenDates(@Param("startDate") LocalDateTime startDate,
                                                 @Param("endDate") LocalDateTime endDate);

    // Doanh thu theo phương thức thanh toán
    @Query("SELECT i.paymentMethod, SUM(i.totalAmount) as total " +
            "FROM Invoice i " +
            "WHERE i.issueDate BETWEEN :startDate AND :endDate AND i.isPaid = true " +
            "GROUP BY i.paymentMethod")
    List<Object[]> getRevenueByPaymentMethodBetweenDates(@Param("startDate") LocalDateTime startDate,
                                                         @Param("endDate") LocalDateTime endDate);

}
