package org.example.turistguide2.controller;

import org.example.turistguide2.model.TouristAttraction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

@WebMvcTest(TouristAttractionControllerTest.class)
class TouristAttractionControllerTest {

    private List<TouristAttraction> attractions;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TouristAttractionRepoService touristAttractionRepoService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown(){

    }

    @Test
    void getAttraction() {
    }

    @Test
    void showAddAttractionForm() {
    }

    @Test
    void saveAttraction() throws Exception {

    }

    @Test
    void editAttraction() {
    }

    @Test
    void deleteAttraction() {
    }
}