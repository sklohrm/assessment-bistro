package org.example.data.impl;

import org.example.DbResetSupport;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for {@link ServerJdbcRepo}.
 * Runs against the real MySQL database (NOT H2), pinned via @TestPropertySource.
 * DbResetSupport attempts to reset state before each test.
 */

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)


class ServerJdbcRepoTest extends DbResetSupport {
    @Autowired ServerJdbcRepo repo;

    /**
     * Returns at least one available server on a seeded date; spot-checks fields.
     */

    @Test
    void getAllAvailableServers_onKnownDate_returnsList() throws Exception {
        var date = java.time.LocalDate.of(2022, 6, 1);
        var list = repo.getAllAvailableServers(date);

        assertNotNull(list);
        assertFalse(list.isEmpty(), "Should return seeded servers on " + date);

        var s = list.get(0);
        assertNotNull(s.getFirstName());
        assertNotNull(s.getHireDate());
    }

    /**
     * Gets a real ID from the availability list and verifies find-by-id.
     * Avoids hard-coding IDs that may vary by seed order.
     */

    @Test
    void getServerById_returnsOne_forExistingId() throws Exception {
        var date = java.time.LocalDate.of(2022, 6, 1);
        var list = repo.getAllAvailableServers(date);
        assertFalse(list.isEmpty(), "Need at least one sever on " + date);

        int id = list.get(0).getServerID();
        var s = repo.getServerById(id);

        assertNotNull(s);
        assertEquals(id, s.getServerID());
    }
}