package org.example.data.impl;

import org.example.DbResetSupport;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:mysql://localhost:3306/SimpleBistro?serverTimezone=America/New_York",
        "spring.datasource.username=root",
        "spring.datasource.password=",
        "spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver"
})

class ServerJdbcRepoTest extends DbResetSupport {
    @Autowired ServerJdbcRepo repo;

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