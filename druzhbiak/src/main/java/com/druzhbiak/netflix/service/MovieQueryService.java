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

import com.druzhbiak.netflix.domain.Movie;
import com.druzhbiak.netflix.domain.*; // for static metamodels
import com.druzhbiak.netflix.repository.MovieRepository;
import com.druzhbiak.netflix.service.dto.MovieCriteria;

/**
 * Service for executing complex queries for {@link Movie} entities in the database.
 * The main input is a {@link MovieCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Movie} or a {@link Page} of {@link Movie} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MovieQueryService extends QueryService<Movie> {

    private final Logger log = LoggerFactory.getLogger(MovieQueryService.class);

    private final MovieRepository movieRepository;

    public MovieQueryService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    /**
     * Return a {@link List} of {@link Movie} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Movie> findByCriteria(MovieCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Movie> specification = createSpecification(criteria);
        return movieRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Movie} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Movie> findByCriteria(MovieCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Movie> specification = createSpecification(criteria);
        return movieRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MovieCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Movie> specification = createSpecification(criteria);
        return movieRepository.count(specification);
    }

    /**
     * Function to convert {@link MovieCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Movie> createSpecification(MovieCriteria criteria) {
        Specification<Movie> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Movie_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Movie_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Movie_.description));
            }
            if (criteria.getReleaseDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReleaseDate(), Movie_.releaseDate));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), Movie_.type));
            }
            if (criteria.getGenre() != null) {
                specification = specification.and(buildSpecification(criteria.getGenre(), Movie_.genre));
            }
            if (criteria.getCreator() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreator(), Movie_.creator));
            }
            if (criteria.getRating() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRating(), Movie_.rating));
            }
            if (criteria.getLink() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLink(), Movie_.link));
            }
            if (criteria.getAvailableInHD() != null) {
                specification = specification.and(buildSpecification(criteria.getAvailableInHD(), Movie_.availableInHD));
            }
            if (criteria.getEpisodesId() != null) {
                specification = specification.and(buildSpecification(criteria.getEpisodesId(),
                    root -> root.join(Movie_.episodes, JoinType.LEFT).get(Episode_.id)));
            }
            if (criteria.getMyListId() != null) {
                specification = specification.and(buildSpecification(criteria.getMyListId(),
                    root -> root.join(Movie_.myList, JoinType.LEFT).get(MyList_.id)));
            }
        }
        return specification;
    }
}
