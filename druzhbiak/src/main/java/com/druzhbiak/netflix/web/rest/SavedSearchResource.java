package com.druzhbiak.netflix.web.rest;

import com.druzhbiak.netflix.domain.SavedSearch;
import com.druzhbiak.netflix.service.SavedSearchService;
import com.druzhbiak.netflix.web.rest.errors.BadRequestAlertException;
import com.druzhbiak.netflix.service.dto.SavedSearchCriteria;
import com.druzhbiak.netflix.service.SavedSearchQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.druzhbiak.netflix.domain.SavedSearch}.
 */
@RestController
@RequestMapping("/api")
public class SavedSearchResource {

    private final Logger log = LoggerFactory.getLogger(SavedSearchResource.class);

    private static final String ENTITY_NAME = "savedSearch";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SavedSearchService savedSearchService;

    private final SavedSearchQueryService savedSearchQueryService;

    public SavedSearchResource(SavedSearchService savedSearchService, SavedSearchQueryService savedSearchQueryService) {
        this.savedSearchService = savedSearchService;
        this.savedSearchQueryService = savedSearchQueryService;
    }

    /**
     * {@code POST  /saved-searches} : Create a new savedSearch.
     *
     * @param savedSearch the savedSearch to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new savedSearch, or with status {@code 400 (Bad Request)} if the savedSearch has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/saved-searches")
    public ResponseEntity<SavedSearch> createSavedSearch(@RequestBody SavedSearch savedSearch) throws URISyntaxException {
        log.debug("REST request to save SavedSearch : {}", savedSearch);
        if (savedSearch.getId() != null) {
            throw new BadRequestAlertException("A new savedSearch cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SavedSearch result = savedSearchService.save(savedSearch);
        return ResponseEntity.created(new URI("/api/saved-searches/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /saved-searches} : Updates an existing savedSearch.
     *
     * @param savedSearch the savedSearch to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated savedSearch,
     * or with status {@code 400 (Bad Request)} if the savedSearch is not valid,
     * or with status {@code 500 (Internal Server Error)} if the savedSearch couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/saved-searches")
    public ResponseEntity<SavedSearch> updateSavedSearch(@RequestBody SavedSearch savedSearch) throws URISyntaxException {
        log.debug("REST request to update SavedSearch : {}", savedSearch);
        if (savedSearch.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SavedSearch result = savedSearchService.save(savedSearch);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, savedSearch.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /saved-searches} : get all the savedSearches.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of savedSearches in body.
     */
    @GetMapping("/saved-searches")
    public ResponseEntity<List<SavedSearch>> getAllSavedSearches(SavedSearchCriteria criteria, Pageable pageable) {
        log.debug("REST request to get SavedSearches by criteria: {}", criteria);
        Page<SavedSearch> page = savedSearchQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /saved-searches/count} : count all the savedSearches.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/saved-searches/count")
    public ResponseEntity<Long> countSavedSearches(SavedSearchCriteria criteria) {
        log.debug("REST request to count SavedSearches by criteria: {}", criteria);
        return ResponseEntity.ok().body(savedSearchQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /saved-searches/:id} : get the "id" savedSearch.
     *
     * @param id the id of the savedSearch to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the savedSearch, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/saved-searches/{id}")
    public ResponseEntity<SavedSearch> getSavedSearch(@PathVariable Long id) {
        log.debug("REST request to get SavedSearch : {}", id);
        Optional<SavedSearch> savedSearch = savedSearchService.findOne(id);
        return ResponseUtil.wrapOrNotFound(savedSearch);
    }

    /**
     * {@code DELETE  /saved-searches/:id} : delete the "id" savedSearch.
     *
     * @param id the id of the savedSearch to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/saved-searches/{id}")
    public ResponseEntity<Void> deleteSavedSearch(@PathVariable Long id) {
        log.debug("REST request to delete SavedSearch : {}", id);
        savedSearchService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
