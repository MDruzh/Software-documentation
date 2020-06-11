package com.druzhbiak.netflix.repository;

import com.druzhbiak.netflix.domain.Episode;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Episode entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EpisodeRepository extends JpaRepository<Episode, Long>, JpaSpecificationExecutor<Episode> {
}
