package com.landingis.api.storage.repository;

import com.landingis.api.storage.model.Province;
import com.landingis.api.storage.model.Settings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface SettingsRepository extends JpaRepository<Settings, Long>, JpaSpecificationExecutor<Settings> {
    @Query("SELECT s FROM Settings s WHERE s.key = ?1")
    Settings findByKey(String key);
}
