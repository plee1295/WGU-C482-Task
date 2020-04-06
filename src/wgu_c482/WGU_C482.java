/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wgu_c482;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author parkerlee
 */
public class WGU_C482 extends Application {
    Stage window;
    private AnchorPane MainScreen;

    public void initMainScreen() throws IOException{

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(WGU_C482.class.getResource("Main.fxml"));
        AnchorPane MainScreen = (AnchorPane) loader.load();
    
        Scene scene = new Scene(MainScreen);
        
        window.setScene(scene);
        window.show();
    }
    
    public void showMainScreen() throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(WGU_C482.class.getResource("Main.fxml"));
        AnchorPane MainScreen = (AnchorPane) loader.load();
        
        MainController controller = loader.getController();
        controller.setMainApp(this);
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle("Inventory Mangement System"); 
        initMainScreen();
        showMainScreen();
        
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
