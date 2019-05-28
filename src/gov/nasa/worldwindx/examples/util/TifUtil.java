package gov.nasa.worldwindx.examples.util;

import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.avlist.AVList;
import gov.nasa.worldwind.avlist.AVListImpl;
import gov.nasa.worldwind.data.BufferedImageRaster;
import gov.nasa.worldwind.formats.tiff.GeotiffWriter;
import gov.nasa.worldwind.geom.Sector;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author tony
 * @date 2019/5/27 20:57
 */
public class TifUtil {

    public static void writeImageToFile(Sector sector, BufferedImage image, File gtFile)
            throws IOException {
        AVList params = new AVListImpl();

        params.setValue(AVKey.SECTOR, sector);
        params.setValue(AVKey.COORDINATE_SYSTEM, AVKey.COORDINATE_SYSTEM_GEOGRAPHIC);
        params.setValue(AVKey.PIXEL_FORMAT, AVKey.IMAGE);
        params.setValue(AVKey.BYTE_ORDER, AVKey.BIG_ENDIAN);

        GeotiffWriter writer = new GeotiffWriter(gtFile);
        try {
            writer.write(BufferedImageRaster.wrapAsGeoreferencedRaster(image, params));
        } finally {
            writer.close();
        }
    }
}
