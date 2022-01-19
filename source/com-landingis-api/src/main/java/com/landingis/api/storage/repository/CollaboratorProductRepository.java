package com.landingis.api.storage.repository;

import com.landingis.api.storage.model.Collaborator;
import com.landingis.api.storage.model.CollaboratorProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface CollaboratorProductRepository extends JpaRepository<CollaboratorProduct, Long>, JpaSpecificationExecutor<CollaboratorProduct> {

    @Query("SELECT c FROM CollaboratorProduct c WHERE  c.collaborator.id = ?1 AND c.product.id = ?2")
    CollaboratorProduct findByCollaboratorIdAndProductId(Long collaboratorId, Long productId);
}
