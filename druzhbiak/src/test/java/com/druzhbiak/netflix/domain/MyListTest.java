package com.druzhbiak.netflix.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.druzhbiak.netflix.web.rest.TestUtil;

public class MyListTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MyList.class);
        MyList myList1 = new MyList();
        myList1.setId(1L);
        MyList myList2 = new MyList();
        myList2.setId(myList1.getId());
        assertThat(myList1).isEqualTo(myList2);
        myList2.setId(2L);
        assertThat(myList1).isNotEqualTo(myList2);
        myList1.setId(null);
        assertThat(myList1).isNotEqualTo(myList2);
    }
}
