package org.eolang;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {
    @Test
    public void test() {
        MatcherAssert.assertThat(
            true,
            Matchers.equalTo(true)
        );
    }
}
