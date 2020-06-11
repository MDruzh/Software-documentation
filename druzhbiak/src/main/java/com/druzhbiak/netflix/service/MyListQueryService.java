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

import com.druzhbiak.netflix.domain.MyList;
import com.druzhbiak.netflix.domain.*; // for static metamodels
import com.druzhbiak.netflix.repository.MyListRepository;
import com.druzhbiak.netflix.service.dto.MyListCriteria;

/**
 * Service for executing complex queries for {@link MyList} entities in the database.
 * The main input is a {@link MyListCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MyList} or a {@link Page} of {@link MyList} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MyListQueryService extends QueryService<MyList> {

    private final Logger log = LoggerFactory.getLogger(MyListQueryService.class);

    private final MyListRepository myListRepository;

    public MyListQueryService(MyListRepository myListRepository) {
        this.myListRepository = myListRepository;
    }

    /**
     * Return a {@link List} of {@link MyList} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MyList> findByCriteria(MyListCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<MyList> specification = createSpecification(criteria);
        return myListRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link MyList} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MyList> findByCriteria(MyListCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MyList> specification = createSpecification(criteria);
        return myListRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MyListCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<MyList> specification = createSpecification(criteria);
        return myListRepository.count(specification);
    }

    /**
     * Function to convert {@link MyListCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MyList> createSpecification(MyListCriteria criteria) {
        Specification<MyList> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), MyList_.id));
            }
            if (criteria.getMoviesId() != null) {
                specification = specification.and(buildSpecification(criteria.getMoviesId(),
                    root -> root.join(MyList_.movies, JoinType.LEFT).get(Movie_.id)));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserId(),
                    root -> root.join(MyList_.user, JoinType.LEFT).get(NetflixUser_.id)));
            }
        }
        return specification;
    }
}
