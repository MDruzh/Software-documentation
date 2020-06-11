package com.druzhbiak.netflix.repository;

import com.druzhbiak.netflix.domain.NetflixUser;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the NetflixUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NetflixUserRepository extends JpaRepository<NetflixUser, Long>, JpaSpecificationExecutor<NetflixUser> {
}
