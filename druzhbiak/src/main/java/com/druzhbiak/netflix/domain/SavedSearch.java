package com.druzhbiak.netflix.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A SavedSearch.
 */
@Entity
@Table(name = "saved_search")
public class SavedSearch implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "search_text")
    private String searchText;

    @ManyToOne
    @JsonIgnoreProperties("savedSearches")
    private NetflixUser user;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSearchText() {
        return searchText;
    }

    public SavedSearch searchText(String searchText) {
        this.searchText = searchText;
        return this;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public NetflixUser getUser() {
        return user;
    }

    public SavedSearch user(NetflixUser netflixUser) {
        this.user = netflixUser;
        return this;
    }

    public void setUser(NetflixUser netflixUser) {
        this.user = netflixUser;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SavedSearch)) {
            return false;
        }
        return id != null && id.equals(((SavedSearch) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "SavedSearch{" +
            "id=" + getId() +
            ", searchText='" + getSearchText() + "'" +
            "}";
    }
}
