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
 * Criteria class for the {@link com.druzhbiak.netflix.domain.SavedSearch} entity. This class is used
 * in {@link com.druzhbiak.netflix.web.rest.SavedSearchResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /saved-searches?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SavedSearchCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter searchText;

    private LongFilter userId;

    public SavedSearchCriteria() {
    }

    public SavedSearchCriteria(SavedSearchCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.searchText = other.searchText == null ? null : other.searchText.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
    }

    @Override
    public SavedSearchCriteria copy() {
        return new SavedSearchCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getSearchText() {
        return searchText;
    }

    public void setSearchText(StringFilter searchText) {
        this.searchText = searchText;
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
        final SavedSearchCriteria that = (SavedSearchCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(searchText, that.searchText) &&
            Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        searchText,
        userId
        );
    }

    @Override
    public String toString() {
        return "SavedSearchCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (searchText != null ? "searchText=" + searchText + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
            "}";
    }

}
