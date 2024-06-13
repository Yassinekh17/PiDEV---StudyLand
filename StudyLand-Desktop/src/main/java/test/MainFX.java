package test;



import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;

public class MainFX extends Application {
        public void start(Stage primaryStage) {
            FXMLLoader loader =new FXMLLoader(getClass().getResource("/SeConnecter.fxml"));
            try {
                Parent root=loader.load();
                Scene scene=new Scene(root);
                primaryStage.setTitle("StudyLand");
                primaryStage.setScene(scene);
                primaryStage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        }
}
