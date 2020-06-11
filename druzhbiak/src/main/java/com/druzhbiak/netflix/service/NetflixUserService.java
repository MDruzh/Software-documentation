package com.druzhbiak.netflix.service;

import com.druzhbiak.netflix.domain.NetflixUser;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link NetflixUser}.
 */
public interface NetflixUserService {

    /**
     * Save a netflixUser.
     *
     * @param netflixUser the entity to save.
     * @return the persisted entity.
     */
    NetflixUser save(NetflixUser netflixUser);

    /**
     * Get all the netflixUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<NetflixUser> findAll(Pageable pageable);

    /**
     * Get the "id" netflixUser.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<NetflixUser> findOne(Long id);

    /**
     * Delete the "id" netflixUser.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
