package controllers;
import entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import security.Session;
import security.UserInfo;
import services.ServiceUser;

import java.io.IOException;
import java.sql.SQLException;

public class DashboardFormateur {

    UserInfo userInfo = Session.getInstance().userInfo;
    @FXML
    private Button btn_projet;
    @FXML
    private Button btnReponses;
    @FXML
    private Button id_formation_admin;
    @FXML
    private Button btn_parameter;

    @FXML
    private Button btn_profile;

    @FXML
    private StackPane centerPane;

    @FXML
    private Button id_evaluation;

    @FXML
    private Button id_formation;
    @FXML
    private ImageView id_image_nav;
    @FXML
    private Button id_home;

    @FXML
    private Label id_nom1;

    public Label getId_nom1() {
        return id_nom1;
    }

    public void setId_nom1(Label id_nom1) {
        this.id_nom1 = id_nom1;
    }

    @FXML
    private Button  btn_refresh;
    @FXML
    private Button id_facture;



    @FXML
    private TextField id_recherche_formation;

    @FXML
    private VBox id_side_bar;

    @FXML
    private BorderPane rootPane;
    @FXML
    private ComboBox<String> combo_login;
    @FXML
    void onComboLoginSelected(ActionEvent event) {
        String selectedOption = combo_login.getValue();
        switch (selectedOption) {
            case "Modifier Email":
                // Pas besoin de se déconnecter de la session ici
                FXMLLoader loader2 = new FXMLLoader(getClass().getResource("/modifierEmail.fxml"));
                try {
                    Parent defaultRoot = loader2.load();
                    ModifierEmail controller = loader2.getController();
                    controller.setId_email(userInfo.email);
                    centerPane.getChildren().clear();
                    centerPane.getChildren().add(defaultRoot);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "Logout":
                Session.getInstance().logout();
                FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/Seconnecter.fxml"));
                try {
                    Parent root = loader1.load();
                    SeConnecter controller = loader1.getController();
                    btn_parameter.getScene().setRoot(root);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case "Aide":
                break;
        }
    }
    User user=new User();
    ServiceUser serviceUser=new ServiceUser();
    @FXML
    public void initialize() {
        try {
            user = serviceUser.rechercheUserParEmail(userInfo.email);
            System.out.println(user);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (user.getImage() != null && !user.getImage().isEmpty()) {
            Image image = new Image("file:" + user.getImage());
            id_image_nav.setImage(image);
        } else {
            Image defaultImage = new Image("C:\\pidev\\StudyLand\\src\\main\\resources\\src\\77.png");
            id_image_nav.setImage(defaultImage);
        }
        combo_login.getItems().addAll( "Modifier Email","Logout", "Aide");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/DashbordFormateurParDefaut.fxml"));
        try {
            Parent defaultRoot = loader.load();
            centerPane.getChildren().clear();
            centerPane.getChildren().add(defaultRoot);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void home(ActionEvent event) {
        FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/home.fxml"));
        try {
            Parent root = loader1.load();
            SeConnecter controller = loader1.getController();
            id_evaluation.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    @FXML
    void modification(ActionEvent event) {
        UserInfo userInfo = Session.getInstance().userInfo;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GererProfile.fxml"));
        try {
            Parent root = loader.load();
            centerPane.getChildren().clear();
            centerPane.getChildren().add(root);
            GererProfile controller = loader.getController();
            controller.getId_nom().setText(userInfo.nom);
            controller.getId_prenom().setText(userInfo.prenom);
            controller.getId_email().setText(userInfo.email);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    @FXML
    void PageParDefaut(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/DashbordFormateurParDefaut.fxml"));
        try {
            Parent defaultRoot = loader.load();
            centerPane.getChildren().clear();
            centerPane.getChildren().add(defaultRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void refreshData(ActionEvent event) {
        loadData();
    }
    private void loadData() {
        ServiceUser serviceUser = new ServiceUser();
        try {
            user = serviceUser.rechercheUserParEmail(userInfo.email);
            Image image = new Image("file:" + user.getImage());
            id_image_nav.setImage(image);
            id_nom1.setText(user.getNom());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void affichageFormatuer(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterFormation.fxml"));
        try {
            Parent root = loader.load();
            centerPane.getChildren().clear();
            centerPane.getChildren().add(root);
            AjouterFormationController controller = loader.getController();
            controller.setCenterPane(centerPane);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


    @FXML
    void projetAdmin(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Ajoutprojet.fxml"));
        try {
            Parent root = loader.load();
            centerPane.getChildren().clear();
            centerPane.getChildren().add(root);
            AjoutProjetController controller = loader.getController();
            controller.setCenterPane(centerPane);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void EvaluationFormateur(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/lesevaluation.fxml"));
        try {
            Parent root = loader.load();
            centerPane.getChildren().clear();
            centerPane.getChildren().add(root);
            Lesevaluation controller = loader.getController();
            controller.setCenterPane(centerPane);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void Question(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajouterQuestion.fxml"));
        try {
            Parent root = loader.load();
            centerPane.getChildren().clear();
            centerPane.getChildren().add(root);
            AjouterQuestion controller = loader.getController();
            controller.setCenterPane(centerPane);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void Reponses(ActionEvent event) {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/reponce.fxml"));
        try {
            Parent root = loader.load();
            centerPane.getChildren().clear();
            centerPane.getChildren().add(root);
            Reponce controller = loader.getController();
            controller.setCenterPane(centerPane);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    public void AfficherMessage(ActionEvent event) {
    }
}
