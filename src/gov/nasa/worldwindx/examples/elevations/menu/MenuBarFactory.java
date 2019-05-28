package gov.nasa.worldwindx.examples.elevations.menu;

import gov.nasa.worldwind.WorldWindow;
import javafx.scene.control.MenuBar;

/**
 * @author tony
 * @date 2019/5/28 21:28
 */
public class MenuBarFactory {

    WorldWindow worldWindow;
    MenuBar menuBar;
    public MenuBarFactory(WorldWindow wwd) {
        worldWindow = wwd;
         menuBar = new MenuBar();
        menuBar.getMenus().addAll(
                new ImageryMenuFactory().getImageryMenu(),
                new ElevationMenuFactory(worldWindow).getElevationMenu(),
                new ToolMenuFactory(worldWindow).getToolMenu(),
                new SystemMenuFactory(worldWindow).getSystemMenu()
        );

    }

    public MenuBar getMenuBar() {
        return menuBar;
    }
}
