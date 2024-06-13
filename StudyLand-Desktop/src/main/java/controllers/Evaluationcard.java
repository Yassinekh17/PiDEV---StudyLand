package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;


public class Evaluationcard {
    @FXML
    private AnchorPane cardsContainer;
    @FXML
    private Button idbouton;

    @FXML
    private Text idtext;

    @FXML
    private Label idtitle;
    public int id;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void consulter(ActionEvent actionEvent) {
        System.out.println("Consulter clicked for Evaluation ID: " + id);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/test.fxml"));
            Parent root = loader.load();

            // Access the controller of Teste after loading the FXML file
            Teste testeController = loader.getController();

            // Call the method to receive the id in Teste controller
            testeController.getEvaluationWithQuestionsById(id);
            testeController.initialize();
            // Create a new scene with the loaded Parent
            Scene scene = new Scene(root);

            // Get the current stage from the button's scene
            Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            // Replace the current scene with the new scene
            currentStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }





    public void setIdtitle(String title) {
        idtitle.setText(title);
    }

    public void setIdtext(String text) {
        idtext.setText(text);
    }



}
