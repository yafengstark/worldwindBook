package gov.nasa.worldwindx.examples.eye;

/**
 * @author tony
 * @date 2019/4/26 20:48
 */


import gov.nasa.worldwind.BasicModel;
import gov.nasa.worldwind.Model;
import gov.nasa.worldwind.View;
import gov.nasa.worldwind.awt.WorldWindowGLJPanel;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.geom.Sector;
import gov.nasa.worldwind.globes.Earth;
import gov.nasa.worldwind.view.ViewUtil;
import gov.nasa.worldwind.view.orbit.BasicOrbitView;
import gov.nasa.worldwind.view.orbit.OrbitView;
import javafx.application.Application;
import javafx.embed.swing.SwingNode;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * @author com *
 */
public class ViewGoTo extends Application {

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





        SwingNode swingNode = new SwingNode();

        swingNode.setContent(wwd);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(swingNode);

        // Create a scene.
        Scene myScene = new Scene(borderPane, 600, 600);

        // Set the scene on the stage.
        myStage.setScene(myScene);

        // Show the stage and its scene.
        myStage.show();

        View view = new BasicOrbitView();
        wwd.setView(view);

//        view.goTo(new Position(new LatLon(Angle.fromDegrees(30),Angle.fromDegrees( 120)),100), 100);

//        view.setHeading(Angle.fromDegrees(90));
        view.setEyePosition(new Position(new LatLon(Angle.fromDegrees(30),Angle.fromDegrees( 120)),10000_00));
//        wwd.getView().goTo();
    }

    // Override the stop() method.
    @Override
    public void stop() {
        System.out.println("Inside the stop() method.");
    }
}