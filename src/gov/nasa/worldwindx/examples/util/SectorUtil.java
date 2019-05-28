package gov.nasa.worldwindx.examples.util;

import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.Sector;
import gov.nasa.worldwind.render.SurfacePolygon;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tony
 * @date 2019/5/26 11:48
 */
public class SectorUtil {
    public static SurfacePolygon getBoundary(Sector sector){

        List<LatLon> latLonList = new ArrayList<>();

        latLonList.add(new LatLon(sector.getMinLatitude(), sector.getMinLongitude()));
        latLonList.add(new LatLon(sector.getMinLatitude(), sector.getMaxLongitude()));
        latLonList.add(new LatLon(sector.getMaxLatitude(), sector.getMinLongitude()));
        latLonList.add(new LatLon(sector.getMaxLatitude(), sector.getMaxLongitude()));
        return new SurfacePolygon(latLonList);
    }


}
