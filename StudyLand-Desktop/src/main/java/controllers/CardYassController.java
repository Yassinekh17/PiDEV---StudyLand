package controllers;

import entities.Project;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import services.ServiceProject;

import java.io.IOException;
import java.sql.SQLException;

public class CardYassController {
    @FXML
    private StackPane centerPane;

    public StackPane getCenterPane() {
        return centerPane;
    }

    public void setCenterPane(StackPane centerPane) {
        this.centerPane = centerPane;
    }

    @FXML
    private Text idDesc;
    @FXML
    private AnchorPane card;
    @FXML
    private Button idbouton;

    @FXML
    private Text idtache;

    @FXML
    private Label idtitle;

    @FXML
    private VBox vbox;
    public int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @FXML
    public void consulter(ActionEvent actionEvent) {
        System.out.println("Consulter clicked for Project ID: " + id);
        try {
            ServiceProject serviceProject = new ServiceProject();
            Project project = serviceProject.findProjectById(id);

            // Create a string with project details
            String projectDetails = "Nom Projet: " + project.getNomProjet() + "\n"
                    + "Description: " + project.getDescProjet() + "\n"
                    + "Date Début: " + project.getDate_D() + "\n"
                    + "Date Fin: " + project.getDate_F() + "\n"
                    + "Nombre de Taches: " + project.getNb_taches();

            // Show an alert with project details
            showAlert("Détails du Projet", projectDetails, Alert.AlertType.INFORMATION);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }



    public Text getIdDesc() {
        return idDesc ;
    }

    public void setIdDesc(Text idDesc) {
        this.idDesc = idDesc;
    }
// comment for testing
    public Text getIdtache() {
        return idtache;
    }

    public void setIdtache(Text idtache) {
        this.idtache = idtache;
    }

    public Label getIdtitle() {
        return idtitle;
    }

    public void setIdtitle(String title) {
        idtitle.setText(title);
    }

    public void setIdtext(String text) {
        idDesc.setText(text);
    }

//    public void setProject(Project project) {
//        idtitle.setText(project.getNomProjet());
//        idDesc.setText("Description: " + project.getDescProjet());
//        idtache.setText("Nombre de Taches: " + project.getNb_taches());
//    }
}
