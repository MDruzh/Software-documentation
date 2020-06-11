package com.druzhbiak.netflix.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.time.LocalDate;

/**
 * A Episode.
 */
@Entity
@Table(name = "episode")
public class Episode implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "producer")
    private String producer;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Column(name = "video_url")
    private String videoURL;

    @ManyToOne
    @JsonIgnoreProperties("episodes")
    private Movie series;

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

    public Episode name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Episode description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProducer() {
        return producer;
    }

    public Episode producer(String producer) {
        this.producer = producer;
        return this;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public Episode releaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
        return this;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public Episode videoURL(String videoURL) {
        this.videoURL = videoURL;
        return this;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public Movie getSeries() {
        return series;
    }

    public Episode series(Movie movie) {
        this.series = movie;
        return this;
    }

    public void setSeries(Movie movie) {
        this.series = movie;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Episode)) {
            return false;
        }
        return id != null && id.equals(((Episode) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Episode{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", producer='" + getProducer() + "'" +
            ", releaseDate='" + getReleaseDate() + "'" +
            ", videoURL='" + getVideoURL() + "'" +
            "}";
    }
}
