package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.sql.SQLException;




public class Books {

    @FXML
    private VBox vbxquesrep;

    private static final String API_BASE_URL = "https://freebooksapi.pyaesonemyo.dev/api/";

    public void initialize() throws SQLException {
        // Uncomment the following line to load books on initialization
        // loadBooks();
    }

    public void loadBooks(ActionEvent actionEvent) {
        try {
            // Example: Get all books
            String allBooksUrl = API_BASE_URL + "latest/";
            String booksResponse = makeApiRequest(allBooksUrl);

            // Parse JSON response
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode booksNode = objectMapper.readTree(booksResponse);

            // Process the list of books
            Platform.runLater(() -> {
                vbxquesrep.getChildren().clear(); // Clear existing content in the VBox

                for (JsonNode book : booksNode) {
                    String title = book.get("title").asText();
                    String author = book.get("author").asText();

                    // You can customize the display logic as needed
                    Label titleLabel = new Label("Title: " + title);
                    Label authorLabel = new Label("Author: " + author);

                    // Add labels to the VBox
                    vbxquesrep.getChildren().addAll(titleLabel, authorLabel);
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String makeApiRequest(String apiUrl) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(apiUrl).build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                throw new IOException("Unexpected response code: " + response.code());
            }
        }
    }
}

