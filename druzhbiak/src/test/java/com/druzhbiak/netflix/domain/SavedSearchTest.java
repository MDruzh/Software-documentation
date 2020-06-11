package com.druzhbiak.netflix.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.druzhbiak.netflix.web.rest.TestUtil;

public class SavedSearchTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SavedSearch.class);
        SavedSearch savedSearch1 = new SavedSearch();
        savedSearch1.setId(1L);
        SavedSearch savedSearch2 = new SavedSearch();
        savedSearch2.setId(savedSearch1.getId());
        assertThat(savedSearch1).isEqualTo(savedSearch2);
        savedSearch2.setId(2L);
        assertThat(savedSearch1).isNotEqualTo(savedSearch2);
        savedSearch1.setId(null);
        assertThat(savedSearch1).isNotEqualTo(savedSearch2);
    }
}
