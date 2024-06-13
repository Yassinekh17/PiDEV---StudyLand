package controllers;
import entities.Project;
import entities.StatutTache;
import entities.taches_projet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import services.ServiceProject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import services.ServiceTaches;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Date;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class AfficheTacheController {

    @FXML
    private StackPane centerPane;

    public StackPane getCenterPane() {
        return centerPane;
    }

    public void setCenterPane(StackPane centerPane) {
        this.centerPane = centerPane;
    }

    @FXML
    private Label DescT;

    @FXML
    private ListView<String> ListeTaches;

    @FXML
    private Label NomT;

    @FXML
    private Label StatutT;

    public Label getDescT() {
        return DescT;
    }

    public void setDescT(String descT) {
        this.DescT.setText(descT);
    }

    public Label getNomT() {
        return NomT;
    }

    public void setNomT(String nomT) {
        this.NomT.setText(nomT);
    }

    public Label getStatutT() {
        return StatutT;
    }

    public void setStatutT(String statutT) {
        this.StatutT.setText(statutT);
    }
    ServiceTaches st = new ServiceTaches();
    @FXML
    void AfficherTachess(ActionEvent event) {
        try {

            List<taches_projet> tacheProjet = st.afficher();

            // Clear any existing items in the ListView
            ListeTaches.getItems().clear();

            // Add each Formation object to the ListView
            for (taches_projet tacheProjets : tacheProjet) {
                ListeTaches.getItems().add(tacheProjets.toString());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void SupprimerTache(ActionEvent actionEvent) {
        // Get the selected Categorie object from the ListView
        String selectedItem = ListeTaches.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            // Parse the ID from the selected item
            int id = parseIdFromSelectedItem(selectedItem);

            try {
                // Delete the Categorie from the database
                st.supprimer(new taches_projet(id,null, null,null,null,null,null,0)); // Create a temporary tache object with only the ID
                // Refresh the ListView
                AfficherTachess(actionEvent);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                // Handle the exception appropriately
            }
        } else {
            System.out.println("No item selected.");
        }
    }
    // Helper method to parse the ID from the string representation of a Categorie object
    private int parseIdFromSelectedItem(String selectedItem) {
        // Assuming your string representation is in the format "Categorie{idCategorie=<id>, nomCategorie=<nom>}"
        int startIndex = selectedItem.indexOf("id_taches=") + "id_taches=".length();
        int endIndex = selectedItem.indexOf(",", startIndex);


        return Integer.parseInt(selectedItem.substring(startIndex, endIndex));
    }


    @FXML
    private void ModifierTache(ActionEvent actionEvent) {
        String selectedItem = ListeTaches.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            taches_projet selectedTache = parseTacheFromString(selectedItem);

            // Check if parsing was successful
            if (selectedTache != null) {
                // Parse the ID from the selected item
                int id = parseIdFromSelectedItem(selectedItem);

                // Show a dialog to prompt the user for the new category name
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Modifier Tache");
                dialog.setHeaderText("Modifier les donnes du Tache");
                dialog.setContentText("Nouveau nom:");

                // Get user input
                Optional<String> result = dialog.showAndWait();
                result.ifPresent(newNom -> {
                    try {
                        // Create a new tache object with the updated name
                        taches_projet updatedTache = new taches_projet(
                                selectedTache.getId_taches(),
                                newNom,
                                selectedTache.getDesc_tache(),
                                selectedTache.getDate_D(),
                                selectedTache.getDate_F(),
                                selectedTache.getStatut(),
                                selectedTache.getResponsable(),
                                selectedTache.getId_projet()
                        );

                        // Call the modifier method in your ServiceTache class
                        st.modifier(updatedTache);

                        // Refresh the ListView
                        AfficherTachess(actionEvent);
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                        // Handle the exception appropriately
                    }
                });
            } else {
                System.out.println("Error parsing selected task.");
            }
        } else {
            System.out.println("No item selected.");
        }
    }

    private taches_projet parseTacheFromString(String tacheString) {
        // Sample string: "taches_projet{id_taches=1, nom_tache='TaskName', desc_tache='TaskDesc', Date_D=2024-02-20, Date_F=2024-02-25, statut=STATUT_VALUE, responsable='ResponsibleName', id_projet=2}"

        // Extracting values using regex
        Pattern pattern = Pattern.compile("id_taches=(\\d+), nom_tache='([^']+)', desc_tache='([^']+)', Date_D=(.*), Date_F=(.*), statut=(\\w+), responsable='([^']+)', id_projet=(\\d+)");
        Matcher matcher = pattern.matcher(tacheString);

        if (matcher.find()) {
            int id_taches = Integer.parseInt(matcher.group(1));
            String nom_tache = matcher.group(2);
            String desc_tache = matcher.group(3);
            Date Date_D = parseDateFromString(matcher.group(4)); // Assuming Date_D is in string format
            Date Date_F = parseDateFromString(matcher.group(5)); // Assuming Date_F is in string format
            StatutTache statut = StatutTache.valueOfIgnoreCase(matcher.group(6)); // Corrected to uppercase

            String responsable = matcher.group(7);
            int id_projet = Integer.parseInt(matcher.group(8));

            return new taches_projet(id_taches, nom_tache, desc_tache, Date_D, Date_F, statut, responsable, id_projet);
        }


        return null;
    }

    @FXML
    void ExportExcel(ActionEvent event) {
        try {
            // Get the list of tasks from the service
            List<taches_projet> tacheProjetList = st.afficher();

            // Create a new workbook and sheet
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Taches");

            // Create header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Nom Tache");
            headerRow.createCell(2).setCellValue("Description Tache");
            headerRow.createCell(3).setCellValue("Date Debut");
            headerRow.createCell(4).setCellValue("Date Fin");
            headerRow.createCell(5).setCellValue("Statut");
            headerRow.createCell(6).setCellValue("Responsable");
            headerRow.createCell(7).setCellValue("ID Projet");

            // Create data rows
            int rowNum = 1;
            for (taches_projet tache : tacheProjetList) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(tache.getId_taches());
                row.createCell(1).setCellValue(tache.getNom_tache());
                row.createCell(2).setCellValue(tache.getDesc_tache());
                row.createCell(3).setCellValue(tache.getDate_D().toString());
                row.createCell(4).setCellValue(tache.getDate_F().toString());
                row.createCell(5).setCellValue(tache.getStatut().name());
                row.createCell(6).setCellValue(tache.getResponsable());
                row.createCell(7).setCellValue(tache.getId_projet());
            }

            // Save the workbook to a file
            try {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save Excel File");
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
                File file = fileChooser.showSaveDialog(null);

                if (file != null) {
                    try (FileOutputStream outputStream = new FileOutputStream(file)) {
                        workbook.write(outputStream);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            workbook.close();

        } catch (SQLException | IOException e) {
            System.out.println(e.getMessage());
        }
    }




    // Helper method to parse Date from string
    private Date parseDateFromString(String dateString) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            // Handle parsing exception
            e.printStackTrace();
            return null;
        }
    }
}
