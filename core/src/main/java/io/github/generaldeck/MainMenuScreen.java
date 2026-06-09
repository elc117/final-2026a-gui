package io.github.generaldeck;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.compression.lzma.Base;
import com.badlogic.gdx.utils.viewport.FitViewport;

/** First screen of the application. Displayed after the application is created. */
public class MainMenuScreen extends BaseScreen {

    private final Main game;
    private final Stage stage;
    private final FitViewport viewport;

    // Skin é o repositório central de estilos e recursos da UI
    // vamos criar um arquivo depois que vai funcionar como se fosse um stylesheet CSS
    // para ditar o estilo dos botões do jogo
    public MainMenuScreen(Main game, Skin skin) {
        super(game);
        this.game = game;

        // resolução base interna do jogo, FitViewport adiciona barras pretas
        // caso a proporção do navegador seja diferente
        this.viewport = new FitViewport(GameConfig.V_WIDTH, GameConfig.V_HEIGHT);
        this.stage = new Stage(this.viewport);

        Table table = new Table();
        table.setFillParent(true);
        this.stage.addActor(table);

        // table é um gerenciador de layout dinamico, como se fosse uma tabela mesmo e ai só dizer em que
        // coluna e linha queremos o botão/elemento
        table.defaults()
            .width(GameConfig.BUTTON_WIDTH)
            .height(GameConfig.BUTTON_HEIGHT)
            .padBottom(GameConfig.PAD_DEFAULT);

        // BOTÃO "CAMPAIGN"
        TextButton campaignButton = UIFactory.createButton("Campaign", skin, () -> {
            Gdx.app.log("MainMenu", "Transition to Campaign");
            GridManager gridManager = new GridManager(GameConfig.GRID_COLS, GameConfig.GRID_ROWS);
            PreparationScreen prepScreen = new PreparationScreen(game, skin, gridManager);
            game.changeScreen(prepScreen);
        });
        table.add(campaignButton).row();

        // BOTÃO "MULTIPLAYER"
        TextButton multiplayerButton = UIFactory.createButton("Multiplayer", skin, () -> {
            Gdx.app.log("MainMenu", "Transition to Multiplayer");
        });
        table.add(multiplayerButton).padBottom(0).row();

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        // "delta" é o tempo desde o ultimo render
        // é usado para que a velocidade seja sempre a mesma independente do FPS
        super.render(delta);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // If the window is minimized on a desktop (LWJGL3) platform, width and height are 0, which causes problems.
        // In that case, we don't resize anything, and wait for the window to be a normal size before updating.
        if(width <= 0 || height <= 0) return;

        viewport.update(width, height, true);
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        // Destroy screen's assets here.
        stage.dispose();
    }
}
