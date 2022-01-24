package com.landingis.api.storage.repository;

import com.landingis.api.storage.model.News;
import com.landingis.api.storage.model.Orders;
import com.landingis.api.storage.projection.OrdersCollaborator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders, Long>, JpaSpecificationExecutor<Orders> {
    @Query("SELECT sum(o.totalMoney) FROM Orders o")
    Double sumMoney();

    @Query("SELECT sum(o.totalMoney) FROM Orders o WHERE o.employee.account.id = ?1")
    Double sumMoneyByEmployeeAccountId(Long id);

    @Query("SELECT sum(o.totalMoney) FROM Orders o WHERE o.collaborator.account.id = ?1")
    Double sumMoneyByCollaboratorAccountId(Long id);


    @Query("SELECT o.id as id, o.totalMoney as totalMoney, o.state as state, o.code as code, o.createdDate as createdDate, SUM(d.collaboratorCommission) as totalCollaboratorCommission, cs.account.fullName as customerFullName" +
            " FROM Orders o" +
            " JOIN Collaborator c ON c.id = o.collaborator.id" +
            " JOIN OrdersDetail d ON o.id = d.orders.id" +
            " JOIN Customer cs ON o.customer.Id = cs.Id" +
            " WHERE (o.createdDate is null OR o.createdDate >= ?1)" +
            " AND (o.createdDate is null OR o.createdDate <= ?2)" +
            " AND (o.state is null OR o.state = ?3)" +
            " AND (c.id = ?4)" +
            " GROUP BY o.id, cs.account.fullName")
    Page<OrdersCollaborator> getOrdersCollaboratorList(Date from, Date to,Integer state, Long id, Pageable pageable);
}//
//"
