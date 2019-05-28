package gov.nasa.worldwindx.examples.elevations.menu;

import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwindx.examples.elevations.ScreenShotEventHandler;
import gov.nasa.worldwindx.examples.elevations.ScreenShotWithBoundaryEventHandler;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author tony
 * @date 2019/5/28 21:33
 */
public class SystemMenuFactory {

    private static Logger logger = LoggerFactory.getLogger(SystemMenuFactory.class);

    private WorldWindow wwd;

    Menu systemMenu;

    public  SystemMenuFactory(WorldWindow worldWindow){
        this.wwd = worldWindow;

         systemMenu = new Menu("系统");
        systemMenu.getItems().addAll(
                createLayerManage(),
                createScreenShot(),
                createScreenShotWithBoundary()
        );


    }

    public Menu getSystemMenu() {
        return systemMenu;
    }

    /**
     *
     * @return
     */
    private  MenuItem createLayerManage() {
        MenuItem outputElevation = new MenuItem("图层管理");
        outputElevation.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                logger.debug("");
//                new BoundarySelectionsView();

            }
        });
        return outputElevation;
    }

    /**
     * javafx
     * @return
     */
    private  MenuItem createScreenShot() {
        MenuItem outputElevation = new MenuItem("截屏");
        outputElevation.setOnAction(new ScreenShotEventHandler(wwd));
        return outputElevation;
    }

    /**
     * javafx
     * @return
     */
    private  MenuItem createScreenShotWithBoundary() {
        MenuItem outputElevation = new MenuItem("带经纬度截屏");
        outputElevation.setOnAction(new ScreenShotWithBoundaryEventHandler(wwd));
        return outputElevation;
    }
}
