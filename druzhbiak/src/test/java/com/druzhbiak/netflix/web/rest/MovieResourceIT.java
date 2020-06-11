package com.druzhbiak.netflix.web.rest;

import com.druzhbiak.netflix.NetflixApp;
import com.druzhbiak.netflix.domain.Movie;
import com.druzhbiak.netflix.domain.Episode;
import com.druzhbiak.netflix.domain.MyList;
import com.druzhbiak.netflix.repository.MovieRepository;
import com.druzhbiak.netflix.service.MovieService;
import com.druzhbiak.netflix.service.dto.MovieCriteria;
import com.druzhbiak.netflix.service.MovieQueryService;

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

import com.druzhbiak.netflix.domain.enumeration.Type;
import com.druzhbiak.netflix.domain.enumeration.Genre;
/**
 * Integration tests for the {@link MovieResource} REST controller.
 */
@SpringBootTest(classes = NetflixApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class MovieResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_RELEASE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_RELEASE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_RELEASE_DATE = LocalDate.ofEpochDay(-1L);

    private static final Type DEFAULT_TYPE = Type.SERIES;
    private static final Type UPDATED_TYPE = Type.FILM;

    private static final Genre DEFAULT_GENRE = Genre.ACTION;
    private static final Genre UPDATED_GENRE = Genre.ANIME;

    private static final String DEFAULT_CREATOR = "AAAAAAAAAA";
    private static final String UPDATED_CREATOR = "BBBBBBBBBB";

    private static final Double DEFAULT_RATING = 1D;
    private static final Double UPDATED_RATING = 2D;
    private static final Double SMALLER_RATING = 1D - 1D;

    private static final String DEFAULT_LINK = "AAAAAAAAAA";
    private static final String UPDATED_LINK = "BBBBBBBBBB";

    private static final Boolean DEFAULT_AVAILABLE_IN_HD = false;
    private static final Boolean UPDATED_AVAILABLE_IN_HD = true;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieService movieService;

    @Autowired
    private MovieQueryService movieQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMovieMockMvc;

    private Movie movie;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Movie createEntity(EntityManager em) {
        Movie movie = new Movie()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .releaseDate(DEFAULT_RELEASE_DATE)
            .type(DEFAULT_TYPE)
            .genre(DEFAULT_GENRE)
            .creator(DEFAULT_CREATOR)
            .rating(DEFAULT_RATING)
            .link(DEFAULT_LINK)
            .availableInHD(DEFAULT_AVAILABLE_IN_HD);
        return movie;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Movie createUpdatedEntity(EntityManager em) {
        Movie movie = new Movie()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .releaseDate(UPDATED_RELEASE_DATE)
            .type(UPDATED_TYPE)
            .genre(UPDATED_GENRE)
            .creator(UPDATED_CREATOR)
            .rating(UPDATED_RATING)
            .link(UPDATED_LINK)
            .availableInHD(UPDATED_AVAILABLE_IN_HD);
        return movie;
    }

    @BeforeEach
    public void initTest() {
        movie = createEntity(em);
    }

    @Test
    @Transactional
    public void createMovie() throws Exception {
        int databaseSizeBeforeCreate = movieRepository.findAll().size();

        // Create the Movie
        restMovieMockMvc.perform(post("/api/movies")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(movie)))
            .andExpect(status().isCreated());

        // Validate the Movie in the database
        List<Movie> movieList = movieRepository.findAll();
        assertThat(movieList).hasSize(databaseSizeBeforeCreate + 1);
        Movie testMovie = movieList.get(movieList.size() - 1);
        assertThat(testMovie.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMovie.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testMovie.getReleaseDate()).isEqualTo(DEFAULT_RELEASE_DATE);
        assertThat(testMovie.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testMovie.getGenre()).isEqualTo(DEFAULT_GENRE);
        assertThat(testMovie.getCreator()).isEqualTo(DEFAULT_CREATOR);
        assertThat(testMovie.getRating()).isEqualTo(DEFAULT_RATING);
        assertThat(testMovie.getLink()).isEqualTo(DEFAULT_LINK);
        assertThat(testMovie.isAvailableInHD()).isEqualTo(DEFAULT_AVAILABLE_IN_HD);
    }

    @Test
    @Transactional
    public void createMovieWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = movieRepository.findAll().size();

        // Create the Movie with an existing ID
        movie.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMovieMockMvc.perform(post("/api/movies")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(movie)))
            .andExpect(status().isBadRequest());

        // Validate the Movie in the database
        List<Movie> movieList = movieRepository.findAll();
        assertThat(movieList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllMovies() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList
        restMovieMockMvc.perform(get("/api/movies?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(movie.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].releaseDate").value(hasItem(DEFAULT_RELEASE_DATE.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].genre").value(hasItem(DEFAULT_GENRE.toString())))
            .andExpect(jsonPath("$.[*].creator").value(hasItem(DEFAULT_CREATOR)))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING.doubleValue())))
            .andExpect(jsonPath("$.[*].link").value(hasItem(DEFAULT_LINK)))
            .andExpect(jsonPath("$.[*].availableInHD").value(hasItem(DEFAULT_AVAILABLE_IN_HD.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getMovie() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get the movie
        restMovieMockMvc.perform(get("/api/movies/{id}", movie.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(movie.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.releaseDate").value(DEFAULT_RELEASE_DATE.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.genre").value(DEFAULT_GENRE.toString()))
            .andExpect(jsonPath("$.creator").value(DEFAULT_CREATOR))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING.doubleValue()))
            .andExpect(jsonPath("$.link").value(DEFAULT_LINK))
            .andExpect(jsonPath("$.availableInHD").value(DEFAULT_AVAILABLE_IN_HD.booleanValue()));
    }


    @Test
    @Transactional
    public void getMoviesByIdFiltering() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        Long id = movie.getId();

        defaultMovieShouldBeFound("id.equals=" + id);
        defaultMovieShouldNotBeFound("id.notEquals=" + id);

        defaultMovieShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMovieShouldNotBeFound("id.greaterThan=" + id);

        defaultMovieShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMovieShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllMoviesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where name equals to DEFAULT_NAME
        defaultMovieShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the movieList where name equals to UPDATED_NAME
        defaultMovieShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllMoviesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where name not equals to DEFAULT_NAME
        defaultMovieShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the movieList where name not equals to UPDATED_NAME
        defaultMovieShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllMoviesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where name in DEFAULT_NAME or UPDATED_NAME
        defaultMovieShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the movieList where name equals to UPDATED_NAME
        defaultMovieShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllMoviesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where name is not null
        defaultMovieShouldBeFound("name.specified=true");

        // Get all the movieList where name is null
        defaultMovieShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllMoviesByNameContainsSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where name contains DEFAULT_NAME
        defaultMovieShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the movieList where name contains UPDATED_NAME
        defaultMovieShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllMoviesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where name does not contain DEFAULT_NAME
        defaultMovieShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the movieList where name does not contain UPDATED_NAME
        defaultMovieShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllMoviesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where description equals to DEFAULT_DESCRIPTION
        defaultMovieShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the movieList where description equals to UPDATED_DESCRIPTION
        defaultMovieShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllMoviesByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where description not equals to DEFAULT_DESCRIPTION
        defaultMovieShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the movieList where description not equals to UPDATED_DESCRIPTION
        defaultMovieShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllMoviesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultMovieShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the movieList where description equals to UPDATED_DESCRIPTION
        defaultMovieShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllMoviesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where description is not null
        defaultMovieShouldBeFound("description.specified=true");

        // Get all the movieList where description is null
        defaultMovieShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllMoviesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where description contains DEFAULT_DESCRIPTION
        defaultMovieShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the movieList where description contains UPDATED_DESCRIPTION
        defaultMovieShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllMoviesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where description does not contain DEFAULT_DESCRIPTION
        defaultMovieShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the movieList where description does not contain UPDATED_DESCRIPTION
        defaultMovieShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllMoviesByReleaseDateIsEqualToSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where releaseDate equals to DEFAULT_RELEASE_DATE
        defaultMovieShouldBeFound("releaseDate.equals=" + DEFAULT_RELEASE_DATE);

        // Get all the movieList where releaseDate equals to UPDATED_RELEASE_DATE
        defaultMovieShouldNotBeFound("releaseDate.equals=" + UPDATED_RELEASE_DATE);
    }

    @Test
    @Transactional
    public void getAllMoviesByReleaseDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where releaseDate not equals to DEFAULT_RELEASE_DATE
        defaultMovieShouldNotBeFound("releaseDate.notEquals=" + DEFAULT_RELEASE_DATE);

        // Get all the movieList where releaseDate not equals to UPDATED_RELEASE_DATE
        defaultMovieShouldBeFound("releaseDate.notEquals=" + UPDATED_RELEASE_DATE);
    }

    @Test
    @Transactional
    public void getAllMoviesByReleaseDateIsInShouldWork() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where releaseDate in DEFAULT_RELEASE_DATE or UPDATED_RELEASE_DATE
        defaultMovieShouldBeFound("releaseDate.in=" + DEFAULT_RELEASE_DATE + "," + UPDATED_RELEASE_DATE);

        // Get all the movieList where releaseDate equals to UPDATED_RELEASE_DATE
        defaultMovieShouldNotBeFound("releaseDate.in=" + UPDATED_RELEASE_DATE);
    }

    @Test
    @Transactional
    public void getAllMoviesByReleaseDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where releaseDate is not null
        defaultMovieShouldBeFound("releaseDate.specified=true");

        // Get all the movieList where releaseDate is null
        defaultMovieShouldNotBeFound("releaseDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllMoviesByReleaseDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where releaseDate is greater than or equal to DEFAULT_RELEASE_DATE
        defaultMovieShouldBeFound("releaseDate.greaterThanOrEqual=" + DEFAULT_RELEASE_DATE);

        // Get all the movieList where releaseDate is greater than or equal to UPDATED_RELEASE_DATE
        defaultMovieShouldNotBeFound("releaseDate.greaterThanOrEqual=" + UPDATED_RELEASE_DATE);
    }

    @Test
    @Transactional
    public void getAllMoviesByReleaseDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where releaseDate is less than or equal to DEFAULT_RELEASE_DATE
        defaultMovieShouldBeFound("releaseDate.lessThanOrEqual=" + DEFAULT_RELEASE_DATE);

        // Get all the movieList where releaseDate is less than or equal to SMALLER_RELEASE_DATE
        defaultMovieShouldNotBeFound("releaseDate.lessThanOrEqual=" + SMALLER_RELEASE_DATE);
    }

    @Test
    @Transactional
    public void getAllMoviesByReleaseDateIsLessThanSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where releaseDate is less than DEFAULT_RELEASE_DATE
        defaultMovieShouldNotBeFound("releaseDate.lessThan=" + DEFAULT_RELEASE_DATE);

        // Get all the movieList where releaseDate is less than UPDATED_RELEASE_DATE
        defaultMovieShouldBeFound("releaseDate.lessThan=" + UPDATED_RELEASE_DATE);
    }

    @Test
    @Transactional
    public void getAllMoviesByReleaseDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where releaseDate is greater than DEFAULT_RELEASE_DATE
        defaultMovieShouldNotBeFound("releaseDate.greaterThan=" + DEFAULT_RELEASE_DATE);

        // Get all the movieList where releaseDate is greater than SMALLER_RELEASE_DATE
        defaultMovieShouldBeFound("releaseDate.greaterThan=" + SMALLER_RELEASE_DATE);
    }


    @Test
    @Transactional
    public void getAllMoviesByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where type equals to DEFAULT_TYPE
        defaultMovieShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the movieList where type equals to UPDATED_TYPE
        defaultMovieShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllMoviesByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where type not equals to DEFAULT_TYPE
        defaultMovieShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the movieList where type not equals to UPDATED_TYPE
        defaultMovieShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllMoviesByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultMovieShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the movieList where type equals to UPDATED_TYPE
        defaultMovieShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllMoviesByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where type is not null
        defaultMovieShouldBeFound("type.specified=true");

        // Get all the movieList where type is null
        defaultMovieShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    public void getAllMoviesByGenreIsEqualToSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where genre equals to DEFAULT_GENRE
        defaultMovieShouldBeFound("genre.equals=" + DEFAULT_GENRE);

        // Get all the movieList where genre equals to UPDATED_GENRE
        defaultMovieShouldNotBeFound("genre.equals=" + UPDATED_GENRE);
    }

    @Test
    @Transactional
    public void getAllMoviesByGenreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where genre not equals to DEFAULT_GENRE
        defaultMovieShouldNotBeFound("genre.notEquals=" + DEFAULT_GENRE);

        // Get all the movieList where genre not equals to UPDATED_GENRE
        defaultMovieShouldBeFound("genre.notEquals=" + UPDATED_GENRE);
    }

    @Test
    @Transactional
    public void getAllMoviesByGenreIsInShouldWork() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where genre in DEFAULT_GENRE or UPDATED_GENRE
        defaultMovieShouldBeFound("genre.in=" + DEFAULT_GENRE + "," + UPDATED_GENRE);

        // Get all the movieList where genre equals to UPDATED_GENRE
        defaultMovieShouldNotBeFound("genre.in=" + UPDATED_GENRE);
    }

    @Test
    @Transactional
    public void getAllMoviesByGenreIsNullOrNotNull() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where genre is not null
        defaultMovieShouldBeFound("genre.specified=true");

        // Get all the movieList where genre is null
        defaultMovieShouldNotBeFound("genre.specified=false");
    }

    @Test
    @Transactional
    public void getAllMoviesByCreatorIsEqualToSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where creator equals to DEFAULT_CREATOR
        defaultMovieShouldBeFound("creator.equals=" + DEFAULT_CREATOR);

        // Get all the movieList where creator equals to UPDATED_CREATOR
        defaultMovieShouldNotBeFound("creator.equals=" + UPDATED_CREATOR);
    }

    @Test
    @Transactional
    public void getAllMoviesByCreatorIsNotEqualToSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where creator not equals to DEFAULT_CREATOR
        defaultMovieShouldNotBeFound("creator.notEquals=" + DEFAULT_CREATOR);

        // Get all the movieList where creator not equals to UPDATED_CREATOR
        defaultMovieShouldBeFound("creator.notEquals=" + UPDATED_CREATOR);
    }

    @Test
    @Transactional
    public void getAllMoviesByCreatorIsInShouldWork() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where creator in DEFAULT_CREATOR or UPDATED_CREATOR
        defaultMovieShouldBeFound("creator.in=" + DEFAULT_CREATOR + "," + UPDATED_CREATOR);

        // Get all the movieList where creator equals to UPDATED_CREATOR
        defaultMovieShouldNotBeFound("creator.in=" + UPDATED_CREATOR);
    }

    @Test
    @Transactional
    public void getAllMoviesByCreatorIsNullOrNotNull() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where creator is not null
        defaultMovieShouldBeFound("creator.specified=true");

        // Get all the movieList where creator is null
        defaultMovieShouldNotBeFound("creator.specified=false");
    }
                @Test
    @Transactional
    public void getAllMoviesByCreatorContainsSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where creator contains DEFAULT_CREATOR
        defaultMovieShouldBeFound("creator.contains=" + DEFAULT_CREATOR);

        // Get all the movieList where creator contains UPDATED_CREATOR
        defaultMovieShouldNotBeFound("creator.contains=" + UPDATED_CREATOR);
    }

    @Test
    @Transactional
    public void getAllMoviesByCreatorNotContainsSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where creator does not contain DEFAULT_CREATOR
        defaultMovieShouldNotBeFound("creator.doesNotContain=" + DEFAULT_CREATOR);

        // Get all the movieList where creator does not contain UPDATED_CREATOR
        defaultMovieShouldBeFound("creator.doesNotContain=" + UPDATED_CREATOR);
    }


    @Test
    @Transactional
    public void getAllMoviesByRatingIsEqualToSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where rating equals to DEFAULT_RATING
        defaultMovieShouldBeFound("rating.equals=" + DEFAULT_RATING);

        // Get all the movieList where rating equals to UPDATED_RATING
        defaultMovieShouldNotBeFound("rating.equals=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    public void getAllMoviesByRatingIsNotEqualToSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where rating not equals to DEFAULT_RATING
        defaultMovieShouldNotBeFound("rating.notEquals=" + DEFAULT_RATING);

        // Get all the movieList where rating not equals to UPDATED_RATING
        defaultMovieShouldBeFound("rating.notEquals=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    public void getAllMoviesByRatingIsInShouldWork() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where rating in DEFAULT_RATING or UPDATED_RATING
        defaultMovieShouldBeFound("rating.in=" + DEFAULT_RATING + "," + UPDATED_RATING);

        // Get all the movieList where rating equals to UPDATED_RATING
        defaultMovieShouldNotBeFound("rating.in=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    public void getAllMoviesByRatingIsNullOrNotNull() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where rating is not null
        defaultMovieShouldBeFound("rating.specified=true");

        // Get all the movieList where rating is null
        defaultMovieShouldNotBeFound("rating.specified=false");
    }

    @Test
    @Transactional
    public void getAllMoviesByRatingIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where rating is greater than or equal to DEFAULT_RATING
        defaultMovieShouldBeFound("rating.greaterThanOrEqual=" + DEFAULT_RATING);

        // Get all the movieList where rating is greater than or equal to UPDATED_RATING
        defaultMovieShouldNotBeFound("rating.greaterThanOrEqual=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    public void getAllMoviesByRatingIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where rating is less than or equal to DEFAULT_RATING
        defaultMovieShouldBeFound("rating.lessThanOrEqual=" + DEFAULT_RATING);

        // Get all the movieList where rating is less than or equal to SMALLER_RATING
        defaultMovieShouldNotBeFound("rating.lessThanOrEqual=" + SMALLER_RATING);
    }

    @Test
    @Transactional
    public void getAllMoviesByRatingIsLessThanSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where rating is less than DEFAULT_RATING
        defaultMovieShouldNotBeFound("rating.lessThan=" + DEFAULT_RATING);

        // Get all the movieList where rating is less than UPDATED_RATING
        defaultMovieShouldBeFound("rating.lessThan=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    public void getAllMoviesByRatingIsGreaterThanSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where rating is greater than DEFAULT_RATING
        defaultMovieShouldNotBeFound("rating.greaterThan=" + DEFAULT_RATING);

        // Get all the movieList where rating is greater than SMALLER_RATING
        defaultMovieShouldBeFound("rating.greaterThan=" + SMALLER_RATING);
    }


    @Test
    @Transactional
    public void getAllMoviesByLinkIsEqualToSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where link equals to DEFAULT_LINK
        defaultMovieShouldBeFound("link.equals=" + DEFAULT_LINK);

        // Get all the movieList where link equals to UPDATED_LINK
        defaultMovieShouldNotBeFound("link.equals=" + UPDATED_LINK);
    }

    @Test
    @Transactional
    public void getAllMoviesByLinkIsNotEqualToSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where link not equals to DEFAULT_LINK
        defaultMovieShouldNotBeFound("link.notEquals=" + DEFAULT_LINK);

        // Get all the movieList where link not equals to UPDATED_LINK
        defaultMovieShouldBeFound("link.notEquals=" + UPDATED_LINK);
    }

    @Test
    @Transactional
    public void getAllMoviesByLinkIsInShouldWork() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where link in DEFAULT_LINK or UPDATED_LINK
        defaultMovieShouldBeFound("link.in=" + DEFAULT_LINK + "," + UPDATED_LINK);

        // Get all the movieList where link equals to UPDATED_LINK
        defaultMovieShouldNotBeFound("link.in=" + UPDATED_LINK);
    }

    @Test
    @Transactional
    public void getAllMoviesByLinkIsNullOrNotNull() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where link is not null
        defaultMovieShouldBeFound("link.specified=true");

        // Get all the movieList where link is null
        defaultMovieShouldNotBeFound("link.specified=false");
    }
                @Test
    @Transactional
    public void getAllMoviesByLinkContainsSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where link contains DEFAULT_LINK
        defaultMovieShouldBeFound("link.contains=" + DEFAULT_LINK);

        // Get all the movieList where link contains UPDATED_LINK
        defaultMovieShouldNotBeFound("link.contains=" + UPDATED_LINK);
    }

    @Test
    @Transactional
    public void getAllMoviesByLinkNotContainsSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where link does not contain DEFAULT_LINK
        defaultMovieShouldNotBeFound("link.doesNotContain=" + DEFAULT_LINK);

        // Get all the movieList where link does not contain UPDATED_LINK
        defaultMovieShouldBeFound("link.doesNotContain=" + UPDATED_LINK);
    }


    @Test
    @Transactional
    public void getAllMoviesByAvailableInHDIsEqualToSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where availableInHD equals to DEFAULT_AVAILABLE_IN_HD
        defaultMovieShouldBeFound("availableInHD.equals=" + DEFAULT_AVAILABLE_IN_HD);

        // Get all the movieList where availableInHD equals to UPDATED_AVAILABLE_IN_HD
        defaultMovieShouldNotBeFound("availableInHD.equals=" + UPDATED_AVAILABLE_IN_HD);
    }

    @Test
    @Transactional
    public void getAllMoviesByAvailableInHDIsNotEqualToSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where availableInHD not equals to DEFAULT_AVAILABLE_IN_HD
        defaultMovieShouldNotBeFound("availableInHD.notEquals=" + DEFAULT_AVAILABLE_IN_HD);

        // Get all the movieList where availableInHD not equals to UPDATED_AVAILABLE_IN_HD
        defaultMovieShouldBeFound("availableInHD.notEquals=" + UPDATED_AVAILABLE_IN_HD);
    }

    @Test
    @Transactional
    public void getAllMoviesByAvailableInHDIsInShouldWork() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where availableInHD in DEFAULT_AVAILABLE_IN_HD or UPDATED_AVAILABLE_IN_HD
        defaultMovieShouldBeFound("availableInHD.in=" + DEFAULT_AVAILABLE_IN_HD + "," + UPDATED_AVAILABLE_IN_HD);

        // Get all the movieList where availableInHD equals to UPDATED_AVAILABLE_IN_HD
        defaultMovieShouldNotBeFound("availableInHD.in=" + UPDATED_AVAILABLE_IN_HD);
    }

    @Test
    @Transactional
    public void getAllMoviesByAvailableInHDIsNullOrNotNull() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where availableInHD is not null
        defaultMovieShouldBeFound("availableInHD.specified=true");

        // Get all the movieList where availableInHD is null
        defaultMovieShouldNotBeFound("availableInHD.specified=false");
    }

    @Test
    @Transactional
    public void getAllMoviesByEpisodesIsEqualToSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);
        Episode episodes = EpisodeResourceIT.createEntity(em);
        em.persist(episodes);
        em.flush();
        movie.addEpisodes(episodes);
        movieRepository.saveAndFlush(movie);
        Long episodesId = episodes.getId();

        // Get all the movieList where episodes equals to episodesId
        defaultMovieShouldBeFound("episodesId.equals=" + episodesId);

        // Get all the movieList where episodes equals to episodesId + 1
        defaultMovieShouldNotBeFound("episodesId.equals=" + (episodesId + 1));
    }


    @Test
    @Transactional
    public void getAllMoviesByMyListIsEqualToSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);
        MyList myList = MyListResourceIT.createEntity(em);
        em.persist(myList);
        em.flush();
        movie.setMyList(myList);
        movieRepository.saveAndFlush(movie);
        Long myListId = myList.getId();

        // Get all the movieList where myList equals to myListId
        defaultMovieShouldBeFound("myListId.equals=" + myListId);

        // Get all the movieList where myList equals to myListId + 1
        defaultMovieShouldNotBeFound("myListId.equals=" + (myListId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMovieShouldBeFound(String filter) throws Exception {
        restMovieMockMvc.perform(get("/api/movies?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(movie.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].releaseDate").value(hasItem(DEFAULT_RELEASE_DATE.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].genre").value(hasItem(DEFAULT_GENRE.toString())))
            .andExpect(jsonPath("$.[*].creator").value(hasItem(DEFAULT_CREATOR)))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING.doubleValue())))
            .andExpect(jsonPath("$.[*].link").value(hasItem(DEFAULT_LINK)))
            .andExpect(jsonPath("$.[*].availableInHD").value(hasItem(DEFAULT_AVAILABLE_IN_HD.booleanValue())));

        // Check, that the count call also returns 1
        restMovieMockMvc.perform(get("/api/movies/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMovieShouldNotBeFound(String filter) throws Exception {
        restMovieMockMvc.perform(get("/api/movies?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMovieMockMvc.perform(get("/api/movies/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingMovie() throws Exception {
        // Get the movie
        restMovieMockMvc.perform(get("/api/movies/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMovie() throws Exception {
        // Initialize the database
        movieService.save(movie);

        int databaseSizeBeforeUpdate = movieRepository.findAll().size();

        // Update the movie
        Movie updatedMovie = movieRepository.findById(movie.getId()).get();
        // Disconnect from session so that the updates on updatedMovie are not directly saved in db
        em.detach(updatedMovie);
        updatedMovie
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .releaseDate(UPDATED_RELEASE_DATE)
            .type(UPDATED_TYPE)
            .genre(UPDATED_GENRE)
            .creator(UPDATED_CREATOR)
            .rating(UPDATED_RATING)
            .link(UPDATED_LINK)
            .availableInHD(UPDATED_AVAILABLE_IN_HD);

        restMovieMockMvc.perform(put("/api/movies")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedMovie)))
            .andExpect(status().isOk());

        // Validate the Movie in the database
        List<Movie> movieList = movieRepository.findAll();
        assertThat(movieList).hasSize(databaseSizeBeforeUpdate);
        Movie testMovie = movieList.get(movieList.size() - 1);
        assertThat(testMovie.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMovie.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMovie.getReleaseDate()).isEqualTo(UPDATED_RELEASE_DATE);
        assertThat(testMovie.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testMovie.getGenre()).isEqualTo(UPDATED_GENRE);
        assertThat(testMovie.getCreator()).isEqualTo(UPDATED_CREATOR);
        assertThat(testMovie.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testMovie.getLink()).isEqualTo(UPDATED_LINK);
        assertThat(testMovie.isAvailableInHD()).isEqualTo(UPDATED_AVAILABLE_IN_HD);
    }

    @Test
    @Transactional
    public void updateNonExistingMovie() throws Exception {
        int databaseSizeBeforeUpdate = movieRepository.findAll().size();

        // Create the Movie

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMovieMockMvc.perform(put("/api/movies")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(movie)))
            .andExpect(status().isBadRequest());

        // Validate the Movie in the database
        List<Movie> movieList = movieRepository.findAll();
        assertThat(movieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMovie() throws Exception {
        // Initialize the database
        movieService.save(movie);

        int databaseSizeBeforeDelete = movieRepository.findAll().size();

        // Delete the movie
        restMovieMockMvc.perform(delete("/api/movies/{id}", movie.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Movie> movieList = movieRepository.findAll();
        assertThat(movieList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
