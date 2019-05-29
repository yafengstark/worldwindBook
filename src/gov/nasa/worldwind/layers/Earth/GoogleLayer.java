/*
 * Copyright (C) 2012 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */
package gov.nasa.worldwind.layers.Earth;

import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.avlist.AVList;
import gov.nasa.worldwind.avlist.AVListImpl;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.layers.mercator.BasicMercatorTiledImageLayer;
import gov.nasa.worldwind.layers.mercator.MercatorSector;
import gov.nasa.worldwind.util.LevelSet;
import gov.nasa.worldwind.util.Tile;
import gov.nasa.worldwind.util.TileUrlBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @version $Id: OSMMapnikLayer.java 1171 2013-02-11 21:45:02Z dcollins $
 */
public class GoogleLayer extends BasicMercatorTiledImageLayer
{
    private static Logger logger = LoggerFactory.getLogger(GoogleLayer.class);
    public GoogleLayer()
    {
        super(makeLevels());
    }

    private static LevelSet makeLevels()
    {
        AVList params = new AVListImpl();

        params.setValue(AVKey.TILE_WIDTH, 256);
        params.setValue(AVKey.TILE_HEIGHT, 256);
        params.setValue(AVKey.DATA_CACHE_NAME, "Earth/google/r");
        params.setValue(AVKey.SERVICE, "https://rt2.map.gtimg.com/tile?styleid=2&scene=0&version=345");
        params.setValue(AVKey.DATASET_NAME, "h");
        params.setValue(AVKey.FORMAT_SUFFIX, ".png");
        params.setValue(AVKey.NUM_LEVELS, 16);
        params.setValue(AVKey.NUM_EMPTY_LEVELS, 0);
        params.setValue(AVKey.LEVEL_ZERO_TILE_DELTA, new LatLon(Angle
            .fromDegrees(22.5d), Angle.fromDegrees(45d)));
        params.setValue(AVKey.SECTOR, new MercatorSector(-1.0, 1.0, Angle.NEG180, Angle.POS180));
        params.setValue(AVKey.TILE_URL_BUILDER, new URLBuilder());

        return new LevelSet(params);
    }

    private static class URLBuilder implements TileUrlBuilder
    {
        public URL getURL(Tile tile, String imageFormat)
            throws MalformedURLException
        {
//            &x=1&y=1&z=1
            // https://rt2.map.gtimg.com/tile?z=9&x=408&y=318&styleid=2&scene=0&version=345
            URL url = new URL(tile.getLevel().getService()
                    + "&z="+tile.getLevelNumber() + "&x=" + tile.getColumn()
                    +"&y="+ tile.getRow());
//            logger.debug(url.getPath());
            logger.debug(url.toString());
            return url;
        }
    }

    @Override
    public String toString()
    {
        return "google_r";
    }
}
