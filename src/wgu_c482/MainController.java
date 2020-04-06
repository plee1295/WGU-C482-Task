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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import static wgu_c482.Inventory.getPartInventory;
import static wgu_c482.Inventory.getProductInventory;
import static wgu_c482.Inventory.removePart;
import static wgu_c482.Inventory.removeProduct;
import static wgu_c482.Inventory.validatePartDelete;

/**
 * FXML Controller class
 *
 * @author parkerlee
 */
public class MainController implements Initializable {

    @FXML
    private TableView<Part> MainPartsTableView;
    @FXML
    private TableColumn<Part, Integer> MainPartIDCol;
    @FXML
    private TableColumn<Part, String> MainPartNameCol;
    @FXML
    private TableColumn<Part, Integer> MainPartInvCol;
    @FXML
    private TableColumn<Part, Double> MainPartPriceCol;
    @FXML
    private TableView<Product> MainProductsTableView;
    @FXML
    private TableColumn<Product, Integer> MainProductIDCol;
    @FXML
    private TableColumn<Product, String> MainProductNameCol;
    @FXML
    private TableColumn<Product, Integer> MainProductInvCol;
    @FXML
    private TableColumn<Product, Double> MainProductPriceCol;
    @FXML
    private TextField MainPartsSearchField;
    @FXML
    private TextField MainProductsSearchField;

    private static Part modifyPart;
    private static int modifyPartIndex;
    private static Product modifyProduct;
    private static int modifyProductIndex;

    public static int partToModifyIndex() {
        return modifyPartIndex;
    }

    public static int productToModifyIndex() {
        return modifyProductIndex;
    }

    public MainController() {
    }

    @FXML
    void handleClearPartsButton(ActionEvent event) throws IOException {
        updatePartTableView();
        MainPartsSearchField.setText("");
    }

    @FXML
    void handleClearProductsButton(ActionEvent event) throws IOException {
        updateProductTableView();
        MainProductsSearchField.setText("");
    }

    @FXML
    void handleExitButton(ActionEvent event) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirmation ");
        alert.setHeaderText("Confirm Exit!");
        alert.setContentText("Are you sure you want to exit?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            System.exit(0);
        } else {
            System.out.println("You clicked cancel. Please complete form.");
        }
    }

    @FXML
    void handleAddPartsButton(ActionEvent event) throws IOException {

        Parent addParts = FXMLLoader.load(getClass().getResource("AddPart.fxml"));
        Scene scene = new Scene(addParts);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    @FXML
    void handleAddProductsButton(ActionEvent event) throws IOException {

        Parent addProducts = FXMLLoader.load(getClass().getResource("AddProduct.fxml"));
        Scene scene = new Scene(addProducts);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    @FXML
    void handleModifyPartsButton(ActionEvent event) throws IOException {

        modifyPart = MainPartsTableView.getSelectionModel().getSelectedItem();
        modifyPartIndex = getPartInventory().indexOf(modifyPart);
        Parent modifyParts = FXMLLoader.load(getClass().getResource("ModifyPart.fxml"));
        Scene scene = new Scene(modifyParts);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    @FXML
    void handleModifyProductsButton(ActionEvent event) throws IOException {

        modifyProduct = MainProductsTableView.getSelectionModel().getSelectedItem();
        modifyProductIndex = getProductInventory().indexOf(modifyProduct);
        Parent modifyProducts = FXMLLoader.load(getClass().getResource("ModifyProduct.fxml"));
        Scene scene = new Scene(modifyProducts);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    @FXML
    void handleSearchProductsButton(ActionEvent event) throws IOException {
        String searchProd = MainProductsSearchField.getText();
        int prodIndex = -1;
        if (Inventory.lookupProduct(searchProd) == -1) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Search Error!");
            alert.setHeaderText("Product not found");
            alert.setContentText("The search term entered does not match any Product!");
            alert.showAndWait();
        } else {
            prodIndex = Inventory.lookupProduct(searchProd);
            Product tempProd = Inventory.getProductInventory().get(prodIndex);
            ObservableList<Product> tempProdList = FXCollections.observableArrayList();
            tempProdList.add(tempProd);
            MainProductsTableView.setItems(tempProdList);
        }
    }

    @FXML
    void handleDeleteProductsButton(ActionEvent event) throws IOException {
        Product product = MainProductsTableView.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Confirm?");
        alert.setContentText("Are you sure you want to delete " + product.getProductName() + "?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            removeProduct(product);
            updateProductTableView();
            System.out.println("Product " + product.getProductName() + " was removed.");
        } else {
            System.out.println("Product " + product.getProductName() + " was not removed.");
        }
    }

    @FXML
    void handlePartsSearchButton(ActionEvent event) throws IOException {
        String searchPart = MainPartsSearchField.getText();
        int partIndex = -1;
        if (Inventory.lookupPart(searchPart) == -1) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Search Error");
            alert.setHeaderText("Part not found");
            alert.setContentText("The search term entered does not match any Part!");
            alert.showAndWait();
        } else {
            partIndex = Inventory.lookupPart(searchPart);
            Part tempPart = Inventory.getPartInventory().get(partIndex);
            ObservableList<Part> tempProdList = FXCollections.observableArrayList();
            tempProdList.add(tempPart);
            MainPartsTableView.setItems(tempProdList);
        }
    }

    @FXML
    void handleDeletePartsButton(ActionEvent event) throws IOException {
        Part part = MainPartsTableView.getSelectionModel().getSelectedItem();
        if (validatePartDelete(part)) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Part Delete Error!");
            alert.setHeaderText("Part cannot be removed!");
            alert.setContentText("This part is used in a product.");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle("Product Delete");
            alert.setHeaderText("Confirm?");
            alert.setContentText("Are you sure you want to delete " + part.getPartName() + "?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                removePart(part);
                updatePartTableView();
                System.out.println("Part " + part.getPartName() + " was removed.");
            } else {
                System.out.println("Part " + part.getPartName() + " was not removed.");
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        MainPartIDCol.setCellValueFactory(cellData -> cellData.getValue().partIDProperty().asObject());
        MainPartNameCol.setCellValueFactory(cellData -> cellData.getValue().partNameProperty());
        MainPartInvCol.setCellValueFactory(cellData -> cellData.getValue().partInvProperty().asObject());
        MainPartPriceCol.setCellValueFactory(cellData -> cellData.getValue().partPriceProperty().asObject());
        MainProductIDCol.setCellValueFactory(cellData -> cellData.getValue().productIDProperty().asObject());
        MainProductNameCol.setCellValueFactory(cellData -> cellData.getValue().productNameProperty());
        MainProductInvCol.setCellValueFactory(cellData -> cellData.getValue().productInvProperty().asObject());
        MainProductPriceCol.setCellValueFactory(cellData -> cellData.getValue().productPriceProperty().asObject());
        updatePartTableView();
        updateProductTableView();
    }

    public void updatePartTableView() {
        MainPartsTableView.setItems(getPartInventory());
    }

    public void updateProductTableView() {
        MainProductsTableView.setItems(getProductInventory());
    }

    public void setMainApp(WGU_C482 mainApp) {
        updatePartTableView();
        updateProductTableView();
    }
}
