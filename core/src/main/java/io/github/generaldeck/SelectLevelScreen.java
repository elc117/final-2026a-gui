package io.github.generaldeck;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class SelectLevelScreen extends BaseScreen {
    private final Stage stage;
    private final Skin skin;

    public SelectLevelScreen(Main game, Skin skin) {
        super(game);

        this.stage = new Stage(new FitViewport(GameConfig.V_WIDTH, GameConfig.V_HEIGHT));
        this.skin = skin;

        buildUI();
    }

    private void buildUI() {
        Table table = new Table();
        table.setFillParent(true);
        this.stage.addActor(table);

        table.defaults().padBottom(GameConfig.PAD_DEFAULT);

        TextButton level1Button = UIFactory.createButton("Level 1", skin, "default", () -> {
            Gdx.app.log("Level Select", "Transition to Level 1");
            GridManager playerGrid = new GridManager(GameConfig.GRID_COLS, GameConfig.GRID_ROWS);

            game.changeScreen(new PreparationScreen(game, skin, playerGrid, 1));
        });

        table.add(level1Button).row();
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
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        if (stage != null) {
            stage.dispose();
        }
    }
}
