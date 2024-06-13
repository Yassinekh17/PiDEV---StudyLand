package controllers;

import controllers.CardUser;
import entities.User;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import services.ServiceFormateur;
import services.ServiceFormation;
import services.ServiceUser;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class DashbordAdminParDefaut {

    @FXML
    private AnchorPane userDetailsContainer;
    @FXML
    private ComboBox<String> com_try;
    @FXML
    private TableColumn<User, String> Role_user;
    @FXML
    private TableView<User> id_tab_user;
    @FXML
    private AnchorPane chartContainer;
    @FXML
    private AnchorPane chartContainer1;
    @FXML
    private TableColumn<User, String> email_user;
    @FXML
    private Label nbAdmin;
    @FXML
    private Label nbApp;
    @FXML
    private Label nbFormateur;
    @FXML
    private TableColumn<User, String> nom_user;
    @FXML
    private TableColumn<User, String> pre_user;


    @FXML
    public void initialize() {
        com_try.getItems().addAll("Tous", "Apprenant", "Admin", "Formateur");
        com_try.setValue("Tous");

        com_try.setOnAction(event -> {
            String selectedRole = com_try.getValue();
            if (selectedRole != null) {
                try {
                    List<User> filteredUsers = null;
                    ServiceUser serviceUser = new ServiceUser();

                    if (selectedRole.equals("Tous")) {
                        filteredUsers = serviceUser.afficher();
                    } else {
                        filteredUsers = serviceUser.afficherByRole(selectedRole);
                    }
                    id_tab_user.setItems(FXCollections.observableArrayList(filteredUsers));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        initTable();
        updatePieChart();
        id_tab_user.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                User selectedUser = id_tab_user.getSelectionModel().getSelectedItem();
                if (selectedUser != null) {
                    ServiceUser serviceUser = new ServiceUser();
                    try {
                        User selectUs = serviceUser.rechercheUserParEmail(selectedUser.getEmail());
                        int userId = selectUs.getId();
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/CardUser.fxml"));
                        AnchorPane card = loader.load();
                        CardUser controller = loader.getController();
                        controller.initData(selectUs);
                        userDetailsContainer.getChildren().clear();
                        userDetailsContainer.getChildren().add(card);
                    } catch (IOException | SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void initTable() {
        try {
            ServiceUser serviceUser = new ServiceUser();
            List<User> users = serviceUser.afficher();
            id_tab_user.setItems(FXCollections.observableArrayList(users));
            nom_user.setCellValueFactory(new PropertyValueFactory<>("nom"));
            pre_user.setCellValueFactory(new PropertyValueFactory<>("prenom"));
            email_user.setCellValueFactory(new PropertyValueFactory<>("email"));
            Role_user.setCellValueFactory(new PropertyValueFactory<>("role"));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updatePieChart() {
        ServiceUser serviceUser = new ServiceUser();
        ServiceFormation serviceFormation=new ServiceFormation();
        int nbApprenants = 0;
        try {
            nbApprenants = serviceUser.countApp();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        int nbFormateurs = 0;
        try {
            nbFormateurs = serviceUser.countFomateur();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        int nbAdmins = 0;
        try {
            nbAdmins = serviceUser.countAdmin();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        int nbFormationJava= 0;
        try {
            nbFormationJava = serviceFormation.statistiqueFormation("java");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        int nbFormationC = 0;
        try {
            nbFormationC =serviceFormation.statistiqueFormation("c++");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        int nbFormationPascal = 0;
        try {
            nbFormationPascal = serviceFormation.statistiqueFormation("pascal");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        nbAdmin.setText(String.valueOf(nbAdmins));
        nbFormateur.setText(String.valueOf(nbFormateurs));
        nbApp.setText(String.valueOf(nbApprenants));
        PieChart.Data apprenantsData = new PieChart.Data("Apprenants", nbApprenants);
        PieChart.Data formateursData = new PieChart.Data("Formateurs", nbFormateurs);
        PieChart.Data adminsData = new PieChart.Data("Admins", nbAdmins);
        PieChart pieChart = new PieChart();
        pieChart.getData().addAll(apprenantsData, formateursData, adminsData);
        pieChart.setTitle("Répartition des utilisateurs en StudyLand");
        chartContainer.getChildren().add(pieChart);
        AnchorPane.setTopAnchor(pieChart, 0.0);
        AnchorPane.setRightAnchor(pieChart, 0.0);
        AnchorPane.setBottomAnchor(pieChart, 0.0);
        AnchorPane.setLeftAnchor(pieChart, 0.0);

        // formation
        BarChart.Data nbFormationJava1 = new BarChart.Data("JAVA", nbFormationJava);
        BarChart.Data nbFormationC1 = new BarChart.Data("c++", nbFormationC);
        BarChart.Data nbFormationPascal1 = new BarChart.Data("Pascal", nbFormationPascal);
        // Créer des séries de données pour chaque catégorie
        XYChart.Series<String, Number> seriesApprenants = new XYChart.Series<>();
        XYChart.Series<String, Number> seriesFormateurs = new XYChart.Series<>();
        XYChart.Series<String, Number> seriesAdmins = new XYChart.Series<>();
        seriesApprenants.getData().add(nbFormationJava1);
        seriesFormateurs.getData().add(nbFormationC1);
        seriesAdmins.getData().add(nbFormationPascal1);
        BarChart<String, Number> barChart1 = new BarChart<>(new CategoryAxis(), new NumberAxis());
        barChart1.getData().addAll(seriesApprenants, seriesFormateurs, seriesAdmins);
        barChart1.setTitle("Répartition des formations");
        chartContainer1.getChildren().add(barChart1);
        AnchorPane.setTopAnchor(barChart1, 0.0);
        AnchorPane.setRightAnchor(barChart1, 0.0);
        AnchorPane.setBottomAnchor(barChart1, 0.0);
        AnchorPane.setLeftAnchor(barChart1, 0.0);
    }
}
