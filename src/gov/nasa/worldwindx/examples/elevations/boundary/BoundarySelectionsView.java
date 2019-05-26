package gov.nasa.worldwindx.examples.elevations.boundary;

import gov.nasa.worldwind.geom.Sector;
import gov.nasa.worldwindx.examples.elevations.ElevationAnalyse;
import gov.nasa.worldwindx.examples.util.SectorSelector;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.IOException;
import java.net.URL;

/**
 * @author tony
 * @date 2019/5/21 22:45
 */
public class BoundarySelectionsView {

    private static Logger logger = LoggerFactory.getLogger(BoundarySelectionsView.class);

    public BoundarySelectionsView(){
        logger.debug("初始化");

        Stage stage = new Stage();
        stage.setTitle("地形分析");
        URL location = getClass().getResource("BoundarySelections.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(location);
        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
        Pane myPane = null;
        try {
            myPane = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        BoundarySelectionsController controller = fxmlLoader.getController();   //获取Controller的实例对象
        //Controller中写的初始化方法

        controller.selector =  new SectorSelector(ElevationAnalyse.wwd);

        controller.selector.setInteriorColor(new Color(1f, 1f, 1f, 0.1f));
        controller.selector.setBorderColor(new Color(1f, 0f, 0f, 0.5f));
        controller.selector.setBorderWidth(3);




        Scene myScene = new Scene(myPane);
        stage.setScene(myScene);
        stage.show();
    }


}
