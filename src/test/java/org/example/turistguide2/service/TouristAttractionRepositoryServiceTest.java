package org.example.turistguide2.service;

import org.example.turistguide2.model.TouristAttraction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TouristAttractionRepositoryServiceTest {

    @Autowired
    private TouristAttractionRepoService service;

    @BeforeEach
    void setUp() {
        service.addTouristAttractionToList(new TouristAttraction("Tivoli", "Forlystelsespark", "København", null));
    }

    @Test
    void deleteTouristAttraction_Success() {
        boolean result = service.deleteTouristAttractionFromList("Tivoli");

        assertTrue(result, "Tivoli burde være slettet");
        assertNull(service.findAttractionByName("Tivoli"), "Tivoli burde ikke findes længere");
    }

    @Test
    void deleteTouristAttraction_NotFound() {
        boolean result = service.deleteTouristAttractionFromList("IkkeEksisterende");

        assertFalse(result, "IkkeEksisterende burde ikke kunne slettes");
    }
}
