package gov.nasa.worldwindx.examples.util;

import gov.nasa.worldwind.View;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.avlist.AVList;
import gov.nasa.worldwind.geom.*;
import gov.nasa.worldwind.globes.Globe;
import gov.nasa.worldwind.view.orbit.OrbitView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * @author tony
 * @date 2019/5/27 21:09
 */
public class ViewUtil {
    private static Logger logger = LoggerFactory.getLogger(ViewUtil.class);

    // 根据视场获取高度
    public static double getViewportScaleFactor(WorldWindow wwd)
    {
        return ((OrbitView) wwd.getView()).getZoom();
    }

    /**
     * TODO：窗口坐标转经纬度
     *
     */


    /**
     * 窗口范围内与地球的范围
     * TODO: 失败了
     *
     * @param wwd
     * @return
     */
    public static Sector getViewSector(WorldWindow wwd){

        Vec4[] lookAtPoints = computeViewLookAtForScene(wwd, wwd.getView());// 长度应该为3
        if (lookAtPoints == null || lookAtPoints.length != 3)
            return null;

        Position position0 = wwd.getModel().getGlobe().computePositionFromPoint(lookAtPoints[0]);
        Position centerPos = wwd.getModel().getGlobe().computePositionFromPoint(lookAtPoints[1]);
        Position position2 = wwd.getModel().getGlobe().computePositionFromPoint(lookAtPoints[2]);

        logger.debug("position0" + position0.toString());
        logger.debug("centerPos" + centerPos.toString());
        logger.debug("position2" + position2.toString());


        // 两个点之间的距离
        double zoom = lookAtPoints[0].distanceTo3(lookAtPoints[1]);
        logger.debug("zoom:" + zoom);

        Extent extent = wwd.getModel().getExtent();
        Position center = wwd.getModel().getGlobe().computePositionFromPoint(extent.getCenter());
        logger.debug("center:" + center.toString());
        logger.debug("半径是：" + extent.getRadius());
        logger.debug("直径是：" + extent.getDiameter());

//        wwd.getSceneController()

        return null;
    }

    private static Vec4[] computeViewLookAtForScene(WorldWindow wwd, View view)
    {
        Globe globe = wwd.getModel().getGlobe();
        double ve = wwd.getSceneController().getVerticalExaggeration();

        ExtentVisibilitySupport vs = new ExtentVisibilitySupport();
//        addExtents(vs);


        Iterable<? extends Extent> extentIterator = vs.getExtents();
//        while ( extentIterator.iterator().hasNext()){
//            Extent  extent = extentIterator.iterator().next();
////            logger.debug("" + extent..height);
////            logger.debug("" + extent.screenBounds.width);
////            logger.debug("" + extent.screenBounds.x);
////            logger.debug("" + extent.screenBounds.y);
//        }



        return vs.computeViewLookAtContainingExtents(globe, ve, view);
    }


    // 获取视角中心位置
    public static Position getViewCenterPosition(WorldWindow wwd)
    {
        Line ray = new Line(wwd.getView().getEyePoint(), wwd.getView().getForwardVector());
        Intersection[] intersection = wwd.getSceneController().getTerrain().intersect(ray);

        if (intersection != null && intersection.length != 0)
        {
            return wwd.getModel().getGlobe().computePositionFromPoint(intersection[0].getIntersectionPoint());
        }
        else if (wwd.getView() instanceof OrbitView)
        {
            // CenterPosition
            return ((OrbitView) wwd.getView()).getCenterPosition();
        }

        return Position.ZERO;
    }

}
