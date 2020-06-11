package com.druzhbiak.netflix.web.rest;

import com.druzhbiak.netflix.NetflixApp;
import com.druzhbiak.netflix.domain.NetflixUser;
import com.druzhbiak.netflix.domain.MyList;
import com.druzhbiak.netflix.domain.SavedSearch;
import com.druzhbiak.netflix.repository.NetflixUserRepository;
import com.druzhbiak.netflix.service.NetflixUserService;
import com.druzhbiak.netflix.service.dto.NetflixUserCriteria;
import com.druzhbiak.netflix.service.NetflixUserQueryService;

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

import com.druzhbiak.netflix.domain.enumeration.Category;
/**
 * Integration tests for the {@link NetflixUserResource} REST controller.
 */
@SpringBootTest(classes = NetflixApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class NetflixUserResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final String DEFAULT_BIO = "AAAAAAAAAA";
    private static final String UPDATED_BIO = "BBBBBBBBBB";

    private static final Category DEFAULT_CATEGORY = Category.CHILD;
    private static final Category UPDATED_CATEGORY = Category.ADULT;

    private static final LocalDate DEFAULT_BIRTH_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BIRTH_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_BIRTH_DATE = LocalDate.ofEpochDay(-1L);

    @Autowired
    private NetflixUserRepository netflixUserRepository;

    @Autowired
    private NetflixUserService netflixUserService;

    @Autowired
    private NetflixUserQueryService netflixUserQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNetflixUserMockMvc;

    private NetflixUser netflixUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NetflixUser createEntity(EntityManager em) {
        NetflixUser netflixUser = new NetflixUser()
            .name(DEFAULT_NAME)
            .email(DEFAULT_EMAIL)
            .password(DEFAULT_PASSWORD)
            .bio(DEFAULT_BIO)
            .category(DEFAULT_CATEGORY)
            .birthDate(DEFAULT_BIRTH_DATE);
        return netflixUser;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NetflixUser createUpdatedEntity(EntityManager em) {
        NetflixUser netflixUser = new NetflixUser()
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .password(UPDATED_PASSWORD)
            .bio(UPDATED_BIO)
            .category(UPDATED_CATEGORY)
            .birthDate(UPDATED_BIRTH_DATE);
        return netflixUser;
    }

    @BeforeEach
    public void initTest() {
        netflixUser = createEntity(em);
    }

    @Test
    @Transactional
    public void createNetflixUser() throws Exception {
        int databaseSizeBeforeCreate = netflixUserRepository.findAll().size();

        // Create the NetflixUser
        restNetflixUserMockMvc.perform(post("/api/netflix-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(netflixUser)))
            .andExpect(status().isCreated());

        // Validate the NetflixUser in the database
        List<NetflixUser> netflixUserList = netflixUserRepository.findAll();
        assertThat(netflixUserList).hasSize(databaseSizeBeforeCreate + 1);
        NetflixUser testNetflixUser = netflixUserList.get(netflixUserList.size() - 1);
        assertThat(testNetflixUser.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testNetflixUser.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testNetflixUser.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testNetflixUser.getBio()).isEqualTo(DEFAULT_BIO);
        assertThat(testNetflixUser.getCategory()).isEqualTo(DEFAULT_CATEGORY);
        assertThat(testNetflixUser.getBirthDate()).isEqualTo(DEFAULT_BIRTH_DATE);
    }

    @Test
    @Transactional
    public void createNetflixUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = netflixUserRepository.findAll().size();

        // Create the NetflixUser with an existing ID
        netflixUser.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restNetflixUserMockMvc.perform(post("/api/netflix-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(netflixUser)))
            .andExpect(status().isBadRequest());

        // Validate the NetflixUser in the database
        List<NetflixUser> netflixUserList = netflixUserRepository.findAll();
        assertThat(netflixUserList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllNetflixUsers() throws Exception {
        // Initialize the database
        netflixUserRepository.saveAndFlush(netflixUser);

        // Get all the netflixUserList
        restNetflixUserMockMvc.perform(get("/api/netflix-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(netflixUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)))
            .andExpect(jsonPath("$.[*].bio").value(hasItem(DEFAULT_BIO)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getNetflixUser() throws Exception {
        // Initialize the database
        netflixUserRepository.saveAndFlush(netflixUser);

        // Get the netflixUser
        restNetflixUserMockMvc.perform(get("/api/netflix-users/{id}", netflixUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(netflixUser.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD))
            .andExpect(jsonPath("$.bio").value(DEFAULT_BIO))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY.toString()))
            .andExpect(jsonPath("$.birthDate").value(DEFAULT_BIRTH_DATE.toString()));
    }


    @Test
    @Transactional
    public void getNetflixUsersByIdFiltering() throws Exception {
        // Initialize the database
        netflixUserRepository.saveAndFlush(netflixUser);

        Long id = netflixUser.getId();

        defaultNetflixUserShouldBeFound("id.equals=" + id);
        defaultNetflixUserShouldNotBeFound("id.notEquals=" + id);

        defaultNetflixUserShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultNetflixUserShouldNotBeFound("id.greaterThan=" + id);

        defaultNetflixUserShouldBeFound("id.lessThanOrEqual=" + id);
        defaultNetflixUserShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllNetflixUsersByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        netflixUserRepository.saveAndFlush(netflixUser);

        // Get all the netflixUserList where name equals to DEFAULT_NAME
        defaultNetflixUserShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the netflixUserList where name equals to UPDATED_NAME
        defaultNetflixUserShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllNetflixUsersByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        netflixUserRepository.saveAndFlush(netflixUser);

        // Get all the netflixUserList where name not equals to DEFAULT_NAME
        defaultNetflixUserShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the netflixUserList where name not equals to UPDATED_NAME
        defaultNetflixUserShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllNetflixUsersByNameIsInShouldWork() throws Exception {
        // Initialize the database
        netflixUserRepository.saveAndFlush(netflixUser);

        // Get all the netflixUserList where name in DEFAULT_NAME or UPDATED_NAME
        defaultNetflixUserShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the netflixUserList where name equals to UPDATED_NAME
        defaultNetflixUserShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllNetflixUsersByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        netflixUserRepository.saveAndFlush(netflixUser);

        // Get all the netflixUserList where name is not null
        defaultNetflixUserShouldBeFound("name.specified=true");

        // Get all the netflixUserList where name is null
        defaultNetflixUserShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllNetflixUsersByNameContainsSomething() throws Exception {
        // Initialize the database
        netflixUserRepository.saveAndFlush(netflixUser);

        // Get all the netflixUserList where name contains DEFAULT_NAME
        defaultNetflixUserShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the netflixUserList where name contains UPDATED_NAME
        defaultNetflixUserShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllNetflixUsersByNameNotContainsSomething() throws Exception {
        // Initialize the database
        netflixUserRepository.saveAndFlush(netflixUser);

        // Get all the netflixUserList where name does not contain DEFAULT_NAME
        defaultNetflixUserShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the netflixUserList where name does not contain UPDATED_NAME
        defaultNetflixUserShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllNetflixUsersByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        netflixUserRepository.saveAndFlush(netflixUser);

        // Get all the netflixUserList where email equals to DEFAULT_EMAIL
        defaultNetflixUserShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the netflixUserList where email equals to UPDATED_EMAIL
        defaultNetflixUserShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllNetflixUsersByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        netflixUserRepository.saveAndFlush(netflixUser);

        // Get all the netflixUserList where email not equals to DEFAULT_EMAIL
        defaultNetflixUserShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the netflixUserList where email not equals to UPDATED_EMAIL
        defaultNetflixUserShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllNetflixUsersByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        netflixUserRepository.saveAndFlush(netflixUser);

        // Get all the netflixUserList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultNetflixUserShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the netflixUserList where email equals to UPDATED_EMAIL
        defaultNetflixUserShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllNetflixUsersByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        netflixUserRepository.saveAndFlush(netflixUser);

        // Get all the netflixUserList where email is not null
        defaultNetflixUserShouldBeFound("email.specified=true");

        // Get all the netflixUserList where email is null
        defaultNetflixUserShouldNotBeFound("email.specified=false");
    }
                @Test
    @Transactional
    public void getAllNetflixUsersByEmailContainsSomething() throws Exception {
        // Initialize the database
        netflixUserRepository.saveAndFlush(netflixUser);

        // Get all the netflixUserList where email contains DEFAULT_EMAIL
        defaultNetflixUserShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the netflixUserList where email contains UPDATED_EMAIL
        defaultNetflixUserShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllNetflixUsersByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        netflixUserRepository.saveAndFlush(netflixUser);

        // Get all the netflixUserList where email does not contain DEFAULT_EMAIL
        defaultNetflixUserShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the netflixUserList where email does not contain UPDATED_EMAIL
        defaultNetflixUserShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }


    @Test
    @Transactional
    public void getAllNetflixUsersByPasswordIsEqualToSomething() throws Exception {
        // Initialize the database
        netflixUserRepository.saveAndFlush(netflixUser);

        // Get all the netflixUserList where password equals to DEFAULT_PASSWORD
        defaultNetflixUserShouldBeFound("password.equals=" + DEFAULT_PASSWORD);

        // Get all the netflixUserList where password equals to UPDATED_PASSWORD
        defaultNetflixUserShouldNotBeFound("password.equals=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    public void getAllNetflixUsersByPasswordIsNotEqualToSomething() throws Exception {
        // Initialize the database
        netflixUserRepository.saveAndFlush(netflixUser);

        // Get all the netflixUserList where password not equals to DEFAULT_PASSWORD
        defaultNetflixUserShouldNotBeFound("password.notEquals=" + DEFAULT_PASSWORD);

        // Get all the netflixUserList where password not equals to UPDATED_PASSWORD
        defaultNetflixUserShouldBeFound("password.notEquals=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    public void getAllNetflixUsersByPasswordIsInShouldWork() throws Exception {
        // Initialize the database
        netflixUserRepository.saveAndFlush(netflixUser);

        // Get all the netflixUserList where password in DEFAULT_PASSWORD or UPDATED_PASSWORD
        defaultNetflixUserShouldBeFound("password.in=" + DEFAULT_PASSWORD + "," + UPDATED_PASSWORD);

        // Get all the netflixUserList where password equals to UPDATED_PASSWORD
        defaultNetflixUserShouldNotBeFound("password.in=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    public void getAllNetflixUsersByPasswordIsNullOrNotNull() throws Exception {
        // Initialize the database
        netflixUserRepository.saveAndFlush(netflixUser);

        // Get all the netflixUserList where password is not null
        defaultNetflixUserShouldBeFound("password.specified=true");

        // Get all the netflixUserList where password is null
        defaultNetflixUserShouldNotBeFound("password.specified=false");
    }
                @Test
    @Transactional
    public void getAllNetflixUsersByPasswordContainsSomething() throws Exception {
        // Initialize the database
        netflixUserRepository.saveAndFlush(netflixUser);

        // Get all the netflixUserList where password contains DEFAULT_PASSWORD
        defaultNetflixUserShouldBeFound("password.contains=" + DEFAULT_PASSWORD);

        // Get all the netflixUserList where password contains UPDATED_PASSWORD
        defaultNetflixUserShouldNotBeFound("password.contains=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    public void getAllNetflixUsersByPasswordNotContainsSomething() throws Exception {
        // Initialize the database
        netflixUserRepository.saveAndFlush(netflixUser);

        // Get all the netflixUserList where password does not contain DEFAULT_PASSWORD
        defaultNetflixUserShouldNotBeFound("password.doesNotContain=" + DEFAULT_PASSWORD);

        // Get all the netflixUserList where password does not contain UPDATED_PASSWORD
        defaultNetflixUserShouldBeFound("password.doesNotContain=" + UPDATED_PASSWORD);
    }


    @Test
    @Transactional
    public void getAllNetflixUsersByBioIsEqualToSomething() throws Exception {
        // Initialize the database
        netflixUserRepository.saveAndFlush(netflixUser);

        // Get all the netflixUserList where bio equals to DEFAULT_BIO
        defaultNetflixUserShouldBeFound("bio.equals=" + DEFAULT_BIO);

        // Get all the netflixUserList where bio equals to UPDATED_BIO
        defaultNetflixUserShouldNotBeFound("bio.equals=" + UPDATED_BIO);
    }

    @Test
    @Transactional
    public void getAllNetflixUsersByBioIsNotEqualToSomething() throws Exception {
        // Initialize the database
        netflixUserRepository.saveAndFlush(netflixUser);

        // Get all the netflixUserList where bio not equals to DEFAULT_BIO
        defaultNetflixUserShouldNotBeFound("bio.notEquals=" + DEFAULT_BIO);

        // Get all the netflixUserList where bio not equals to UPDATED_BIO
        defaultNetflixUserShouldBeFound("bio.notEquals=" + UPDATED_BIO);
    }

    @Test
    @Transactional
    public void getAllNetflixUsersByBioIsInShouldWork() throws Exception {
        // Initialize the database
        netflixUserRepository.saveAndFlush(netflixUser);

        // Get all the netflixUserList where bio in DEFAULT_BIO or UPDATED_BIO
        defaultNetflixUserShouldBeFound("bio.in=" + DEFAULT_BIO + "," + UPDATED_BIO);

        // Get all the netflixUserList where bio equals to UPDATED_BIO
        defaultNetflixUserShouldNotBeFound("bio.in=" + UPDATED_BIO);
    }

    @Test
    @Transactional
    public void getAllNetflixUsersByBioIsNullOrNotNull() throws Exception {
        // Initialize the database
        netflixUserRepository.saveAndFlush(netflixUser);

        // Get all the netflixUserList where bio is not null
        defaultNetflixUserShouldBeFound("bio.specified=true");

        // Get all the netflixUserList where bio is null
        defaultNetflixUserShouldNotBeFound("bio.specified=false");
    }
                @Test
    @Transactional
    public void getAllNetflixUsersByBioContainsSomething() throws Exception {
        // Initialize the database
        netflixUserRepository.saveAndFlush(netflixUser);

        // Get all the netflixUserList where bio contains DEFAULT_BIO
        defaultNetflixUserShouldBeFound("bio.contains=" + DEFAULT_BIO);

        // Get all the netflixUserList where bio contains UPDATED_BIO
        defaultNetflixUserShouldNotBeFound("bio.contains=" + UPDATED_BIO);
    }

    @Test
    @Transactional
    public void getAllNetflixUsersByBioNotContainsSomething() throws Exception {
        // Initialize the database
        netflixUserRepository.saveAndFlush(netflixUser);

        // Get all the netflixUserList where bio does not contain DEFAULT_BIO
        defaultNetflixUserShouldNotBeFound("bio.doesNotContain=" + DEFAULT_BIO);

        // Get all the netflixUserList where bio does not contain UPDATED_BIO
        defaultNetflixUserShouldBeFound("bio.doesNotContain=" + UPDATED_BIO);
    }


    @Test
    @Transactional
    public void getAllNetflixUsersByCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        netflixUserRepository.saveAndFlush(netflixUser);

        // Get all the netflixUserList where category equals to DEFAULT_CATEGORY
        defaultNetflixUserShouldBeFound("category.equals=" + DEFAULT_CATEGORY);

        // Get all the netflixUserList where category equals to UPDATED_CATEGORY
        defaultNetflixUserShouldNotBeFound("category.equals=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    public void getAllNetflixUsersByCategoryIsNotEqualToSomething() throws Exception {
        // Initialize the database
        netflixUserRepository.saveAndFlush(netflixUser);

        // Get all the netflixUserList where category not equals to DEFAULT_CATEGORY
        defaultNetflixUserShouldNotBeFound("category.notEquals=" + DEFAULT_CATEGORY);

        // Get all the netflixUserList where category not equals to UPDATED_CATEGORY
        defaultNetflixUserShouldBeFound("category.notEquals=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    public void getAllNetflixUsersByCategoryIsInShouldWork() throws Exception {
        // Initialize the database
        netflixUserRepository.saveAndFlush(netflixUser);

        // Get all the netflixUserList where category in DEFAULT_CATEGORY or UPDATED_CATEGORY
        defaultNetflixUserShouldBeFound("category.in=" + DEFAULT_CATEGORY + "," + UPDATED_CATEGORY);

        // Get all the netflixUserList where category equals to UPDATED_CATEGORY
        defaultNetflixUserShouldNotBeFound("category.in=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    public void getAllNetflixUsersByCategoryIsNullOrNotNull() throws Exception {
        // Initialize the database
        netflixUserRepository.saveAndFlush(netflixUser);

        // Get all the netflixUserList where category is not null
        defaultNetflixUserShouldBeFound("category.specified=true");

        // Get all the netflixUserList where category is null
        defaultNetflixUserShouldNotBeFound("category.specified=false");
    }

    @Test
    @Transactional
    public void getAllNetflixUsersByBirthDateIsEqualToSomething() throws Exception {
        // Initialize the database
        netflixUserRepository.saveAndFlush(netflixUser);

        // Get all the netflixUserList where birthDate equals to DEFAULT_BIRTH_DATE
        defaultNetflixUserShouldBeFound("birthDate.equals=" + DEFAULT_BIRTH_DATE);

        // Get all the netflixUserList where birthDate equals to UPDATED_BIRTH_DATE
        defaultNetflixUserShouldNotBeFound("birthDate.equals=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    public void getAllNetflixUsersByBirthDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        netflixUserRepository.saveAndFlush(netflixUser);

        // Get all the netflixUserList where birthDate not equals to DEFAULT_BIRTH_DATE
        defaultNetflixUserShouldNotBeFound("birthDate.notEquals=" + DEFAULT_BIRTH_DATE);

        // Get all the netflixUserList where birthDate not equals to UPDATED_BIRTH_DATE
        defaultNetflixUserShouldBeFound("birthDate.notEquals=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    public void getAllNetflixUsersByBirthDateIsInShouldWork() throws Exception {
        // Initialize the database
        netflixUserRepository.saveAndFlush(netflixUser);

        // Get all the netflixUserList where birthDate in DEFAULT_BIRTH_DATE or UPDATED_BIRTH_DATE
        defaultNetflixUserShouldBeFound("birthDate.in=" + DEFAULT_BIRTH_DATE + "," + UPDATED_BIRTH_DATE);

        // Get all the netflixUserList where birthDate equals to UPDATED_BIRTH_DATE
        defaultNetflixUserShouldNotBeFound("birthDate.in=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    public void getAllNetflixUsersByBirthDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        netflixUserRepository.saveAndFlush(netflixUser);

        // Get all the netflixUserList where birthDate is not null
        defaultNetflixUserShouldBeFound("birthDate.specified=true");

        // Get all the netflixUserList where birthDate is null
        defaultNetflixUserShouldNotBeFound("birthDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllNetflixUsersByBirthDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        netflixUserRepository.saveAndFlush(netflixUser);

        // Get all the netflixUserList where birthDate is greater than or equal to DEFAULT_BIRTH_DATE
        defaultNetflixUserShouldBeFound("birthDate.greaterThanOrEqual=" + DEFAULT_BIRTH_DATE);

        // Get all the netflixUserList where birthDate is greater than or equal to UPDATED_BIRTH_DATE
        defaultNetflixUserShouldNotBeFound("birthDate.greaterThanOrEqual=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    public void getAllNetflixUsersByBirthDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        netflixUserRepository.saveAndFlush(netflixUser);

        // Get all the netflixUserList where birthDate is less than or equal to DEFAULT_BIRTH_DATE
        defaultNetflixUserShouldBeFound("birthDate.lessThanOrEqual=" + DEFAULT_BIRTH_DATE);

        // Get all the netflixUserList where birthDate is less than or equal to SMALLER_BIRTH_DATE
        defaultNetflixUserShouldNotBeFound("birthDate.lessThanOrEqual=" + SMALLER_BIRTH_DATE);
    }

    @Test
    @Transactional
    public void getAllNetflixUsersByBirthDateIsLessThanSomething() throws Exception {
        // Initialize the database
        netflixUserRepository.saveAndFlush(netflixUser);

        // Get all the netflixUserList where birthDate is less than DEFAULT_BIRTH_DATE
        defaultNetflixUserShouldNotBeFound("birthDate.lessThan=" + DEFAULT_BIRTH_DATE);

        // Get all the netflixUserList where birthDate is less than UPDATED_BIRTH_DATE
        defaultNetflixUserShouldBeFound("birthDate.lessThan=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    public void getAllNetflixUsersByBirthDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        netflixUserRepository.saveAndFlush(netflixUser);

        // Get all the netflixUserList where birthDate is greater than DEFAULT_BIRTH_DATE
        defaultNetflixUserShouldNotBeFound("birthDate.greaterThan=" + DEFAULT_BIRTH_DATE);

        // Get all the netflixUserList where birthDate is greater than SMALLER_BIRTH_DATE
        defaultNetflixUserShouldBeFound("birthDate.greaterThan=" + SMALLER_BIRTH_DATE);
    }


    @Test
    @Transactional
    public void getAllNetflixUsersByListIsEqualToSomething() throws Exception {
        // Initialize the database
        netflixUserRepository.saveAndFlush(netflixUser);
        MyList list = MyListResourceIT.createEntity(em);
        em.persist(list);
        em.flush();
        netflixUser.setList(list);
        netflixUserRepository.saveAndFlush(netflixUser);
        Long listId = list.getId();

        // Get all the netflixUserList where list equals to listId
        defaultNetflixUserShouldBeFound("listId.equals=" + listId);

        // Get all the netflixUserList where list equals to listId + 1
        defaultNetflixUserShouldNotBeFound("listId.equals=" + (listId + 1));
    }


    @Test
    @Transactional
    public void getAllNetflixUsersBySavedSearchesIsEqualToSomething() throws Exception {
        // Initialize the database
        netflixUserRepository.saveAndFlush(netflixUser);
        SavedSearch savedSearches = SavedSearchResourceIT.createEntity(em);
        em.persist(savedSearches);
        em.flush();
        netflixUser.addSavedSearches(savedSearches);
        netflixUserRepository.saveAndFlush(netflixUser);
        Long savedSearchesId = savedSearches.getId();

        // Get all the netflixUserList where savedSearches equals to savedSearchesId
        defaultNetflixUserShouldBeFound("savedSearchesId.equals=" + savedSearchesId);

        // Get all the netflixUserList where savedSearches equals to savedSearchesId + 1
        defaultNetflixUserShouldNotBeFound("savedSearchesId.equals=" + (savedSearchesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNetflixUserShouldBeFound(String filter) throws Exception {
        restNetflixUserMockMvc.perform(get("/api/netflix-users?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(netflixUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)))
            .andExpect(jsonPath("$.[*].bio").value(hasItem(DEFAULT_BIO)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())));

        // Check, that the count call also returns 1
        restNetflixUserMockMvc.perform(get("/api/netflix-users/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNetflixUserShouldNotBeFound(String filter) throws Exception {
        restNetflixUserMockMvc.perform(get("/api/netflix-users?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNetflixUserMockMvc.perform(get("/api/netflix-users/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingNetflixUser() throws Exception {
        // Get the netflixUser
        restNetflixUserMockMvc.perform(get("/api/netflix-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNetflixUser() throws Exception {
        // Initialize the database
        netflixUserService.save(netflixUser);

        int databaseSizeBeforeUpdate = netflixUserRepository.findAll().size();

        // Update the netflixUser
        NetflixUser updatedNetflixUser = netflixUserRepository.findById(netflixUser.getId()).get();
        // Disconnect from session so that the updates on updatedNetflixUser are not directly saved in db
        em.detach(updatedNetflixUser);
        updatedNetflixUser
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .password(UPDATED_PASSWORD)
            .bio(UPDATED_BIO)
            .category(UPDATED_CATEGORY)
            .birthDate(UPDATED_BIRTH_DATE);

        restNetflixUserMockMvc.perform(put("/api/netflix-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedNetflixUser)))
            .andExpect(status().isOk());

        // Validate the NetflixUser in the database
        List<NetflixUser> netflixUserList = netflixUserRepository.findAll();
        assertThat(netflixUserList).hasSize(databaseSizeBeforeUpdate);
        NetflixUser testNetflixUser = netflixUserList.get(netflixUserList.size() - 1);
        assertThat(testNetflixUser.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testNetflixUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testNetflixUser.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testNetflixUser.getBio()).isEqualTo(UPDATED_BIO);
        assertThat(testNetflixUser.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testNetflixUser.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingNetflixUser() throws Exception {
        int databaseSizeBeforeUpdate = netflixUserRepository.findAll().size();

        // Create the NetflixUser

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNetflixUserMockMvc.perform(put("/api/netflix-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(netflixUser)))
            .andExpect(status().isBadRequest());

        // Validate the NetflixUser in the database
        List<NetflixUser> netflixUserList = netflixUserRepository.findAll();
        assertThat(netflixUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteNetflixUser() throws Exception {
        // Initialize the database
        netflixUserService.save(netflixUser);

        int databaseSizeBeforeDelete = netflixUserRepository.findAll().size();

        // Delete the netflixUser
        restNetflixUserMockMvc.perform(delete("/api/netflix-users/{id}", netflixUser.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<NetflixUser> netflixUserList = netflixUserRepository.findAll();
        assertThat(netflixUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
