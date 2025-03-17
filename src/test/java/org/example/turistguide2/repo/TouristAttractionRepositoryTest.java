package org.example.turistguide2.repo;

import org.example.turistguide2.model.TouristAttraction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class TouristAttractionRepositoryTest {

    private TouristAttractionRepository repo;

    @BeforeEach
    void setUp() {
        repo = mock(TouristAttractionRepository.class);
        repo.createAttraction(new TouristAttraction("Tivoli", "Forlystelsespark", "København", null));
        repo.createAttraction(new TouristAttraction("Legoland", "Forlystelsespark", "Billund", null));
    }

    @Test
    void deleteTouristAttractionFromList_Success() {
        boolean result = repo.deleteAttraction(repo.findAttractionByName("tivoli").getId());

        assertTrue(result, "Tivoli burde være slettet");
        assertNull(repo.findAttractionByName("Tivoli"), "Tivoli burde ikke findes længere");
    }

    @Test
    void deleteTouristAttractionFromList_NotFound() {
        boolean result = repo.deleteAttraction(10032423);

        assertFalse(result, "FalskAttraktion burde ikke kunne slettes");
    }
}
