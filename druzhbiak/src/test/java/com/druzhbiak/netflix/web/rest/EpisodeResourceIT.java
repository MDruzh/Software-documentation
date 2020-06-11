package com.druzhbiak.netflix.web.rest;

import com.druzhbiak.netflix.NetflixApp;
import com.druzhbiak.netflix.domain.Episode;
import com.druzhbiak.netflix.domain.Movie;
import com.druzhbiak.netflix.repository.EpisodeRepository;
import com.druzhbiak.netflix.service.EpisodeService;
import com.druzhbiak.netflix.service.dto.EpisodeCriteria;
import com.druzhbiak.netflix.service.EpisodeQueryService;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link EpisodeResource} REST controller.
 */
@SpringBootTest(classes = NetflixApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class EpisodeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_PRODUCER = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_RELEASE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_RELEASE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_RELEASE_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_VIDEO_URL = "AAAAAAAAAA";
    private static final String UPDATED_VIDEO_URL = "BBBBBBBBBB";

    @Autowired
    private EpisodeRepository episodeRepository;

    @Autowired
    private EpisodeService episodeService;

    @Autowired
    private EpisodeQueryService episodeQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEpisodeMockMvc;

    private Episode episode;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Episode createEntity(EntityManager em) {
        Episode episode = new Episode()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .producer(DEFAULT_PRODUCER)
            .releaseDate(DEFAULT_RELEASE_DATE)
            .videoURL(DEFAULT_VIDEO_URL);
        return episode;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Episode createUpdatedEntity(EntityManager em) {
        Episode episode = new Episode()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .producer(UPDATED_PRODUCER)
            .releaseDate(UPDATED_RELEASE_DATE)
            .videoURL(UPDATED_VIDEO_URL);
        return episode;
    }

    @BeforeEach
    public void initTest() {
        episode = createEntity(em);
    }

    @Test
    @Transactional
    public void createEpisode() throws Exception {
        int databaseSizeBeforeCreate = episodeRepository.findAll().size();

        // Create the Episode
        restEpisodeMockMvc.perform(post("/api/episodes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(episode)))
            .andExpect(status().isCreated());

        // Validate the Episode in the database
        List<Episode> episodeList = episodeRepository.findAll();
        assertThat(episodeList).hasSize(databaseSizeBeforeCreate + 1);
        Episode testEpisode = episodeList.get(episodeList.size() - 1);
        assertThat(testEpisode.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testEpisode.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testEpisode.getProducer()).isEqualTo(DEFAULT_PRODUCER);
        assertThat(testEpisode.getReleaseDate()).isEqualTo(DEFAULT_RELEASE_DATE);
        assertThat(testEpisode.getVideoURL()).isEqualTo(DEFAULT_VIDEO_URL);
    }

    @Test
    @Transactional
    public void createEpisodeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = episodeRepository.findAll().size();

        // Create the Episode with an existing ID
        episode.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEpisodeMockMvc.perform(post("/api/episodes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(episode)))
            .andExpect(status().isBadRequest());

        // Validate the Episode in the database
        List<Episode> episodeList = episodeRepository.findAll();
        assertThat(episodeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllEpisodes() throws Exception {
        // Initialize the database
        episodeRepository.saveAndFlush(episode);

        // Get all the episodeList
        restEpisodeMockMvc.perform(get("/api/episodes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(episode.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].producer").value(hasItem(DEFAULT_PRODUCER)))
            .andExpect(jsonPath("$.[*].releaseDate").value(hasItem(DEFAULT_RELEASE_DATE.toString())))
            .andExpect(jsonPath("$.[*].videoURL").value(hasItem(DEFAULT_VIDEO_URL)));
    }
    
    @Test
    @Transactional
    public void getEpisode() throws Exception {
        // Initialize the database
        episodeRepository.saveAndFlush(episode);

        // Get the episode
        restEpisodeMockMvc.perform(get("/api/episodes/{id}", episode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(episode.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.producer").value(DEFAULT_PRODUCER))
            .andExpect(jsonPath("$.releaseDate").value(DEFAULT_RELEASE_DATE.toString()))
            .andExpect(jsonPath("$.videoURL").value(DEFAULT_VIDEO_URL));
    }


    @Test
    @Transactional
    public void getEpisodesByIdFiltering() throws Exception {
        // Initialize the database
        episodeRepository.saveAndFlush(episode);

        Long id = episode.getId();

        defaultEpisodeShouldBeFound("id.equals=" + id);
        defaultEpisodeShouldNotBeFound("id.notEquals=" + id);

        defaultEpisodeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEpisodeShouldNotBeFound("id.greaterThan=" + id);

        defaultEpisodeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEpisodeShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllEpisodesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        episodeRepository.saveAndFlush(episode);

        // Get all the episodeList where name equals to DEFAULT_NAME
        defaultEpisodeShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the episodeList where name equals to UPDATED_NAME
        defaultEpisodeShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllEpisodesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        episodeRepository.saveAndFlush(episode);

        // Get all the episodeList where name not equals to DEFAULT_NAME
        defaultEpisodeShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the episodeList where name not equals to UPDATED_NAME
        defaultEpisodeShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllEpisodesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        episodeRepository.saveAndFlush(episode);

        // Get all the episodeList where name in DEFAULT_NAME or UPDATED_NAME
        defaultEpisodeShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the episodeList where name equals to UPDATED_NAME
        defaultEpisodeShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllEpisodesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        episodeRepository.saveAndFlush(episode);

        // Get all the episodeList where name is not null
        defaultEpisodeShouldBeFound("name.specified=true");

        // Get all the episodeList where name is null
        defaultEpisodeShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllEpisodesByNameContainsSomething() throws Exception {
        // Initialize the database
        episodeRepository.saveAndFlush(episode);

        // Get all the episodeList where name contains DEFAULT_NAME
        defaultEpisodeShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the episodeList where name contains UPDATED_NAME
        defaultEpisodeShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllEpisodesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        episodeRepository.saveAndFlush(episode);

        // Get all the episodeList where name does not contain DEFAULT_NAME
        defaultEpisodeShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the episodeList where name does not contain UPDATED_NAME
        defaultEpisodeShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllEpisodesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        episodeRepository.saveAndFlush(episode);

        // Get all the episodeList where description equals to DEFAULT_DESCRIPTION
        defaultEpisodeShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the episodeList where description equals to UPDATED_DESCRIPTION
        defaultEpisodeShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllEpisodesByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        episodeRepository.saveAndFlush(episode);

        // Get all the episodeList where description not equals to DEFAULT_DESCRIPTION
        defaultEpisodeShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the episodeList where description not equals to UPDATED_DESCRIPTION
        defaultEpisodeShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllEpisodesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        episodeRepository.saveAndFlush(episode);

        // Get all the episodeList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultEpisodeShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the episodeList where description equals to UPDATED_DESCRIPTION
        defaultEpisodeShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllEpisodesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        episodeRepository.saveAndFlush(episode);

        // Get all the episodeList where description is not null
        defaultEpisodeShouldBeFound("description.specified=true");

        // Get all the episodeList where description is null
        defaultEpisodeShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllEpisodesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        episodeRepository.saveAndFlush(episode);

        // Get all the episodeList where description contains DEFAULT_DESCRIPTION
        defaultEpisodeShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the episodeList where description contains UPDATED_DESCRIPTION
        defaultEpisodeShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllEpisodesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        episodeRepository.saveAndFlush(episode);

        // Get all the episodeList where description does not contain DEFAULT_DESCRIPTION
        defaultEpisodeShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the episodeList where description does not contain UPDATED_DESCRIPTION
        defaultEpisodeShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllEpisodesByProducerIsEqualToSomething() throws Exception {
        // Initialize the database
        episodeRepository.saveAndFlush(episode);

        // Get all the episodeList where producer equals to DEFAULT_PRODUCER
        defaultEpisodeShouldBeFound("producer.equals=" + DEFAULT_PRODUCER);

        // Get all the episodeList where producer equals to UPDATED_PRODUCER
        defaultEpisodeShouldNotBeFound("producer.equals=" + UPDATED_PRODUCER);
    }

    @Test
    @Transactional
    public void getAllEpisodesByProducerIsNotEqualToSomething() throws Exception {
        // Initialize the database
        episodeRepository.saveAndFlush(episode);

        // Get all the episodeList where producer not equals to DEFAULT_PRODUCER
        defaultEpisodeShouldNotBeFound("producer.notEquals=" + DEFAULT_PRODUCER);

        // Get all the episodeList where producer not equals to UPDATED_PRODUCER
        defaultEpisodeShouldBeFound("producer.notEquals=" + UPDATED_PRODUCER);
    }

    @Test
    @Transactional
    public void getAllEpisodesByProducerIsInShouldWork() throws Exception {
        // Initialize the database
        episodeRepository.saveAndFlush(episode);

        // Get all the episodeList where producer in DEFAULT_PRODUCER or UPDATED_PRODUCER
        defaultEpisodeShouldBeFound("producer.in=" + DEFAULT_PRODUCER + "," + UPDATED_PRODUCER);

        // Get all the episodeList where producer equals to UPDATED_PRODUCER
        defaultEpisodeShouldNotBeFound("producer.in=" + UPDATED_PRODUCER);
    }

    @Test
    @Transactional
    public void getAllEpisodesByProducerIsNullOrNotNull() throws Exception {
        // Initialize the database
        episodeRepository.saveAndFlush(episode);

        // Get all the episodeList where producer is not null
        defaultEpisodeShouldBeFound("producer.specified=true");

        // Get all the episodeList where producer is null
        defaultEpisodeShouldNotBeFound("producer.specified=false");
    }
                @Test
    @Transactional
    public void getAllEpisodesByProducerContainsSomething() throws Exception {
        // Initialize the database
        episodeRepository.saveAndFlush(episode);

        // Get all the episodeList where producer contains DEFAULT_PRODUCER
        defaultEpisodeShouldBeFound("producer.contains=" + DEFAULT_PRODUCER);

        // Get all the episodeList where producer contains UPDATED_PRODUCER
        defaultEpisodeShouldNotBeFound("producer.contains=" + UPDATED_PRODUCER);
    }

    @Test
    @Transactional
    public void getAllEpisodesByProducerNotContainsSomething() throws Exception {
        // Initialize the database
        episodeRepository.saveAndFlush(episode);

        // Get all the episodeList where producer does not contain DEFAULT_PRODUCER
        defaultEpisodeShouldNotBeFound("producer.doesNotContain=" + DEFAULT_PRODUCER);

        // Get all the episodeList where producer does not contain UPDATED_PRODUCER
        defaultEpisodeShouldBeFound("producer.doesNotContain=" + UPDATED_PRODUCER);
    }


    @Test
    @Transactional
    public void getAllEpisodesByReleaseDateIsEqualToSomething() throws Exception {
        // Initialize the database
        episodeRepository.saveAndFlush(episode);

        // Get all the episodeList where releaseDate equals to DEFAULT_RELEASE_DATE
        defaultEpisodeShouldBeFound("releaseDate.equals=" + DEFAULT_RELEASE_DATE);

        // Get all the episodeList where releaseDate equals to UPDATED_RELEASE_DATE
        defaultEpisodeShouldNotBeFound("releaseDate.equals=" + UPDATED_RELEASE_DATE);
    }

    @Test
    @Transactional
    public void getAllEpisodesByReleaseDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        episodeRepository.saveAndFlush(episode);

        // Get all the episodeList where releaseDate not equals to DEFAULT_RELEASE_DATE
        defaultEpisodeShouldNotBeFound("releaseDate.notEquals=" + DEFAULT_RELEASE_DATE);

        // Get all the episodeList where releaseDate not equals to UPDATED_RELEASE_DATE
        defaultEpisodeShouldBeFound("releaseDate.notEquals=" + UPDATED_RELEASE_DATE);
    }

    @Test
    @Transactional
    public void getAllEpisodesByReleaseDateIsInShouldWork() throws Exception {
        // Initialize the database
        episodeRepository.saveAndFlush(episode);

        // Get all the episodeList where releaseDate in DEFAULT_RELEASE_DATE or UPDATED_RELEASE_DATE
        defaultEpisodeShouldBeFound("releaseDate.in=" + DEFAULT_RELEASE_DATE + "," + UPDATED_RELEASE_DATE);

        // Get all the episodeList where releaseDate equals to UPDATED_RELEASE_DATE
        defaultEpisodeShouldNotBeFound("releaseDate.in=" + UPDATED_RELEASE_DATE);
    }

    @Test
    @Transactional
    public void getAllEpisodesByReleaseDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        episodeRepository.saveAndFlush(episode);

        // Get all the episodeList where releaseDate is not null
        defaultEpisodeShouldBeFound("releaseDate.specified=true");

        // Get all the episodeList where releaseDate is null
        defaultEpisodeShouldNotBeFound("releaseDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllEpisodesByReleaseDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        episodeRepository.saveAndFlush(episode);

        // Get all the episodeList where releaseDate is greater than or equal to DEFAULT_RELEASE_DATE
        defaultEpisodeShouldBeFound("releaseDate.greaterThanOrEqual=" + DEFAULT_RELEASE_DATE);

        // Get all the episodeList where releaseDate is greater than or equal to UPDATED_RELEASE_DATE
        defaultEpisodeShouldNotBeFound("releaseDate.greaterThanOrEqual=" + UPDATED_RELEASE_DATE);
    }

    @Test
    @Transactional
    public void getAllEpisodesByReleaseDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        episodeRepository.saveAndFlush(episode);

        // Get all the episodeList where releaseDate is less than or equal to DEFAULT_RELEASE_DATE
        defaultEpisodeShouldBeFound("releaseDate.lessThanOrEqual=" + DEFAULT_RELEASE_DATE);

        // Get all the episodeList where releaseDate is less than or equal to SMALLER_RELEASE_DATE
        defaultEpisodeShouldNotBeFound("releaseDate.lessThanOrEqual=" + SMALLER_RELEASE_DATE);
    }

    @Test
    @Transactional
    public void getAllEpisodesByReleaseDateIsLessThanSomething() throws Exception {
        // Initialize the database
        episodeRepository.saveAndFlush(episode);

        // Get all the episodeList where releaseDate is less than DEFAULT_RELEASE_DATE
        defaultEpisodeShouldNotBeFound("releaseDate.lessThan=" + DEFAULT_RELEASE_DATE);

        // Get all the episodeList where releaseDate is less than UPDATED_RELEASE_DATE
        defaultEpisodeShouldBeFound("releaseDate.lessThan=" + UPDATED_RELEASE_DATE);
    }

    @Test
    @Transactional
    public void getAllEpisodesByReleaseDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        episodeRepository.saveAndFlush(episode);

        // Get all the episodeList where releaseDate is greater than DEFAULT_RELEASE_DATE
        defaultEpisodeShouldNotBeFound("releaseDate.greaterThan=" + DEFAULT_RELEASE_DATE);

        // Get all the episodeList where releaseDate is greater than SMALLER_RELEASE_DATE
        defaultEpisodeShouldBeFound("releaseDate.greaterThan=" + SMALLER_RELEASE_DATE);
    }


    @Test
    @Transactional
    public void getAllEpisodesByVideoURLIsEqualToSomething() throws Exception {
        // Initialize the database
        episodeRepository.saveAndFlush(episode);

        // Get all the episodeList where videoURL equals to DEFAULT_VIDEO_URL
        defaultEpisodeShouldBeFound("videoURL.equals=" + DEFAULT_VIDEO_URL);

        // Get all the episodeList where videoURL equals to UPDATED_VIDEO_URL
        defaultEpisodeShouldNotBeFound("videoURL.equals=" + UPDATED_VIDEO_URL);
    }

    @Test
    @Transactional
    public void getAllEpisodesByVideoURLIsNotEqualToSomething() throws Exception {
        // Initialize the database
        episodeRepository.saveAndFlush(episode);

        // Get all the episodeList where videoURL not equals to DEFAULT_VIDEO_URL
        defaultEpisodeShouldNotBeFound("videoURL.notEquals=" + DEFAULT_VIDEO_URL);

        // Get all the episodeList where videoURL not equals to UPDATED_VIDEO_URL
        defaultEpisodeShouldBeFound("videoURL.notEquals=" + UPDATED_VIDEO_URL);
    }

    @Test
    @Transactional
    public void getAllEpisodesByVideoURLIsInShouldWork() throws Exception {
        // Initialize the database
        episodeRepository.saveAndFlush(episode);

        // Get all the episodeList where videoURL in DEFAULT_VIDEO_URL or UPDATED_VIDEO_URL
        defaultEpisodeShouldBeFound("videoURL.in=" + DEFAULT_VIDEO_URL + "," + UPDATED_VIDEO_URL);

        // Get all the episodeList where videoURL equals to UPDATED_VIDEO_URL
        defaultEpisodeShouldNotBeFound("videoURL.in=" + UPDATED_VIDEO_URL);
    }

    @Test
    @Transactional
    public void getAllEpisodesByVideoURLIsNullOrNotNull() throws Exception {
        // Initialize the database
        episodeRepository.saveAndFlush(episode);

        // Get all the episodeList where videoURL is not null
        defaultEpisodeShouldBeFound("videoURL.specified=true");

        // Get all the episodeList where videoURL is null
        defaultEpisodeShouldNotBeFound("videoURL.specified=false");
    }
                @Test
    @Transactional
    public void getAllEpisodesByVideoURLContainsSomething() throws Exception {
        // Initialize the database
        episodeRepository.saveAndFlush(episode);

        // Get all the episodeList where videoURL contains DEFAULT_VIDEO_URL
        defaultEpisodeShouldBeFound("videoURL.contains=" + DEFAULT_VIDEO_URL);

        // Get all the episodeList where videoURL contains UPDATED_VIDEO_URL
        defaultEpisodeShouldNotBeFound("videoURL.contains=" + UPDATED_VIDEO_URL);
    }

    @Test
    @Transactional
    public void getAllEpisodesByVideoURLNotContainsSomething() throws Exception {
        // Initialize the database
        episodeRepository.saveAndFlush(episode);

        // Get all the episodeList where videoURL does not contain DEFAULT_VIDEO_URL
        defaultEpisodeShouldNotBeFound("videoURL.doesNotContain=" + DEFAULT_VIDEO_URL);

        // Get all the episodeList where videoURL does not contain UPDATED_VIDEO_URL
        defaultEpisodeShouldBeFound("videoURL.doesNotContain=" + UPDATED_VIDEO_URL);
    }


    @Test
    @Transactional
    public void getAllEpisodesBySeriesIsEqualToSomething() throws Exception {
        // Initialize the database
        episodeRepository.saveAndFlush(episode);
        Movie series = MovieResourceIT.createEntity(em);
        em.persist(series);
        em.flush();
        episode.setSeries(series);
        episodeRepository.saveAndFlush(episode);
        Long seriesId = series.getId();

        // Get all the episodeList where series equals to seriesId
        defaultEpisodeShouldBeFound("seriesId.equals=" + seriesId);

        // Get all the episodeList where series equals to seriesId + 1
        defaultEpisodeShouldNotBeFound("seriesId.equals=" + (seriesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEpisodeShouldBeFound(String filter) throws Exception {
        restEpisodeMockMvc.perform(get("/api/episodes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(episode.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].producer").value(hasItem(DEFAULT_PRODUCER)))
            .andExpect(jsonPath("$.[*].releaseDate").value(hasItem(DEFAULT_RELEASE_DATE.toString())))
            .andExpect(jsonPath("$.[*].videoURL").value(hasItem(DEFAULT_VIDEO_URL)));

        // Check, that the count call also returns 1
        restEpisodeMockMvc.perform(get("/api/episodes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEpisodeShouldNotBeFound(String filter) throws Exception {
        restEpisodeMockMvc.perform(get("/api/episodes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEpisodeMockMvc.perform(get("/api/episodes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingEpisode() throws Exception {
        // Get the episode
        restEpisodeMockMvc.perform(get("/api/episodes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEpisode() throws Exception {
        // Initialize the database
        episodeService.save(episode);

        int databaseSizeBeforeUpdate = episodeRepository.findAll().size();

        // Update the episode
        Episode updatedEpisode = episodeRepository.findById(episode.getId()).get();
        // Disconnect from session so that the updates on updatedEpisode are not directly saved in db
        em.detach(updatedEpisode);
        updatedEpisode
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .producer(UPDATED_PRODUCER)
            .releaseDate(UPDATED_RELEASE_DATE)
            .videoURL(UPDATED_VIDEO_URL);

        restEpisodeMockMvc.perform(put("/api/episodes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedEpisode)))
            .andExpect(status().isOk());

        // Validate the Episode in the database
        List<Episode> episodeList = episodeRepository.findAll();
        assertThat(episodeList).hasSize(databaseSizeBeforeUpdate);
        Episode testEpisode = episodeList.get(episodeList.size() - 1);
        assertThat(testEpisode.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEpisode.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEpisode.getProducer()).isEqualTo(UPDATED_PRODUCER);
        assertThat(testEpisode.getReleaseDate()).isEqualTo(UPDATED_RELEASE_DATE);
        assertThat(testEpisode.getVideoURL()).isEqualTo(UPDATED_VIDEO_URL);
    }

    @Test
    @Transactional
    public void updateNonExistingEpisode() throws Exception {
        int databaseSizeBeforeUpdate = episodeRepository.findAll().size();

        // Create the Episode

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEpisodeMockMvc.perform(put("/api/episodes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(episode)))
            .andExpect(status().isBadRequest());

        // Validate the Episode in the database
        List<Episode> episodeList = episodeRepository.findAll();
        assertThat(episodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteEpisode() throws Exception {
        // Initialize the database
        episodeService.save(episode);

        int databaseSizeBeforeDelete = episodeRepository.findAll().size();

        // Delete the episode
        restEpisodeMockMvc.perform(delete("/api/episodes/{id}", episode.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Episode> episodeList = episodeRepository.findAll();
        assertThat(episodeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
