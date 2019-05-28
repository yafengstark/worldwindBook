package gov.nasa.worldwindx.examples.elevations;

import com.jogamp.opengl.util.awt.AWTGLReadBufferUtil;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.event.RenderingEvent;
import gov.nasa.worldwind.event.RenderingListener;
import gov.nasa.worldwind.geom.Sector;
import gov.nasa.worldwindx.examples.util.TifUtil;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.media.opengl.GLAutoDrawable;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author tony
 * @date 2019/5/26 22:04
 */
public class ScreenShotWithBoundaryEventHandler<ActionEvent> implements EventHandler, RenderingListener{

    private static Logger logger = LoggerFactory.getLogger(ScreenShotWithBoundaryEventHandler.class);

    WorldWindow worldWindow;
    private File snapFile;
    private FileChooser fileChooser;

    public ScreenShotWithBoundaryEventHandler(WorldWindow wwd){
        this.worldWindow = wwd;
        this.fileChooser = new FileChooser();
    }


    @Override
    public void handle(Event event) {
        this.snapFile = this.chooseFile();
    }

    private File chooseFile(){
        File outFile = null;
        try
        {

                configureFileChooser(fileChooser);
                Stage stage = new Stage();
                outFile = fileChooser.showSaveDialog(stage);
                if (outFile == null) // Shouldn't happen, but include a reaction just in case
                {
                    return null;
                }

                if (!outFile.getPath().endsWith(".png")) {
                    outFile = new File(outFile.getPath() + ".png");
                }
                if (outFile.exists())
                {

                }


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        this.worldWindow.removeRenderingListener(this); // ensure not to add a duplicate
        this.worldWindow.addRenderingListener(this); // 关键在这

        return outFile;
    }

    @Override
    public void stageChanged(RenderingEvent event) {
        if (event.getStage().equals(RenderingEvent.AFTER_BUFFER_SWAP) && this.snapFile != null)
        {
            try
            {
                GLAutoDrawable glad = (GLAutoDrawable) event.getSource();
                AWTGLReadBufferUtil glReadBufferUtil = new AWTGLReadBufferUtil(glad.getGLProfile(), false);
                BufferedImage bufferedImage = glReadBufferUtil.readPixelsToBufferedImage(glad.getGL(), true);
//                String suffix = WWIO.getSuffix(this.snapFile.getPath());
//                ImageIO.write(bufferedImage, suffix, this.snapFile);

                Sector sector = null;
                TifUtil.writeImageToFile(sector, bufferedImage,
                        this.snapFile);

                System.out.printf("Image saved to file %s\n", this.snapFile.getPath());
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            finally
            {
                this.snapFile = null;
                this.worldWindow.removeRenderingListener(this);
            }
        }
    }

    private static void configureFileChooser(
            final FileChooser fileChooser) {
        fileChooser.setTitle("View Pictures");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );


        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );
    }
}
