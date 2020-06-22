package Main;
import java.util.*;
import builder.*;
import siteInteraction.*;
import java.awt.event.KeyEvent;

public class Main {
public static int keyPressSpeed = 35;
public static int[][] grid = new int[20][10];
public static ArrayList < Tetromino > queue = new ArrayList < Tetromino > ();
public static Tetromino hold = null;

// Best time in 40 sprint: 5.849s

public static void main(String[] args) {
        System.out.println("Booting");
        VisualInput.initialize();
        KeyboardOutput.initialize();
        wait(2000);
        KeyboardOutput.reloadPage();
        System.out.println("Reloaded page");
        while(!VisualInput.hasLoaded()) {
                wait(keyPressSpeed);
        }
        System.out.println("Loaded queue");
        queue = VisualInput.getQueue();
        wait(860);
        long startTime = System.nanoTime();
        System.out.println("GO");
        while (true) {
                BotFunctions.takeBestMove(grid, queue, hold);
                if (System.nanoTime()-startTime > 7.5e9) {
                        break;
                }
                wait(keyPressSpeed);
        }
}

public static void printRealQueue() {
        String output = "R: ";
        for (int i =0; i<5; i++) {
                output += VisualInput.getQueueElement(i).type + ", ";
        }
        System.out.println(output);
}

public static void printQueueHold() {
        String out = "Q: ";
        if (hold != null) {
                out += hold.type + "| ";
        }
        for (int i = 0; i < queue.size(); i++) {
                out += queue.get(i).type + ", ";
        }
        System.out.println(out);
}

public static void wait(int time) {
        try {
                Thread.sleep(time);
        } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
                System.out.println(exception);
        }
}


public static void print2dArray(int[][] array) {
        for (int i = 0; i < array.length; i++) {
                String line = "";
                for (int j = 0; j < array[i].length; j++) {
                        line += array[i][j];
                        line += " ";
                }
                System.out.println(line);
        }
        System.out.println("");
}

// public static void printArray(int[] array) {
//         for (int i : array) {
//                 System.out.println(i);
//         }
// }

}
