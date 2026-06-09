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

    public CombatScreen(Main game, String[][] gridState) {
        super(game);
        this.viewport = new FitViewport(GameConfig.V_WIDTH, GameConfig.V_HEIGHT);

        this.shapeRenderer = new ShapeRenderer();

        this.battalionManager = new BattalionManager();

        parseGridAndSpawnUnits(gridState);
    }

    private void parseGridAndSpawnUnits(String[][] gridState) {
        int cols = gridState.length;
        int rows = gridState[0].length;

        float startX = (GameConfig.V_WIDTH - (cols * GameConfig.TILE_SIZE)) / 2f;
        float startY = (GameConfig.V_HEIGHT - (rows * GameConfig.TILE_SIZE)) / 2f;

        for (int x = 0; x < cols; x++) {
            for (int y = 0; y < rows; y++) {
                String unitType = gridState[x][y];

                if (unitType != null) {
                    // Converte a Coordenada da Matriz [X, Y] para Pixels no Mundo
                    float worldX = startX + (x * GameConfig.TILE_SIZE) + (GameConfig.TILE_SIZE / 2f);
                    float worldY = startY + (y * GameConfig.TILE_SIZE) + (GameConfig.TILE_SIZE / 2f);

                    // SPAWNA AS UNIDADES
                    spawnSquad(unitType, worldX, worldY, 60);
                }
            }
        }
    }

    private void spawnSquad(String type, float centerX, float centerY, int count) {
        for (int i = 0; i < count; i++) {
            // Adiciona uma pequena variação (jitter) para as unidades não nascerem exatamente em cima da outra
            float jitterX = (float) (Math.random() * 100 - 20);
            float jitterY = (float) (Math.random() * 100 - 20);

            // O Pool entra em ação: Zero alocação de 'new Unit()' no Heap!
            battalionManager.spawnUnit(type, centerX + jitterX, centerY + jitterY);
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        viewport.apply(); // Aplica as transformações de câmera

        // 1. Atualiza a lógica de IA/Física de todas as tropas (sem 'new')
        battalionManager.update(delta);

        // 2. Renderiza as tropas otimizadamente
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        battalionManager.render(shapeRenderer); // Delegamos o desenho ao manager

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
