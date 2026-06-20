package io.github.generaldeck;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class CombatScreen extends BaseScreen {

    private final FitViewport viewport;
    private final SpriteBatch batch;
    private final BattalionManager battalionManager;
    private final Stage stage;
    private final Skin skin;

    private float stateTime = 0f;
    private int currentLevel;

    private boolean isPaused = false;
    private boolean isFastForward = false;
    private boolean isGameOver = false;

    private Texture combatBackgroundTexture;

    public CombatScreen(Main game, Skin skin, String[][] playerGrid, String[][] enemyGrid, int level) {
        super(game);
        this.skin = skin;
        this.currentLevel = level;
        this.viewport = new FitViewport(GameConfig.V_WIDTH, GameConfig.V_HEIGHT);
        this.batch = new SpriteBatch();
        this.stage = new Stage(this.viewport);

        this.battalionManager = new BattalionManager();
        battalionManager.spawnArmies(playerGrid, enemyGrid);

        String combatBgFile;
        switch (currentLevel) {
            case 1:
                combatBgFile = "Background_Combate.png";
                break;
            case 2:
                combatBgFile = "Background_Combate2.png";
                break;
            case 3:
                combatBgFile = "Background_Combate3.png";
                break;
            default:
                combatBgFile = "Background_Combate.png";
        }
        this.combatBackgroundTexture = new Texture(Gdx.files.internal(combatBgFile));

        buildUI();
    }

    private void buildUI() {
        Table overlayTable = new Table();
        overlayTable.setFillParent(true);
        overlayTable.bottom();

        TextButton retreatButton = UIFactory.createButton("Desistir", skin, "button_regular", () -> {
            Gdx.app.log("CombatScreen", "Jogador desistiu");
            game.changeScreen(new MainMenuScreen(game, skin));
        });
        retreatButton.getLabel().setFontScale(0.7f);

        TextButton[] speedBtn = new TextButton[1];
        speedBtn[0] = UIFactory.createButton("Vel: 1x", skin, "button_regular", () -> {
            if (!isGameOver) {
                isFastForward = !isFastForward;
                speedBtn[0].setText(isFastForward ? "Vel: 2x" : "Vel: 1x");
            }
        });
        speedBtn[0].getLabel().setFontScale(0.7f);

        TextButton[] pauseBtn = new TextButton[1];
        pauseBtn[0] = UIFactory.createButton("Pausar", skin, "button_regular", () -> {
            if (!isGameOver) {
                isPaused = !isPaused;
                pauseBtn[0].setText(isPaused ? "Retomar" : "Pausar");
            }
        });
        pauseBtn[0].getLabel().setFontScale(0.7f);

        overlayTable.add(retreatButton).size(150, 55).padBottom(20).padLeft(20).left();
        overlayTable.add().expandX();
        overlayTable.add(speedBtn[0]).size(130, 55).padBottom(20).padRight(15).right();
        overlayTable.add(pauseBtn[0]).size(130, 55).padBottom(20).padRight(20).right();

        stage.addActor(overlayTable);

        Table topTable = new Table();
        topTable.setFillParent(true);
        topTable.top();

        Table levelBox = new Table();
        levelBox.setBackground(skin.get("button_regular", TextButton.TextButtonStyle.class).up);
        levelBox.pad(8f).padLeft(20f).padRight(20f);

        Label levelLabel = new Label("Nível " + currentLevel, skin, "small");

        levelBox.add(levelLabel);

        topTable.add(levelBox).padTop(20);

        stage.addActor(topTable);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        super.render(delta); // limpa o ecrã

        viewport.apply();

        if (!isPaused && !isGameOver) {
            float timeStep = isFastForward ? delta * 2.0f : delta;

            stateTime += timeStep;
            battalionManager.update(timeStep);
        }

        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();

        batch.draw(combatBackgroundTexture, 0, 0, GameConfig.V_WIDTH, GameConfig.V_HEIGHT);

        battalionManager.render(batch, stateTime);

        batch.end();

        if (!isGameOver && !isPaused) {
            if (battalionManager.isTeamDead(1)) {
                isGameOver = true;
                showEndGameUI(true);
            } else if (battalionManager.isTeamDead(0)) {
                isGameOver = true;
                showEndGameUI(false);
            }
        }

        stage.act(delta);
        stage.draw();
    }

    private void showEndGameUI(boolean isVictory) {
        Table overlayTable = new Table();
        overlayTable.setFillParent(true);

        String mensagem = isVictory ? "Nível Concluído!" : "Nível Perdido!";
        Label resultLabel = new Label(mensagem, skin, "text_blue_ribbon");
        resultLabel.setFontScale(1.4f);

        String textBotao;
        if (isVictory) {
            if (currentLevel < LevelManager.MAX_LEVEL) {
                textBotao = "Próximo Nível";
            } else {
                textBotao = "Finalizar Jogo";
            }
        } else {
            textBotao = "Tentar Novamente";
        }

        TextButton actionButton = UIFactory.createButton(textBotao, skin, "button_regular", () -> {
            if (isVictory) {
                if (currentLevel < LevelManager.MAX_LEVEL) {
                    game.changeScreen(new PreparationScreen(game, skin, new GridManager(5, 5), currentLevel + 1));
                } else {
                    game.changeScreen(new MainMenuScreen(game, skin));
                }
            } else {
                game.changeScreen(new PreparationScreen(game, skin, new GridManager(5, 5), currentLevel));
            }
        });

        actionButton.getLabel().setFontScale(0.85f);

        overlayTable.add(resultLabel).expand().top().padTop(120).row();
        overlayTable.add(actionButton).size(320, 90).expand().bottom().padBottom(100);

        stage.addActor(overlayTable);
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
        if (combatBackgroundTexture != null) combatBackgroundTexture.dispose();
    }
}
