package org.example.turistguide2.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TouristAttraction {
    private int id;
    private String name;
    private String description;
    private String city;
    private List<Tag> tags;


    public TouristAttraction(int id, String name, String description, String city, List<Tag> tags) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.city = city;
        this.tags = tags;
    }

    public TouristAttraction(String name, String description, String city, List<Tag> tags) {
        this.name = name;
        this.description = description;
        this.city = city;
        this.tags = tags;
    }

    public TouristAttraction(String name, String description, String city) {
        this.name = name;
        this.description = description;
        this.city = city;
    }

    public TouristAttraction() {
        this.tags = new ArrayList<>();
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public void addTag (Tag tag) {
        this.tags.add(tag);
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> convertTagsToStringList() {
        List<Tag> tags = this.tags;
        return tags.stream()
                .map(Tag::getTagName) // Extract the name from each Tag
                .collect(Collectors.toList()); // Collect the names into a List<String>
    }
}
