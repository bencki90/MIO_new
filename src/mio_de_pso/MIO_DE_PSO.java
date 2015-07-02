/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mio_de_pso;

import javafx.application.Application;
import javafx.stage.Stage;


public class MIO_DE_PSO extends Application {

    @Override
    public void start(Stage primaryStage) {
                
        MainForm root = new MainForm();

        root.setVisible(true);
    }
   
    
    public static void main(String[] args) {
        launch(args);
    }  
}
