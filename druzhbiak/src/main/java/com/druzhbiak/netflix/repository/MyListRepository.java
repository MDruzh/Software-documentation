package com.druzhbiak.netflix.repository;

import com.druzhbiak.netflix.domain.MyList;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the MyList entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MyListRepository extends JpaRepository<MyList, Long>, JpaSpecificationExecutor<MyList> {
}
