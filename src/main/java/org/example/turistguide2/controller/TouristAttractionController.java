package org.example.turistguide2.controller;

import org.example.turistguide2.model.Tag;
import org.example.turistguide2.model.TouristAttraction;
import org.example.turistguide2.service.TouristAttractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/attractions")
public class TouristAttractionController {
    private final TouristAttractionService touristAttractionService;

    @Autowired
    public TouristAttractionController(TouristAttractionService touristAttractionService) {
        this.touristAttractionService = touristAttractionService;
    }

    @GetMapping
    public String getAttractions(@RequestParam(value = "search", required = false) String search, Model model) {
        List<TouristAttraction> filteredAttractions = new ArrayList<>(touristAttractionService.getFirstAttractions()); /* by standard it's the first
        10 attractions that will be shown on the html side. */

        if (search != null && !search.isEmpty()) { // if the search bar is not empty the html file will search for what contains search-url-attribute
            filteredAttractions.clear();
            for (TouristAttraction attraction : touristAttractionService.findAll()) {
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

        TouristAttraction touristAttraction = touristAttractionService.findAttractionByName(name);

        model.addAttribute("viewAttraction", touristAttraction);
        return "tags";
    }

    @GetMapping("/{name}/edit")
    public String editAttraction(@PathVariable("name") String name, Model model) {
        // Ensure we always fetch the latest version of the attraction
        TouristAttraction touristAttraction = touristAttractionService.findAttractionByName(name);

        if (touristAttraction == null) {
            return "redirect:/attractions";
        }

        List<String> tagList = new ArrayList<>(touristAttractionService.getAvailableTags().keySet());
        List<String> attractionTags = touristAttraction.convertTagsToStringList();


        model.addAttribute("attraction", touristAttraction);
        model.addAttribute("availableTags", tagList);
        model.addAttribute("attractionTags", attractionTags);
        return "updateAttraction";
    }


    @PostMapping("/update")
    public String updateAttraction(@RequestParam("name") String name,
                                   @RequestParam(value = "description", required = false) String newDescription,
                                   @RequestParam(value = "selectedTags", required = false) List<String> selectedTags) {

        TouristAttraction touristAttraction = touristAttractionService.findAttractionByName(name);

        if (touristAttraction == null) {
            throw new RuntimeException("Attraction not found: " + name);
        }

        if (newDescription != null && !newDescription.trim().isEmpty()) {
            touristAttraction.setDescription(newDescription);
        }

        if (selectedTags != null && !selectedTags.isEmpty()) {
            List<Tag> checkedTags = new ArrayList<>();
            Map<String, Integer> availableTags = touristAttractionService.getAvailableTags(); // Fetch once

            for (String selectedTag : selectedTags) {
                if (availableTags.containsKey(selectedTag)) {
                    Integer tagId = availableTags.get(selectedTag);
                    checkedTags.add(new Tag(tagId, selectedTag));
                }
            }
            touristAttraction.setTags(checkedTags);
        } else {
            touristAttraction.setTags(new ArrayList<>()); // Ensure empty list if no tags are selected
        }

        touristAttractionService.updateTouristAttraction(touristAttraction);
        return "redirect:/attractions#attractions";
    }


    @GetMapping("/add")
    public String addAttraction(Model model) {
        TouristAttraction touristAttraction = new TouristAttraction();
        List<String> tagList = new ArrayList<>();
        tagList.addAll(touristAttractionService.getAvailableTags().keySet());

        model.addAttribute("tags", tagList);
        model.addAttribute("touristAttraction", touristAttraction);
        return "addAttraction";
    }

    @PostMapping("/save")
    public String saveAttraction(@ModelAttribute TouristAttraction touristAttraction,
                                 @RequestParam(value = "selectedTags", required = false) List<String> selectedTags) {

        Map<String, Integer> availableTags = touristAttractionService.getAvailableTags();
        List<Tag> checkedTags = new ArrayList<>();

        for (String tagName : selectedTags) {
            if (availableTags.containsKey(tagName)) { //
                Integer tagId = availableTags.get(tagName);
                checkedTags.add(new Tag(tagId, tagName)); //
            }
        }

        touristAttraction.setTags(checkedTags);
        touristAttractionService.addTouristAttractionToList(touristAttraction);

        return "redirect:/attractions#attractions";
    }


    /*
        // Display the delete confirmation page
        @GetMapping("/attractions/{name}/delete")
        public String showDeleteForm(@PathVariable("name") String name, Model model) {
            TouristAttraction attraction = touristAttractionRepoService.findAttractionByName(name);
            if (attraction != null) {
                model.addAttribute("attraction", attraction);
                return "delete_attraction"; // Return the delete confirmation page
            }
            return "error"; // Handle error if the attraction is not found
        }
    */
    @PostMapping("/delete/{name}")
    public String deleteAttraction(@PathVariable String name, RedirectAttributes redirectAttributes) {
       touristAttractionService.deleteAttraction(touristAttractionService.findAttractionByName(name).getId());


        return "redirect:/attractions#attractions";
    }

}
