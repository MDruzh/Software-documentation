package com.druzhbiak.netflix.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import com.druzhbiak.netflix.domain.enumeration.Category;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the {@link com.druzhbiak.netflix.domain.NetflixUser} entity. This class is used
 * in {@link com.druzhbiak.netflix.web.rest.NetflixUserResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /netflix-users?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class NetflixUserCriteria implements Serializable, Criteria {
    /**
     * Class for filtering Category
     */
    public static class CategoryFilter extends Filter<Category> {

        public CategoryFilter() {
        }

        public CategoryFilter(CategoryFilter filter) {
            super(filter);
        }

        @Override
        public CategoryFilter copy() {
            return new CategoryFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter email;

    private StringFilter password;

    private StringFilter bio;

    private CategoryFilter category;

    private LocalDateFilter birthDate;

    private LongFilter listId;

    private LongFilter savedSearchesId;

    public NetflixUserCriteria() {
    }

    public NetflixUserCriteria(NetflixUserCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.password = other.password == null ? null : other.password.copy();
        this.bio = other.bio == null ? null : other.bio.copy();
        this.category = other.category == null ? null : other.category.copy();
        this.birthDate = other.birthDate == null ? null : other.birthDate.copy();
        this.listId = other.listId == null ? null : other.listId.copy();
        this.savedSearchesId = other.savedSearchesId == null ? null : other.savedSearchesId.copy();
    }

    @Override
    public NetflixUserCriteria copy() {
        return new NetflixUserCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getEmail() {
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getPassword() {
        return password;
    }

    public void setPassword(StringFilter password) {
        this.password = password;
    }

    public StringFilter getBio() {
        return bio;
    }

    public void setBio(StringFilter bio) {
        this.bio = bio;
    }

    public CategoryFilter getCategory() {
        return category;
    }

    public void setCategory(CategoryFilter category) {
        this.category = category;
    }

    public LocalDateFilter getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDateFilter birthDate) {
        this.birthDate = birthDate;
    }

    public LongFilter getListId() {
        return listId;
    }

    public void setListId(LongFilter listId) {
        this.listId = listId;
    }

    public LongFilter getSavedSearchesId() {
        return savedSearchesId;
    }

    public void setSavedSearchesId(LongFilter savedSearchesId) {
        this.savedSearchesId = savedSearchesId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final NetflixUserCriteria that = (NetflixUserCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(email, that.email) &&
            Objects.equals(password, that.password) &&
            Objects.equals(bio, that.bio) &&
            Objects.equals(category, that.category) &&
            Objects.equals(birthDate, that.birthDate) &&
            Objects.equals(listId, that.listId) &&
            Objects.equals(savedSearchesId, that.savedSearchesId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        email,
        password,
        bio,
        category,
        birthDate,
        listId,
        savedSearchesId
        );
    }

    @Override
    public String toString() {
        return "NetflixUserCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (email != null ? "email=" + email + ", " : "") +
                (password != null ? "password=" + password + ", " : "") +
                (bio != null ? "bio=" + bio + ", " : "") +
                (category != null ? "category=" + category + ", " : "") +
                (birthDate != null ? "birthDate=" + birthDate + ", " : "") +
                (listId != null ? "listId=" + listId + ", " : "") +
                (savedSearchesId != null ? "savedSearchesId=" + savedSearchesId + ", " : "") +
            "}";
    }

}
