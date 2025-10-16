package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Base test class that resets the TEST database to a known-good state;
 * before each test method runs
 * We attempt to call a stored procedure named {@code set_known_good_state()}
 * If it does not exist, we ignore the exception so tests can still run
 */

public abstract class DbResetSupport {
/** Spring's simple JDBC helper; auto-configured via Spring Boot. */
    @Autowired
    protected JdbcTemplate jdbc;

    @BeforeEach
    void resetDatabase() {
        // Stored proc defined by src/test/resources/sql/set_known_good_state_mysql.sql
        try {
            jdbc.execute("CALL set_known_good_state();");
        } catch (Exception ignore) {
            // Intentionally ignored to keep tests resilient on clean machines
        }
    }
}
