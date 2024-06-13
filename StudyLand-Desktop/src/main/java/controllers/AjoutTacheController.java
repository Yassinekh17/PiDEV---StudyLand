package controllers;
import entities.Project;
import entities.StatutTache;
import entities.taches_projet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import services.ServiceProject;
import services.ServiceTaches;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import javafx.scene.text.Text;
public class AjoutTacheController {
    ServiceTaches st = new ServiceTaches();
    @FXML
    private Text errorMessage;
    @FXML
    private StackPane CenterPane;

    public StackPane getCenterPane() {
        return CenterPane;
    }

    public void setCenterPane(StackPane centerPane) {
        CenterPane = centerPane;
    }

    @FXML
    private DatePicker DateDT;

    @FXML
    private DatePicker DateFT;

    @FXML
    private TextField DescTache;

    @FXML
    private TextField NomTache;



    @FXML
    private TextField Resp;

    @FXML
    private ChoiceBox<StatutTache> Statut;
    private Date convertToUtilDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    @FXML
    void initialize() {
        // Initialize the Statut ChoiceBox with enum values
        ObservableList<StatutTache> statutOptions = FXCollections.observableArrayList(StatutTache.values());
        Statut.setItems(statutOptions);

    }
    @FXML
    void AjouterTache(ActionEvent event) {
        // Check for empty input fields

        if (NomTache.getText().isEmpty() || DescTache.getText().isEmpty()  || Resp.getText().isEmpty()
                || DateDT.getValue() == null || DateFT.getValue() == null) {
            // Display error message to user
            // Display error message to user
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Formulaire vide");
            successAlert.setHeaderText("Veuillez remplir le formulaire de formation");
            successAlert.showAndWait();
            return;
        }
        String nompValue = NomTache.getText();
        if (nompValue.length() > 8) {
            // Display error message to the user
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Invalid Input");
            successAlert.setHeaderText("Le champ 'nom tache' ne doit pas dépasser 8 caractères.");
            successAlert.showAndWait();
            return;
        }

        LocalDate startDate = DateDT.getValue();
        LocalDate endDate = DateFT.getValue();
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
        String respValue = Resp.getText();
        if (!respValue.matches("^[a-zA-Z]+$") || respValue.length() > 8) {
            // Display error message to the user
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Invalid Input");
            successAlert.setHeaderText("Le champ 'resp' doit contenir uniquement des lettres et ne doit pas dépasser 8 caractères.");
            successAlert.showAndWait();
            return;
        }

        Date dateDebut = convertToUtilDate(startDate);
        Date dateFin = convertToUtilDate(endDate);
        try {
            StatutTache statut = Statut.getValue();
            taches_projet tache=new taches_projet(NomTache.getText(),DescTache.getText(),dateDebut,dateFin,statut
                    ,Resp.getText() ,AjoutProjetController.id_projet_class);
            System.out.println(AjoutProjetController.id_projet_class);
            st.ajouter(tache);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }catch (IllegalArgumentException e) {
        System.out.println("Invalid Statut value: " + Statut.getValue());
    }
    }
    @FXML
    void AfficherTache(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficheTache.fxml"));
        try {
            Parent root = loader.load();
            CenterPane.getChildren().clear();
            CenterPane.getChildren().add(root);
            AfficheTacheController controller = loader.getController();
            controller.setCenterPane(CenterPane);
            controller.setNomT(NomTache.getText());
           controller.setDescT(DescTache.getText());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }



}
