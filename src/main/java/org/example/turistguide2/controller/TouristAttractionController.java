package org.example.turistguide2.controller;

import org.example.turistguide2.model.TouristAttraction;
import org.example.turistguide2.model.Tags;
import org.example.turistguide2.service.TouristAttractionRepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/attractions")
public class TouristAttractionController {
    private final TouristAttractionRepoService touristAttractionRepoService;

    @Autowired
    public TouristAttractionController(TouristAttractionRepoService touristAttractionRepoService) {
        this.touristAttractionRepoService = touristAttractionRepoService;
    }

    @GetMapping
    public String getAttractions(@RequestParam(value = "search", required = false) String search, Model model) {
        List<TouristAttraction> filteredAttractions = new ArrayList<>(touristAttractionRepoService.getFirstAttractions()); /* by standard it's the first
        10 attractions that will be shown on the html side. */

        if (search != null && !search.isEmpty()) { // if the search bar is not empty the html file will search for what contains search-url-attribute
            filteredAttractions.clear();
            for (TouristAttraction attraction : touristAttractionRepoService.getTouristAttractionList()) {
                if (attraction.getName().toLowerCase().contains(search.toLowerCase())) {
                    filteredAttractions.add(attraction);
                }
            }
        }
        model.addAttribute("attractions", filteredAttractions);
        return "attractionList";
    }

    @GetMapping("/{name}/tags")
    public String getTags(@PathVariable String name, Model model) {

        TouristAttraction touristAttraction = touristAttractionRepoService.findAttractionByName(name);

        model.addAttribute("viewAttraction", touristAttraction);
        return "tags";
    }

    @GetMapping("/{name}/edit")
    public String editAttraction(@PathVariable("name") String name, Model model) {
        TouristAttraction touristAttraction = touristAttractionRepoService.findAttractionByName(name);

        if (touristAttraction == null) {
            return "redirect:/attractions";
        }

        model.addAttribute("attraction", touristAttraction);
        model.addAttribute("tags", Tags.values());
        return "";
    }

    @PostMapping("/update")
    public String updateAttraction(@ModelAttribute("attraction") TouristAttraction touristAttraction,
                                   @RequestParam("tags") List<Tags> tags) {

        touristAttraction.setTags(tags);
        touristAttractionRepoService.updateTouristAttraction(touristAttraction);

        return "updateAttraction";
    }

    @GetMapping("/add")
    public String showAddAttractionForm() {
        return "addAttraction";
    }

    @PostMapping("/save")
    public String saveAttraction(@RequestParam String name,
                                 @RequestParam String description,
                                 @RequestParam String city) {
        TouristAttraction newAttraction = new TouristAttraction(name, description, city, new ArrayList<>());
        touristAttractionRepoService.addTouristAttractionToList(newAttraction);
        return "redirect:/attractions/list";
    }
}
