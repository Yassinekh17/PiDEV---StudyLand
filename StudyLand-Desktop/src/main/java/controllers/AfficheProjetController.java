package controllers;

import entities.Project;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import services.ServiceProject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Date;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AfficheProjetController {
    @FXML
    private StackPane CenterPane;

    public StackPane getCenterPane() {
        return CenterPane;
    }

    public void setCenterPane(StackPane centerPane) {
        CenterPane = centerPane;
    }

    @FXML
    private Label labeldesc;

    @FXML
    private Label labelfromdb;
    @FXML
    private ListView<String> labelfromdbb;
    private List<Project> project;
    @FXML
    private Label labelnom;
    @FXML
    private AnchorPane mainAnchorPane;
    private String generatedQRCodeURL;
    public Label getLabeldesc() {
        return labeldesc;
    }

    public void setLabeldesc(String labeldesc) {
        this.labeldesc.setText(labeldesc);
    }

    public Label getLabelnom() {
        return labelnom;
    }

    public void setLabelnom(String labelnom) {
        this.labelnom.setText(labelnom);
    }
    ServiceProject sp = new ServiceProject();


    @FXML
    void TachesP(ActionEvent event) {
        CenterPane.getChildren().clear();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjoutTache.fxml"));

        try {
            //FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjoutTache.fxml"));
            Parent root = loader.load();
            CenterPane.getChildren().clear();
            CenterPane.getChildren().add(root);
            AjoutTacheController ajouterTach = loader.getController();
            ajouterTach.setCenterPane(CenterPane);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void listeP(ActionEvent event) {
        CenterPane.getChildren().clear();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListeProjetsAnchorPane.fxml"));

        try {
            //FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjoutTache.fxml"));
            Parent root = loader.load();
            CenterPane.getChildren().clear();
            CenterPane.getChildren().add(root);
            ListeProjetsAnchorPaneController listProjectsPane = loader.getController();
         //   labeldesc.getScene().setRoot(CenterPane);
            listProjectsPane.setCenterPane(CenterPane);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void QRCode(ActionEvent event) {
        CenterPane.getChildren().clear();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/QRCode.fxml"));

        try {
            Parent root = loader.load();
            CenterPane.getChildren().clear();
            CenterPane.getChildren().add(root);
            QRCodeController codeController = loader.getController();
            //   labeldesc.getScene().setRoot(CenterPane);
            codeController.setCenterPane(CenterPane);
            codeController.setQrCodeURL(generatedQRCodeURL);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }





    public void setGeneratedQRCodeURL(String generatedQRCodeURL) {
        this.generatedQRCodeURL = generatedQRCodeURL;

        // If needed, you can pass this URL to QRCodeController when navigating to QR code page
    }

//    public void ModifierDB(ActionEvent actionEvent) {
//        // Get the selected Project object from the ListView
//        String selectedItem = labelfromdbb.getSelectionModel().getSelectedItem();
//        if (selectedItem != null) {
//            // Parse the ID from the selected item
//            int id = parseIdFromSelectedItem(selectedItem);
//
//            // Show a dialog or use a form to get updated values for the Project
//            // For simplicity, let's assume you have a method to show a form and get updated values
//            Project updatedProject = showUpdateForm(); // Implement this method
//
//            try {
//                // Update the Project in the database
//                sp.modifier(updatedProject);
//                // Refresh the ListView
//                afficherDB(actionEvent);
//            } catch (SQLException e) {
//                System.out.println(e.getMessage());
//                // Handle the exception appropriately
//            }
//        } else {
//            System.out.println("No item selected.");
//        }
//    }
//
//    // Helper method to show a form/dialog to get updated values for the Project
//    private Project showUpdateForm() {
//        // Implement a method to show a form or dialog to get updated values for the Project
//        // Return a new Project object with the updated values
//        // For simplicity, you can use a temporary Project object with placeholder values
//        return new Project(0, "Updated Project", "Updated Description", new Date(), new Date(), 0);
//    }
}
