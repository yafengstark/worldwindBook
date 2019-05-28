package gov.nasa.worldwindx.examples.jfx;

/**
 * @author tony
 * @date 2019/4/26 20:48
 */


import gov.nasa.worldwind.*;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.awt.WorldWindowGLJPanel;
import gov.nasa.worldwind.event.RenderingExceptionListener;
import gov.nasa.worldwind.exception.WWAbsentRequirementException;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.geom.Sector;
import gov.nasa.worldwind.globes.Earth;
import gov.nasa.worldwind.globes.EarthFlat;
import gov.nasa.worldwindx.applications.worldwindow.WorldWindow;
import gov.nasa.worldwindx.applications.worldwindow.core.Constants;
import gov.nasa.worldwindx.applications.worldwindow.util.Util;
import javafx.application.Application;
import javafx.embed.swing.SwingNode;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;

/**
 * @author com *
 */
public class JavaFxFirst extends Application {

    public static void main(String[] args) {

        System.out.println("Launching JavaFX application.");

        // Start the JavaFX application by calling launch().
        launch(args);
    }

    // Override the init() method.
    @Override
    public void init() {
        System.out.println("Inside the init() method.");
    }

    // Override the start() method.
    @Override
    public void start(Stage myStage) {

        System.out.println("Inside the start() method.");

        // Give the stage a title.
        myStage.setTitle("JavaFX Skeleton.");

        // Create a root node. In this case, a flow layout
        // is used, but several alternatives exist.
        WorldWindowGLJPanel wwd = new WorldWindowGLJPanel();
        Model m = new BasicModel();
        m.setGlobe(new Earth());
        wwd.setModel(m);

        // -------------

        Sector sector = Sector.fromDegrees(20, 30, -110, -100);


        wwd.getView().setEyePosition(new Position(sector.getCentroid(), 100_000));

        SwingNode swingNode = new SwingNode();

        swingNode.setContent(wwd);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(swingNode);




        // Create a scene.
        Scene myScene = new Scene(borderPane, 300, 200);

        // Set the scene on the stage.
        myStage.setScene(myScene);

        // Show the stage and its scene.
        myStage.show();
    }

    // Override the stop() method.
    @Override
    public void stop() {
        System.out.println("Inside the stop() method.");
    }
}