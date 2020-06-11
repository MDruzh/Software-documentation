package com.druzhbiak.netflix.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.druzhbiak.netflix.domain.enumeration.Category;

/**
 * A NetflixUser.
 */
@Entity
@Table(name = "netflix_user")
public class NetflixUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "bio")
    private String bio;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private Category category;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @OneToOne
    @JoinColumn(unique = true)
    private MyList list;

    @OneToMany(mappedBy = "user")
    private Set<SavedSearch> savedSearches = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public NetflixUser name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public NetflixUser email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public NetflixUser password(String password) {
        this.password = password;
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBio() {
        return bio;
    }

    public NetflixUser bio(String bio) {
        this.bio = bio;
        return this;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Category getCategory() {
        return category;
    }

    public NetflixUser category(Category category) {
        this.category = category;
        return this;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public NetflixUser birthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public MyList getList() {
        return list;
    }

    public NetflixUser list(MyList myList) {
        this.list = myList;
        return this;
    }

    public void setList(MyList myList) {
        this.list = myList;
    }

    public Set<SavedSearch> getSavedSearches() {
        return savedSearches;
    }

    public NetflixUser savedSearches(Set<SavedSearch> savedSearches) {
        this.savedSearches = savedSearches;
        return this;
    }

    public NetflixUser addSavedSearches(SavedSearch savedSearch) {
        this.savedSearches.add(savedSearch);
        savedSearch.setUser(this);
        return this;
    }

    public NetflixUser removeSavedSearches(SavedSearch savedSearch) {
        this.savedSearches.remove(savedSearch);
        savedSearch.setUser(null);
        return this;
    }

    public void setSavedSearches(Set<SavedSearch> savedSearches) {
        this.savedSearches = savedSearches;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NetflixUser)) {
            return false;
        }
        return id != null && id.equals(((NetflixUser) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "NetflixUser{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", email='" + getEmail() + "'" +
            ", password='" + getPassword() + "'" +
            ", bio='" + getBio() + "'" +
            ", category='" + getCategory() + "'" +
            ", birthDate='" + getBirthDate() + "'" +
            "}";
    }
}
