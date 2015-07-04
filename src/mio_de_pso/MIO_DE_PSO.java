/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mio_de_pso;

import algorithms.PSO.Swarm;
import algorithms.common.IEvolutionaryAlgorithm;
import functionParsing.RPNEvaluator;
import functionParsing.ShuntingYard;
import javafx.application.Application;
import javafx.stage.Stage;


public class MIO_DE_PSO extends Application {

    public String quinticFunction = "abs ( x1 ^ 5 - 3 * x1 ^ 4 + 4 * x1 ^ 3 + 2 * x1 ^ 2 - 10 * x1 - 4 ) + abs ( x2 ^ 5 - 3 * x2 ^ 4 + 4 * x2 ^ 3 + 2 * x2 ^ 2 - 10 * x2 - 4 ) + abs ( x3 ^ 5 - 3 * x3 ^ 4 + 4 * x3 ^ 3 + 2 * x3 ^ 2 - 10 * x3 - 4 ) + abs ( x4 ^ 5 - 3 * x4 ^ 4 + 4 * x4 ^ 3 + 2 * x4 ^ 2 - 10 * x4 - 4 )";
    
    @Override
    public void start(Stage primaryStage) {
        
        /*RPNEvaluator.initDecimalformat();
        String rpn = ShuntingYard.infixToPostfix(quinticFunction);
        
        
        
        IEvolutionaryAlgorithm pso = new Swarm(rpn, 30, )*/
        
        
        
        MainForm root = new MainForm();

        root.setVisible(true);
    }
   
    
    public static void main(String[] args) {
        launch(args);
    }  
}
