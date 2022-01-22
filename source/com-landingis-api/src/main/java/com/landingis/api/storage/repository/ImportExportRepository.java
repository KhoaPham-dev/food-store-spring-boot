package com.landingis.api.storage.repository;

import com.landingis.api.storage.model.Collaborator;
import com.landingis.api.storage.model.ImportExport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

public interface ImportExportRepository extends JpaRepository<ImportExport, Long>, JpaSpecificationExecutor<ImportExport> {

    @Query("SELECT SUM(i.money) FROM ImportExport i WHERE i.kind = ?1 AND i.createdDate BETWEEN ?2 AND ?3")
    Double sumMoneyByKindAndFromAndTo(Integer kind, Date from, Date to);

    @Query("SELECT SUM(i.money) FROM ImportExport i WHERE i.kind = ?1 AND i.createdDate >= ?2")
    Double sumMoneyByKindAndFrom(Integer kind, Date from);

    @Query("SELECT SUM(i.money) FROM ImportExport i WHERE i.kind = ?1 AND i.createdDate <= ?2")
    Double sumMoneyByKindAndTo(Integer kind, Date to);

    @Query("SELECT SUM(i.money) FROM ImportExport i WHERE i.kind = ?1")
    Double sumMoneyByKind(Integer kind);

}
