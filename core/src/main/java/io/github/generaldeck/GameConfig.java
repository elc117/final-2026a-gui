package io.github.generaldeck;

import com.badlogic.gdx.Game;

public class GameConfig {

    // resolução virtual base
    public static final float V_WIDTH = 1280f;
    public static final float V_HEIGHT = 720f;

    // padrões de UI
    public static final float BUTTON_WIDTH = 250f;
    public static final float BUTTON_HEIGHT = 50f;
    public static final float PAD_DEFAULT = 20f;
    public static final float PAD_SMALL = 10f;

    // tamanho do grid
    public static final int TILE_SIZE = 64;
    public static final int GRID_COLS = 10;
    public static final int GRID_ROWS = 10;
    public static final float UNIT_ICON_SIZE = 64f;
    public static final float DRAG_OFFSET_X = 32f;
    public static final float DRAG_OFFSET_Y = -32f;

    private GameConfig() {
        throw new UnsupportedOperationException("Esta classe não deve ser instaciada. " +
            "Serve apenas para conter as configurações do jogo");
    }
}
