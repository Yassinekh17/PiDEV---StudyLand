package controllers;

import entities.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.*;
import javafx.scene.layout.AnchorPane;
import security.Session;
import security.UserInfo;
import services.ServiceFormation;
import services.ServiceUser;

import java.io.IOException;
import java.sql.SQLException;

public class DashbordFormateurParDefaut {
    @FXML
    private AnchorPane chartContainer1;

    @FXML
    private AnchorPane id_userdetail;

    public AnchorPane getChartContainer1() {
        return chartContainer1;
    }

    public void setChartContainer1(AnchorPane chartContainer1) {
        this.chartContainer1 = chartContainer1;
    }

    @FXML
    public void initialize() {
        UserInfo userInfo = Session.getInstance().userInfo;
        User user=new User();

        updatePieChart();
        ServiceUser serviceUser2=new ServiceUser();
        try {
            user=serviceUser2.rechercheUserParEmail(userInfo.email);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CardUser.fxml"));
            AnchorPane card = loader.load();
            id_userdetail.getChildren().add(card);
            CardUser controller = loader.getController();
                controller.initData(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void updatePieChart() {
        UserInfo userInfo = Session.getInstance().userInfo;
        User user=new User();

        ServiceFormation serviceFormation=new ServiceFormation();
        int nbFormationJava= 0;
        try {
            nbFormationJava = serviceFormation.statistiqueFormationFormateur(userInfo.id,"java");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        int nbFormationC = 0;
        try {
            nbFormationC =serviceFormation.statistiqueFormationFormateur(userInfo.id, "c++");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        int nbFormationPascal = 0;
        try {
            nbFormationPascal = serviceFormation.statistiqueFormationFormateur(userInfo.id, "pascal");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

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





