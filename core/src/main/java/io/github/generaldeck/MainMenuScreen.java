package io.github.generaldeck;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class MainMenuScreen extends BaseScreen {

    private final Stage stage;
    private final FitViewport viewport;
    private Texture backgroundTexture;

    public MainMenuScreen(Main game, Skin skin) {
        super(game);
        this.viewport = new FitViewport(GameConfig.V_WIDTH, GameConfig.V_HEIGHT);
        this.stage = new Stage(this.viewport);

        backgroundTexture = new Texture(Gdx.files.internal("game_background_4_corte_menu_principal.png"));
        Image backgroundImage = new Image(backgroundTexture);
        backgroundImage.setFillParent(true);
        this.stage.addActor(backgroundImage);

        buildUI(skin);
    }

    private void buildUI(Skin skin) {
        Table rootTable = new Table();
        rootTable.setFillParent(true);
        this.stage.addActor(rootTable);

        Label titleLabel = new Label("GENERAL DECK", skin, "text_blue_ribbon");
        titleLabel.setFontScale(1.8f);

        TextButton campaignButton = UIFactory.createButton("Campanha", skin, "button_regular", () -> {
            Gdx.app.log("MainMenu", "Transition to Level Selection");
            game.changeScreen(new SelectLevelScreen(game, skin));
        });

        TextButton multiplayerButton = UIFactory.createButton("Multiplayer", skin, "button_red", () -> {
            Gdx.app.log("MainMenu", "Transition to Multiplayer");
        });
        multiplayerButton.getLabel().setFontScale(0.7f);

        Label creditsLabel = new Label("Criado por Guilherme Martini e Guilherme Dapieve", skin, "small");
        creditsLabel.setFontScale(0.75f);
        creditsLabel.getColor().a = 1f;

        rootTable.add(titleLabel).padTop(100).padBottom(150).row();

        rootTable.add(campaignButton).size(280, 80).padBottom(20).row();
        rootTable.add(multiplayerButton).size(280, 80).row();

        rootTable.add(creditsLabel).expand().bottom().padBottom(15);
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
        if(width <= 0 || height <= 0) return;
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        if (stage != null) stage.dispose();
        if (backgroundTexture != null) backgroundTexture.dispose();
    }
}
