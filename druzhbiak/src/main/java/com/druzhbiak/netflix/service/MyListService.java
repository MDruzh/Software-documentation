package com.druzhbiak.netflix.service;

import com.druzhbiak.netflix.domain.MyList;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link MyList}.
 */
public interface MyListService {

    /**
     * Save a myList.
     *
     * @param myList the entity to save.
     * @return the persisted entity.
     */
    MyList save(MyList myList);

    /**
     * Get all the myLists.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MyList> findAll(Pageable pageable);
    /**
     * Get all the MyListDTO where User is {@code null}.
     *
     * @return the list of entities.
     */
    List<MyList> findAllWhereUserIsNull();

    /**
     * Get the "id" myList.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MyList> findOne(Long id);

    /**
     * Delete the "id" myList.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
