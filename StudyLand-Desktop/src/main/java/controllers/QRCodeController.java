package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.ResourceBundle;
import java.net.URL;
public class QRCodeController {
    @FXML
    private StackPane centerPane;

    public StackPane getCenterPane() {
        return centerPane;
    }

    public void setCenterPane(StackPane centerPane) {
        this.centerPane = centerPane;
    }

    @FXML
    private ImageView qrCodeImageView;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    private String qrCodeURL;
    public void setQrCodeURL(String qrCodeURL) {
        this.qrCodeURL = qrCodeURL;

        // Load the image into the ImageView
        Image qrCodeImage = new Image(qrCodeURL);
        qrCodeImageView.setImage(qrCodeImage);
    }

    @FXML
    void initialize() {
        // Assuming you have a method to get the QR code image URL
//        String imageUrl = "https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=Project+ID%3A+0%2C+Name%3A+azer";
//
//        // Load the image into the ImageView
//        Image qrCodeImage = new Image(imageUrl);
//        qrCodeImageView.setImage(qrCodeImage);
    }

    public static String generateQRCode(String apiEndpoint ,String data) throws IOException {
        String encodedData = URLEncoder.encode(data, StandardCharsets.UTF_8.toString());


        String url = apiEndpoint + "?size=150x150&data=" + encodedData;

        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);

        // Execute the request
        HttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();

        if (entity != null) {
            try (InputStream inputStream = entity.getContent()) {
                // Convert the image content to Base64
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, bytesRead);
                }

                byte[] imageBytes = byteArrayOutputStream.toByteArray();
                String base64Image = Base64.getEncoder().encodeToString(imageBytes);

                // For demonstration purposes, you can print the Base64-encoded image
                System.out.println("Base64-encoded Image:\n" + base64Image);

                return url; // You might want to return the image URL or do something else with it
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    public void retour(ActionEvent actionEvent) {
        try {
            // Load the "AfficheProjet.fxml" file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficheProjet.fxml"));
            Parent afficheProjetPane = loader.load();

            // Set the "AfficheProjet.fxml" as the root of the current scene
            qrCodeImageView.getScene().setRoot(afficheProjetPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}