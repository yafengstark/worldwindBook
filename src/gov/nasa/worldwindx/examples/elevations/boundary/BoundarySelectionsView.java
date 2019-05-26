package gov.nasa.worldwindx.examples.elevations.boundary;

import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.geom.Sector;
import gov.nasa.worldwind.render.SurfaceShape;
import gov.nasa.worldwindx.examples.elevations.ElevationImage;
import gov.nasa.worldwindx.examples.util.SectorSelector;
import gov.nasa.worldwindx.examples.util.SectorUtil;
import javafx.beans.value.ObservableValue;
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

    public BoundarySelectionsView(WorldWindow worldWindow){
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
        controller.worldWindow = worldWindow;
        controller.stage = stage;

        controller.selector =  new SectorSelector(ElevationImage.wwd);

        controller.selector.setInteriorColor(new Color(1f, 1f, 1f, 0.1f));
        controller.selector.setBorderColor(new Color(1f, 0f, 0f, 0.5f));
        controller.selector.setBorderWidth(3);

        controller.sizeSlider.valueProperty().addListener((
                ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) -> {

            controller.desiredSize.setText(String.format("%.2f", new_val));


            if(controller.selector.getSector() != null){
                // 求长宽
                int[] size = controller.adjustSize(controller.selector.getSector(),
                        new Double(controller.sizeSlider.getValue()).intValue()); // 调整这个desiredSize, 分辨率会提高
                int width = size[0], height = size[1];
                controller.widthText.setText(width+"");
                controller.heightText.setText(height+"");


                // 求分辨率
                Sector sector = controller.selector.getSector();
                SurfaceShape surfaceShape = SectorUtil.getBoundary(sector);

                double area = surfaceShape.getArea(worldWindow.getModel().getGlobe(), false);
                logger.debug("面积是：" +area);
                logger.debug("总像素数："+ width*height);

                controller.resolutionText.setText(area/(width*height)+"");


            }







        });



        Scene myScene = new Scene(myPane);
        stage.setScene(myScene);
        stage.show();
    }


}
