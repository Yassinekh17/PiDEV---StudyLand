package controllers;

import entities.Project;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class ProjectCard extends AnchorPane {

    private Project project;

    public ProjectCard(Project project) {
        this.project = project;
        initialize();
    }

    private void initialize() {
        // Card styling
        setPrefWidth(190.0);
        setPrefHeight(254.0);
        setStyle("-fx-background-color: #011522; -fx-border-radius: 8px; -fx-padding: 10px;");

        // Tools (circles)
        HBox tools = new HBox(5);
        tools.getChildren().addAll(
                createCircle("red", "#ff605c"),
                createCircle("yellow", "#ffbd44"),
                createCircle("green", "#00ca4e")
        );

        // Card content
        StackPane cardContent = new StackPane();
        Label projectNameLabel = new Label(project.getNomProjet());
        Label projectDescLabel = new Label(project.getDescProjet());
        cardContent.getChildren().addAll(projectNameLabel, projectDescLabel);

        // Set layout
        AnchorPane.setTopAnchor(tools, 0.0);
        AnchorPane.setLeftAnchor(tools, 0.0);
        AnchorPane.setTopAnchor(cardContent, 40.0);
        AnchorPane.setLeftAnchor(cardContent, 0.0);

        getChildren().addAll(tools, cardContent);
    }

    private StackPane createCircle(String styleClass, String backgroundColor) {
        StackPane circle = new StackPane();
        circle.getStyleClass().add("circle");
        circle.setStyle("-fx-background-color: " + backgroundColor + ";");
        Label box = new Label();
        box.getStyleClass().add(styleClass);
        circle.getChildren().add(box);
        return circle;
    }
}
