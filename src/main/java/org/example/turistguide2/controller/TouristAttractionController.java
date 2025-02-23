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

    @GetMapping("/list")
    public String getAttraction() {
        return "attractionList";
    }

    @GetMapping("attractions/{name}/edit")
    public String editAttraction(@PathVariable("name") String name, Model model) {
        TouristAttraction touristAttraction = touristAttractionRepoService.findAttractionByName(name);

        if (touristAttraction == null) {
            return "redirect:/attractions";
        }

        model.addAttribute("attraction", touristAttraction);
        model.addAttribute("tags", Tags.values());
        return "";
    }

    @PostMapping("attractions/update")
    public String updateAttraction(@ModelAttribute("attraction") TouristAttraction touristAttraction,
                                   @RequestParam("tags") List<Tags> tags) {

        touristAttraction.setTags(tags);
        touristAttractionRepoService.updateTouristAttraction(touristAttraction);

        return "";
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

    @PostMapping("/attractions/delete/{name}")
    public String deleteAttraction(@PathVariable String name) {
        boolean deleted = touristAttractionRepoService.deleteTouristAttractionFromList(name);
        if (deleted) {
            return "redirect:/attractions/list"; // Gå tilbage til listen efter sletning
        } else {
            return "error"; // Vis en fejlside, hvis attraktionen ikke fandtes
        }
    }

}
