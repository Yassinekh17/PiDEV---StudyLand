package controllers;

import entities.Categorie;
import entities.Formation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import services.ServiceCategorie;
import services.ServiceFormation;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class CategorieController {

    @FXML
    private TextField NomC;
    private final ServiceCategorie CS= new ServiceCategorie();

    @FXML
    private ListView<String> categorieListView;
    private List<Categorie> categories;




    public void AjouterCategorie(ActionEvent actionEvent) {
        if (NomC.getText().isEmpty()) {
            System.out.println("Please fill in all fields");
            // Display error message to user
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Categorie vide");
            successAlert.setHeaderText("Veuillez remplir le formulaire pour ajouter un Categorie !!");
            successAlert.showAndWait();
            return;
        }
        // Check if a category with the same name already exists
        try {
            boolean categoryExists = CS.checkCategoryExists(NomC.getText());
            if (categoryExists) {
                // Display error message to user
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Duplicate Category");
                alert.setHeaderText("A category with the same name already exists");
                alert.showAndWait();
                return;
            }
        } catch (SQLException e) {
            // Handle the exception appropriately, e.g., show an error message
            System.out.println(e.getMessage());
            return;
        }
        try {
            CS.ajouter(new Categorie(NomC.getText()));
            // Display success message to user
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Success");
            successAlert.setHeaderText("Category added successfully");
            successAlert.showAndWait();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            // Handle the exception appropriately, e.g., show an error message
        }}

    public void AfficherCat(ActionEvent actionEvent) {
        try {
            List<Categorie> categories = CS.afficher();

            // Clear any existing items in the ListView
            categorieListView.getItems().clear();
            for (Categorie categorie : categories) {
                categorieListView.getItems().add(categorie.toString());
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }



    @FXML
    public void supprimerCategorie(ActionEvent actionEvent) {
        // Get the selected Categorie object from the ListView
        String selectedItem = categorieListView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            // Parse the ID from the selected item
            int id = parseIdFromSelectedItem(selectedItem);

            try {
                // Delete the Categorie from the database
                CS.supprimer(new Categorie(id, null)); // Create a temporary Categorie object with only the ID
                // Refresh the ListView
                AfficherCat(actionEvent);
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
        int startIndex = selectedItem.indexOf("idCategorie=") + "idCategorie=".length();
        int endIndex = selectedItem.indexOf(",", startIndex);
        return Integer.parseInt(selectedItem.substring(startIndex, endIndex));
    }
    @FXML
    private void modifierCategorie(ActionEvent actionEvent) {
        // Get the selected Categorie object from the ListView
        String selectedItem = categorieListView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            // Parse the ID from the selected item
            int id = parseIdFromSelectedItem(selectedItem);

            // Show a dialog to prompt the user for the new category name
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Modifier Categorie");
            dialog.setHeaderText("Modifier le nom de la categorie");
            dialog.setContentText("Nouveau nom:");

            // Get user input
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(newNom -> {
                try {
                    // Create a new Categorie object with the updated name
                    Categorie updatedCategorie = new Categorie(id, newNom);
                    // Call the modifier method in your ServiceCategorie class
                    CS.modifier(updatedCategorie);
                    // Refresh the ListView
                    AfficherCat(actionEvent);
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                    // Handle the exception appropriately
                }
            });
        } else {
            System.out.println("No item selected.");
        }
    }
    @FXML
    void accederFormation(ActionEvent event) {
        FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/AjouterFormation.fxml"));
        try {
            Parent root = loader1.load();
            AjouterFormationController controller = loader1.getController();
            NomC.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println(e.getMessage());

        }
    }


}

