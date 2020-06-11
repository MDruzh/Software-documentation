package com.druzhbiak.netflix.repository;

import com.druzhbiak.netflix.domain.SavedSearch;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the SavedSearch entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SavedSearchRepository extends JpaRepository<SavedSearch, Long>, JpaSpecificationExecutor<SavedSearch> {
}
