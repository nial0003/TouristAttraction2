package org.example.turistguide2.repo;

import org.example.turistguide2.model.Tag;
import org.example.turistguide2.model.TouristAttraction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.*;


@Repository
public class TouristAttractionRepository {
    private final Map<String, Integer> availableTags = new HashMap<>();
    private final String url;
    private final String username;
    private final String password;

    @Autowired
    public TouristAttractionRepository(
            @Value("${spring.datasource.url}") String url,
            @Value("${spring.datasource.username}") String username,
            @Value("${spring.datasource.password}") String password) {
        this.url = url;
        this.username = username;
        this.password = password;
        loadAvailableTags(); //
    }

    // get all tags from database
    private void loadAvailableTags() {
        String sql = "SELECT tag_id, tag_name FROM tags";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                int tagId = resultSet.getInt("tag_id");
                String tagName = resultSet.getString("tag_name");
                availableTags.put(tagName, tagId);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Could not load available tags: " + e.getMessage());
        }
    }

    public Map<String, Integer> getAvailableTags() {
        return availableTags;
    }


    // Find all
    public List<TouristAttraction> findAll() {
        String sql = "SELECT a.attraction_id, a.attraction_name, a.attraction_description, a.attraction_city, " +
                "t.tag_id, t.tag_name " +
                "FROM attractions a " +
                "LEFT JOIN tags_for_attractions tfa ON a.attraction_id = tfa.attraction_id " +
                "LEFT JOIN tags t ON t.tag_id = tfa.tag_id";

        HashMap<Integer, TouristAttraction> foundAttractions = new HashMap<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                int attractionId = resultSet.getInt("attraction_id");
                String attractionName = resultSet.getString("attraction_name");
                String attractionDescription = resultSet.getString("attraction_description");
                String attractionCity = resultSet.getString("attraction_city");

                // Retrieve tag information
                Integer tagId = resultSet.getObject("tag_id", Integer.class); // This can be NULL
                String tagName = resultSet.getString("tag_name"); // This can also be NULL

                TouristAttraction attraction = foundAttractions.get(attractionId);

                if (attraction == null) {
                    // Create a new attraction if it doesn't exist
                    List<Tag> tags = new ArrayList<>();
                    if (tagId != null) { // Only add the tag if tagId is not NULL
                        tags.add(new Tag(tagId, tagName));
                    }
                    attraction = new TouristAttraction(attractionId, attractionName, attractionDescription, attractionCity, tags);
                    foundAttractions.put(attractionId, attraction);
                } else {
                    // If the attraction already exists, add the tag if it is not NULL
                    if (tagId != null) {
                        attraction.addTag(new Tag(tagId, tagName));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Could not read from database: " + e.getMessage());
        }

        return new ArrayList<>(foundAttractions.values());
    }

    //Find first 10 attractions
    public List<TouristAttraction> getFirstAttractions() {
        String sql = "SELECT a.attraction_id, a.attraction_name, a.attraction_description, a.attraction_city, " +
                "t.tag_id, t.tag_name " +
                "FROM attractions a " +
                "LEFT JOIN tags_for_attractions tfa ON a.attraction_id = tfa.attraction_id " +
                "LEFT JOIN tags t ON t.tag_id = tfa.tag_id " +
                "LIMIT 10";

        HashMap<Integer, TouristAttraction> foundAttractions = new HashMap<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                int attractionId = resultSet.getInt("attraction_id");
                String attractionName = resultSet.getString("attraction_name");
                String attractionDescription = resultSet.getString("attraction_description");
                String attractionCity = resultSet.getString("attraction_city");

                // Retrieve tag information
                Integer tagId = resultSet.getObject("tag_id", Integer.class); // This can be NULL
                String tagName = resultSet.getString("tag_name"); // This can also be NULL

                TouristAttraction attraction = foundAttractions.get(attractionId);

                if (attraction == null) {
                    // Create a new attraction if it doesn't exist
                    List<Tag> tags = new ArrayList<>();
                    if (tagId != null) { // Only add the tag if tagId is not NULL
                        tags.add(new Tag(tagId, tagName));
                    }
                    attraction = new TouristAttraction(attractionId, attractionName, attractionDescription, attractionCity, tags);
                    foundAttractions.put(attractionId, attraction);
                } else {
                    // If the attraction already exists, add the tag if it is not NULL
                    if (tagId != null) {
                        attraction.addTag(new Tag(tagId, tagName));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Could not read from database: " + e.getMessage());
        }

        return new ArrayList<>(foundAttractions.values());
    }

    // Find by name
    public TouristAttraction findAttractionByName(String name) {
        String sql = "SELECT a.attraction_id, a.attraction_name, a.attraction_description, a.attraction_city, " +
                "t.tag_name, t.tag_id " +
                "FROM attractions a " +
                "LEFT JOIN tags_for_attractions tfa ON tfa.attraction_id = a.attraction_id " +
                "LEFT JOIN tags t ON t.tag_id = tfa.tag_id " +
                "WHERE a.attraction_name = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, name);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Tag> tags = new ArrayList<>();
                TouristAttraction attraction = null;

                while (resultSet.next()) {

                    if (attraction == null) {
                        int attractionId = resultSet.getInt("attraction_id");
                        String attractionName = resultSet.getString("attraction_name");
                        String attractionDescription = resultSet.getString("attraction_description");
                        String attractionCity = resultSet.getString("attraction_city");
                        attraction = new TouristAttraction(attractionId, attractionName, attractionDescription, attractionCity, tags);
                    }

                    int tagId = resultSet.getInt("tag_id");
                    String tagName = resultSet.getString("tag_name");

                    if (tagName != null) {
                        tags.add(new Tag(tagId, tagName)); // Add each tag dynamically
                    }

                }
                return attraction;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Cannot find attraction with given name in the database: " + e.getMessage());
        }
    }

    // Delete by ID
    public boolean deleteAttraction(int id) {
        String checkAttractionSql = "SELECT 1 FROM attractions WHERE attraction_id = ?";
        String deleteAttractionsSql = "DELETE FROM attractions WHERE attraction_id = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement checkAttractionStatement = connection.prepareStatement(checkAttractionSql);
             PreparedStatement deleteAttractionStatement = connection.prepareStatement(deleteAttractionsSql)) {

            checkAttractionStatement.setInt(1, id);
            ResultSet resultSet = checkAttractionStatement.executeQuery();

            if (!resultSet.next()) {
                System.out.println("No attraction found with ID: " + id);
                return false; // Nothing was deleted
            }

            deleteAttractionStatement.setInt(1, id);
            deleteAttractionStatement.executeUpdate();
            return true; // Deletion successful

        } catch (SQLException e) {
            throw new RuntimeException("Could not delete the object: " + e.getMessage());
        }
    }


    // Create new Attraction
    public void createAttraction(TouristAttraction attraction) {
        String insertCitySql = "INSERT IGNORE INTO cities (attraction_city) VALUES (?)";
        String insertAttractionSql = "INSERT INTO attractions (attraction_name, attraction_description, attraction_city) VALUES (?, ?, ?)";
        String insertTagsSql = "INSERT INTO tags_for_attractions (attraction_id, tag_id) VALUES (?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement insertCityStatement = connection.prepareStatement(insertCitySql);
             PreparedStatement insertAttractionStatement = connection.prepareStatement(insertAttractionSql, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement insertTagsStatement = connection.prepareStatement(insertTagsSql)) {

            connection.setAutoCommit(false); // Start transaction

            // Ensure the city exists before inserting the attraction
            insertCityStatement.setString(1, attraction.getCity());
            insertCityStatement.executeUpdate();

            // Insert the new attraction
            insertAttractionStatement.setString(1, attraction.getName());
            insertAttractionStatement.setString(2, attraction.getDescription());
            insertAttractionStatement.setString(3, attraction.getCity());
            int rowsAffected = insertAttractionStatement.executeUpdate();

            if (rowsAffected == 0) {
                throw new RuntimeException("Failed to insert attraction: No rows affected.");
            }

            // Get the generated ID of the new attraction
            try (ResultSet generatedKeys = insertAttractionStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int newAttractionId = generatedKeys.getInt(1);
                    attraction.setId(newAttractionId);

                    // Insert tags
                    if (attraction.getTags() != null && !attraction.getTags().isEmpty()) {
                        for (Tag tag : attraction.getTags()) {
                            String tagName = tag.getTagName();
                            Integer tagId = availableTags.get(tagName);

                            if (tagId == null) {
                                System.err.println("Warning: Tag '" + tagName + "' does not exist in the database.");
                                continue;
                            }

                            insertTagsStatement.setInt(1, newAttractionId);
                            insertTagsStatement.setInt(2, tagId);
                            insertTagsStatement.executeUpdate();
                        }
                    }
                } else {
                    throw new RuntimeException("Failed to retrieve the generated ID for the new attraction.");
                }
            }

            connection.commit(); // Commit transaction
            System.out.println("Attraction created successfully!");

        } catch (SQLException e) {
            throw new RuntimeException("Could not create attraction: " + e.getMessage());
        }
    }

    // Update attraction
    public void updateAttraction(TouristAttraction attraction) {

        String insertCitySql = "INSERT IGNORE INTO cities (attraction_city) VALUES (?)";
        String updateAttractionSql = "UPDATE attractions SET attraction_name = ?, attraction_description = ?, attraction_city = ? WHERE attraction_id = ?";
        String deleteOldTagsSql = "DELETE FROM tags_for_attractions WHERE attraction_id = ?";
        String insertNewTagsSql = "INSERT INTO tags_for_attractions (attraction_id, tag_id) VALUES (?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement insertCityStatement = connection.prepareStatement(insertCitySql);
             PreparedStatement updateAttractionStatement = connection.prepareStatement(updateAttractionSql);
             PreparedStatement deleteTagsStatement = connection.prepareStatement(deleteOldTagsSql);
             PreparedStatement insertTagsStatement = connection.prepareStatement(insertNewTagsSql)) {

            connection.setAutoCommit(false); // Start transaction

            // Ensure the city exists before updating the attraction
            insertCityStatement.setString(1, attraction.getCity());
            insertCityStatement.executeUpdate();

            // Update attraction details
            updateAttractionStatement.setString(1, attraction.getName() != null ? attraction.getName() : "");
            updateAttractionStatement.setString(2, attraction.getDescription() != null ? attraction.getDescription() : "");
            updateAttractionStatement.setString(3, attraction.getCity() != null ? attraction.getCity() : "");
            updateAttractionStatement.setInt(4, attraction.getId());
            updateAttractionStatement.executeUpdate();

            // Delete old tags before inserting new ones
            deleteTagsStatement.setInt(1, attraction.getId());
            deleteTagsStatement.executeUpdate();

            // Insert new tags
            if (attraction.getTags() != null && !attraction.getTags().isEmpty()) {
                for (Tag tag : attraction.getTags()) {
                    insertTagsStatement.setInt(1, attraction.getId());
                    insertTagsStatement.setInt(2, tag.getTagId());
                    insertTagsStatement.executeUpdate();
                }
            }

            connection.commit(); // Commit transaction
            System.out.println("Attraction updated successfully!");
        } catch (SQLException e) {
            throw new RuntimeException("Could not update attraction: " + e.getMessage());
        }
    }


}
