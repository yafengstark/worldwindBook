package gov.nasa.worldwindx.examples.elevations;


import gov.nasa.worldwind.Model;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.awt.WorldWindowGLJPanel;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.geom.Sector;
import gov.nasa.worldwind.layers.ViewControlsLayer;
import gov.nasa.worldwind.layers.ViewControlsSelectListener;
import gov.nasa.worldwind.util.statusbar.StatusBarView;
import gov.nasa.worldwindx.examples.elevations.boundary.BoundarySelectionsView;
import javafx.application.Application;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 画框选择范围
 *
 * @author com *
 */
public class ElevationImage extends Application {

    private static Logger logger= LoggerFactory.getLogger(ElevationImage.class);

    public static void main(String[] args) {

        System.out.println("Launching JavaFX application.");

        // Start the JavaFX application by calling launch().
        launch(args);
    }

    public static WorldWindowGLJPanel wwd;

    // Override the init() method.
    @Override
    public void init() {
        System.out.println("Inside the init() method.");
    }

    // Override the start() method.
    @Override
    public void start(Stage myStage) {
        wwd = new WorldWindowGLJPanel();

        System.out.println("Inside the start() method.");

        // Give the stage a title.
        myStage.setTitle("JavaFX Skeleton.");
        BorderPane borderPane = new BorderPane();

        MenuBar menuBar = new MenuBar();
        borderPane.setTop(menuBar);

        Menu elevationMenu = new Menu("地形");
        elevationMenu.getItems().addAll(
                createOutputElevations()
//                createExportElevationTif()
        );

        Menu imageryMenu = new Menu("影像");
        imageryMenu.getItems().addAll(

        );

        menuBar.getMenus().addAll(
                imageryMenu,
                elevationMenu
        );



        // Create a root node. In this case, a flow layout
        // is used, but several alternatives exist.


        // Create the default model as described in the current worldwind properties.
        Model m = (Model) WorldWind.createConfigurationComponent(AVKey.MODEL_CLASS_NAME);
        this.wwd.setModel(m);

        ViewControlsLayer viewControlsLayer = new ViewControlsLayer();
        wwd.getModel().getLayers().add(viewControlsLayer);
        ViewControlsSelectListener listener = new ViewControlsSelectListener(wwd, viewControlsLayer);
        listener.setRepeatTimerDelay(30);
        listener.setZoomIncrement(0.5);
        listener.setPanIncrement(0.5);
        wwd.addSelectListener(listener);
        // -------------


        Sector sector = Sector.fromDegrees(20, 30, 110, 120);


        wwd.getView().setEyePosition(new Position(sector.getCentroid(), 1000_000));

//        m.getGlobe().getElevations()


        SwingNode swingNode = new SwingNode();

        swingNode.setContent(wwd);


        borderPane.setCenter(swingNode);



        borderPane.setBottom(new StatusBarView(wwd).getPane());

        // Create a scene.
        Scene myScene = new Scene(borderPane, 800, 800);

        // Set the scene on the stage.
        myStage.setScene(myScene);

        // Show the stage and its scene.
        myStage.show();
    }

    /**
     * 输出区域内的高程值
     * @return
     */
    private MenuItem createOutputElevations() {
        MenuItem outputElevation = new MenuItem("输出范围内的...");
        outputElevation.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                new BoundarySelectionsView(wwd);
            }
        });
        return outputElevation;
    }

    /**
     * 输出高程tif
     * @return
     */
    private MenuItem createExportElevationTif() {
        MenuItem outputElevation = new MenuItem("输出高程tif");
        outputElevation.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                logger.debug("");
//                new BoundarySelectionsView();


            }
        });
        return outputElevation;
    }

    // Override the stop() method.
    @Override
    public void stop() {
        System.out.println("Inside the stop() method.");
    }
}