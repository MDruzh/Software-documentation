package com.druzhbiak.netflix.service.impl;

import com.druzhbiak.netflix.service.NetflixUserService;
import com.druzhbiak.netflix.domain.NetflixUser;
import com.druzhbiak.netflix.repository.NetflixUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link NetflixUser}.
 */
@Service
@Transactional
public class NetflixUserServiceImpl implements NetflixUserService {

    private final Logger log = LoggerFactory.getLogger(NetflixUserServiceImpl.class);

    private final NetflixUserRepository netflixUserRepository;

    public NetflixUserServiceImpl(NetflixUserRepository netflixUserRepository) {
        this.netflixUserRepository = netflixUserRepository;
    }

    /**
     * Save a netflixUser.
     *
     * @param netflixUser the entity to save.
     * @return the persisted entity.
     */
    @Override
    public NetflixUser save(NetflixUser netflixUser) {
        log.debug("Request to save NetflixUser : {}", netflixUser);
        return netflixUserRepository.save(netflixUser);
    }

    /**
     * Get all the netflixUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<NetflixUser> findAll(Pageable pageable) {
        log.debug("Request to get all NetflixUsers");
        return netflixUserRepository.findAll(pageable);
    }

    /**
     * Get one netflixUser by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<NetflixUser> findOne(Long id) {
        log.debug("Request to get NetflixUser : {}", id);
        return netflixUserRepository.findById(id);
    }

    /**
     * Delete the netflixUser by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete NetflixUser : {}", id);
        netflixUserRepository.deleteById(id);
    }
}
