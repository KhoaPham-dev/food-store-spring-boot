package com.landingis.api.storage.repository;

import com.landingis.api.storage.model.Category;
import com.landingis.api.storage.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface CustomerRepository extends JpaRepository<Customer,Long> , JpaSpecificationExecutor<Customer> {
    @Query("SELECT c FROM Customer c WHERE c.account.phone = ?1")
    Customer findByPhone(String phone);
}
