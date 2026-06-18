package io.github.generaldeck;

import com.badlogic.gdx.Gdx;

public class GridManager {
    // matriz para armazenar o ID do tipo de batalhão em cada célula
    private final String[][] gridState;
    private final int cols;
    private final int rows;

    public GridManager(int cols, int rows) {
        this.cols = cols;
        this.rows = rows;
        this.gridState = new String[cols][rows];
    }

    public void placeBattalion(int x, int y, String battalionType) {
        if (x >= 0 && x < cols && y >= 0 && y < rows) {
            gridState[x][y] = battalionType;
            Gdx.app.log("GridManager", "Batalhão " + battalionType + " em [" + x + "," + y + "]");
        }
    }

    public void removeBattalion(int x, int y) {
        String[][] gridState = getGridState();
        if (x >= 0 && x < getCols() && y >= 0 && y < getRows()) {
            gridState[x][y] = null;
        }
    }

    public String[][] getGridState() {
        return gridState;
    }

    public int getCols() { return cols; }
    public int getRows() { return rows; }


}
