package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(ApplicationExtension.class)
class MainTest {
    @Start
    public void start (Stage stage) throws Exception {
        Parent mainNode = FXMLLoader.load(Main.class.getResource("sample.fxml"));
        stage.setScene(new Scene(mainNode));
        stage.show();
        stage.toFront();
    }

    @Test
    public void provjeraIme (FxRobot robot) {
        TextField ime = robot.lookup("#imeField").queryAs(TextField.class);
        robot.clickOn("#imeField");
        assertAll(
                () -> {
                    robot.write("Elma24");
                    assertTrue(ime.getStyleClass().contains("poljeNijeIspravno"));
                },
                () -> {
                    robot.type(KeyCode.BACK_SPACE);
                    robot.type(KeyCode.BACK_SPACE);
                    assertTrue(ime.getStyleClass().contains("poljeIspravno"));
                }
        );
    }
}
