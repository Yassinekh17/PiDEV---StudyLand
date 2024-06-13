package controllers;
import entities.Apprenant;
import entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import security.Session;
import security.UserInfo;
import services.ServiceApprenant;
import services.ServiceUser;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;
public class DashboardAdmin {
    @FXML
    private Button btnQuestions;
    @FXML
    private Button categorieAd;
    @FXML
    private Button btnReponses;
    @FXML
    private ImageView id_image_nav;
    @FXML
    private Button btn_projetAdmin;
    @FXML
    private Button btn_Admin;
    @FXML
    private Button id_formation_admin;
    @FXML
    private Button btn_App;
    @FXML
    private Button btn_refresh;
    @FXML
    private Button btn_formateur;
    @FXML
    private Button btn_profile;
    @FXML
    private Button btn_parameter;


    @FXML
    private StackPane centerPane;

    @FXML
    private Button id_home;
    @FXML
    private ImageView btn_refrech;

    @FXML
    private Label id_nom1;
    //get et set
    UserInfo userInfo = Session.getInstance().userInfo;
    ServiceUser serviceUser = new ServiceUser();
    User user = new User();


    public void setId_nom1(Label id_nom1) {
        this.id_nom1 = id_nom1;
    }

    public Label getId_nom1() {
        return id_nom1;
    }

    @FXML
    private ComboBox<String> combo_login;

    @FXML
    private Button id_projet;

    @FXML
    private TextField id_recherche;

    @FXML
    private VBox id_side_bar;

    @FXML
    private BorderPane rootPane;
    @FXML
    private Label labelImage;
    private ImageView imageView; // Assurez-vous que imageView est correctement injecté depuis votre fichier FXML

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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/DashbordAdminParDefaut.fxml"));
        try {
            Parent defaultRoot = loader.load();
            centerPane.getChildren().clear();
            centerPane.getChildren().add(defaultRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }
        loadData();

    }

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
                    btn_App.getScene().setRoot(root);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case "Aide":
                break;
        }
    }

    // Vos autres méthodes et champs

    @FXML
    void afficherApprenants(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AffichageApprenant.fxml"));
        try {
            Parent affichageApprenantRoot = loader.load();
            centerPane.getChildren().clear();
            centerPane.getChildren().add(affichageApprenantRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void afficherFormateur(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ForamteurAffichage.fxml"));
        try {
            Parent affichageFormateurRoot = loader.load();
            centerPane.getChildren().clear();
            centerPane.getChildren().add(affichageFormateurRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @FXML
    void afficherAdmin(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminAffichage.fxml"));
        try {
            Parent affichageAdminRoot = loader.load();
            centerPane.getChildren().clear();
            centerPane.getChildren().add(affichageAdminRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void afficherParametre(ActionEvent event) {
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
    void home(ActionEvent event) {
        FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/home.fxml"));
        try {
            Parent root = loader1.load();
            SeConnecter controller = loader1.getController();
          btn_App.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    @FXML
    void onREcherche(ActionEvent event) {
        String email = id_recherche.getText();
        System.out.println(email);
        try {
            ServiceUser  serviceUser = new ServiceUser();
            try {
                User user = serviceUser.rechercheUserParEmail(email);
                if (user != null) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/CardUser.fxml"));
                    Parent root = loader.load();
                    centerPane.getChildren().clear();
                    centerPane.getChildren().add(root);
                    CardUser cardUserController = loader.getController();
                    cardUserController.getEmail().setText(user.getEmail());
                    cardUserController.getNom_user().setText(user.getNom());
                    cardUserController.getPrenom_user().setText(user.getPrenom());
                    cardUserController.getRole().setText(user.getRole());
                } else {
                    System.out.println("Aucun utilisateur trouvé pour l'email spécifié.");
                }
            } catch (SQLException e) {
                // Gérer l'exception SQL
                e.printStackTrace();
                System.out.println("Erreur lors de la recherche de l'utilisateur.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement de l'interface utilisateur.");
        }
    }
    @FXML
    void pageParDefaut(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/DashbordAdminParDefaut.fxml"));
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
    void formationAdmin(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherFormationAdmin.fxml"));
        try {
            Parent root = loader.load();
            centerPane.getChildren().clear();
            centerPane.getChildren().add(root);
            AfficherFormationAdmin controller = loader.getController();
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
    @FXML
    void AfficherMessage(ActionEvent event) {

    }

    @FXML
    void Question(ActionEvent event) {
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

    @FXML
    void Reponses(ActionEvent event) {
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
    @FXML
    void AfficherEvaluation(ActionEvent event) {
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
    @FXML
    void categoreisAdmin(ActionEvent event) {

    }


}


