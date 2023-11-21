package com.kjms.repository;

import com.kjms.domain.EntitySiteConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EntitySiteConfigRepository extends JpaRepository<EntitySiteConfig, String> {
}
