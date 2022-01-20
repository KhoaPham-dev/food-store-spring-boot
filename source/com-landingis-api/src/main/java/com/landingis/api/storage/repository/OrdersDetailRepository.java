package com.landingis.api.storage.repository;

import com.landingis.api.storage.model.Orders;
import com.landingis.api.storage.model.OrdersDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrdersDetailRepository extends JpaRepository<OrdersDetail, Long>, JpaSpecificationExecutor<OrdersDetail> {
    @Query("SELECT o FROM OrdersDetail o WHERE o.orders.id = ?1")
    List<OrdersDetail> findAllByOrderId(Long id);

    @Query("SELECT COUNT(o) FROM OrdersDetail o WHERE o.orders.id = ?1")
    int countByOrdersId(Long id);

    @Query("SELECT o FROM OrdersDetail o WHERE o.orders.id = ?2 AND o.product.id = ?1")
    OrdersDetail findByProductIdAndOrdersId(Long productId, Long ordersId);
}
