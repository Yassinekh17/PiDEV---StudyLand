package entities;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public class YouTubeAPIExample {

    private static final String API_KEY = "AIzaSyB7cLfSQ8ffmwrffFBGoR4KPyecQOELQGs"; // Remplacez par votre clé d'API YouTube
    private static final String APPLICATION_NAME = "StudyLand";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private final YouTube youtubeService;
    private final String apiKey;
    public static void main(String[] args) throws GeneralSecurityException, IOException {
        YouTube youtubeService = getService();

        // Define the API request for retrieving search results.
        YouTube.Search.List request = youtubeService.search().list("snippet");
        request.setKey(API_KEY);
        request.setQ("Java Programming"); // Recherche de vidéos sur la programmation Java
        request.setType("video");

        // Execute the API request and process the response.
        SearchListResponse response = request.execute();
        List<SearchResult> items = response.getItems();
        for (SearchResult item : items) {
            System.out.println("Title: " + item.getSnippet().getTitle());
            System.out.println("Video ID: " + item.getId().getVideoId());
            System.out.println("Description: " + item.getSnippet().getDescription());
            System.out.println();
        }
    }
    public YouTubeAPIExample(String apiKey) throws GeneralSecurityException, IOException {
        this.apiKey = apiKey;
        this.youtubeService = getService();
    }

    public List<SearchResult> searchVideos(String query) throws IOException {
        YouTube.Search.List request = youtubeService.search().list("snippet");
        request.setKey(apiKey);
        request.setQ(query);
        request.setType("video");
        request.setMaxResults(Long.valueOf(1)); // Limit to one result for simplicity
        SearchListResponse response = request.execute();
        return response.getItems();
    }

    private static YouTube getService() throws GeneralSecurityException, IOException {
        final JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        return new YouTube.Builder(GoogleNetHttpTransport.newTrustedTransport(), jsonFactory, null)
                .setApplicationName("YouTube API Example")
                .build();
    }











}

