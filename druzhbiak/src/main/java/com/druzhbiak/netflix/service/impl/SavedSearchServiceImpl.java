package com.druzhbiak.netflix.service.impl;

import com.druzhbiak.netflix.service.SavedSearchService;
import com.druzhbiak.netflix.domain.SavedSearch;
import com.druzhbiak.netflix.repository.SavedSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link SavedSearch}.
 */
@Service
@Transactional
public class SavedSearchServiceImpl implements SavedSearchService {

    private final Logger log = LoggerFactory.getLogger(SavedSearchServiceImpl.class);

    private final SavedSearchRepository savedSearchRepository;

    public SavedSearchServiceImpl(SavedSearchRepository savedSearchRepository) {
        this.savedSearchRepository = savedSearchRepository;
    }

    /**
     * Save a savedSearch.
     *
     * @param savedSearch the entity to save.
     * @return the persisted entity.
     */
    @Override
    public SavedSearch save(SavedSearch savedSearch) {
        log.debug("Request to save SavedSearch : {}", savedSearch);
        return savedSearchRepository.save(savedSearch);
    }

    /**
     * Get all the savedSearches.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SavedSearch> findAll(Pageable pageable) {
        log.debug("Request to get all SavedSearches");
        return savedSearchRepository.findAll(pageable);
    }

    /**
     * Get one savedSearch by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<SavedSearch> findOne(Long id) {
        log.debug("Request to get SavedSearch : {}", id);
        return savedSearchRepository.findById(id);
    }

    /**
     * Delete the savedSearch by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete SavedSearch : {}", id);
        savedSearchRepository.deleteById(id);
    }
}
