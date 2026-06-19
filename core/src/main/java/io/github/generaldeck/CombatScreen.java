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
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class CombatScreen extends BaseScreen {

    private final FitViewport    viewport;
    private final SpriteBatch    batch;
    private final BattalionManager battalionManager;
    private final Stage          stage;
    private final Skin           skin;

    private boolean isGameOver = false;

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

    private void showEndGameUI(boolean isVictory) {
        Table overlayTable = new Table();
        overlayTable.setFillParent(true);

        String mensagem = isVictory ? "Nível Concluído!" : "Nível Perdido!";
        Label resultLabel = new Label(mensagem, skin, "text_blue_ribbon");

        resultLabel.setFontScale(1.4f);

        String textBotao = isVictory ? "Próximo Nível" : "Tentar Novamente";

        TextButton actionButton = UIFactory.createButton(textBotao, skin, "button_regular", () -> {
            if (isVictory) {
                game.changeScreen(new PreparationScreen(game, skin, new GridManager(5, 5), 2));
            } else {
                game.changeScreen(new PreparationScreen(game, skin, new GridManager(5, 5), 1));
            }
        });

        actionButton.getLabel().setFontScale(0.85f);

        overlayTable.add(resultLabel).expand().top().padTop(120).row();

        overlayTable.add(actionButton).size(320, 90).expand().bottom().padBottom(100);

        stage.addActor(overlayTable);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        viewport.apply();
        stateTime += delta;

        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();

        batch.draw(AnimationManager.battleBackground, 0, 0, GameConfig.V_WIDTH, GameConfig.V_HEIGHT);

        battalionManager.update(delta);
        battalionManager.render(batch, stateTime);

        batch.end();

        // NOVA LÓGICA: Checa as condições de vitória ou derrota se o jogo ainda estiver rolando
        if (!isGameOver) {
            if (battalionManager.isTeamDead(1)) {
                // Time 1 (Inimigos) morreu. Vitória!
                isGameOver = true;
                showEndGameUI(true);
            } else if (battalionManager.isTeamDead(0)) {
                // Time 0 (Jogador) morreu. Derrota!
                isGameOver = true;
                showEndGameUI(false);
            }
        }

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        if (width <= 0 || height <= 0) return;
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        batch.dispose();
        stage.dispose();

        battalionManager.dispose();
    }
}
