package controllers;

import entities.Project;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import services.ServiceProject;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ListeProjetsAnchorPaneController {
    @FXML
    private Label labeldesc;
    @FXML
    private StackPane centerPane;

    public StackPane getCenterPane() {
        return centerPane;
    }

    public void setCenterPane(StackPane centerPane) {
        this.centerPane = centerPane;
    }

    @FXML
    private Label labelfromdb;
    @FXML
    private ListView<String> labelfromdbb;
    private List<Project> project;
    @FXML
    private Label labelnom;
    @FXML
    private AnchorPane mainAnchorPane;
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
    void afficherDB(ActionEvent event) {
        try {
            List<Project> project = sp.afficher();

            // Clear any existing items in the ListView
            labelfromdbb.getItems().clear();

            // Add each Formation object to the ListView
            for (Project projects : project) {
                labelfromdbb.getItems().add(projects.toString());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    @FXML
    public void SupprimerDB(ActionEvent actionEvent) {
        // Get the selected Categorie object from the ListView
        String selectedItem = labelfromdbb.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            // Parse the ID from the selected item
            int id = parseIdFromSelectedItem(selectedItem);

            try {
                // Delete the Categorie from the database
                sp.supprimer(new Project(id, null,null,null,null,0)); // Create a temporary Categorie object with only the ID
                // Refresh the ListView
                afficherDB(actionEvent);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                // Handle the exception appropriately
            }
        } else {
            System.out.println("No item selected.");
        }
    }

    // Helper method to parse the ID from the string representation of a Categorie object
    private int parseIdFromSelectedItem(String selectedItem) {
        // Assuming your string representation is in the format "Categorie{idCategorie=<id>, nomCategorie=<nom>}"
        int startIndex = selectedItem.indexOf("id_Projet=") + "id_Projet=".length();
        int endIndex = selectedItem.indexOf(",", startIndex);


        return Integer.parseInt(selectedItem.substring(startIndex, endIndex));
    }
    // Get the selected Categorie object from the ListView
    @FXML
    private void ModifierDB(ActionEvent actionEvent) {
        String selectedItem = labelfromdbb.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            Project selectedProject = parseProjectFromString(selectedItem);
            // Parse the ID from the selected item
            int id = parseIdFromSelectedItem(selectedItem);

            // Show a dialog to prompt the user for the new project details
            Dialog<Project> dialog = new Dialog<>();
            dialog.setTitle("Modifier Projet");
            dialog.setHeaderText("Modifier les données du projet");

            // Set the button types
            ButtonType modifierButtonType = new ButtonType("Modifier", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(modifierButtonType, ButtonType.CANCEL);

            // Create form elements for project details
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            TextField nomField = new TextField();
            nomField.setText(selectedProject.getNomProjet());
            TextField descField = new TextField();
            descField.setText(selectedProject.getDescProjet());
            TextField nbTachesField = new TextField();
            nbTachesField.setText(String.valueOf(selectedProject.getNb_taches()));

            grid.add(new Label("Nouveau nom:"), 0, 0);
            grid.add(nomField, 1, 0);
            grid.add(new Label("Nouvelle description:"), 0, 1);
            grid.add(descField, 1, 1);
            grid.add(new Label("Nouveau nombre de taches:"), 0, 2);
            grid.add(nbTachesField, 1, 2);

            dialog.getDialogPane().setContent(grid);

            // Request focus on the nomField by default
            Platform.runLater(() -> nomField.requestFocus());

            // Convert the result to a project when the modifier button is clicked
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == modifierButtonType) {
                    return new Project(
                            selectedProject.getId_Projet(),
                            nomField.getText(),
                            descField.getText(),
                            selectedProject.getDate_D(),
                            selectedProject.getDate_F(),
                            Integer.parseInt(nbTachesField.getText())
                    );
                }
                return null;
            });

            Optional<Project> result = dialog.showAndWait();

            result.ifPresent(updatedProject -> {
                try {
                    // Call the modifier method in your ServiceProject class
                    sp.modifier(updatedProject);
                    // Refresh the ListView
                    afficherDB(actionEvent);
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                    // Handle the exception appropriately
                }
            });
        } else {
            System.out.println("No item selected.");
        }
    }

    @FXML
    void ExportWord(ActionEvent event) {
        try {
            List<Project> projects = sp.afficher();

            // Create a string to hold the content of the Word document
            StringBuilder content = new StringBuilder();

            // Append title
            String title = "La liste des Projets";
            content.append(title).append("\n\n");

            // Append project details to the content string
            for (Project project : projects) {
                content.append("Nom du Projet: ").append(project.getNomProjet()).append("\n");
                content.append("Description: ").append(project.getDescProjet()).append("\n");
                content.append("Date Début: ").append(project.getDate_D()).append("\n");
                content.append("Date Fin: ").append(project.getDate_F()).append("\n");
                content.append("Nombre de tâches: ").append(project.getNb_taches()).append("\n\n");
                content.append("\n\n");
                content.append("\n\n");
                content.append("\n\n");
                content.append("\n\n");
                content.append("\n\n");
                content.append("\n\n");
                content.append("\n\n");
                content.append("\n\n");
                content.append("\n\n");
                content.append("\n\n");
                content.append("\n\n");
                content.append("\n\n");
                content.append("\n\n");
                content.append("\n\n");
                content.append("\n\n");
                content.append("\n\n");
                content.append("\n\n");
                content.append("\n\n");
                content.append("\n\n");
                content.append("\n\n");
                content.append("\n\n");
                content.append("\n\n");
                content.append("\n\n");
                content.append("\n\n");
                content.append("\r");
            }

            // Prompt the user for the file path to save the Word document
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Word Document");
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Word Documents (*.docx)", "*.docx");
            fileChooser.getExtensionFilters().add(extFilter);

            File file = fileChooser.showSaveDialog(new Stage());

            if (file != null) {
                // Save the content to the Word document
                WordExporter.exportToWord(title, content.toString(), file.getAbsolutePath());
                showAlert(Alert.AlertType.INFORMATION, "Export Successful", "Projects exported to Word document.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Export Error", "No file selected for export.");
            }

        } catch (SQLException | IOException e) {
            showAlert(Alert.AlertType.ERROR, "Export Error", "An error occurred during export: " + e.getMessage());
            System.out.println(e.getMessage());
        }
    }


    // Helper method to show an alert
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @FXML
    void ExportPDF(ActionEvent event) {
        try {
            List<Project> projects = sp.afficher();

            // Prompt the user for the file path to save the PDF document
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save PDF Document");
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF Documents (*.pdf)", "*.pdf");
            fileChooser.getExtensionFilters().add(extFilter);

            File file = fileChooser.showSaveDialog(new Stage());

            if (file != null) {
                // Save the content to the PDF document
                PDFExporter.exportToPDF("La liste des Projets", projects, file.getAbsolutePath());
                showAlert(Alert.AlertType.INFORMATION, "Export Successful", "Projects exported to PDF document.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Export Error", "No file selected for export.");
            }
        } catch (SQLException | IOException e) {
            showAlert(Alert.AlertType.ERROR, "Export Error", "An error occurred during export: " + e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    private Project parseProjectFromString(String projectString) {
        // Sample string: "Project{id_Projet=1, NomProjet='ProjectName', DescProjet='ProjectDesc', Date_D=2024-02-20, Date_F=2024-02-25, nb_taches=3}"

        // Extracting values using regex
        Pattern pattern = Pattern.compile("id_Projet=(\\d+), NomProjet='([^']+)', DescProjet='([^']+)', Date_D=(.*), Date_F=(.*), nb_taches=(\\d+)");
        Matcher matcher = pattern.matcher(projectString);

        if (matcher.find()) {
            int id = Integer.parseInt(matcher.group(1));
            String name = matcher.group(2);
            String desc = matcher.group(3);
            String dateDString = matcher.group(4); // Assuming Date_D is in string format
            String dateFString = matcher.group(5); // Assuming Date_F is in string format
            Date dateD = parseDateFromString(dateDString); // Implement parseDateFromString method
            Date dateF = parseDateFromString(dateFString); // Implement parseDateFromString method
            int nbTaches = Integer.parseInt(matcher.group(6));

            return new Project(id, name, desc, dateD, dateF, nbTaches);
        }

        return null;
    }

    // Helper method to parse Date from string
    private Date parseDateFromString(String dateString) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            // Handle parsing exception
            e.printStackTrace();
            return null;
        }
    }

    public void retour(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficheProjet.fxml"));
            Parent afficheProjetPane = loader.load();

            // Set the "AfficheProjet.fxml" as the root of the current scene
            mainAnchorPane.getScene().setRoot(afficheProjetPane);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
