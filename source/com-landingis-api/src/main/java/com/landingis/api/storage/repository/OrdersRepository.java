package com.landingis.api.storage.repository;

import com.landingis.api.storage.model.News;
import com.landingis.api.storage.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface OrdersRepository extends JpaRepository<Orders, Long>, JpaSpecificationExecutor<Orders> {
    @Query("SELECT sum(o.totalMoney) FROM Orders o")
    Double sumMoney();

    @Query("SELECT sum(o.totalMoney) FROM Orders o WHERE o.employee.account.id = ?1")
    Double sumMoneyByEmployeeAccountId(Long id);

    @Query("SELECT sum(o.totalMoney) FROM Orders o WHERE o.collaborator.account.id = ?1")
    Double sumMoneyByCollaboratorAccountId(Long id);
}
