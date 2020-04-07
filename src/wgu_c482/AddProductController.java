/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wgu_c482;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import static wgu_c482.Inventory.getPartInventory;

/**
 * FXML Controller class
 *
 * @author parkerlee
 */
public class AddProductController implements Initializable {
    
    private final ObservableList<Part> currentParts = FXCollections.observableArrayList();
    private String exceptionMessage = new String();
    private int productID;
    private Product productToAdd;

    @FXML
    private TextField AddProductsIDField;
    @FXML
    private TextField AddProductsNameField;
    @FXML
    private TextField AddProductsPriceField;
    @FXML
    private TextField AddProductsInvField;
    @FXML
    private TextField AddProductsMinField;
    @FXML
    private TextField AddProductsMaxField;
    @FXML
    private TextField AddProductDeletePartSearchField;
    @FXML
    private TextField AddProductAddPartSearchField;
    @FXML
    private TableView<Part> AddProductsAddTableView;
    @FXML
    private TableColumn<Part, Integer> AddProductPartIDCol;
    @FXML
    private TableColumn<Part, String> AddProductPartNameCol;
    @FXML
    private TableColumn<Part, Integer> AddProductInvLevelCol;
    @FXML
    private TableColumn<Part, Double> AddProductPriceCol;
    @FXML
    private TableView<Part> AddProductsDeleteTableView;
    @FXML
    private TableColumn<Part, Integer> AddProductCurrentPartIDCol;
    @FXML
    private TableColumn<Part, String> AddProductCurrentPartNameCol;
    @FXML
    private TableColumn<Part, Integer> AddProductCurrentInvCol;
    @FXML
    private TableColumn<Part, Double> AddProductCurrentPriceCol;

    public AddProductController() {}

    @FXML void ClearSearchAdd(ActionEvent event) {
        updatePartTableView();
        AddProductAddPartSearchField.setText("");
    }
        
    @FXML void ClearSearchRemove(ActionEvent event) {
        updateCurrentPartTableView();
        AddProductDeletePartSearchField.setText("");
    }
    
    @FXML
    void handleAddProductsSearchPartAddButton(ActionEvent event) {

        String searchPart = AddProductAddPartSearchField.getText();
        int partIndex = -1;

        if (Inventory.lookupPart(searchPart) == -1) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Search");
            alert.setHeaderText("Part not found");
            alert.setContentText("The part has not been found.");
            alert.showAndWait();
        } else {
            partIndex = Inventory.lookupPart(searchPart);
            Part tempPart = Inventory.getPartInventory().get(partIndex);

            ObservableList<Part> tempProdList = FXCollections.observableArrayList();
            tempProdList.add(tempPart);
            AddProductsAddTableView.setItems(tempProdList);
        }
    }


    @FXML
    void handleAddProductsAddPartButton(ActionEvent event) throws IOException {
        Boolean found = false;
        Part part = AddProductsAddTableView.getSelectionModel().getSelectedItem();
        
        //make sure a part was selected
        if(part != null) {
            for (int i = 0; i < currentParts.size(); i++) {
                if(currentParts.get(i).equals(part)) {
                    found = true;
                }
            } 

            if(found) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Part Duplication");
                alert.setHeaderText("Part already in Product");
                alert.setContentText("This part is already linked to the product");
                alert.showAndWait();
            } else {
                currentParts.add(part);
                AddProductsDeleteTableView.setItems(currentParts);    
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setTitle("No Part selected");
            alert.setHeaderText("Please select a part from the existing list to add to the product"); 
            alert.showAndWait();
        }
    }

    @FXML
    void handleAddProductsSearchPartDeleteButton(ActionEvent event) {

        String searchPart = AddProductDeletePartSearchField.getText();
        int partIndex = -1;

        if (Inventory.lookupPart(searchPart) == -1) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Search");
            alert.setHeaderText("Part not found");
            alert.setContentText("The part has not been found.");
            alert.showAndWait();
        } else {
            partIndex = Inventory.lookupPart(searchPart);
            Part tempPart = Inventory.getPartInventory().get(partIndex);

            ObservableList<Part> tempProdList = FXCollections.observableArrayList();
            tempProdList.add(tempPart);
            AddProductsDeleteTableView.setItems(tempProdList);
        }
    }

    
    
    @FXML
    void handleAddProductsDeletePartButton(ActionEvent event) {

        Part part = AddProductsDeleteTableView.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Confirm Current Part Delete!");
        alert.setContentText("Are you sure you want to delete part " + part.getPartName() + " from parts?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            System.out.println("Part deleted.");
            currentParts.remove(part);
        } else {
            System.out.println("Cancel clicked.");
        }
    }

    @FXML
    void handleAddProductsSaveButton(ActionEvent event) throws IOException {

        String productName = AddProductsNameField.getText();
        String productInv = AddProductsInvField.getText();
        String productPrice = AddProductsPriceField.getText();
        String productMin = AddProductsMinField.getText();
        String productMax = AddProductsMaxField.getText();

        try {

            exceptionMessage = Product.validateProduct(productName, Integer.parseInt(productMin), Integer.parseInt(productMax), Integer.parseInt(productInv),
                    Double.parseDouble(productPrice), currentParts, exceptionMessage);

            if (exceptionMessage.length() > 0) {

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error Adding Product");
                alert.setHeaderText("Error");
                alert.setContentText(exceptionMessage);
                alert.showAndWait();
                exceptionMessage = "";
            } else {
                System.out.println("Product name: " + productName);

                Product newProduct = new Product();

                newProduct.setProductID(productID);
                newProduct.setProductName(productName);
                newProduct.setProductPrice(Double.parseDouble(productPrice));
                newProduct.setProductInStock(Integer.parseInt(productInv));
                newProduct.setProductMin(Integer.parseInt(productMin));
                newProduct.setProductMax(Integer.parseInt(productMax));
                newProduct.setProductParts(currentParts);
                Inventory.addProduct(newProduct);

                Parent productsSave = FXMLLoader.load(getClass().getResource("Main.fxml"));
                Scene scene = new Scene(productsSave);

                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

                window.setScene(scene);
                window.show();
            }
        } catch (NumberFormatException e) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error Adding Part!");
            alert.setHeaderText("Error!");
            alert.setContentText("Fields cannot be left blank!");
            alert.showAndWait();
        }
    }

    @FXML
    void handleAddProductsCancelButton(ActionEvent event) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirmation Needed!");
        alert.setHeaderText("Confirm Product Delete!");
        alert.setContentText("Are you sure you want to delete product " + AddProductsNameField.getText() + "?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {

            Parent partsCancel = FXMLLoader.load(getClass().getResource("Main.fxml"));
            Scene scene = new Scene(partsCancel);

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

            window.setScene(scene);
            window.show();
        } else {
            System.out.println("You clicked cancel. Please complete part info.");
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        AddProductPartIDCol.setCellValueFactory(cellData -> cellData.getValue().partIDProperty().asObject());
        AddProductPartNameCol.setCellValueFactory(cellData -> cellData.getValue().partNameProperty());
        AddProductInvLevelCol.setCellValueFactory(cellData -> cellData.getValue().partInvProperty().asObject());
        AddProductPriceCol.setCellValueFactory(cellData -> cellData.getValue().partPriceProperty().asObject());

        AddProductCurrentPartIDCol.setCellValueFactory(cellData -> cellData.getValue().partIDProperty().asObject());
        AddProductCurrentPartNameCol.setCellValueFactory(cellData -> cellData.getValue().partNameProperty());
        AddProductCurrentInvCol.setCellValueFactory(cellData -> cellData.getValue().partInvProperty().asObject());
        AddProductCurrentPriceCol.setCellValueFactory(cellData -> cellData.getValue().partPriceProperty().asObject());

        updatePartTableView();
        updateCurrentPartTableView();

        productID = Inventory.getProductIDCount();
        AddProductsIDField.setText("AUTO GEN: " + productID);
    }

    public void updatePartTableView() {
        AddProductsAddTableView.setItems(getPartInventory());
    }

    public void updateCurrentPartTableView() {
        AddProductsDeleteTableView.setItems(currentParts);
    }
}
