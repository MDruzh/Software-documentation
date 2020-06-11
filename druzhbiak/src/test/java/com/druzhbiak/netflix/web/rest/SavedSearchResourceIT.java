package com.druzhbiak.netflix.web.rest;

import com.druzhbiak.netflix.NetflixApp;
import com.druzhbiak.netflix.domain.SavedSearch;
import com.druzhbiak.netflix.domain.NetflixUser;
import com.druzhbiak.netflix.repository.SavedSearchRepository;
import com.druzhbiak.netflix.service.SavedSearchService;
import com.druzhbiak.netflix.service.dto.SavedSearchCriteria;
import com.druzhbiak.netflix.service.SavedSearchQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link SavedSearchResource} REST controller.
 */
@SpringBootTest(classes = NetflixApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class SavedSearchResourceIT {

    private static final String DEFAULT_SEARCH_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_SEARCH_TEXT = "BBBBBBBBBB";

    @Autowired
    private SavedSearchRepository savedSearchRepository;

    @Autowired
    private SavedSearchService savedSearchService;

    @Autowired
    private SavedSearchQueryService savedSearchQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSavedSearchMockMvc;

    private SavedSearch savedSearch;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SavedSearch createEntity(EntityManager em) {
        SavedSearch savedSearch = new SavedSearch()
            .searchText(DEFAULT_SEARCH_TEXT);
        return savedSearch;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SavedSearch createUpdatedEntity(EntityManager em) {
        SavedSearch savedSearch = new SavedSearch()
            .searchText(UPDATED_SEARCH_TEXT);
        return savedSearch;
    }

    @BeforeEach
    public void initTest() {
        savedSearch = createEntity(em);
    }

    @Test
    @Transactional
    public void createSavedSearch() throws Exception {
        int databaseSizeBeforeCreate = savedSearchRepository.findAll().size();

        // Create the SavedSearch
        restSavedSearchMockMvc.perform(post("/api/saved-searches")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(savedSearch)))
            .andExpect(status().isCreated());

        // Validate the SavedSearch in the database
        List<SavedSearch> savedSearchList = savedSearchRepository.findAll();
        assertThat(savedSearchList).hasSize(databaseSizeBeforeCreate + 1);
        SavedSearch testSavedSearch = savedSearchList.get(savedSearchList.size() - 1);
        assertThat(testSavedSearch.getSearchText()).isEqualTo(DEFAULT_SEARCH_TEXT);
    }

    @Test
    @Transactional
    public void createSavedSearchWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = savedSearchRepository.findAll().size();

        // Create the SavedSearch with an existing ID
        savedSearch.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSavedSearchMockMvc.perform(post("/api/saved-searches")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(savedSearch)))
            .andExpect(status().isBadRequest());

        // Validate the SavedSearch in the database
        List<SavedSearch> savedSearchList = savedSearchRepository.findAll();
        assertThat(savedSearchList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllSavedSearches() throws Exception {
        // Initialize the database
        savedSearchRepository.saveAndFlush(savedSearch);

        // Get all the savedSearchList
        restSavedSearchMockMvc.perform(get("/api/saved-searches?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(savedSearch.getId().intValue())))
            .andExpect(jsonPath("$.[*].searchText").value(hasItem(DEFAULT_SEARCH_TEXT)));
    }
    
    @Test
    @Transactional
    public void getSavedSearch() throws Exception {
        // Initialize the database
        savedSearchRepository.saveAndFlush(savedSearch);

        // Get the savedSearch
        restSavedSearchMockMvc.perform(get("/api/saved-searches/{id}", savedSearch.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(savedSearch.getId().intValue()))
            .andExpect(jsonPath("$.searchText").value(DEFAULT_SEARCH_TEXT));
    }


    @Test
    @Transactional
    public void getSavedSearchesByIdFiltering() throws Exception {
        // Initialize the database
        savedSearchRepository.saveAndFlush(savedSearch);

        Long id = savedSearch.getId();

        defaultSavedSearchShouldBeFound("id.equals=" + id);
        defaultSavedSearchShouldNotBeFound("id.notEquals=" + id);

        defaultSavedSearchShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSavedSearchShouldNotBeFound("id.greaterThan=" + id);

        defaultSavedSearchShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSavedSearchShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllSavedSearchesBySearchTextIsEqualToSomething() throws Exception {
        // Initialize the database
        savedSearchRepository.saveAndFlush(savedSearch);

        // Get all the savedSearchList where searchText equals to DEFAULT_SEARCH_TEXT
        defaultSavedSearchShouldBeFound("searchText.equals=" + DEFAULT_SEARCH_TEXT);

        // Get all the savedSearchList where searchText equals to UPDATED_SEARCH_TEXT
        defaultSavedSearchShouldNotBeFound("searchText.equals=" + UPDATED_SEARCH_TEXT);
    }

    @Test
    @Transactional
    public void getAllSavedSearchesBySearchTextIsNotEqualToSomething() throws Exception {
        // Initialize the database
        savedSearchRepository.saveAndFlush(savedSearch);

        // Get all the savedSearchList where searchText not equals to DEFAULT_SEARCH_TEXT
        defaultSavedSearchShouldNotBeFound("searchText.notEquals=" + DEFAULT_SEARCH_TEXT);

        // Get all the savedSearchList where searchText not equals to UPDATED_SEARCH_TEXT
        defaultSavedSearchShouldBeFound("searchText.notEquals=" + UPDATED_SEARCH_TEXT);
    }

    @Test
    @Transactional
    public void getAllSavedSearchesBySearchTextIsInShouldWork() throws Exception {
        // Initialize the database
        savedSearchRepository.saveAndFlush(savedSearch);

        // Get all the savedSearchList where searchText in DEFAULT_SEARCH_TEXT or UPDATED_SEARCH_TEXT
        defaultSavedSearchShouldBeFound("searchText.in=" + DEFAULT_SEARCH_TEXT + "," + UPDATED_SEARCH_TEXT);

        // Get all the savedSearchList where searchText equals to UPDATED_SEARCH_TEXT
        defaultSavedSearchShouldNotBeFound("searchText.in=" + UPDATED_SEARCH_TEXT);
    }

    @Test
    @Transactional
    public void getAllSavedSearchesBySearchTextIsNullOrNotNull() throws Exception {
        // Initialize the database
        savedSearchRepository.saveAndFlush(savedSearch);

        // Get all the savedSearchList where searchText is not null
        defaultSavedSearchShouldBeFound("searchText.specified=true");

        // Get all the savedSearchList where searchText is null
        defaultSavedSearchShouldNotBeFound("searchText.specified=false");
    }
                @Test
    @Transactional
    public void getAllSavedSearchesBySearchTextContainsSomething() throws Exception {
        // Initialize the database
        savedSearchRepository.saveAndFlush(savedSearch);

        // Get all the savedSearchList where searchText contains DEFAULT_SEARCH_TEXT
        defaultSavedSearchShouldBeFound("searchText.contains=" + DEFAULT_SEARCH_TEXT);

        // Get all the savedSearchList where searchText contains UPDATED_SEARCH_TEXT
        defaultSavedSearchShouldNotBeFound("searchText.contains=" + UPDATED_SEARCH_TEXT);
    }

    @Test
    @Transactional
    public void getAllSavedSearchesBySearchTextNotContainsSomething() throws Exception {
        // Initialize the database
        savedSearchRepository.saveAndFlush(savedSearch);

        // Get all the savedSearchList where searchText does not contain DEFAULT_SEARCH_TEXT
        defaultSavedSearchShouldNotBeFound("searchText.doesNotContain=" + DEFAULT_SEARCH_TEXT);

        // Get all the savedSearchList where searchText does not contain UPDATED_SEARCH_TEXT
        defaultSavedSearchShouldBeFound("searchText.doesNotContain=" + UPDATED_SEARCH_TEXT);
    }


    @Test
    @Transactional
    public void getAllSavedSearchesByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        savedSearchRepository.saveAndFlush(savedSearch);
        NetflixUser user = NetflixUserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        savedSearch.setUser(user);
        savedSearchRepository.saveAndFlush(savedSearch);
        Long userId = user.getId();

        // Get all the savedSearchList where user equals to userId
        defaultSavedSearchShouldBeFound("userId.equals=" + userId);

        // Get all the savedSearchList where user equals to userId + 1
        defaultSavedSearchShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSavedSearchShouldBeFound(String filter) throws Exception {
        restSavedSearchMockMvc.perform(get("/api/saved-searches?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(savedSearch.getId().intValue())))
            .andExpect(jsonPath("$.[*].searchText").value(hasItem(DEFAULT_SEARCH_TEXT)));

        // Check, that the count call also returns 1
        restSavedSearchMockMvc.perform(get("/api/saved-searches/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSavedSearchShouldNotBeFound(String filter) throws Exception {
        restSavedSearchMockMvc.perform(get("/api/saved-searches?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSavedSearchMockMvc.perform(get("/api/saved-searches/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingSavedSearch() throws Exception {
        // Get the savedSearch
        restSavedSearchMockMvc.perform(get("/api/saved-searches/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSavedSearch() throws Exception {
        // Initialize the database
        savedSearchService.save(savedSearch);

        int databaseSizeBeforeUpdate = savedSearchRepository.findAll().size();

        // Update the savedSearch
        SavedSearch updatedSavedSearch = savedSearchRepository.findById(savedSearch.getId()).get();
        // Disconnect from session so that the updates on updatedSavedSearch are not directly saved in db
        em.detach(updatedSavedSearch);
        updatedSavedSearch
            .searchText(UPDATED_SEARCH_TEXT);

        restSavedSearchMockMvc.perform(put("/api/saved-searches")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedSavedSearch)))
            .andExpect(status().isOk());

        // Validate the SavedSearch in the database
        List<SavedSearch> savedSearchList = savedSearchRepository.findAll();
        assertThat(savedSearchList).hasSize(databaseSizeBeforeUpdate);
        SavedSearch testSavedSearch = savedSearchList.get(savedSearchList.size() - 1);
        assertThat(testSavedSearch.getSearchText()).isEqualTo(UPDATED_SEARCH_TEXT);
    }

    @Test
    @Transactional
    public void updateNonExistingSavedSearch() throws Exception {
        int databaseSizeBeforeUpdate = savedSearchRepository.findAll().size();

        // Create the SavedSearch

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSavedSearchMockMvc.perform(put("/api/saved-searches")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(savedSearch)))
            .andExpect(status().isBadRequest());

        // Validate the SavedSearch in the database
        List<SavedSearch> savedSearchList = savedSearchRepository.findAll();
        assertThat(savedSearchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSavedSearch() throws Exception {
        // Initialize the database
        savedSearchService.save(savedSearch);

        int databaseSizeBeforeDelete = savedSearchRepository.findAll().size();

        // Delete the savedSearch
        restSavedSearchMockMvc.perform(delete("/api/saved-searches/{id}", savedSearch.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SavedSearch> savedSearchList = savedSearchRepository.findAll();
        assertThat(savedSearchList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
