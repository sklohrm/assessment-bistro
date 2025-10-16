package org.example.data.impl;

import org.example.DbResetSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for {@link org.example.data.impl.PaymentTypeJdbcRepo}.
 * These tests run against the real MySQL database (NOT H2).
 * The datasource is pinned via {@code @TestPropertySource} and H2 replacement
 * is disabled via {@code @AutoConfigureTestDatabase(replace = NONE)}.
 * {@link org.example.DbResetSupport} attempts to reset DB state before each test.
 */

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PaymentTypeJdbcRepoTest extends DbResetSupport {

    @Autowired
    PaymentTypeJdbcRepo repo;

    /**
     * Sanity check: Spring should create the bean and wire JdbcTemplate correctly.
     */

    @Test
    void sanity() {
        assertNotNull(repo, "Repo should be autowired");
    }

    /**
     * Verifies that the repository returns at least one seeded payment type.
     */

    @Test
    void getAll_returnsRows() throws Exception {
        assertNotNull(repo, "Repo should be autowired");
        var list = repo.getAll();
        assertNotNull(list);
        assertTrue(list.size() > 0, "Should return seeded payment types");
    }
}
