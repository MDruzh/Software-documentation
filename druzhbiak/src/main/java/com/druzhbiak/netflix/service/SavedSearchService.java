package com.druzhbiak.netflix.service;

import com.druzhbiak.netflix.domain.SavedSearch;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link SavedSearch}.
 */
public interface SavedSearchService {

    /**
     * Save a savedSearch.
     *
     * @param savedSearch the entity to save.
     * @return the persisted entity.
     */
    SavedSearch save(SavedSearch savedSearch);

    /**
     * Get all the savedSearches.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SavedSearch> findAll(Pageable pageable);

    /**
     * Get the "id" savedSearch.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SavedSearch> findOne(Long id);

    /**
     * Delete the "id" savedSearch.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
