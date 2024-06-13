package controllers;

import entities.Project;
import entities.taches_projet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import services.ServiceTaches;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class DetailProjetControllers {

    @FXML
    private StackPane centerPane;

    public StackPane getCenterPane() {
        return centerPane;
    }

    public void setCenterPane(StackPane centerPane) {
        this.centerPane = centerPane;
    }
    @FXML
    private Label labelDateDebut;

    @FXML
    private Label labelDateFin;

    @FXML
    private Label labelDesc;

    @FXML
    private Label labelNbTaches;

    @FXML
    private Label labelNom;
    @FXML
    private Label labeltache;
    @FXML
    private Button qrCodeButton;
    public void setProjectDetails(Project project) {
        labelNom.setText("Nom Projet: " + project.getNomProjet());
        labelDesc.setText("Description: " + project.getDescProjet());
        labelDateDebut.setText("Date Début: " + project.getDate_D());
        labelDateFin.setText("Date Fin: " + project.getDate_F());
        labelNbTaches.setText("Nombre de Taches: " + project.getNb_taches());
        setProjectTasks(project.getId_Projet());
    }

    private void setProjectTasks(int projectId) {
        try {
            ServiceTaches serviceTaches = new ServiceTaches();
            List<taches_projet> tasks = serviceTaches.getTasksByProjectId(projectId);

            StringBuilder tasksText = new StringBuilder("Tâches: ");
            for (taches_projet task : tasks) {
                tasksText.append("\n- ").append(task.getNom_tache());
            }

            labeltache.setText(tasksText.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void retour(ActionEvent actionEvent) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ProjetEtud.fxml"));
            Parent projetEtudPane = loader.load();

            // Set the "AfficheProjet.fxml" as the root of the current scene
            labelDateDebut.getScene().setRoot(projetEtudPane);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
