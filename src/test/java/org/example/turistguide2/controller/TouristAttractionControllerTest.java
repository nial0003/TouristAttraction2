package org.example.turistguide2.controller;

import org.example.turistguide2.model.Tag;
import org.example.turistguide2.model.TouristAttraction;
import org.example.turistguide2.service.TouristAttractionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class TouristAttractionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TouristAttractionService touristAttractionService;

    @InjectMocks
    private TouristAttractionController touristAttractionController;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(touristAttractionController).build();
    }

    @Test
    void testGetAttractions() throws Exception {
        TouristAttraction attraction1 = new TouristAttraction();
        attraction1.setName("Bakken");
        TouristAttraction attraction2 = new TouristAttraction();
        attraction2.setName("Tivoli");

        List<TouristAttraction> attractions = Arrays.asList(attraction1, attraction2);
        when(touristAttractionService.getFirstAttractions()).thenReturn(attractions);

        mockMvc.perform(get("/attractions"))
                .andExpect(status().isOk())
                .andExpect(view().name("attractionList"))
                .andExpect(model().attributeExists("attractions"))
                .andExpect(model().attribute("attractions", attractions));

        verify(touristAttractionService, times(1)).getFirstAttractions();
    }

    @Test
    void testGetTags() throws Exception {
        String attractionName = "Bakken";
        TouristAttraction attraction = new TouristAttraction();
        attraction.setName(attractionName);

        when(touristAttractionService.findAttractionByName(attractionName)).thenReturn(attraction);

        mockMvc.perform(get("/attractions/{name}/tags", "Bakken"))
                .andExpect(status().isOk())
                .andExpect(view().name("tags.html"))
                .andExpect(model().attributeExists("viewAttraction"))
                .andExpect(model().attribute("viewAttraction", attraction));

        verify(touristAttractionService, times(1)).findAttractionByName(attractionName);
    }

    @Test
    public void testEditAttraction() throws Exception{
        Map<String, Integer> mockTags = Map.of("GRATIS", 1, "CHILD_FRIENDLY", 2);

        TouristAttraction touristAttraction = new TouristAttraction("Bakken", "Amusement Park",
                "CPH", List.of(new Tag(1, "GRATIS"), new Tag(2, "CHILD_FRIENDLY")));

        TouristAttraction spyAttraction = Mockito.spy(touristAttraction);

        when(touristAttractionService.findAttractionByName("Bakken")).thenReturn(spyAttraction);
        when(touristAttractionService.getAvailableTags()).thenReturn(mockTags);
        doReturn(List.of("GRATIS", "CHILD_FRIENDLY")).when(spyAttraction).convertTagsToStringList();

        mockMvc.perform(get("/attractions/{name}/edit", touristAttraction.getName()))
                .andExpect(status().isOk())
                .andExpect(view().name("updateAttraction"))
                .andExpect(model().attributeExists("attraction"))
                .andExpect(model().attributeExists("availableTags"))
                .andExpect(model().attributeExists("attractionTags"))
                .andExpect(model().attribute("attraction", hasProperty("name", equalTo("Bakken"))))
                .andExpect(model().attribute("attraction", hasProperty("description", equalTo("Amusement Park"))))
                .andExpect(model().attribute("attraction", hasProperty("city", equalTo("CPH"))))
                .andExpect(model().attribute("availableTags", containsInAnyOrder("GRATIS", "CHILD_FRIENDLY")))
                .andExpect(model().attribute("attractionTags", containsInAnyOrder("GRATIS", "CHILD_FRIENDLY")));

        verify(touristAttractionService, times(1)).findAttractionByName("Bakken");
        verify(touristAttractionService, times(1)).getAvailableTags();

    }

    @Test
    public void testUpdateAttraction() throws Exception {
        TouristAttraction touristAttraction = new TouristAttraction();
        touristAttraction.setName("Bakken");
        touristAttraction.setDescription("Amusement park in Copenhagen");
        touristAttraction.setCity("CPH");
        touristAttraction.setTags(new ArrayList<>());

        Map<String, Integer> availableTags = new HashMap<>();
        availableTags.put("GRATIS", 1);
        availableTags.put("CHILD_FRIENDLY", 2);

        when(touristAttractionService.findAttractionByName("Bakken")).thenReturn(touristAttraction);
        when(touristAttractionService.getAvailableTags()).thenReturn(availableTags);

        mockMvc.perform(post("/attractions/update")
                        .param("name", "Bakken")
                        .param("description", "new description")
                        .param("selectedTags", "GRATIS", "CHILD_FRIENDLY"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/attractions#attractions"));

        verify(touristAttractionService, times(1)).findAttractionByName("Bakken");
        verify(touristAttractionService, times(1)).updateTouristAttraction(any());
    }

    @Test
    public void testSaveAttraction() throws Exception {
        mockMvc.perform(post("/attractions/save")
                        .param("selectedTags", "GRAITS", "CHILD_FRIENDLY")
                        .flashAttr("touristAttraction", new TouristAttraction("Bakken", "Amustment Park", "CPH")))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/attractions#attractions"));

        verify(touristAttractionService, times(1)).addTouristAttractionToList(any());
    }

    @Test
    public void testDeleteAttraction() throws Exception {
        String attractionName = "Bakken";
        TouristAttraction touristAttraction = new TouristAttraction(attractionName, "Amusement Park", "CPH");
        touristAttraction.setId(1);

        when(touristAttractionService.findAttractionByName(attractionName)).thenReturn(touristAttraction);
        doNothing().when(touristAttractionService).deleteAttraction(touristAttraction.getId());

        mockMvc.perform(post("/attractions/delete/{name}", attractionName))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/attractions#attractions"));

        verify(touristAttractionService, times(1)).findAttractionByName(attractionName);
        verify(touristAttractionService, times(1)).deleteAttraction(1);
    }

    @Test
    public void testAddAttractionPage() throws Exception {
        Map<String, Integer> mockTags = new HashMap<>();
        mockTags.put("GRATIS", 1);
        mockTags.put("CHILD_FRIENDLY", 2);

        when(touristAttractionService.getAvailableTags()).thenReturn(mockTags);

        mockMvc.perform(get("/attractions/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("addAttraction"))
                .andExpect(model().attributeExists("tags"))
                .andExpect(model().attributeExists("touristAttraction"))
                .andExpect(model().attribute("tags", new ArrayList<>(mockTags.keySet())));

        verify(touristAttractionService, times(1)).getAvailableTags();
    }
}
