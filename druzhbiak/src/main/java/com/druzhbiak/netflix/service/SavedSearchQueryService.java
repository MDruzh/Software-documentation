package com.druzhbiak.netflix.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.druzhbiak.netflix.domain.SavedSearch;
import com.druzhbiak.netflix.domain.*; // for static metamodels
import com.druzhbiak.netflix.repository.SavedSearchRepository;
import com.druzhbiak.netflix.service.dto.SavedSearchCriteria;

/**
 * Service for executing complex queries for {@link SavedSearch} entities in the database.
 * The main input is a {@link SavedSearchCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SavedSearch} or a {@link Page} of {@link SavedSearch} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SavedSearchQueryService extends QueryService<SavedSearch> {

    private final Logger log = LoggerFactory.getLogger(SavedSearchQueryService.class);

    private final SavedSearchRepository savedSearchRepository;

    public SavedSearchQueryService(SavedSearchRepository savedSearchRepository) {
        this.savedSearchRepository = savedSearchRepository;
    }

    /**
     * Return a {@link List} of {@link SavedSearch} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SavedSearch> findByCriteria(SavedSearchCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SavedSearch> specification = createSpecification(criteria);
        return savedSearchRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link SavedSearch} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SavedSearch> findByCriteria(SavedSearchCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SavedSearch> specification = createSpecification(criteria);
        return savedSearchRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SavedSearchCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SavedSearch> specification = createSpecification(criteria);
        return savedSearchRepository.count(specification);
    }

    /**
     * Function to convert {@link SavedSearchCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SavedSearch> createSpecification(SavedSearchCriteria criteria) {
        Specification<SavedSearch> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SavedSearch_.id));
            }
            if (criteria.getSearchText() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSearchText(), SavedSearch_.searchText));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserId(),
                    root -> root.join(SavedSearch_.user, JoinType.LEFT).get(NetflixUser_.id)));
            }
        }
        return specification;
    }
}
