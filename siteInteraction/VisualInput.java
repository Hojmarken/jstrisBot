package siteInteraction;
import java.awt.Color;
import java.awt.Robot;
import java.awt.AWTException;
import java.util.*;
import builder.Tetromino;

public class VisualInput {
public static Robot robot;
public static HashMap<Integer, Integer> rgbToType = new HashMap<Integer, Integer>();
public static int queueX = 489;

public static void initialize() {
        createRobot();
        fillRgbToType();
}

public static void fillRgbToType() {
        rgbToType.put(0, 1);
        rgbToType.put(40, 2);
        rgbToType.put(69, 3);
        rgbToType.put(166, 4);
        rgbToType.put(36, 5);
        rgbToType.put(143, 6);
        rgbToType.put(35, 7);
}


public static Tetromino getQueueElement(int index) {
        return new Tetromino(rgbToType.get(
                                     robot.getPixelColor(queueX, 275+index*72).getGreen()
                                     ));
}

public static boolean hasLoaded() {
        return 192 == robot.getPixelColor(queueX-127, 505).getRed();
}

public static ArrayList < Tetromino > getQueue() {
        ArrayList < Tetromino > output = new ArrayList < Tetromino >();
        for (int i =0; i<5; i++) {
                output.add(VisualInput.getQueueElement(i));
        }
        return output;
}

public static void createRobot() {
        try {
                robot = new Robot();
        }
        catch (AWTException e) {
                e.printStackTrace();
        }
}
}
