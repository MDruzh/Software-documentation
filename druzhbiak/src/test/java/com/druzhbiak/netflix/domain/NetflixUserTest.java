package com.druzhbiak.netflix.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.druzhbiak.netflix.web.rest.TestUtil;

public class NetflixUserTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NetflixUser.class);
        NetflixUser netflixUser1 = new NetflixUser();
        netflixUser1.setId(1L);
        NetflixUser netflixUser2 = new NetflixUser();
        netflixUser2.setId(netflixUser1.getId());
        assertThat(netflixUser1).isEqualTo(netflixUser2);
        netflixUser2.setId(2L);
        assertThat(netflixUser1).isNotEqualTo(netflixUser2);
        netflixUser1.setId(null);
        assertThat(netflixUser1).isNotEqualTo(netflixUser2);
    }
}
