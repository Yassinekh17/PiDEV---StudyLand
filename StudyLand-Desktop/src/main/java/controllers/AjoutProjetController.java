package controllers;

import entities.Project;
import javafx.event.ActionEvent;
import entities.Project;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import services.ServiceProject;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import javafx.scene.text.Text;
import controllers.QRCodeController;
public class AjoutProjetController {

    ServiceProject sp = new ServiceProject();
    @FXML
    private StackPane CenterPane;

    public StackPane getCenterPane() {
        return CenterPane;
    }

    public void setCenterPane(StackPane centerPane) {
        CenterPane = centerPane;
    }

    @FXML
    private Text errorMessage;
    @FXML
    private DatePicker DateD;
    @FXML
    private DatePicker DateF;

    @FXML
    private TextField DescP;

    @FXML
    private TextField nbtache;

    @FXML
    private TextField nomp;

    private String generatedQRCodeURL;
    //test
    public static int id_projet_class;

    private Date convertToUtilDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    @FXML
    void AjouterProjet(ActionEvent event) {
        // Check for empty input fields
        if (nomp.getText().isEmpty() || DescP.getText().isEmpty() || nbtache.getText().isEmpty()
                || DateD.getValue() == null || DateF.getValue() == null) {
            // Display error message to user
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Formulaire vide");
            successAlert.setHeaderText("Veuillez remplir le formulaire de formation");
            successAlert.showAndWait();
            return;
        }
        // Validate nomp input (should not exceed 8 characters)
        String nompValue = nomp.getText();
        if (nompValue.length() > 8) {
            // Display error message to the user
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Invalid Input");
            successAlert.setHeaderText("Le champ 'nomp' ne doit pas dépasser 8 caractères.");
            successAlert.showAndWait();
            return;
        }
        // Convert date pickers to Date objects
        LocalDate startDate = DateD.getValue();
        LocalDate endDate = DateF.getValue();

        // Check if the selected dates are from today onwards
        LocalDate today = LocalDate.now();
        if (startDate.isBefore(today) || endDate.isBefore(today)) {
            // Display error message to user
            // Display success message to user
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Wrong date");
            successAlert.setHeaderText("Please select dates from today onwards");
            successAlert.showAndWait();
            return;
        }

        // Check if the start date is after the end date
        if (startDate.isAfter(endDate)) {
            // Display error message to user
            System.out.println("Start date cannot be after end date");
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Wrong date");
            successAlert.setHeaderText("Start date cannot be after end date");
            successAlert.showAndWait();
            return;
        }

        // Validate nbtache input (should be a number and not higher than 99)
        String nbtacheValue = nbtache.getText();
        if (!nbtacheValue.matches("\\d+") || Integer.parseInt(nbtacheValue) > 99) {
            // Display error message to the user
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Invalid Input");
            successAlert.setHeaderText("Le champ 'nbtache' doit être un nombre et ne doit pas dépasser 99.");
            successAlert.showAndWait();
            return;
        }

        Date dateDebut = convertToUtilDate(startDate);
        Date dateFin = convertToUtilDate(endDate);

        try {
            Project project = new Project(nomp.getText(), DescP.getText(), dateDebut, dateFin,
                    Integer.parseInt(nbtache.getText()));
            // Add the project to the database
            sp.ajouter(project);
            id_projet_class = sp.recherche(nomp.getText());
            System.out.println(id_projet_class);
            generateQRCodeForProject(project);
            // CalendarController.addEventToCalendar(id_projet_class, dateDebut, dateFin, nomp.getText());
            // Navigate to AfficheProjet.fxml

            CenterPane.getChildren().clear();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficheProjet.fxml"));
            try {
                Parent root = loader.load();
                CenterPane.getChildren().clear();
                CenterPane.getChildren().add(root);
                AfficheProjetController afficheProjet = loader.getController();
                afficheProjet.setGeneratedQRCodeURL(generatedQRCodeURL);
                afficheProjet.setLabelnom(nomp.getText());
                afficheProjet.setLabeldesc(DescP.getText());
                afficheProjet.setCenterPane(getCenterPane());
                //DateD.getScene().setRoot(root);

            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

            // Pass the generated QR code URL to AfficheProjetController
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void generateQRCodeForProject(Project project) throws IOException {
        // Assuming project.getId_Projet() returns the unique identifier for the project
        String dataToEncode = "Name: " + project.getNomProjet() +
                ", Description: " + project.getDescProjet() +
                ", Number of Tasks: " + project.getNb_taches();

        // Use the QRCodeController method to generate the QR code
        generatedQRCodeURL = QRCodeController.generateQRCode("https://api.qrserver.com/v1/create-qr-code/", dataToEncode);

        // Print or use the QR code image URL as needed
        System.out.println("QR Code Image URL: " + generatedQRCodeURL);


    }




}
