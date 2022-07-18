package com.javarush.games.game2048;

import com.javarush.engine.cell.*;

public class Game2048 extends Game {
    private static final int SIDE = 4;
    private int[][] gameField = new int[SIDE][SIDE];
    private boolean isGameStopped = false;
    private int score = 0;

    @Override
    public void initialize() {
        setScreenSize(SIDE, SIDE);
        createGame();
        drawScene();
    }

    @Override
    public void onKeyPress(Key key) {
        if (isGameStopped) {
            if (key == Key.SPACE) {
                isGameStopped = false;
                score = 0;
                setScore(score);
                createGame();
                drawScene();
            } else {
                return;
            }
        }

        if (!canUserMove()) {
            gameOver();
        } else {

            switch (key) {
                case LEFT:
                    moveLeft();
                    break;
                case RIGHT:
                    moveRight();
                    break;
                case UP:
                    moveUp();
                    break;
                case DOWN:
                    moveDown();
                    break;
                default:
                    return;
            }
            drawScene();
        }
    }
    
    private void createGame() {
        gameField = new int[SIDE][SIDE];
        createNewNumber();
        createNewNumber();
    }
    
    private boolean canUserMove() {
        boolean lose = false;
        for (int i = 0; i < SIDE; i++) {
            for (int j = 0; j < SIDE; j++) {
                if (gameField[i][j] == 0) {
                    lose = true;
                }else if (j < SIDE - 1 && gameField[i][j] == gameField[i][j + 1]) {
                        lose = true;
                } else if (i < SIDE - 1 && gameField[i][j] == gameField[i + 1][j]) {
                        lose = true;
                }
            }
        }
        return lose;
    }
    
    private void createNewNumber() {
        int x = getRandomNumber(SIDE);
        int y = getRandomNumber(SIDE);
        int a = getRandomNumber(10);

        if (getMaxTileValue() >= 2048) {
            win();
        }

        if (gameField[x][y] != 0) {
            createNewNumber();
        } else if (a == 9) {
            gameField[x][y] = 4;
        } else if (gameField[x][y] < 9) {
            gameField[x][y] = 2;
        }
    }
    
    private int getMaxTileValue() {
        int max = gameField[0][0];
        for (int i = 0; i < SIDE; i++) {
            for (int j = 0; j < SIDE; j++) {
                if (max < gameField[i][j])
                    max = gameField[i][j];
            }
        }
        return max;
    }
    
    private void gameOver() {
        isGameStopped = true;
        showMessageDialog(Color.BLACK, "Game Over...", Color.GREEN, 50);
    }
    
    private void win() {
        isGameStopped = true;
        showMessageDialog(Color.GOLD, "You WIN!!!", Color.DARKGREY, 50);
    }

    private void setCellColoredNumber(int x, int y, int value) {
        if (value != 0) {
            setCellValueEx(x, y, getColorByValue(value), "" + value);
        } else {
            setCellValueEx(x, y, getColorByValue(value), "");
        }
    }
    
    private Color getColorByValue(int value) {
        switch (value) {
            case (0):
                return Color.LIGHTGRAY;
            case (2):
                return Color.LIGHTBLUE;
            case (4):
                return Color.DARKSEAGREEN;
            case (8):
                return Color.ROSYBROWN;
            case (16):
                return Color.SLATEBLUE;
            case (32):
                return Color.SEAGREEN;
            case (64):
                return Color.GREY;
            case (128):
                return Color.DARKGOLDENROD;
            case (256):
                return Color.DARKBLUE;
            case (512):
                return Color.FIREBRICK;
            case (1024):
                return Color.SIENNA;
            case (2048):
                return Color.TEAL;
            default:
                return Color.BLACK;
        }
    }
    
    private void moveLeft() {
        boolean compressRow = false;
        boolean mergeRow = false;
        for (int i = 0; i < SIDE; i++) {
            if (compressRow(gameField[i])) {
                compressRow = true;
            }
            if (mergeRow(gameField[i])) {
                mergeRow = true;
            }
            if (compressRow(gameField[i])) {
                compressRow = true;
            }
        }
        if (compressRow || mergeRow) {
            createNewNumber();
        }
    }

    private void moveUp() {
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();
    }

    private void moveRight() {
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
    }

    private void moveDown() {
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
    }

    private boolean compressRow(int[] row) {
        int position = 0;
        boolean pow = false;
        for (int i = 0; i < SIDE; i++) {
            if (row[i] > 0) {
                if (i != position) {
                        row[position] = row[i];
                        row[i] = 0;
                        pow = true;
                    }
                position++;
            }
        }
        return pow;
    }

    private boolean mergeRow(int[] row) {
        boolean isRow = false;
        for (int i = 0; i < SIDE - 1; i++) {
            if (row[i] != 0 && row[i] == row[i + 1]) {
                    row[i] = row[i] + row[i + 1];
                    row[i + 1] = 0;
                    isRow = true;
                    score = score + row[i];
                    setScore(score);
            }
        }
        return isRow;
    }

    private void rotateClockwise() {
        int[][] rotated = new int[SIDE][SIDE];
        for (int i = 0; i < SIDE; i++) {
            for (int j = 0; j < SIDE; j++) {
                rotated[j][i] = gameField[SIDE - i - 1][j];
            }
        }
        gameField = rotated;
    }
    
    private void drawScene() {
        for (int x = 0; x < SIDE; x++) {
            for (int y = 0; y < SIDE; y++) {
                setCellColoredNumber(y, x, gameField[x][y]);
            }
        }
    }
}
