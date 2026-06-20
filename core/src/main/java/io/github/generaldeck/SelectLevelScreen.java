package io.github.generaldeck;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class SelectLevelScreen extends BaseScreen {

    private final Stage stage;
    private final FitViewport viewport;
    private Texture backgroundTexture;

    public SelectLevelScreen(Main game, Skin skin) {
        super(game);
        this.viewport = new FitViewport(GameConfig.V_WIDTH, GameConfig.V_HEIGHT);
        this.stage = new Stage(this.viewport);

        backgroundTexture = new Texture(Gdx.files.internal("Background_Combate.png"));
        Image backgroundImage = new Image(backgroundTexture);
        backgroundImage.setFillParent(true);
        this.stage.addActor(backgroundImage);

        buildUI(skin);
    }

    private void buildUI(Skin skin) {
        Table table = new Table();
        table.setFillParent(true);
        table.center(); // Centraliza tudo na tela

        // Um loop analítico: cria botões dinamicamente do 1 até o MAX_LEVEL
        for (int i = 1; i <= LevelManager.MAX_LEVEL; i++) {
            final int level = i; // Variável final exigida para usar dentro do lambda do botão

            TextButton btn = UIFactory.createButton("Nível " + level, skin, "button_regular", () -> {
                game.changeScreen(new PreparationScreen(game, skin, new GridManager(5, 5), level));
            });

            // --- A LÓGICA DE BLOQUEIO ---
            if (level > LevelManager.highestLevelUnlocked) {
                // Desliga o clique do mouse (impede o som e a transição)
                btn.setTouchable(Touchable.disabled);
                // Reduz a opacidade da tinta do botão para 50%
                btn.getColor().a = 0.5f;
            }

            // Adiciona o botão na tabela com o tamanho exato da sua imagem de referência
            table.add(btn).size(280, 80).padBottom(20).row();
        }

        // --- BOTÃO DE VOLTAR ---
        // É essencial ter uma forma de retornar ao MainMenuScreen
        TextButton backBtn = UIFactory.createButton("Voltar", skin, "button_regular", () -> {
            game.changeScreen(new MainMenuScreen(game, skin));
        });
        backBtn.getLabel().setFontScale(0.7f); // Texto menor para não competir com os níveis

        // Dá um espaçamento extra (padTop) para separar dos níveis
        table.add(backBtn).size(150, 55).padTop(40).row();

        stage.addActor(table);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
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
        if (stage != null) stage.dispose();
        if (backgroundTexture != null) backgroundTexture.dispose();
    }
}
