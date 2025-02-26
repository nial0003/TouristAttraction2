package org.example.turistguide2.repo;

import org.example.turistguide2.model.Tags;
import org.example.turistguide2.model.TouristAttraction;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
@Repository
public class TouristAttractionRepo {
    private List<TouristAttraction> touristAttractionList;

    public TouristAttractionRepo() {
        this.touristAttractionList = List.of(
                new TouristAttraction("Tivoli", "Amusement park in copenhagen", "Copenhagen", List.of(Tags.CHILD_FRIENDLY)),
                new TouristAttraction("Amalienborg", "Where the king and queen lives", "Copenhagen", List.of(Tags.ROYAL_FAMILY, Tags.CULTURAL_INHERITANCE, Tags.HISTORY)),
                new TouristAttraction("Viking Ship Museum", "A museum about the vikings and their ships", "Roskilde", List.of(Tags.HISTORY, Tags.MUSEUM, Tags.CHILD_FRIENDLY, Tags.CULTURAL_INHERITANCE)),
                new TouristAttraction("Legoland Billund", "A theme park based on Lego bricks", "Billund", List.of(Tags.CHILD_FRIENDLY, Tags.CULTURAL_INHERITANCE)),
                new TouristAttraction("Aarhus Art Museum (ARoS)", "A modern art museum with the famous rainbow panorama", "Aarhus", List.of(Tags.ART, Tags.MUSEUM)),
                new TouristAttraction("Kronborg Castle", "The castle that inspired Shakespeare's Hamlet", "Helsingør", List.of(Tags.HISTORY, Tags.CULTURAL_INHERITANCE, Tags.ROYAL_FAMILY)),
                new TouristAttraction("Ribe Viking Center", "An open-air museum showcasing Viking life", "Ribe", List.of(Tags.HISTORY, Tags.CHILD_FRIENDLY, Tags.CULTURAL_INHERITANCE)),
                new TouristAttraction("Skagen Grenen", "The northernmost point of Denmark where two seas meet", "Skagen", List.of(Tags.NATURE, Tags.GRATIS)),
                new TouristAttraction("Den Gamle By", "An open-air museum depicting Danish urban history", "Aarhus", List.of(Tags.HISTORY, Tags.MUSEUM, Tags.CULTURAL_INHERITANCE)),
                new TouristAttraction("Møns Klint", "Beautiful white chalk cliffs with stunning nature views", "Møn", List.of(Tags.NATURE, Tags.GRATIS)));
    }

    public List<TouristAttraction> getTouristAttractionList(){
        return touristAttractionList;
    }

    public List<TouristAttraction> getFirstAttractions() {
        List<TouristAttraction> firstAttractions = new ArrayList<>();

        // Ensure we don't exceed the actual list size
        int limit = Math.min(8, touristAttractionList.size());

        for (int i = 0; i < limit; i++) {
            firstAttractions.add(touristAttractionList.get(i));
        }

        return firstAttractions;
    }

    public void addTouristAttractionToList(TouristAttraction touristAttraction){
            touristAttractionList.add(touristAttraction);
    }

    public boolean deleteTouristAttractionFromList(String name){
        return touristAttractionList.removeIf(touristAttraction -> touristAttraction.getName().equalsIgnoreCase(name));
    }

    public TouristAttraction findAttractionByName(String name){
        for (TouristAttraction touristAttraction : touristAttractionList){
            if (touristAttraction.getName().equalsIgnoreCase(name)){
                return touristAttraction;
            }
        }
        return null;
    }

//    public void updateTouristAttraction(String name, String newDescription, List<Tags> newTags){
//        TouristAttraction touristAttractionToBeUpdated = findAttractionByName(name);
//
//        if (touristAttractionToBeUpdated != null){
//            if (!newDescription.isEmpty()){
//                touristAttractionToBeUpdated.setDescription(newDescription);
//            }
//            touristAttractionToBeUpdated.setTags(newTags);
//        }
//    }
    public void updateTouristAttraction(TouristAttraction touristAttraction){
        TouristAttraction attractionToBeUpdated = findAttractionByName(touristAttraction.getName());

        if (attractionToBeUpdated != null){
            attractionToBeUpdated.setDescription(touristAttraction.getDescription());
            attractionToBeUpdated.setTags(touristAttraction.getTags());
        }
    }
}
