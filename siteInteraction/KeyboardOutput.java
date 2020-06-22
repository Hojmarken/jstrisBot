package siteInteraction;
import siteInteraction.VisualInput;
import builder.Tetromino;
import java.awt.event.KeyEvent;
import java.awt.Robot;
import java.awt.AWTException;

public class KeyboardOutput {
public static Robot reloadBot;

public static void press(int key) {
        VisualInput.robot.keyPress(key);
        VisualInput.robot.keyRelease(key);
}

public static void initialize() {
        try {
                reloadBot = new Robot();
                reloadBot.setAutoDelay(10);
        }
        catch (AWTException e) {
                e.printStackTrace();
        }
}

public static void jstrisPlacePiece(Tetromino tetromino, int x, int r, boolean hold) {
        tetromino.reset();
        if (hold) {
                press(67); // c
        }
        if (r == 1) {
                press(88); // x
        } else if (r == 2) {
                press(65); // a
        } else if (r == 3) {
                press(90); // z
        }
        int xDifference = x - tetromino.x;
        int directionKey = xDifference >= 0 ? 39 : 37; // --> || <--
        for (int i = 0; i < Math.abs(xDifference); i++) {
                press(directionKey);
        }
        press(32); // space
}

public static void reloadPage() {
        reloadBot.keyPress(KeyEvent.VK_META);
        reloadBot.keyRelease(KeyEvent.VK_META);
        reloadBot.keyPress(KeyEvent.VK_META);
        reloadBot.keyPress(KeyEvent.VK_R);
        reloadBot.keyRelease(KeyEvent.VK_R);
        reloadBot.keyRelease(KeyEvent.VK_META);
}

}
