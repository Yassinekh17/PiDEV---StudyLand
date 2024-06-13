package controllers;

import entities.Project;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import services.ServiceProject;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ProjetEtudController {
    @FXML
    private StackPane centerPane;

    public StackPane getCenterPane() {
        return centerPane;
    }

    public void setCenterPane(StackPane centerPane) {
        this.centerPane = centerPane;
    }

    @FXML
    private FlowPane cardsFlowPane;

    @FXML
    private Button idbutton;

    @FXML
    private TextField textrecherche;
    @FXML
    private ListView<Project> projectListView;


    ServiceProject sp = new ServiceProject();

    public void initialize() {
        try {
            List<Project> projects = sp.afficher();

            for (Project proj : projects) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/CardYass.fxml"));
                Parent cardNode = loader.load();

                // Access the controller
                CardYassController cardController = loader.getController();
                cardController.setId(proj.getId_Projet()); // Set the ID

                cardController.setIdtitle(proj.getNomProjet());
                cardController.setIdtext(proj.getDescProjet());

                // Add the card to the FlowPane
                cardsFlowPane.getChildren().add(cardNode);
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }

    public void recherche(ActionEvent actionEvent) {
        try {
            String caractereRecherche = textrecherche.getText().trim();

            // Clear existing cards before displaying new search results
            cardsFlowPane.getChildren().clear();

            if (!caractereRecherche.isEmpty()) {
                List<Project> ProjectTrouvees = sp.rechercherParCaractere(caractereRecherche);

                for (Project proj : ProjectTrouvees) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/CardYass.fxml"));
                    Parent cardNode = loader.load();

                    // Access the controller of the Evaluationcard
                    CardYassController cardController = loader.getController();

                    cardController.setIdtitle(proj.getNomProjet());
                    cardController.setIdtext(proj.getDescProjet());

                    // Add the card to the FlowPane
                    cardsFlowPane.getChildren().add(cardNode);
                }
            } else {
                // If the search field is empty, display all evaluations
                initialize();
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }
}