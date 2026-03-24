package com.example.bai5_qlsp.repository;

import com.example.bai5_qlsp.model.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {

    @Query("SELECT DISTINCT o FROM CustomerOrder o LEFT JOIN FETCH o.details d LEFT JOIN FETCH d.product WHERE o.id = :id")
    Optional<CustomerOrder> findDetailedById(@Param("id") Long id);
}
