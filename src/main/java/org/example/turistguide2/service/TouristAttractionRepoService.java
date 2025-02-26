package org.example.turistguide2.service;

import org.example.turistguide2.model.Tags;
import org.example.turistguide2.model.TouristAttraction;
import org.example.turistguide2.repo.TouristAttractionRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TouristAttractionRepoService {
    private final TouristAttractionRepo touristAttractionRepo;

    public TouristAttractionRepoService() {
        touristAttractionRepo = new TouristAttractionRepo();
    }

    public List<TouristAttraction> getTouristAttractionList() {
        return touristAttractionRepo.getTouristAttractionList();
    }

    public List<TouristAttraction> getFirstAttractions () {
        return touristAttractionRepo.getFirstAttractions();
    }

    public void addTouristAttractionToList(TouristAttraction touristAttraction) {
        touristAttractionRepo.addTouristAttractionToList(touristAttraction);
    }

    public boolean deleteTouristAttractionFromList(String name) {
        return touristAttractionRepo.deleteTouristAttractionFromList(name);
    }

    public TouristAttraction findAttractionByName(String name) {
        return touristAttractionRepo.findAttractionByName(name);
    }

    //    public void updateTouristAttraction(String name, String newDescription, List<Tags> newTags){
//        touristAttractionRepo.updateTouristAttraction(name,newDescription,newTags);
//    }
    public void updateTouristAttraction(TouristAttraction touristAttraction){
        touristAttractionRepo.updateTouristAttraction(touristAttraction);
    }
}
