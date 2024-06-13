package controllers;
import com.google.api.services.youtube.model.SearchResult;
import entities.YouTubeAPIExample;
import javafx.fxml.FXML;
import javafx.scene.web.WebView;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
public class YoutubeViewer {
    @FXML
    private WebView webView;

    private static final String API_KEY = "AIzaSyB7cLfSQ8ffmwrffFBGoR4KPyecQOELQGs";

    public void initialize() {
        try {
            YouTubeAPIExample youtubeAPIExample = new YouTubeAPIExample(API_KEY);
            List<SearchResult> searchResults = youtubeAPIExample.searchVideos("Java Programming");
            if (!searchResults.isEmpty()) {
                SearchResult firstResult = searchResults.get(0);
                String videoId = firstResult.getId().getVideoId();
                String videoUrl = "https://www.youtube.com/watch?v=PtUOftDMjCM" + videoId;
                webView.getEngine().load(videoUrl);
            }
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
    }
}
