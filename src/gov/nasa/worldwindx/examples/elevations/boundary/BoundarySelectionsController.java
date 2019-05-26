package gov.nasa.worldwindx.examples.elevations.boundary;

import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.Sector;
import gov.nasa.worldwindx.examples.elevations.ElevationAnalyse;
import gov.nasa.worldwindx.examples.util.ElevationUtil;
import gov.nasa.worldwindx.examples.util.SectorSelector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;


/**
 * @author tony
 * @date 2019/5/21 22:44
 */
public class BoundarySelectionsController {


    private static Logger logger = LoggerFactory.getLogger(BoundarySelectionsController.class);

    @FXML
    TextField minLonTextField;

    @FXML
    TextField maxLonTextField;

    @FXML
    TextField minLatTextField;
    @FXML
    TextField maxLatTextField;

    SectorSelector selector;

    @FXML
    public void choose(ActionEvent event) {

        logger.debug("choose");


        selector.enable();

        Sector sector = selector.getSector();

        this.selector.addPropertyChangeListener(SectorSelector.SECTOR_PROPERTY, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                minLatTextField.textProperty().set(selector.getSector().getMinLatitude().degrees + "");
                maxLatTextField.textProperty().set(selector.getSector().getMaxLatitude().degrees + "");
                minLonTextField.textProperty().set(selector.getSector().getMinLongitude().degrees + "");
                maxLonTextField.textProperty().set(selector.getSector().getMaxLongitude().degrees + "");
            }
        });

//

//        controller.minLatTextField.textProperty().set(controller.selector.getSector().getMinLatitude().degrees+"");
//        controller.maxLatTextField.textProperty().set(controller.selector.getSector().getMaxLatitude().degrees+"");
//        controller.minLonTextField.textProperty().set(controller.selector.getSector().getMinLongitude().degrees+"");
//        controller.minLonTextField.textProperty().set(controller.selector.getSector().getMaxLongitude().degrees+"");


    }


    /**
     * 输出 五个点位最优高程值。
     * @param event
     */
    @FXML
    public void output(ActionEvent event) {
        logger.debug("output");

        System.out.println("choose");

        List<LatLon> latLonList = new ArrayList<>();

        latLonList.add(new LatLon(selector.getSector().getMinLatitude(), selector.getSector().getMinLongitude()));
        latLonList.add(new LatLon(selector.getSector().getMinLatitude(), selector.getSector().getMaxLongitude()));
        latLonList.add(new LatLon(selector.getSector().getMaxLatitude(), selector.getSector().getMinLongitude()));
        latLonList.add(new LatLon(selector.getSector().getMaxLatitude(), selector.getSector().getMaxLongitude()));


        double[] elevations = ElevationUtil.getBestElevations(ElevationAnalyse.wwd, latLonList);//
        for(int i=0; i< elevations.length; i++){
            System.out.println(latLonList.get(i));
            System.out.println(elevations[i]);
        }

    }

    @FXML
    public void cancelBoxSelect(ActionEvent event){
        System.out.println("取消");
        selector.disable();
    }

}
