package com.ffpalu.concessionaria.repository;

import com.ffpalu.concessionaria.dto.support.SellerMonthlyRevenue;
import com.ffpalu.concessionaria.entity.Sale;
import com.ffpalu.concessionaria.entity.Seller;
import com.ffpalu.concessionaria.entity.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SaleRepository extends JpaRepository<Sale, UUID> {
    boolean existsByVehicleId(UUID vehicle);

    Page<Sale> findBySellerId(UUID seller, Pageable pageable);


    @Query("""
        SELECT new com.ffpalu.concessionaria.dto.support.SellerMonthlyRevenue(
            s.seller.id,
            s.seller.employeeCode,
            s.seller.user.firstName,
            s.seller.user.lastName,
            SUM(s.price)
        )
        FROM Sale s
        WHERE MONTH(s.sellDate) = :month AND YEAR(s.sellDate) = :year
        GROUP BY s.sellerId, s.seller.employeeCode, s.seller.user.firstName, s.seller.user.lastName
""")
    List<SellerMonthlyRevenue> findMonthlyRevenueBySeller(
            @Param("month") int month,
            @Param("year") int year
    );
}
