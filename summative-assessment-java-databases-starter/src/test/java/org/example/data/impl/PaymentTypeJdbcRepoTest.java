package org.example.data.impl;

import org.example.DbResetSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
// @SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {PaymentTypeJdbcRepo.class})
class PaymentTypeJdbcRepoTest extends DbResetSupport {

    @Autowired
    PaymentTypeJdbcRepo repo;

    @Test
    void sanity() {
        assertNotNull(repo, "Repo should be autowired");
    }
}