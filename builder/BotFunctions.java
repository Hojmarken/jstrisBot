package builder;
import builder.*;
import siteInteraction.*;
import Main.Main;
import java.lang.Math;
import java.util.*;
import java.lang.*;

public class BotFunctions {
public static boolean recoveryMode = false;
public static int bestMoveScore;
public static boolean mustHold;
public static int[][] moveGrid;
public static int moveX;
public static int moveRotation;

public static int getGridScore(int x1, int x2, int[][] grid) {
        int[] coloumnHeights = new int[x2 - x1];
        int lowest = Integer.MAX_VALUE;
        int highest = Integer.MIN_VALUE;
        int totalSquareScore = 0;
        int flatAreaScore = 0;
        for (int i = 0; i < coloumnHeights.length; i++) {
                coloumnHeights[i] = GameFunctions.getColoumnHeight(x1 + i, grid);
        }
        for (int i = 0; i < coloumnHeights.length; i++) {
                if (coloumnHeights[i] < lowest) {
                        lowest = coloumnHeights[i];
                }
                if (coloumnHeights[i] > highest) {
                        highest = coloumnHeights[i];
                }
                if (i < coloumnHeights.length - 1) {
                        int difference = coloumnHeights[i + 1] - coloumnHeights[i];
                        if (Math.abs(difference) > 1) {
                                totalSquareScore += difference * difference;
                        } else if (difference == 0) {
                                flatAreaScore = -5;
                        }
                }
        }
        return totalSquareScore + flatAreaScore + Math.abs(highest - lowest - 1);
}

public static boolean placementLeavesHoles(Tetromino tetromino, int[][] grid) {
        int startY = tetromino.y;
        tetromino.y += GameFunctions.getFallHeight(tetromino, grid);
        for (int i = 0; i < tetromino.matrix.length; i++) {
                for (int j = 0; j < tetromino.matrix[0].length; j++) {
                        if (tetromino.matrix[i][j] != 0 && tetromino.y + i < 19 && grid[tetromino.y + i + 1][tetromino.x + j] == 0 && (i == tetromino.matrix.length - 1 || tetromino.matrix[i + 1][j] == 0)) {
                                tetromino.y = startY;
                                return true;
                        }
                }
        }
        tetromino.y = startY;
        return false;
}

public static boolean isOutOfScreen(Tetromino tetromino) {
        for (int i = 0; i < tetromino.matrix.length; i++) {
                for (int j = 0; j < tetromino.matrix[0].length; j++) {
                        if (tetromino.matrix[i][j] != 0) {
                                if (tetromino.y + i < 0) {
                                        return true;
                                } else {
                                        return false;
                                }
                        }
                }
        }
        return false;
}

public static boolean canBeDropped(Tetromino tetromino, int[][] grid) {
        int startY = tetromino.y;
        tetromino.y += GameFunctions.getFallHeight(tetromino, grid);
        boolean output = !isOutOfScreen(tetromino);
        tetromino.y = startY;
        return output;
}

public static boolean canTetrisClear(int[][] grid) {
        for (int i = 16; i < 20; i++) {
                for (int j = 1; j < 10; j++) {
                        if (grid[i][j] == 0) {
                                return false;
                        }
                }
        }
        return true;
}

public static void tetrisClear(int[][] grid, ArrayList < Tetromino > queue, ArrayList < Tetromino > nextQueue) {
        KeyboardOutput.jstrisPlacePiece(queue.get(0), -2, 1, false);
        for (int i = 15; i >= 0; i--) {
                for (int j = 0; j < 10; j++) {
                        grid[i + 4][j] = grid[i][j];
                }
        }
        Main.queue = nextQueue;
}

public static void fillQueue(ArrayList < Tetromino > queue) {
        if (queue.size() == 5) {
                queue.add(VisualInput.getQueueElement(4));
        } else if (queue.size() == 4) {
                queue.add(VisualInput.getQueueElement(3));
                queue.add(VisualInput.getQueueElement(4));
        }
}

public static Tetromino[][] getQueueHoldCombinations(Tetromino[] queue, Tetromino hold) {
        // {{F, F}
        //  {F, T}
        //  {T, F}
        //  {T, T}}
        Tetromino[][] output = new Tetromino[4][2];
        output[0][0] = queue[0].copy();
        output[0][1] = queue[1].copy();
        if (hold == null) {
                output[1][0] = queue[0].copy();
                output[1][1] = queue[2].copy();
                output[2][0] = queue[1].copy();
                output[2][1] = queue[2].copy();
                output[3][0] = queue[1].copy();
                output[3][1] = queue[0].copy();
        } else {
                output[1][0] = queue[0].copy();
                output[1][1] = hold.copy();
                output[2][0] = hold.copy();
                output[2][1] = queue[1].copy();
                output[3][0] = hold.copy();
                output[3][1] = queue[0].copy();
        }
        return output;
}

public static void takeBestMove(int[][] grid, ArrayList < Tetromino > queue, Tetromino hold) {
        ArrayList < Tetromino > nextQueue = VisualInput.getQueue();
        if (!recoveryMode && queue.get(0).type == 1 && canTetrisClear(grid)) { //&&
                tetrisClear(grid, queue, nextQueue);
        } else {
                bestMoveScore = Integer.MAX_VALUE;
                Tetromino[] input;
                if (!recoveryMode && queue.get(1).type == 1 && canTetrisClear(grid)) {
                        input = new Tetromino[] {
                                queue.get(0), queue.get(2), queue.get(3)
                        };
                } else {
                        input = new Tetromino[] {
                                queue.get(0), queue.get(1), queue.get(2)
                        };
                }
                Tetromino[][] combinations = getQueueHoldCombinations(input, hold);
                for (int i = 0; i < combinations.length; i++) {
                        ArrayList < int[][] > grids = new ArrayList < int[][] > ();
                        grids.add(grid);
                        calculateBestMove(combinations[i], grids, 0, i >= 2, -100, -100);
                }
                if (bestMoveScore != Integer.MAX_VALUE) {
                        Main.grid = moveGrid;
                        if (mustHold) {
                                boolean removeFirst = hold == null;
                                if (removeFirst) {
                                        KeyboardOutput.jstrisPlacePiece(queue.get(1), moveX, moveRotation, true);
                                        nextQueue.remove(0);
                                } else {
                                        KeyboardOutput.jstrisPlacePiece(hold, moveX, moveRotation, true);
                                }
                                Main.hold = queue.get(0);
                        } else {
                                KeyboardOutput.jstrisPlacePiece(queue.get(0), moveX, moveRotation, false);
                        }
                        if (recoveryMode) {
                                handleRecoveryMode(grid);
                        }
                } else {
                        forceMove(queue, hold, grid);
                }
        }
        Main.queue = nextQueue;
}

public static void calculateBestMove(Tetromino[] tetrominos, ArrayList < int[][] > grids, int index, boolean _mustHold, int _moveX, int _moveRotation) {
        Tetromino tetromino = tetrominos[index];
        int[][] possiblePlacements = getPossiblePlacements(tetromino);
        for (int i = 0; i < possiblePlacements.length; i++) {
                int[] e = possiblePlacements[i];
                for (int x1 = e[0]; x1 <= e[1]; x1++) {
                        tetromino.x = x1;
                        if (canBeDropped(tetromino, grids.get(index)) && !placementLeavesHoles(tetromino, grids.get(index))) {
                                int startY = tetromino.y;
                                tetromino.y += GameFunctions.getFallHeight(tetromino, grids.get(index));
                                int[][] newGrid = GameFunctions.getTetrominoToGrid(tetromino, grids.get(index));
                                if (index == tetrominos.length - 1) {
                                        int score = getGridScore(1, 10, newGrid);
                                        if (score < bestMoveScore) {
                                                bestMoveScore = score;
                                                mustHold = _mustHold;
                                                moveGrid = grids.get(1);
                                                moveX = _moveX;
                                                moveRotation = _moveRotation;
                                        }
                                } else {
                                        ArrayList < int[][] > newGrids = (ArrayList) grids.clone();
                                        newGrids.add(newGrid);
                                        calculateBestMove(tetrominos, newGrids, index + 1, _mustHold, _moveX == -100 ? x1 : _moveX, _moveRotation == -100 ? i : _moveRotation);
                                }
                                tetromino.y = startY;
                        }
                }
                tetromino.rotate(true);
        }
        tetromino.reset();
}

public static void forceMove(ArrayList < Tetromino > queue, Tetromino hold, int[][] grid) {
        System.out.println("Forcemove");
        // Should check: if peace out of screen --> game over
        int forceMoveScore = Integer.MAX_VALUE;
        int[][] forceMoveGrid = null;
        boolean mustForceHold = false;
        Tetromino[] possiblePieces = new Tetromino[] {
                queue.get(0), hold
        };
        for (int i = 0; i < possiblePieces.length; i++) {
                Tetromino tetromino = possiblePieces[i];
                if (tetromino == null) {
                        continue;
                }
                for (int[] rotation: getPossiblePlacements(tetromino)) {
                        tetromino.x = rotation[0] - 1;
                        int startY = tetromino.y;
                        tetromino.y += GameFunctions.getFallHeight(tetromino, grid);
                        if (isValidForceMove(tetromino, grid)) {
                                int[][] newGrid = GameFunctions.getTetrominoToGrid(tetromino, grid);
                                int score = GameFunctions.getColoumnHeight(0, newGrid);
                                if (score < forceMoveScore) {
                                        forceMoveGrid = newGrid;
                                        mustForceHold = i == 1;
                                }
                        }
                        tetromino.y = startY;
                        tetromino.rotate(true);
                }
                tetromino.reset();
        }
        if (forceMoveGrid != null) {
                Main.grid = forceMoveGrid;
                if (mustForceHold) {
                        Main.hold = queue.get(0);
                        if (hold == null) {
                                queue.remove(0);
                        }
                }
                queue.remove(0);
                fillQueue(queue);
                recoveryMode = true;
                handleRecoveryMode(grid);
        } else {
                System.out.println("deep trouble");
        }
}

public static boolean isValidForceMove(Tetromino tetromino, int[][] grid) {
        int startY = tetromino.y;
        tetromino.y += GameFunctions.getFallHeight(tetromino, grid);
        for (int i = 0; i < tetromino.matrix.length; i++) {
                for (int j = 0; j < tetromino.matrix[i].length; j++) {
                        if (tetromino.x + j == 0) {
                                continue;
                        }
                        if (tetromino.matrix[i][j] != 0 && tetromino.y + i + 1 <= 19 && grid[tetromino.y + i + 1][tetromino.x + j] == 0 && (i == tetromino.matrix.length - 1 || tetromino.matrix[i + 1][j] == 0)) {
                                tetromino.y = startY;
                                return false;
                        }
                }
        }
        tetromino.y = startY;
        return true;
}

public static void handleRecoveryMode(int[][] grid) {
        Main.grid = GameFunctions.getClearedRows(grid);
        if (GameFunctions.getColoumnHeight(0, grid) == 0) {
                recoveryMode = false;
        }
}

public static int[][] getPossiblePlacements(Tetromino tetromino) {
        int[][] output = null;
        switch (tetromino.type) {
        case 1:
                output = new int[][] {
                        {
                                1,
                                6
                        }, {
                                -1,
                                7
                        }
                };
                break;
        case 2:
        case 3:
                output = new int[][] {
                        {
                                1,
                                7
                        }, {
                                0,
                                7
                        }, {
                                1,
                                7
                        }, {
                                1,
                                8
                        }
                };
                break;
        case 4:
        case 5:
                output = new int[][] {
                        {
                                1,
                                7
                        }, {
                                0,
                                7
                        }
                };
                break;
        case 6:
                output = new int[][] {
                        {
                                1,
                                8
                        }
                };
                break;
        case 7:
                output = new int[][] {
                        {
                                1,
                                7
                        }, {
                                0,
                                7
                        }, {
                                1,
                                7
                        }, {
                                1,
                                8
                        }
                };
                break;
        }
        return output;
}

}
