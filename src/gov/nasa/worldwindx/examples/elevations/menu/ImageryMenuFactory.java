package gov.nasa.worldwindx.examples.elevations.menu;

import javafx.scene.control.Menu;

/**
 * @author tony
 * @date 2019/5/28 22:25
 */
public class ImageryMenuFactory {

    private Menu imageryMenu;

    public ImageryMenuFactory() {
        imageryMenu = new Menu("影像");
        imageryMenu.getItems().addAll(

        );


    }

    public Menu getImageryMenu() {
        return imageryMenu;
    }
}

