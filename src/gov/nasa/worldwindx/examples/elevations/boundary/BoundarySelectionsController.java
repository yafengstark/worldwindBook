package gov.nasa.worldwindx.examples.elevations.boundary;

import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.avlist.AVList;
import gov.nasa.worldwind.avlist.AVListImpl;
import gov.nasa.worldwind.data.ByteBufferRaster;
import gov.nasa.worldwind.formats.tiff.GeotiffWriter;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.Sector;
import gov.nasa.worldwind.globes.ElevationModel;
import gov.nasa.worldwind.globes.Globe;
import gov.nasa.worldwind.layers.LayerList;
import gov.nasa.worldwind.layers.TiledImageLayer;
import gov.nasa.worldwind.render.DrawContext;
import gov.nasa.worldwindx.examples.elevations.ElevationImage;
import gov.nasa.worldwindx.examples.util.TifUtil;
import gov.nasa.worldwindx.examples.util.ElevationUtil;
import gov.nasa.worldwindx.examples.util.SectorSelector;
import gov.nasa.worldwindx.examples.util.jfx.AlertUtil;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.swing.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;


/**
 * @author tony
 * @date 2019/5/21 22:44
 */
public class BoundarySelectionsController {


    private static Logger logger = LoggerFactory.getLogger(BoundarySelectionsController.class);
    private static final double MISSING_DATA_SIGNAL = (double) Short.MIN_VALUE;

    WorldWindow worldWindow;
    Stage stage;

    @FXML
    TextField minLonTextField;

    @FXML
    TextField maxLonTextField;

    @FXML
    TextField minLatTextField;
    @FXML
    TextField maxLatTextField;

    @FXML
    Slider sizeSlider;
    @FXML
    Text desiredSize;

    @FXML
    Text widthText;
    @FXML
    Text heightText;

    @FXML
    Text resolutionText;


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
     *
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


        double[] elevations = ElevationUtil.getBestElevations(ElevationImage.wwd, latLonList);//
        for (int i = 0; i < elevations.length; i++) {
            System.out.println(latLonList.get(i));
            System.out.println(elevations[i]);
        }

    }

    @FXML
    public void cancelBoxSelect(ActionEvent event) {
        System.out.println("取消");
        selector.disable();
    }

    @FXML
    public void exportTif(ActionEvent event) {
        logger.debug("");

    }

    @FXML
    public void doSaveElevations(ActionEvent event) {

        final FileChooser fileChooser = new FileChooser();
        configureFileChooser(fileChooser);
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
//            openFile(file);
            logger.debug(file.getAbsolutePath());
        } else {
            logger.debug("选择为空");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("选择路径不合法");
            alert.setContentText("重新选择");

            alert.showAndWait();
            return;
        }


        Thread t = new Thread(new Runnable() {
            public void run() {
                Platform.runLater(() -> {
                    saveElevation(file);
                });


            }
        });

//        this.setCursor(new Cursor(Cursor.WAIT_CURSOR)); // 设置鼠标状态
        this.worldWindow.redraw();
        t.start();
    }

    private void saveElevation(File file) {
        try {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("正在导出");
            alert.setHeaderText(null);
            alert.setContentText("正在导出");
            alert.show();

            // 最大支持2048
            int width = 0;
            int height = 0;
            try {
                width = Integer.parseInt(widthText.getText());
                height = Integer.parseInt(heightText.getText());
            } catch (Exception e) {
                e.printStackTrace();
                AlertUtil.alertFail("长宽格式不对");
                return;
            }


            logger.debug("生成的图片大小:", width + ", " + height);

            alert.setContentText("获取高程值开始");
            double[] elevations = readElevations(selector.getSector(), width, height);
            alert.setContentText("获取高程值结束，开始生成图片");


            if (null != elevations) {
//                        jd.setTitle("Writing elevations to " + saveToFile.getName());
                writeElevationsToFile(selector.getSector(), width, height, elevations, file);

                alert.setContentText("存储文件成功\n" + file.getAbsolutePath());
//                            alert.close();
            } else {
                AlertUtil.alertFail("存储失败\n" + file.getAbsolutePath());
//                            alert.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
//                            worldWindow.(Cursor.getDefaultCursor());
                    worldWindow.redraw();
//                            jd.setVisible(false);
                }
            });
        }
    }

    @FXML
    public void doSaveImage(ActionEvent event) {
        final FileChooser fileChooser = new FileChooser();
        configureFileChooser(fileChooser);
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
//            openFile(file);
            logger.debug(file.getAbsolutePath());
        } else {
            logger.debug("选择为空");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("选择路径不合法");
            alert.setContentText("重新选择");

            alert.showAndWait();
            return;
        }

        TiledImageLayer currentLayer = null;
        LayerList list = worldWindow.getModel().getLayers();
        DrawContext dc = worldWindow.getSceneController().getDrawContext();

        ListIterator iterator = list.listIterator();
        while (iterator.hasNext()) {
            Object o = iterator.next();
            if (o instanceof TiledImageLayer) {
                TiledImageLayer layer = (TiledImageLayer) o;
                if (layer.isEnabled() && layer.isLayerActive(dc) && layer.isLayerInView(dc)) {
                    currentLayer = layer;
                }
            }
        }

        if (null == currentLayer) {
            return;
        }


        if (file == null)
            return;

        final TiledImageLayer activeLayer = currentLayer;


        Thread t = new Thread(new Runnable() {
            public void run() {

                Platform.runLater(() ->{
                    saveElevations(activeLayer, file);
                });

            }
        });


        worldWindow.redraw();
        t.start();
    }

    private void saveElevations(TiledImageLayer activeLayer, File file) {
        try {
            System.out.println("输出影像的图层名：" + activeLayer.getName());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("正在导出");
            alert.setHeaderText(null);
            alert.setContentText("正在导出的图层" + activeLayer.getName());
            alert.show();

            // 最大支持2048
            int width = 0;
            int height = 0;
            try {
                width = Integer.parseInt(widthText.getText());
                height = Integer.parseInt(heightText.getText());
            } catch (Exception e) {
                e.printStackTrace();
                AlertUtil.alertFail("长宽格式不对");
                return;
            }


            logger.debug("生成的图片大小:", width + ", " + height);


            BufferedImage bufferedImage = captureImage(activeLayer, selector.getSector(), width, height);//2048

            if (null != bufferedImage) {

                alert.setContentText("写文件到： " + file.getName());
                TifUtil.writeImageToFile(selector.getSector(), bufferedImage, file);
                alert.setContentText("生成成功： " + file.getName());
            } else {
                alert.setContentText("生成失败： " + file.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {

                    worldWindow.redraw();

                }
            });
        }
    }



    private BufferedImage captureImage(TiledImageLayer layer, Sector sector, int width, int height)
            throws Exception {

        String mimeType = layer.getDefaultImageFormat();
        if (layer.isImageFormatAvailable("image/png"))
            mimeType = "image/png";
        else if (layer.isImageFormatAvailable("image/jpg"))
            mimeType = "image/jpg";

        return layer.composeImageForSector(sector, width, height, 1d, -1, mimeType, true, null, 30000);
    }

    /**
     * @param sector
     * @param desiredSize 最长的那个边的值，通过其和比例计算短边
     * @return
     */
    public int[] adjustSize(Sector sector, int desiredSize) {
        int[] size = new int[]{desiredSize, desiredSize};

        if (null != sector && desiredSize > 0) {
            LatLon centroid = sector.getCentroid();
            // 高方向上对应的大圆弧度
            Angle dLat = LatLon.greatCircleDistance(new LatLon(sector.getMinLatitude(), sector.getMinLongitude()),
                    new LatLon(sector.getMaxLatitude(), sector.getMinLongitude()));
            // 宽方向上对应的大圆弧度
            Angle dLon = LatLon.greatCircleDistance(new LatLon(centroid.getLatitude(), sector.getMinLongitude()),
                    new LatLon(centroid.getLatitude(), sector.getMaxLongitude()));

            double max = Math.max(dLat.radians, dLon.radians);
            double min = Math.min(dLat.radians, dLon.radians);

            int minSize = (int) ((min == 0d) ? desiredSize : ((double) desiredSize * min / max));

            if (dLon.radians > dLat.radians) {
                size[0] = desiredSize;      // width
                size[1] = minSize;  // height
            } else {
                size[0] = minSize;  // width
                size[1] = desiredSize;      // height
            }
        }

        return size;
    }

    /**
     * 影像宽高
     *
     * @param sector
     * @param width  图像宽度
     * @param height
     * @return
     */
    private double[] readElevations(Sector sector, int width, int height) {
        double[] elevations;

        double latMin = sector.getMinLatitude().radians;
        double latMax = sector.getMaxLatitude().radians;
        double dLat = (latMax - latMin) / (double) (height - 1); // delta

        double lonMin = sector.getMinLongitude().radians;
        double lonMax = sector.getMaxLongitude().radians;
        double dLon = (lonMax - lonMin) / (double) (width - 1);

        ArrayList<LatLon> latlons = new ArrayList<LatLon>(width * height);

        int maxx = width - 1, maxy = height - 1;

        double lat = latMin;
        for (int y = 0; y < height; y++) {
            double lon = lonMin;

            for (int x = 0; x < width; x++) {
                latlons.add(LatLon.fromRadians(lat, lon));
                lon = (x == maxx) ? lonMax : (lon + dLon);
            }

            lat = (y == maxy) ? latMax : (lat + dLat);
        }

        try {
            Globe globe = worldWindow.getModel().getGlobe();
            ElevationModel model = globe.getElevationModel();

            elevations = new double[latlons.size()];
            Arrays.fill(elevations, MISSING_DATA_SIGNAL);

            // retrieve elevations
            // 核心功能
            model.composeElevations(sector, latlons, width, elevations);
        } catch (Exception e) {
            e.printStackTrace();
            elevations = null;
        }

        return elevations;
    }

    /**
     * @param sector
     * @param width
     * @param height
     * @param elevations
     * @param gtFile
     * @throws IOException
     */
    private void writeElevationsToFile(Sector sector, int width, int height, double[] elevations, File gtFile)
            throws IOException {
        // These parameters are required for writeElevation
        AVList elev32 = new AVListImpl();

        elev32.setValue(AVKey.SECTOR, sector);
        elev32.setValue(AVKey.WIDTH, width);
        elev32.setValue(AVKey.HEIGHT, height);
        elev32.setValue(AVKey.COORDINATE_SYSTEM, AVKey.COORDINATE_SYSTEM_GEOGRAPHIC);
        elev32.setValue(AVKey.PIXEL_FORMAT, AVKey.ELEVATION);
        elev32.setValue(AVKey.DATA_TYPE, AVKey.FLOAT32);
        elev32.setValue(AVKey.ELEVATION_UNIT, AVKey.UNIT_METER);
        elev32.setValue(AVKey.BYTE_ORDER, AVKey.BIG_ENDIAN);
        elev32.setValue(AVKey.MISSING_DATA_SIGNAL, MISSING_DATA_SIGNAL);

        ByteBufferRaster raster = (ByteBufferRaster) ByteBufferRaster.createGeoreferencedRaster(elev32);
        // copy elevation values to the elevation raster
        int i = 0;
        for (int y = height - 1; y >= 0; y--) {
            for (int x = 0; x < width; x++) {
                raster.setDoubleAtPosition(y, x, elevations[i++]);
            }
        }

        GeotiffWriter writer = new GeotiffWriter(gtFile);
        try {
            writer.write(raster);
        } finally {
            writer.close();
        }
    }

    private static void configureFileChooser(
            final FileChooser fileChooser) {
        fileChooser.setTitle("选择图片");
        fileChooser.setInitialDirectory(
                new File("C:")
        );

        fileChooser.getExtensionFilters().addAll(
//                new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter("tif", "*.tif")

        );
    }

}

