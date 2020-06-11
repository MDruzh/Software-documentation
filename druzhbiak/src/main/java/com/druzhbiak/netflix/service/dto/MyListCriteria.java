package com.druzhbiak.netflix.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.druzhbiak.netflix.domain.MyList} entity. This class is used
 * in {@link com.druzhbiak.netflix.web.rest.MyListResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /my-lists?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class MyListCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter moviesId;

    private LongFilter userId;

    public MyListCriteria() {
    }

    public MyListCriteria(MyListCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.moviesId = other.moviesId == null ? null : other.moviesId.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
    }

    @Override
    public MyListCriteria copy() {
        return new MyListCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getMoviesId() {
        return moviesId;
    }

    public void setMoviesId(LongFilter moviesId) {
        this.moviesId = moviesId;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MyListCriteria that = (MyListCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(moviesId, that.moviesId) &&
            Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        moviesId,
        userId
        );
    }

    @Override
    public String toString() {
        return "MyListCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (moviesId != null ? "moviesId=" + moviesId + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
            "}";
    }

}
