package builder;
import builder.Tetromino;
import java.util.Arrays;

public class GameFunctions {

public static boolean isColliding(Tetromino tetromino, int[][] grid) {
        for (int i = 0; i < tetromino.matrix.length; i++) {
                for (int j = 0; j < tetromino.matrix[0].length; j++) {
                        if (tetromino.matrix[i][j] != 0) {
                                int x = j + tetromino.x;
                                int y = i + tetromino.y;
                                if (x < 0 || x >= 10 || y >= 20 || (y >= 0 && grid[y][x] != 0)) {
                                        return true;
                                }
                        }
                }
        }
        return false;
}

public static int[][] getTetrominoToGrid(Tetromino tetromino, int[][] grid) {
        int[][] output = new int[grid.length][grid[0].length];
        for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[0].length; j++) {
                        output[i][j] = grid[i][j];
                }
        }
        for (int i = 0; i < tetromino.matrix.length; i++) {
                for (int j = 0; j < tetromino.matrix[0].length; j++) {
                        if (tetromino.matrix[i][j] != 0) {
                                int x = j + tetromino.x;
                                int y = i + tetromino.y;
                                output[y][x] = tetromino.matrix[i][j];
                        }
                }
        }
        return output;
}

public static int[][] getClearedRows(int[][] grid) {
        int[][] output = new int[grid.length][grid[0].length];
        int copyingIndex = grid.length - 1;
        for (int i = grid.length - 1; i >= 0; i--) {
                boolean rowIsFull = true;
                for (int j = 0; j < grid[0].length; j++) {
                        if (grid[i][j] == 0) {
                                rowIsFull = false;
                                break;
                        }
                }
                if (!rowIsFull) {
                        for (int j = 0; j < grid[0].length; j++) {
                                output[copyingIndex][j] = grid[i][j];
                        }
                        copyingIndex--;
                }
        }
        return output;
}

public static int getFallHeight(Tetromino tetromino, int[][] grid) {
        int[] bottomMap = new int[tetromino.matrix[0].length];
        Arrays.fill(bottomMap, -10);
        for (int i = tetromino.matrix.length - 1; i >= 0; i--) {
                for (int j = 0; j < tetromino.matrix[0].length; j++) {
                        if (bottomMap[j] == -10 && tetromino.matrix[i][j] != 0) {
                                bottomMap[j] = i + tetromino.y;
                        }
                }
        }
        int smallestDistance = Integer.MAX_VALUE;
        for (int j = 0; j < bottomMap.length; j++) {
                if (bottomMap[j] != -10) {
                        int coloumnDistance = 19 - getColoumnHeight(j + tetromino.x, grid) - bottomMap[j];
                        if (coloumnDistance < smallestDistance) {
                                smallestDistance = coloumnDistance;
                        }
                }
        }
        return smallestDistance;
}

public static int getColoumnHeight(int x, int[][] grid) {
        for (int y = 0; y < grid.length; y++) {
                if (grid[y][x] != 0) {
                        return 20 - y;
                }
        }
        return 0;
}

}
