package org.example.data.impl;

import org.example.DbResetSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PaymentTypeJdbcRepoTest extends DbResetSupport {

    @Autowired
    PaymentTypeJdbcRepo repo;

    @Test
    void getAll_returnsRows() throws Exception {
        assertNotNull(repo, "Repo should be autowired");
        var list = repo.getAll();
        assertNotNull(list);
        assertTrue(list.size() > 0, "Should return seeded payment types");
    }
}
