package com.druzhbiak.netflix.web.rest;

import com.druzhbiak.netflix.domain.MyList;
import com.druzhbiak.netflix.service.MyListService;
import com.druzhbiak.netflix.web.rest.errors.BadRequestAlertException;
import com.druzhbiak.netflix.service.dto.MyListCriteria;
import com.druzhbiak.netflix.service.MyListQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

/**
 * REST controller for managing {@link com.druzhbiak.netflix.domain.MyList}.
 */
@RestController
@RequestMapping("/api")
public class MyListResource {

    private final Logger log = LoggerFactory.getLogger(MyListResource.class);

    private static final String ENTITY_NAME = "myList";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MyListService myListService;

    private final MyListQueryService myListQueryService;

    public MyListResource(MyListService myListService, MyListQueryService myListQueryService) {
        this.myListService = myListService;
        this.myListQueryService = myListQueryService;
    }

    /**
     * {@code POST  /my-lists} : Create a new myList.
     *
     * @param myList the myList to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new myList, or with status {@code 400 (Bad Request)} if the myList has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/my-lists")
    public ResponseEntity<MyList> createMyList(@RequestBody MyList myList) throws URISyntaxException {
        log.debug("REST request to save MyList : {}", myList);
        if (myList.getId() != null) {
            throw new BadRequestAlertException("A new myList cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MyList result = myListService.save(myList);
        return ResponseEntity.created(new URI("/api/my-lists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /my-lists} : Updates an existing myList.
     *
     * @param myList the myList to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated myList,
     * or with status {@code 400 (Bad Request)} if the myList is not valid,
     * or with status {@code 500 (Internal Server Error)} if the myList couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/my-lists")
    public ResponseEntity<MyList> updateMyList(@RequestBody MyList myList) throws URISyntaxException {
        log.debug("REST request to update MyList : {}", myList);
        if (myList.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MyList result = myListService.save(myList);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, myList.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /my-lists} : get all the myLists.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of myLists in body.
     */
    @GetMapping("/my-lists")
    public ResponseEntity<List<MyList>> getAllMyLists(MyListCriteria criteria, Pageable pageable) {
        log.debug("REST request to get MyLists by criteria: {}", criteria);
        Page<MyList> page = myListQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /my-lists/count} : count all the myLists.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/my-lists/count")
    public ResponseEntity<Long> countMyLists(MyListCriteria criteria) {
        log.debug("REST request to count MyLists by criteria: {}", criteria);
        return ResponseEntity.ok().body(myListQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /my-lists/:id} : get the "id" myList.
     *
     * @param id the id of the myList to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the myList, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/my-lists/{id}")
    public ResponseEntity<MyList> getMyList(@PathVariable Long id) {
        log.debug("REST request to get MyList : {}", id);
        Optional<MyList> myList = myListService.findOne(id);
        return ResponseUtil.wrapOrNotFound(myList);
    }

    /**
     * {@code DELETE  /my-lists/:id} : delete the "id" myList.
     *
     * @param id the id of the myList to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/my-lists/{id}")
    public ResponseEntity<Void> deleteMyList(@PathVariable Long id) {
        log.debug("REST request to delete MyList : {}", id);
        myListService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
