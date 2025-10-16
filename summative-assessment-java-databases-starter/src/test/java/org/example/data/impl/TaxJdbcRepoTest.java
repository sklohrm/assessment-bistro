package org.example.data.impl;

import org.example.DbResetSupport;
import org.example.data.exceptions.RecordNotFoundException;
import org.example.model.Tax;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:mysql://localhost:3306/SimpleBistro?serverTimezone=America/New_York",
        "spring.datasource.username=root",
        "spring.datasource.password=",
        "spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver"
})

class TaxJdbcRepoTest extends DbResetSupport {

    @Autowired
    TaxJdbcRepo repo;

    @Test
    void getCurrentTax_returnsRow_forSeededDate() throws Exception {
        LocalDate dateOf = LocalDate.of(2022, 6, 1);

        Tax tax = repo.getCurrentTax(dateOf);

        assertNotNull(tax);
        assertNotNull(tax.getTaxPercentage(), "Tax percentage should be set");
        assertTrue(
                (tax.getStartDate() == null || !dateOf.isBefore(tax.getStartDate())) &&
                        (tax.getEndDate() == null || !dateOf.isAfter(tax.getEndDate())),
                "Returned tax should cover the requested date"
        );
    }

    @Test
    void getCurrentTax_throwsWhenNoRangeCoversDate() {
        LocalDate farPast = LocalDate.of(1990, 1, 1);
        assertThrows(RecordNotFoundException.class, () -> repo.getCurrentTax(farPast));
    }
}


