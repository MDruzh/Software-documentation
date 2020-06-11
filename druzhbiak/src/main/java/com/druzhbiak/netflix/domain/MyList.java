package com.druzhbiak.netflix.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.HashSet;
import java.util.Set;

/**
 * A MyList.
 */
@Entity
@Table(name = "my_list")
public class MyList implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "myList")
    private Set<Movie> movies = new HashSet<>();

    @OneToOne(mappedBy = "list")
    @JsonIgnore
    private NetflixUser user;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Movie> getMovies() {
        return movies;
    }

    public MyList movies(Set<Movie> movies) {
        this.movies = movies;
        return this;
    }

    public MyList addMovies(Movie movie) {
        this.movies.add(movie);
        movie.setMyList(this);
        return this;
    }

    public MyList removeMovies(Movie movie) {
        this.movies.remove(movie);
        movie.setMyList(null);
        return this;
    }

    public void setMovies(Set<Movie> movies) {
        this.movies = movies;
    }

    public NetflixUser getUser() {
        return user;
    }

    public MyList user(NetflixUser netflixUser) {
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
        if (!(o instanceof MyList)) {
            return false;
        }
        return id != null && id.equals(((MyList) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "MyList{" +
            "id=" + getId() +
            "}";
    }
}
