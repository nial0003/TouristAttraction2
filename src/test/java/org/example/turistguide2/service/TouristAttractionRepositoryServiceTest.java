package org.example.turistguide2.service;

import org.example.turistguide2.model.TouristAttraction;
import org.example.turistguide2.repo.TouristAttractionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class TouristAttractionRepositoryServiceTest
{

    private TouristAttractionService service;
    private TouristAttractionRepository repositoryMock;

    @BeforeEach
    void setUp()
    {
        repositoryMock = Mockito.mock(TouristAttractionRepository.class);
        service = new TouristAttractionService(repositoryMock);

        TouristAttraction tivoli = new TouristAttraction("Tivoli", "Forlystelsespark", "København", null);
        tivoli.setId(1);
        service.addTouristAttractionToList(tivoli);
    }

    @Test
    void deleteTouristAttraction_Success()
    {
        TouristAttraction tivoli = new TouristAttraction("Tivoli", "Forlystelsespark", "København", null);
        tivoli.setId(1);

        Mockito.when(repositoryMock.findAttractionByName("Tivoli")).thenReturn(tivoli);
        Mockito.when(repositoryMock.deleteAttraction(tivoli.getId())).thenReturn(true);

        TouristAttraction attraction = service.findAttractionByName("Tivoli");
        assertNotNull(attraction, "Tivoli burde være tilføjet");

        boolean result = service.deleteAttraction(attraction.getId());
        assertTrue(result, "Tivoli burde være slettet");
    }


    @Test
    void deleteTouristAttraction_NotFound()
    {
        TouristAttraction attraction = service.findAttractionByName("NonExisting");
        assertNull(attraction, "Attraktionen burde ikke findes");
    }
}


/*
package org.example.turistguide2.service;

import org.example.turistguide2.model.TouristAttraction;
import org.example.turistguide2.repo.TouristAttractionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TouristAttractionRepositoryServiceTest {

    @Mock
    private TouristAttractionRepository repository;

    @InjectMocks
    private TouristAttractionService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        TouristAttraction tivoli = new TouristAttraction("Tivoli", "Forlystelsespark", "København", null);
        tivoli.setId(1);
        service.addTouristAttractionToList(tivoli);
    }

    @Test
    void deleteTouristAttraction_Success() {
        TouristAttraction tivoli = new TouristAttraction("Tivoli", "Forlystelsespark", "København", null);
        tivoli.setId(1);

        when(repository.findAttractionByName("Tivoli")).thenReturn(tivoli);
        when(repository.deleteAttraction(tivoli.getId())).thenReturn(true);

        TouristAttraction attraction = service.findAttractionByName("Tivoli");
        assertNotNull(attraction, "Tivoli burde være tilføjet");

        boolean result = service.deleteAttraction(attraction.getId());
        assertTrue(result, "Tivoli burde være slettet");
    }




    @Test
    void deleteTouristAttraction_NotFound() {
        TouristAttraction attraction = service.findAttractionByName("NonExisting");
        assertNull(attraction, "Attraktionen burde ikke findes");
    }
}
*/
// NOTE TIL SELV: SLET EFTER
/*
package org.example.turistguide2.service;

import org.example.turistguide2.TuristGuide2Application;
import org.example.turistguide2.model.TouristAttraction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = TuristGuide2Application.class)
class TouristAttractionRepositoryServiceTest
{



    @Autowired
    private TouristAttractionService service;

    @BeforeEach
    void setUp()
    {
        service.addTouristAttractionToList(new TouristAttraction("Tivoli", "Forlystelsespark", "København", null));
    }

    @Test
    void deleteTouristAttraction_Success()
    {
        TouristAttraction attraction = service.findAttractionByName("Tivoli");
        assertNotNull(attraction, "Tivoli burde være tilføjet");

        boolean result = service.deleteAttraction(attraction.getId());

        assertTrue(result, "Tivoli burde være slettet");
        assertNull(service.findAttractionByName("Tivoli"), "Tivoli burder ikke findes længere");

    }

    @Test
    void deleteTouristAttraction_NotFound()
    {
        TouristAttraction attraction = service.findAttractionByName("NonExisting");
        assertNull(attraction, "Attraktionen burde ikke findes");
    }
}*/
