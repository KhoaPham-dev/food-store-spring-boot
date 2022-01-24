package com.landingis.api.storage.repository;

import com.landingis.api.storage.model.Collaborator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface CollaboratorRepository extends JpaRepository<Collaborator, Long>, JpaSpecificationExecutor<Collaborator> {
    @Query("SELECT c FROM Collaborator c WHERE c.account.id = ?1")
    Collaborator findByAccountId(Long id);
}
