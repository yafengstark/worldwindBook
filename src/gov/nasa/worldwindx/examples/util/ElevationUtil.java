package gov.nasa.worldwindx.examples.util;

import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.Sector;
import gov.nasa.worldwind.globes.Globe;

import java.util.List;

/**
 * @author tony
 * @date 2019/5/22 23:05
 */
public class ElevationUtil {

    /**
     * Retrieve elevations for a specified list of locations. The elevations returned are the best currently
     * available for the dataset and the area bounding the locations. Since the necessary elevation data might not
     * be in memory at the time of the call, this method iterates until the necessary elevation data is in memory
     * and can be used to determine the locations' elevations.
     * <p/>
     * The locations must have a bounding sector, so more than one location is required. If the bounding region is
     * large, a huge amount of data must be retrieved from the server. Be aware that you are overriding the system's
     * resolution selection mechanism by requesting the highest resolution data, which could easily be gigabytes.
     * Requesting data for a large region will take a long time, will dump a lot of data into the local disk cache,
     * and may cause the server to throttle your access.
     *
     * @param locations a list of locations to determine elevations for
     *
     * @return the resolution actually achieved.
     */
    public static double[] getBestElevations(WorldWindow worldWindow, List<LatLon> locations)
    {
        Globe globe = worldWindow.getModel().getGlobe();
        Sector sector = Sector.boundingSector(locations);
        double[] elevations = new double[locations.size()];

        // Iterate until the best resolution is achieved. Use the elevation model to determine the best elevation.
        double targetResolution = globe.getElevationModel().getBestResolution(sector);
        double actualResolution = Double.MAX_VALUE;
        while (actualResolution > targetResolution)
        {
            actualResolution = globe.getElevations(sector, locations, targetResolution, elevations);
            // Uncomment the two lines below if you want to watch the resolution converge
//                System.out.printf("Target resolution = %s, Actual resolution = %s\n",
//                    Double.toString(targetResolution), Double.toString(actualResolution));
            try
            {
                Thread.sleep(200); // give the system a chance to retrieve data from the disk cache or the server
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }

        return elevations;
    }
}
