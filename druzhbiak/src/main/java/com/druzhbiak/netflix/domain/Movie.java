package com.druzhbiak.netflix.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.druzhbiak.netflix.domain.enumeration.Type;

import com.druzhbiak.netflix.domain.enumeration.Genre;

/**
 * A Movie.
 */
@Entity
@Table(name = "movie")
public class Movie implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private Type type;

    @Enumerated(EnumType.STRING)
    @Column(name = "genre")
    private Genre genre;

    @Column(name = "creator")
    private String creator;

    @Column(name = "rating")
    private Double rating;

    @Column(name = "link")
    private String link;

    @Column(name = "available_in_hd")
    private Boolean availableInHD;

    @OneToMany(mappedBy = "series")
    private Set<Episode> episodes = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("movies")
    private MyList myList;

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

    public Movie name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Movie description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public Movie releaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
        return this;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Type getType() {
        return type;
    }

    public Movie type(Type type) {
        this.type = type;
        return this;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Genre getGenre() {
        return genre;
    }

    public Movie genre(Genre genre) {
        this.genre = genre;
        return this;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public String getCreator() {
        return creator;
    }

    public Movie creator(String creator) {
        this.creator = creator;
        return this;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Double getRating() {
        return rating;
    }

    public Movie rating(Double rating) {
        this.rating = rating;
        return this;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getLink() {
        return link;
    }

    public Movie link(String link) {
        this.link = link;
        return this;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Boolean isAvailableInHD() {
        return availableInHD;
    }

    public Movie availableInHD(Boolean availableInHD) {
        this.availableInHD = availableInHD;
        return this;
    }

    public void setAvailableInHD(Boolean availableInHD) {
        this.availableInHD = availableInHD;
    }

    public Set<Episode> getEpisodes() {
        return episodes;
    }

    public Movie episodes(Set<Episode> episodes) {
        this.episodes = episodes;
        return this;
    }

    public Movie addEpisodes(Episode episode) {
        this.episodes.add(episode);
        episode.setSeries(this);
        return this;
    }

    public Movie removeEpisodes(Episode episode) {
        this.episodes.remove(episode);
        episode.setSeries(null);
        return this;
    }

    public void setEpisodes(Set<Episode> episodes) {
        this.episodes = episodes;
    }

    public MyList getMyList() {
        return myList;
    }

    public Movie myList(MyList myList) {
        this.myList = myList;
        return this;
    }

    public void setMyList(MyList myList) {
        this.myList = myList;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Movie)) {
            return false;
        }
        return id != null && id.equals(((Movie) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Movie{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", releaseDate='" + getReleaseDate() + "'" +
            ", type='" + getType() + "'" +
            ", genre='" + getGenre() + "'" +
            ", creator='" + getCreator() + "'" +
            ", rating=" + getRating() +
            ", link='" + getLink() + "'" +
            ", availableInHD='" + isAvailableInHD() + "'" +
            "}";
    }
}
