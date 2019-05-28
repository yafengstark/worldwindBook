package gov.nasa.worldwindx.examples.elevations.menu;

import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwindx.examples.elevations.boundary.BoundarySelectionsView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

/**
 * @author tony
 * @date 2019/5/28 22:27
 */
public class ElevationMenuFactory {
    Menu elevationMenu;

    WorldWindow wwd;

    public ElevationMenuFactory(WorldWindow worldWindow) {

        wwd = worldWindow;
        elevationMenu = new Menu("地形");
        elevationMenu.getItems().addAll(
                createOutputElevations()
//                createExportElevationTif()
        );
    }

    public Menu getElevationMenu() {
        return elevationMenu;
    }

    /**
     * 输出区域内的高程值
     *
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
}
