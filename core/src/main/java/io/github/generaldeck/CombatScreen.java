package io.github.generaldeck;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class CombatScreen extends BaseScreen {

    private final FitViewport    viewport;
    private final SpriteBatch    batch;
    private final BattalionManager battalionManager;
    private final Stage          stage;
    private final Skin           skin;

    private float stateTime = 0f;

    public CombatScreen(Main game, Skin skin, String[][] playerGrid, String[][] enemyGrid) {
        super(game);
        this.skin     = skin;
        this.viewport = new FitViewport(GameConfig.V_WIDTH, GameConfig.V_HEIGHT);
        this.batch    = new SpriteBatch();
        this.stage    = new Stage(this.viewport);

        this.battalionManager = new BattalionManager();
        battalionManager.spawnArmies(playerGrid, enemyGrid);

        buildUI();
    }

    private void buildUI() {

        Table root = new Table();
        root.setFillParent(true);
        root.bottom().left();
        // Botão "Desistir" no canto inferior esquerdo
        TextButton retreatButton = UIFactory.createButton("Desistir", skin, "button_regular", () -> {
            Gdx.app.log("CombatScreen", "Jogador desistiu");
            game.changeScreen(new MainMenuScreen(game, skin));
        });

        root.add(retreatButton).pad(GameConfig.PAD_DEFAULT);

        stage.addActor(root);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        super.render(delta); // limpa a tela

        viewport.apply();
        stateTime += delta;

        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();

        // 1. Desenha o background esticado para cobrir a tela inteira
        batch.draw(
            AnimationManager.battleBackground,
            0, 0,
            GameConfig.V_WIDTH,
            GameConfig.V_HEIGHT
        );

        // 2. Desenha as unidades por cima
        battalionManager.update(delta);
        battalionManager.render(batch, stateTime);

        batch.end();

        // 3. UI por cima de tudo
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        if (width <= 0 || height <= 0) return;
        viewport.update(width, height, true);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        batch.dispose();
        stage.dispose();
    }
}
