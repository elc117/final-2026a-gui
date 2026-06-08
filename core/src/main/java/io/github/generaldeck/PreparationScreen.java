package io.github.generaldeck;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class PreparationScreen extends ScreenAdapter {
    private final Stage stage;
    // o Scene2D tem um sistema de Drag-and-Drop nativo
    private final DragAndDrop dragAndDrop;
    private final GridManager gridManager;
    private final Skin skin;

    public PreparationScreen(Game game, Skin skin, GridManager gridManager) {
        this.stage = new Stage(new FitViewport(GameConfig.V_WIDTH, GameConfig.V_HEIGHT));
        this.dragAndDrop = new DragAndDrop();
        this.gridManager = gridManager;
        this.skin = skin;

        Table rootTable = new Table();
        rootTable.setFillParent(true);
        stage.addActor(rootTable);

        Table gridTable = new Table();

        // MODO DEBUG !!!!
        gridTable.setDebug(true);
        rootTable.setDebug(true);

        int cols = gridManager.getCols();
        int rows = gridManager.getRows();

        for(int y = rows; y >= 0; y--) {
            for(int x = 0; x < cols; x++) {
                final int cellX = x;
                final int cellY = y;

                Table cell = new Table();
                cell.setDebug(true); // MODO DEBUG !!!!

                gridTable.add(cell).size(GameConfig.TILE_SIZE, GameConfig.TILE_SIZE);

                dragAndDrop.addTarget(new DragAndDrop.Target(cell) {
                    @Override
                    public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                        // verificação se o gridManager diz que a célula está ocupada vai aqui
                        return true;
                    }

                    @Override
                    public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                        String unitType = (String) payload.getObject();

                        gridManager.placeBattalion(cellX, cellY, unitType);

                        Table targetCell = (Table) getActor();
                        targetCell.clearChildren();

                        Image placedImage = new Image(skin.getDrawable("default-round"));

                        if("WARRIOR".equals(unitType)) placedImage.setColor(Color.RED);
                        else if ("ARCHER".equals(unitType)) placedImage.setColor(Color.GREEN);

                        targetCell.add(placedImage).expand().fill().pad(5);
                    }
                });
            }
            gridTable.row();
        }

        Table paletteTable = new Table();
        paletteTable.add(new Label("Arraste as tropas para o tabuleiro:", skin)).
            colspan(2)
            .padBottom(GameConfig.PAD_SMALL)
            .row();

        paletteTable.add(createDraggableUnit("WARRIOR", Color.RED))
            .size(GameConfig.UNIT_ICON_SIZE)
            .padRight(GameConfig.PAD_DEFAULT);
        paletteTable.add(createDraggableUnit("ARCHER", Color.GREEN))
            .size(GameConfig.UNIT_ICON_SIZE);

        rootTable.add(gridTable).expand().center().row();
        rootTable.add(paletteTable).padBottom(GameConfig.PAD_DEFAULT).bottom();

        TextButton startButton = new TextButton("Iniciar Combate", skin);

        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("Preparation", "Iniciando combate! Extraindo dados do Grid...");

                String[][] finalFormation = gridManager.getGridState();

                Main mainGame = (Main) game;

                mainGame.changeScreen(new CombatScreen(mainGame, finalFormation));
            }
        });

        rootTable.add(startButton).padTop(GameConfig.PAD_DEFAULT).padBottom(GameConfig.PAD_DEFAULT).row();
    }

    private Image createDraggableUnit(final String unitType, Color color) {
        Image icon = new Image(skin.getDrawable("default-round"));
        icon.setColor(color);

        dragAndDrop.addSource(new DragAndDrop.Source(icon) {
            @Override
            public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
                DragAndDrop.Payload payload = new DragAndDrop.Payload();
                payload.setObject(unitType);

                Image dragImage = new Image(skin.getDrawable("default-round"));
                dragImage.setColor(color);
                dragImage.setSize(GameConfig.UNIT_ICON_SIZE, GameConfig.UNIT_ICON_SIZE);
                payload.setDragActor(dragImage);

                dragAndDrop.setDragActorPosition(GameConfig.DRAG_OFFSET_X, GameConfig.DRAG_OFFSET_Y);

                return payload;
            }
        });
        return icon;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

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
        stage.dispose();
    }
}

