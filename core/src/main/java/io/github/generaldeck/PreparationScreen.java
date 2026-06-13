package io.github.generaldeck;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class PreparationScreen extends BaseScreen {
    private final Stage stage;
    // o Scene2D tem um sistema de Drag-and-Drop nativo
    private final DragAndDrop dragAndDrop;
    private final GridManager gridManager;
    private final Skin skin;
    private final String[][] enemyGrid;
    private final int currentLevel;

    public PreparationScreen(Main game, Skin skin, GridManager gridManager, int level) {
        super(game);

        this.stage = new Stage(new FitViewport(GameConfig.V_WIDTH, GameConfig.V_HEIGHT));
        this.dragAndDrop = new DragAndDrop();
        this.gridManager = gridManager;
        this.skin = skin;
        this.currentLevel = level;

        this.enemyGrid = LevelManager.getEnemyCampaignFormation(level, gridManager.getCols(), gridManager.getRows());

        buildUI();
    }

    private void buildUI() {
        Table rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.setDebug(true); // MODO DEBUG !!!!
        stage.addActor(rootTable);

        Table playerGridTable = createGridTable();
        Table enemyGridTable = createEnemyGridPreview();
        Table paletteTable = createPaletteTable();
        TextButton startButton = createStartButton();

        Table gridsContainer = new Table();
        gridsContainer.add(playerGridTable).padRight(50);
        gridsContainer.add(enemyGridTable).padRight(50);

        rootTable.add(new Label("Nível " + currentLevel, skin)).padBottom(20).row();
        rootTable.add(gridsContainer).expand().center().row();

        // não sei se faz muito sentido usar gameconfig.pad_default aqui ou só deixar um número mágico
        rootTable.add(paletteTable).padBottom(GameConfig.PAD_DEFAULT).bottom().row();
        rootTable.add(startButton).padTop(GameConfig.PAD_DEFAULT).padBottom(GameConfig.PAD_DEFAULT).row();
    }

    private Table createEnemyGridPreview() {
        Table table = new Table();
        int cols = enemyGrid.length;
        int rows = enemyGrid[0].length;

        for (int y = rows - 1; y >= 0; y--) {
            for (int x = 0; x < cols; x++) {
                Table cell = new Table();

                // LÓGICA DE ESPELHAMENTO VISUAL
                int mirroredX = (cols - 1) - x;
                String unitType = enemyGrid[mirroredX][y];

                if (unitType != null) {
                    Image enemyImg = new Image(skin.getDrawable("default-round"));
                    if ("WARRIOR".equals(unitType)) enemyImg.setColor(Color.RED);
                    else if ("ARCHER".equals(unitType)) enemyImg.setColor(Color.GREEN);
                    cell.add(enemyImg).expand().fill().pad(5);
                }

                table.add(cell).size(GameConfig.TILE_SIZE, GameConfig.TILE_SIZE);
            }
            table.row();
        }
        return table;
    }

    private Table createGridTable() {
        Table gridTable = new Table();
        gridTable.setDebug(true); // MODO DEBUG !!!!

        int cols = gridManager.getCols();
        int rows = gridManager.getRows();

        for (int y = rows - 1; y >= 0; y--) {
            for (int x = 0; x < cols; x++) {
                Table cell = new Table();
                cell.setDebug(true); // MODO DEBUG !!!!
                gridTable.add(cell).size(GameConfig.TILE_SIZE, GameConfig.TILE_SIZE);

                // Extraímos a lógica pesada do Drag & Drop para outro método!
                setupDragAndDropTarget(cell, x, y);
            }
            gridTable.row();
        }
        return gridTable;
    }

    private void setupDragAndDropTarget(Table cell, final int cellX, final int cellY) {
        dragAndDrop.addTarget(new DragAndDrop.Target(cell) {
            @Override
            public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                String[][] gridState = gridManager.getGridState();
                if (gridState[cellX][cellY] != null) {
                    Gdx.app.log("Erro", "Já existe uma tropa posicionada aqui");
                    return false;
                }
                return true;
            }

            @Override
            public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                String unitType = (String) payload.getObject();
                gridManager.placeBattalion(cellX, cellY, unitType);

                Table targetCell = (Table) getActor();
                targetCell.clearChildren();

                Image placedImage = new Image(skin.getDrawable("default-round"));
                if ("WARRIOR".equals(unitType)) placedImage.setColor(Color.RED);
                else if ("ARCHER".equals(unitType)) placedImage.setColor(Color.GREEN);

                targetCell.add(placedImage).expand().fill().pad(5);
            }
        });
    }

    private Table createPaletteTable() {
        Table paletteTable = new Table();
        paletteTable.add(new Label("Arraste as tropas para o tabuleiro:", skin))
            .colspan(2)
            .padBottom(GameConfig.PAD_SMALL)
            .row();

        paletteTable.add(createDraggableUnit("WARRIOR", Color.RED))
            .size(GameConfig.UNIT_ICON_SIZE)
            .padRight(GameConfig.PAD_DEFAULT);

        paletteTable.add(createDraggableUnit("ARCHER", Color.GREEN))
            .size(GameConfig.UNIT_ICON_SIZE);

        return paletteTable;
    }

    private TextButton createStartButton() {
        return UIFactory.createButton("Iniciar Combate", skin, () -> {
            Gdx.app.log("Preparation", "Iniciando combate! Extraindo dados do Grid...");
            String[][] playerFormation = gridManager.getGridState();
            game.changeScreen(new CombatScreen(game, playerFormation, enemyGrid));
        });
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
        stage.dispose();
    }
}

