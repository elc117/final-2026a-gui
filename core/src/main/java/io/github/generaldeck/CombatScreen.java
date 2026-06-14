package io.github.generaldeck;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class CombatScreen extends BaseScreen {
    private final FitViewport viewport;
    private final SpriteBatch batch;
    private final BattalionManager battalionManager;
    private float stateTime = 0f; // cronômetro para a animação

    public CombatScreen(Main game, String[][] playerGrid, String[][] enemyGrid) {
        super(game);
        this.viewport = new FitViewport(GameConfig.V_WIDTH, GameConfig.V_HEIGHT);

        this.batch = new SpriteBatch();

        this.battalionManager = new BattalionManager();

        battalionManager.spawnArmies(playerGrid, enemyGrid);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        viewport.apply(); // Aplica as transformações de câmera

        stateTime += delta; // Atualiza o tempo da animação
        // atualiza a lógica de IA/Física de todas as tropas
        battalionManager.update(delta);

        // configura o batch para desenhar na câmera correta
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();

        battalionManager.render(batch, stateTime);

        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        batch.dispose(); // Evita vazamento de memória
    }
}
