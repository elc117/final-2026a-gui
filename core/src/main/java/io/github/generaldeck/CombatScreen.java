package io.github.generaldeck;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
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

    // --- NOVAS VARIÁVEIS DE CONTROLO DE TEMPO ---
    private boolean isPaused = false;
    private boolean isFastForward = false;
    private boolean isGameOver = false;

    public CombatScreen(Main game, Skin skin, String[][] playerGrid, String[][] enemyGrid, int level) {
        super(game);
        this.skin = skin;
        this.currentLevel = level;
        this.viewport = new FitViewport(GameConfig.V_WIDTH, GameConfig.V_HEIGHT);
        this.batch = new SpriteBatch();
        this.stage = new Stage(this.viewport);

        this.battalionManager = new BattalionManager();
        battalionManager.spawnArmies(playerGrid, enemyGrid);

        buildUI();
    }

    private void buildUI() {
        // --- CAMADA INFERIOR (BOTÕES DE AÇÃO) ---
        Table overlayTable = new Table();
        overlayTable.setFillParent(true);
        overlayTable.bottom(); // Alinha ao fundo do ecrã

        // 1. Botão Desistir
        TextButton retreatButton = UIFactory.createButton("Desistir", skin, "button_regular", () -> {
            Gdx.app.log("CombatScreen", "Jogador desistiu");
            game.changeScreen(new MainMenuScreen(game, skin));
        });
        retreatButton.getLabel().setFontScale(0.7f);

        // 2. Botão Velocidade 2x
        TextButton[] speedBtn = new TextButton[1];
        speedBtn[0] = UIFactory.createButton("Vel: 1x", skin, "button_regular", () -> {
            if (!isGameOver) {
                isFastForward = !isFastForward;
                speedBtn[0].setText(isFastForward ? "Vel: 2x" : "Vel: 1x");
            }
        });
        speedBtn[0].getLabel().setFontScale(0.7f);

        // 3. Botão Pausa
        TextButton[] pauseBtn = new TextButton[1];
        pauseBtn[0] = UIFactory.createButton("Pausar", skin, "button_regular", () -> {
            if (!isGameOver) {
                isPaused = !isPaused;
                pauseBtn[0].setText(isPaused ? "Retomar" : "Pausar");
            }
        });
        pauseBtn[0].getLabel().setFontScale(0.7f);

        // Monta a barra inferior
        overlayTable.add(retreatButton).size(150, 55).padBottom(20).padLeft(20).left();
        overlayTable.add().expandX();
        overlayTable.add(speedBtn[0]).size(130, 55).padBottom(20).padRight(15).right();
        overlayTable.add(pauseBtn[0]).size(130, 55).padBottom(20).padRight(20).right();

        stage.addActor(overlayTable);

        // --- NOVA CAMADA SUPERIOR (HUD DO NÍVEL) ---
        Table topTable = new Table();
        topTable.setFillParent(true);
        topTable.top(); // Fixa esta tabela no topo da tela

        // Cria a caixinha de madeira
        Table levelBox = new Table();
        levelBox.setBackground(skin.get("button_regular", TextButton.TextButtonStyle.class).up);
        levelBox.pad(8f).padLeft(20f).padRight(20f); // Dá um respiro para o texto não ficar esmagado

        // Cria o texto pequeno e legível
        Label levelLabel = new Label("Nível " + currentLevel, skin, "small");

        levelBox.add(levelLabel);

        // Adiciona a caixinha ao centro do topo com um pequeno espaço da borda
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

        // --- A MÁGICA DO CONTROLO DE TEMPO ---
        // Se não estiver em pausa e o jogo não tiver acabado, a simulação avança!
        if (!isPaused && !isGameOver) {
            // Se o botão 2x estiver ativo, a matemática avança o dobro de tempo por frame
            float timeStep = isFastForward ? delta * 2.0f : delta;

            stateTime += timeStep;
            battalionManager.update(timeStep);
        }

        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();

        batch.draw(AnimationManager.battleBackground, 0, 0, GameConfig.V_WIDTH, GameConfig.V_HEIGHT);

        // Desenhamos as unidades com o stateTime. Como ele não aumenta no pause,
        // as animações congelam exatamente no frame em que estavam!
        battalionManager.render(batch, stateTime);

        batch.end();

        // --- CHECAGEM DE FIM DE JOGO ---
        if (!isGameOver && !isPaused) {
            if (battalionManager.isTeamDead(1)) {
                isGameOver = true;
                showEndGameUI(true);
            } else if (battalionManager.isTeamDead(0)) {
                isGameOver = true;
                showEndGameUI(false);
            }
        }

        // A interface UI atualiza independente do jogo estar em pausa,
        // permitindo que o jogador clique nos botões!
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
    }
}
