package builder;
import builder.TetrominoMatrices;

public class Tetromino {
public int type;
public int[][] matrix;
public int x;
public int y;

public Tetromino(int _type) {
        type = _type;
        reset();
}
public void reset() {
        matrix = TetrominoMatrices.array[type-1];
        if (matrix.length == 4) {
                y = -1;
        } else {
                y = -matrix.length/2;
        }
        x = (int)(5 - matrix[0].length/(float)2);
}
public void rotate(boolean doClockwise) {
        if (doClockwise) {
                matrix = Tetromino.transpose(matrix);
                matrix = Tetromino.reverse(matrix);
        } else {
                matrix = Tetromino.reverse(matrix);
                matrix = Tetromino.transpose(matrix);
        }
}
public static int[][] transpose(int[][] matrix) {
        int[][] output = new int[matrix[0].length][matrix.length];
        for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[i].length; j++) {
                        output[j][i] = matrix[i][j];
                }
        }
        return output;
}
public static int[][] reverse(int[][] matrix) {
        int[][] output = new int[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[i].length; j++) {
                        output[i][j] = matrix[i][matrix[i].length-j-1];
                }
        }
        return output;
}
public Tetromino copy() {
        return new Tetromino(type);
}
}
