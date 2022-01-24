package com.landingis.api.storage.repository;

import com.landingis.api.storage.model.Customer;
import com.landingis.api.storage.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface EmployeeRepository extends JpaRepository<Employee,Long>, JpaSpecificationExecutor<Employee> {
    @Query("SELECT e FROM Employee e WHERE e.account.id = ?1")
    Employee findByAccountId(Long id);
}
