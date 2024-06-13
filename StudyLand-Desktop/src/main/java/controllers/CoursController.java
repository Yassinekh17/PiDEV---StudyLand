package controllers;

import entities.Categorie;
import entities.Cours;
import entities.Formation;
import entities.YouTubeAPIExample;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import services.ServiceCours;
import services.ServiceFormation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

public class CoursController {
    @FXML
    private TextField NomCours;

    @FXML
    private ComboBox<Formation> idFormation;
    @FXML
    private HBox affichagecoursvbox;


    @FXML
    private WebView courseWebView;

    @FXML
    private ListView<Cours> CoursListView;
    @FXML
    private VBox afficherpdf;
    @FXML
    private Label nomCategorie;

    private ServiceCours serviceC = new ServiceCours();
    private ServiceFormation serviceFormation = new ServiceFormation();
    private Cours selectedCours;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private StackPane centerPane;

    public StackPane getCenterPane() {
        return centerPane;
    }

    public void setCenterPane(StackPane centerPane) {
        this.centerPane = centerPane;
    }

    private boolean darkMode = false;

    @FXML
    private void toggleBackgroundColor(ActionEvent event) {
        if (darkMode) {
            anchorPane.setStyle("-fx-background-color: #ffffff;");
        } else {
            anchorPane.setStyle("-fx-background-color: #2b2b2b;");
        }
        darkMode = !darkMode;
    }



    @FXML
    private void initialize() {
        // Set up cell factory to display only the ID of the Formation
        idFormation.setCellFactory(param -> new ListCell<Formation>() {
            @Override
            protected void updateItem(Formation item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.valueOf(item.getIdFormation()));
                }
            }
        });

        // Populate the ComboBox with existing formations
        try {
            List<Formation> formations = serviceFormation.afficher();
            idFormation.getItems().addAll(formations);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        courseWebView = new WebView(); // Initialize the WebView object

    }

    @FXML
    void AjouterCours(ActionEvent event) {
        // Open a file chooser dialog to select the PDF file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select PDF File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
        );
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile == null) {
            // No file selected, return
            return;
        }


        // Retrieve the selected formation from the ComboBox
        Formation selectedFormation = idFormation.getValue();
        if (selectedFormation == null) {
            // Display error message to user
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("cours vide");
            successAlert.setHeaderText("Veuillez remplir le formulaire pour ajouter un cours !!");
            successAlert.showAndWait();
            return;
        }

        // Retrieve other necessary data for the course
        String nomCours = NomCours.getText();
        byte[] descriptionBytes;
        try {
            descriptionBytes = Files.readAllBytes(selectedFile.toPath());
        } catch (IOException e) {
            e.printStackTrace();
            return; // Exit the method if an error occurs
        }
        // Check if a course with the same name already exists for the selected formation
        try {
            boolean courseExists = serviceC.checkCourseExists(nomCours, selectedFormation.getIdFormation());
            if (courseExists) {
                // Display error message to user
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Un cours avec le même nom existe déjà pour la formation sélectionnée !");
                alert.showAndWait();
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception appropriately, e.g., show an error message
            return;
        }

        // Create a new course instance
        Cours cours = new Cours(nomCours, descriptionBytes, selectedFormation.getIdFormation());

        // Call the method to add the course to the database
        try {
            serviceC.ajouter(cours);

            // Display an alert for successful addition
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Course added successfully!");
            alert.showAndWait();

            // Load the selected PDF file into the WebView
            URL url = selectedFile.toURI().toURL();
            courseWebView.getEngine().load(url.toString());
        } catch (SQLException | MalformedURLException e) {
            // Display an alert for error
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("Error adding course: " + e.getMessage());
            errorAlert.showAndWait();
        }
    }



    @FXML
    void AfficherCours(ActionEvent event) {
        try {
            // Clear the existing content in the VBox
            affichagecoursvbox.getChildren().clear();

            // Retrieve the list of courses from the database
            List<Cours> coursList = serviceC.afficher();

            if (coursList.isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION, "Information", "No Courses", "There are no courses to display.");
                return;
            }

            // Loop through the list of courses
            for (Cours cours : coursList) {
                // Create labels to display course details
                Label coursLabel = new Label("Nom: " + cours.getNom_Cours());

                // Decode the description from byte array to string
                String description = new String(cours.getDescription_Cours());
                Label descriptionLabel = new Label("Description: " + description);

//                // Create a WebView to display the PDF content
//                WebView courseWebView = new WebView();
//                courseWebView.setPrefSize(800, 600); // Set WebView size
//                loadPDFContent(cours.getDescription_Cours(), courseWebView);
                // Create a Hyperlink to download/open the PDF
                Hyperlink pdfLink = new Hyperlink("Download PDF");
                pdfLink.setOnAction(e -> {
                    // Handle the action to download/open the PDF
                    try {
                        // Logic to download the PDF file associated with the selected course
                        String fileName = cours.getNom_Cours() + ".pdf"; // Construct the file name
                        byte[] pdfData = cours.getDescription_Cours(); // Get the PDF data from the course
                        FileChooser fileChooser = new FileChooser();
                        fileChooser.setTitle("Save PDF File");
                        fileChooser.setInitialFileName(fileName);
                        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
                        fileChooser.getExtensionFilters().add(extFilter);
                        File file = fileChooser.showSaveDialog(new Stage());
                        if (file != null) {
                            try (FileOutputStream fos = new FileOutputStream(file)) {
                                fos.write(pdfData); // Write the PDF data to the file
                                showAlert(Alert.AlertType.INFORMATION, "Download Successful", "PDF downloaded successfully", "File saved as: " + file.getAbsolutePath());
                            } catch (IOException ex) {
                                showAlert(Alert.AlertType.ERROR, "Error", "File Save Error", "An error occurred while saving the PDF file: " + ex.getMessage());
                            }
                        }
                    } catch (Exception ex) {
                        showAlert(Alert.AlertType.ERROR, "Error", "PDF Download Error", "An error occurred while downloading the PDF: " + ex.getMessage());
                    }
                });

                // Optionally, you can add an image to represent the course
                ImageView imageView = new ImageView(new Image("/src/cours.png"));
                imageView.setFitWidth(100);
                imageView.setPreserveRatio(true);

                // Create a VBox to hold course details and WebView
                VBox courseBox = new VBox(imageView, coursLabel, pdfLink);
                courseBox.setSpacing(5); // Adjust spacing between elements

                // Create the "Supprimer" button
                Button deleteButton = new Button("Supprimer");
                deleteButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: #fff; -fx-padding: 5px 10px; -fx-background-radius: 5px; -fx-font-size: 14px; -fx-font-family: 'Arial';");
                deleteButton.setOnAction(e -> supprimerCours(cours)); // Attach the event handler


                // Optionally, you can add a button for editing
                Button modifierButton = new Button("Modifier");
                modifierButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: #fff; -fx-padding: 5px 10px; -fx-background-radius: 5px; -fx-font-size: 14px; -fx-font-family: 'Arial';");
                modifierButton.setOnAction(e -> ModiferButton(cours));

                // Add buttons to the VBox
                courseBox.getChildren().addAll(deleteButton, modifierButton);

                // Add the courseBox to the main VBox
                affichagecoursvbox.getChildren().add(courseBox);
            }
        } catch (SQLException e) {
            // Handle the exception appropriately
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Database Error", "An error occurred while retrieving courses from the database.");
        }
    }
    private void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    // Helper method to convert byte array to Base64 and embed it into HTML for WebView
    private String getPDFContent(byte[] pdfData) {
        try {
            // Check if the provided PDF data is null or empty
            if (pdfData == null || pdfData.length == 0) {
                // Handle the case of empty or null PDF data
                return "<p>No PDF content available</p>";
            }

            // Convert byte array to Base64 encoding
            String base64Encoded = Base64.getEncoder().encodeToString(pdfData);

            // Construct HTML content to embed the PDF
            String htmlContent = "<embed width='100%' height='100%' name='plugin' type='application/pdf' src='data:application/pdf;base64," + base64Encoded + "' />";

            return htmlContent;
        } catch (Exception e) {
            e.printStackTrace();
            return "<p>Error loading PDF content</p>";
        }
    }


    private void loadPDFContent(byte[] pdfData, WebView webView) {
        try {
            // Check if the provided PDF data is null or empty
            if (pdfData == null || pdfData.length == 0) {
                // Handle the case of empty or null PDF data
                showAlert(Alert.AlertType.WARNING, "Warning", "Empty PDF", "The PDF content is empty.");
                return;
            }

            // Load the PDF content into the WebView
            webView.getEngine().loadContent(getPDFContent(pdfData), "application/pdf");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "PDF Loading Error", "An error occurred while loading PDF content.");
        }
    }




    @FXML
    void ModiferButton(ActionEvent event) {
        // Get the selected course from the ListView
        Cours selectedCours = CoursListView.getSelectionModel().getSelectedItem();

        if (selectedCours != null) {
            // Open a dialog box or a form to allow the user to modify the course details

            // For example, you can use TextInputDialog to get the new course name
            TextInputDialog dialog = new TextInputDialog(selectedCours.getNom_Cours());
            dialog.setTitle("Modify Course");
            dialog.setHeaderText(null);
            dialog.setContentText("Enter the new course name:");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(newName -> {
                // Update the course name
                selectedCours.setNom_Cours(newName);

                try {
                    // Call the modifier method to update the course in the database
                    serviceC.modifier(selectedCours);

                    // Refresh the view
                    AfficherCours(new ActionEvent());
                    // Create the "Modifier" button
                    Button modifierButton = new Button("Modifier");
                    modifierButton.getStyleClass().add("custom-button"); // Add CSS class
                    modifierButton.setOnAction(e -> ModiferButton(new ActionEvent())); // Attach the event handler

                    // Add the "Modifier" button to your layout
                    // For example, if you're using a VBox, you can add it like this:
                    VBox layout = new VBox();
                    layout.getChildren().add(modifierButton);

                    // Show a success message
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Course Modified", "Course details have been updated successfully.");
                } catch (SQLException e) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Database Error", "An error occurred while modifying the course: " + e.getMessage());
                }
            });
        } else {
            showAlert(Alert.AlertType.WARNING, "Warning", "No Course Selected", "Please select a course to modify.");
        }
    }


    @FXML
    void RetournerFormation(ActionEvent event) {
        FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/AjouterFormation.fxml"));
        try {
            Parent root = loader1.load();
            AjouterFormationController controller = loader1.getController();
            NomCours.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println(e.getMessage());

        }
    }
    @FXML
    private void handleLabelClick(MouseEvent event) {
        Node source = (Node) event.getSource();

        if (source instanceof Label) {
            Label clickedLabel = (Label) source;

            // Retrieve the Formation object associated with the clicked label
            Cours clickedCours = (Cours) clickedLabel.getUserData();

            if (clickedCours != null) {
                // Set the selectedFormation to the clickedFormation
                selectedCours = clickedCours;

                // Display both the title and the ID of the selected formation
                String message = "nom label clicked for cours: " + clickedCours.getNom_Cours() + "\n";
                message += "Cours ID: " + clickedCours.getIdCour();
                nomCategorie.setText(message);
            } else {
                System.out.println("No cours associated with the clicked label.");
            }
        }
    }
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void ModiferButton(Cours cours) {
        // Show a dialog to prompt the user for the new course name
        TextInputDialog dialog = new TextInputDialog(cours.getNom_Cours());
        dialog.setTitle("Modifier cours");
        dialog.setHeaderText("Modifier le nom du cours");
        dialog.setContentText("Nouveau nom:");

        // Get user input
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(newName -> {
            if (!newName.isEmpty()) {
                try {
                    // Update the course name with the new name
                    cours.setNom_Cours(newName);

                    // Call the modifier method in your ServiceCours class to update the course name in the database
                    serviceC.modifier(cours);

                    // Provide feedback to the user
                    showAlert("Success", "Course name updated successfully.", Alert.AlertType.INFORMATION);
                } catch (SQLException e) {
                    System.out.println("Error updating course name: " + e.getMessage());
                    showAlert("Error", "Failed to update course name: " + e.getMessage(), Alert.AlertType.ERROR);
                }
            } else {
                showAlert("Error", "New name cannot be empty.", Alert.AlertType.ERROR);
            }
        });
    }
    private void supprimerCours(Cours cours) {
        try {
            // Delete related records in the cour_formation table
            serviceC.deleteCourById(cours.getIdCour());

            // Delete the Formation from the database
            serviceC.supprimer(cours);

            // Refresh the ListView
            AfficherCours(new ActionEvent());
        } catch (SQLException e) {
            System.out.println("Error deleting formation: " + e.getMessage());
        }
    }
        public void supprimerCours(ActionEvent actionEvent) {
            // Get the selected Cours object from the ListView
            Cours selectedCours = CoursListView.getSelectionModel().getSelectedItem();

            if (selectedCours != null) {
                try {
                    // Delete the selected course from the database
                    serviceC.supprimer(selectedCours);

                    // Remove the deleted course from the ListView
                    CoursListView.getItems().remove(selectedCours);

                } catch (SQLException e) {
                    System.out.println("Error deleting course: " + e.getMessage());
                    // Handle the exception appropriately
                }
            } else {
                System.out.println("No item selected.");
            }}


    @FXML
    void consultervideo(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/youtube_viewer.fxml"));

            Parent root = loader.load();
            centerPane.getChildren().clear();
            centerPane.getChildren().add(root);
            YouTubeAPIExample controller = loader.getController();

    }






}
