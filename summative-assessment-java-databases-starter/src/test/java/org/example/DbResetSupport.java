package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Base test class that resets the TEST database to a known-good state;
 * before each test method runs
 */

public abstract class DbResetSupport {

    @Autowired
    protected JdbcTemplate jdbc;

    @BeforeEach
    void resetDatabase() {
        // Stored proc defined by src/test/resources/sql/set_known_good_state_mysql.sql
        try {
            jdbc.execute("CALL set_known_good_state();");
        } catch (Exception ignore) {

        }
    }
}
