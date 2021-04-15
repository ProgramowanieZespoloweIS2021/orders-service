package com.eszop.ordersservice.orders.repository;

import com.eszop.ordersservice.orders.orm.OrderOrm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaOrderRepository extends JpaRepository<OrderOrm, Long> {

}
