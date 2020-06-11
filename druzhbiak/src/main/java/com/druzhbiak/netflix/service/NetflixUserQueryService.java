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

import com.druzhbiak.netflix.domain.NetflixUser;
import com.druzhbiak.netflix.domain.*; // for static metamodels
import com.druzhbiak.netflix.repository.NetflixUserRepository;
import com.druzhbiak.netflix.service.dto.NetflixUserCriteria;

/**
 * Service for executing complex queries for {@link NetflixUser} entities in the database.
 * The main input is a {@link NetflixUserCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link NetflixUser} or a {@link Page} of {@link NetflixUser} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NetflixUserQueryService extends QueryService<NetflixUser> {

    private final Logger log = LoggerFactory.getLogger(NetflixUserQueryService.class);

    private final NetflixUserRepository netflixUserRepository;

    public NetflixUserQueryService(NetflixUserRepository netflixUserRepository) {
        this.netflixUserRepository = netflixUserRepository;
    }

    /**
     * Return a {@link List} of {@link NetflixUser} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<NetflixUser> findByCriteria(NetflixUserCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<NetflixUser> specification = createSpecification(criteria);
        return netflixUserRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link NetflixUser} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NetflixUser> findByCriteria(NetflixUserCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NetflixUser> specification = createSpecification(criteria);
        return netflixUserRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NetflixUserCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<NetflixUser> specification = createSpecification(criteria);
        return netflixUserRepository.count(specification);
    }

    /**
     * Function to convert {@link NetflixUserCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NetflixUser> createSpecification(NetflixUserCriteria criteria) {
        Specification<NetflixUser> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NetflixUser_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), NetflixUser_.name));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), NetflixUser_.email));
            }
            if (criteria.getPassword() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPassword(), NetflixUser_.password));
            }
            if (criteria.getBio() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBio(), NetflixUser_.bio));
            }
            if (criteria.getCategory() != null) {
                specification = specification.and(buildSpecification(criteria.getCategory(), NetflixUser_.category));
            }
            if (criteria.getBirthDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBirthDate(), NetflixUser_.birthDate));
            }
            if (criteria.getListId() != null) {
                specification = specification.and(buildSpecification(criteria.getListId(),
                    root -> root.join(NetflixUser_.list, JoinType.LEFT).get(MyList_.id)));
            }
            if (criteria.getSavedSearchesId() != null) {
                specification = specification.and(buildSpecification(criteria.getSavedSearchesId(),
                    root -> root.join(NetflixUser_.savedSearches, JoinType.LEFT).get(SavedSearch_.id)));
            }
        }
        return specification;
    }
}
