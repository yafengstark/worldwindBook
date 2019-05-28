package gov.nasa.worldwindx.examples.util.jfx;

import javafx.scene.control.Alert;

/**
 * @author tony
 * @date 2019/5/26 16:47
 */
public class AlertUtil {
    public  static void alertSuccess(String content){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("成功");
        alert.setHeaderText(null);
        alert.setContentText(content);

        alert.showAndWait();

    }

    public static void alertFail(String content){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("失败");
        alert.setHeaderText(null);
        alert.setContentText(content);

        alert.showAndWait();

    }

}
