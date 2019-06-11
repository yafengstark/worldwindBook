package gov.nasa.worldwind.util.statusbar;

import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.event.PositionEvent;
import gov.nasa.worldwind.event.PositionListener;
import gov.nasa.worldwind.event.RenderingEvent;
import gov.nasa.worldwind.event.RenderingListener;
import gov.nasa.worldwind.geom.Position;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;

/**
 * @author tony
 * @date 2019/5/24 20:57
 */
public class StatusBarView  implements PositionListener, RenderingListener {

    private static Logger logger = LoggerFactory.getLogger(StatusBarView.class);

    DecimalFormat df   = new DecimalFormat("######0.00");

    private Pane pane;
    private WorldWindow worldWindow;
    private StatusBarController statusBarController;

    public StatusBarView(WorldWindow worldWindow){
        this.worldWindow = worldWindow;

        URL location = getClass().getResource("StatusBar.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(location);
        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
        try {
            pane = fxmlLoader.load();
        } catch (IOException e) {

            e.printStackTrace();
        }
        statusBarController = fxmlLoader.getController();

        worldWindow.addPositionListener(this);
        worldWindow.addRenderingListener(this);

    }

    public Pane getPane() {
        return pane;
    }

    @Override
    public void moved(PositionEvent event) {

        handleCursorPositionChange(event);
    }


    protected void handleCursorPositionChange(PositionEvent event)
    {
        Position newPos = event.getPosition();
        if (newPos != null)
        {
            statusBarController.latText.setText(df.format(newPos.getLatitude().degrees)+"");
            statusBarController.lonText.setText(df.format(newPos.getLongitude().degrees)+"");
            statusBarController.eleText.setText(df.format(worldWindow.getModel().getGlobe().getElevation(
                    newPos.getLatitude(), newPos.getLongitude()))+"");
        }
        else
        {
            statusBarController.latText.setText("未选中");
            statusBarController.lonText.setText("未选中");
            statusBarController.eleText.setText("未选中");
        }
    }

    @Override
    public void stageChanged(RenderingEvent event)
    {
        if (!event.getStage().equals(RenderingEvent.BEFORE_BUFFER_SWAP))
            return;

        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                if (worldWindow.getView() != null && worldWindow.getView().getEyePosition() != null) {

                    statusBarController.altText.setText(df.format(
                            worldWindow.getView().getEyePosition().getElevation()) + "");
                }

            }
        });
    }


}
