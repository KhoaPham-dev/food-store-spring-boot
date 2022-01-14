package com.landingis.api.storage.repository;

import com.landingis.api.storage.model.Category;
import com.landingis.api.storage.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CustomerRepository extends JpaRepository<Customer,Long> , JpaSpecificationExecutor<Customer> {
}
