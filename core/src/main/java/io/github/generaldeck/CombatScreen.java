package io.github.generaldeck;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class CombatScreen extends BaseScreen {
    private final FitViewport viewport;
    private final ShapeRenderer shapeRenderer;
    private final BattalionManager battalionManager;

    public CombatScreen(Main game, String[][] playerGrid, String[][] enemyGrid) {
        super(game);
        this.viewport = new FitViewport(GameConfig.V_WIDTH, GameConfig.V_HEIGHT);

        this.shapeRenderer = new ShapeRenderer();

        this.battalionManager = new BattalionManager();

        battalionManager.spawnArmies(playerGrid, enemyGrid);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        viewport.apply(); // Aplica as transformações de câmera

        // atualiza a lógica de IA/Física de todas as tropas
        battalionManager.update(delta);

        // renderiza as tropas
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        battalionManager.render(shapeRenderer); // delegamos o desenho ao manager

        shapeRenderer.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose(); // Previne vazamento de memória de vídeo
    }
}
