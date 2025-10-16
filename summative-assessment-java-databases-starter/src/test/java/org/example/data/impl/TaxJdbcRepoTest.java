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

/**
 * Integration tests for {@link org.example.data.impl.TaxJdbcRepo}.
 * Queries the real MySQL DB to fetch the tax row that covers a given date.
 * Also asserts that the repository throws {@code RecordNotFoundException}
 * when no configured range includes the requested date.
 */

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

class TaxJdbcRepoTest extends DbResetSupport {

    @Autowired
    TaxJdbcRepo repo;

    /**
     * A seeded date should return exactly one current tax row.
     */

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

    /**
     * Asserts that the repo throws when no tax band covers the given date.
     */

    @Test
    void getCurrentTax_throwsWhenNoRangeCoversDate() {
        LocalDate farPast = LocalDate.of(1990, 1, 1);
        assertThrows(RecordNotFoundException.class, () -> repo.getCurrentTax(farPast));
    }
}


