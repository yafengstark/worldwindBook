package gov.nasa.worldwindx.examples.elevations.menu;

import gov.nasa.worldwind.View;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwindx.examples.util.ViewUtil;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author tony
 * @date 2019/5/28 21:29
 */
public class ToolMenuFactory {

    private static Logger logger = LoggerFactory.getLogger(ToolMenuFactory.class);

    private WorldWindow wwd;
    Menu toolMenu;

    public ToolMenuFactory(WorldWindow worldWindow) {
        this.wwd = worldWindow;

        toolMenu = new Menu("工具");
        toolMenu.getItems().addAll(
                createOutputBoundary()
        );

    }

    public Menu getToolMenu() {
        return toolMenu;
    }

    /**
     * 输出区域内的高程值
     *
     * @return
     */
    private MenuItem createOutputBoundary() {
        MenuItem outputElevation = new MenuItem("输出视窗范围");
        outputElevation.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                logger.debug("");
                View view = wwd.getView();
                ViewUtil.getViewSector(wwd);

            }
        });
        return outputElevation;
    }
}
