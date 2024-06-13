package controllers;

import entities.evaluation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import services.EvalService;

import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public class Evaluationcards {
    @FXML
    private Button idbutton;
    @FXML
    private StackPane centerPane;

    public StackPane getCenterPane() {
        return centerPane;
    }

    public void setCenterPane(StackPane centerPane) {
        this.centerPane = centerPane;
    }

    @FXML
    private TextField textrecherche;

    @FXML
    private FlowPane cardsFlowPane;
    @FXML
    private PieChart pieChart;
    EvalService eval = new EvalService();

    public void initialize() {
        try {
            Map<String, Long> statistics = eval.getStatisticsByDomaine();

            // Clear existing data in PieChart
            pieChart.getData().clear();

            // Populate PieChart with statistical data
            for (Map.Entry<String, Long> entry : statistics.entrySet()) {
                PieChart.Data slice = new PieChart.Data(entry.getKey(), entry.getValue());
                pieChart.getData().add(slice);
            }

            // Clear existing cards in FlowPane
            cardsFlowPane.getChildren().clear();

            // Display evaluations in FlowPane
            List<evaluation> evaluations = eval.afficher();
            for (evaluation eval : evaluations) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/evaluationcard.fxml"));
                Parent cardNode = loader.load();

                // Access the controller of the Evaluationcard
                Evaluationcard cardController = loader.getController();
                cardController.setId(eval.getId_evaluation()); // Set the ID

                cardController.setIdtitle(eval.getTitre_evaluation());
                cardController.setIdtext(eval.getDescription());

                // Add the card to the FlowPane
                cardsFlowPane.getChildren().add(cardNode);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

        public void recherche(ActionEvent actionEvent) {
            try {
                String caractereRecherche = textrecherche.getText().trim();
                cardsFlowPane.getChildren().clear();

                if (!caractereRecherche.isEmpty()) {
                    List<evaluation> evaluationsTrouvees = eval.rechercherParCaractere(caractereRecherche);

                    for (evaluation eval : evaluationsTrouvees) {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/evaluationcard.fxml"));
                        Parent cardNode = loader.load();

                        Evaluationcard cardController = loader.getController();

                        cardController.setIdtitle(eval.getTitre_evaluation());
                        cardController.setIdtext(eval.getDescription());

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



