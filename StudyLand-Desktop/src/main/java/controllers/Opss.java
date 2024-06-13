package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class Opss {

    @FXML
    private Text resultat;
 private   double score;
    public void setScore(double newScore) {
        this.score = newScore;
    }
    public  double scoreTest;
  @FXML
    public void initialize() throws SQLException {
      String formattedScore = String.format("%.2f%%", scoreTest);
      resultat.setText(formattedScore);
  }
    @FXML
    public void updateScore(double newScore) {
        resultat.setText(String.valueOf(scoreTest));
         // Mettez à jour le texte lors de la mise à jour du score
    }    @FXML
    void reessayer(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ProfileApprenant.fxml"));
            Parent root = loader.load();

            // Access the controller of Teste after loading the FXML file
            ProfileApprenant testeController = loader.getController();


            Scene scene = new Scene(root);

            // Get the current stage from the button's scene
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Replace the current scene with the new scene
            currentStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }



    }}
