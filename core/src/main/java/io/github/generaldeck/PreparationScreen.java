package io.github.generaldeck;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import static com.badlogic.gdx.scenes.scene2d.Touchable.enabled;

public class PreparationScreen extends BaseScreen {
    private final Stage stage;
    // o Scene2D tem um sistema de Drag-and-Drop nativo
    private final DragAndDrop dragAndDrop;
    private final GridManager gridManager;
    private final Skin skin;
    private final String[][] enemyGrid;
    private final int currentLevel;

    private Texture backgroundTexture;

    public PreparationScreen(Main game, Skin skin, GridManager gridManager, int level) {
        super(game);

        this.stage = new Stage(new FitViewport(GameConfig.V_WIDTH, GameConfig.V_HEIGHT));
        this.dragAndDrop = new DragAndDrop();

        this.dragAndDrop.setKeepWithinStage(false); // personagem pode vazar para fora da tela

        this.gridManager = gridManager;
        this.skin = skin;
        this.currentLevel = level;

        this.enemyGrid = LevelManager.getEnemyCampaignFormation(level, gridManager.getCols(), gridManager.getRows());

        buildUI();
    }

    private void buildUI() {
        backgroundTexture = new Texture(Gdx.files.internal("background_preparation.png")); // POR O NEGOCIO AQUI!!!!
        Image bgImage = new Image(backgroundTexture);
        bgImage.setFillParent(true);
        stage.addActor(bgImage);

        Table rootTable = new Table();
        rootTable.setFillParent(true);
        //rootTable.setDebug(true); // MODO DEBUG !!!!
        stage.addActor(rootTable);

        Table playerGridTable = createGridTable();
        Table enemyGridTable = createEnemyGridPreview();
        Table paletteTable = createPaletteTable();
        TextButton startButton = createStartButton();

        Table gridsContainer = new Table();
        gridsContainer.add(playerGridTable).padRight(50);
        gridsContainer.add(enemyGridTable).padRight(50);

        Label titleLabel = new Label("Nível " + currentLevel, skin, "text_blue_ribbon");
        rootTable.add(titleLabel).padBottom(0).row();

        rootTable.add(gridsContainer).expand().center().row();
        rootTable.add(paletteTable).padBottom(GameConfig.PAD_DEFAULT).bottom().row();
        rootTable.add(startButton).padTop(GameConfig.PAD_DEFAULT).padBottom(GameConfig.PAD_DEFAULT).row();
    }

    private Table createEnemyGridPreview() {
        Table table = new Table();
        int cols = enemyGrid.length;
        int rows = enemyGrid[0].length;

        // da pra adicionar isso no skin mais pra frente
        Texture textureTeste = new Texture(Gdx.files.internal("celula_teste_red.png"));
        TextureRegionDrawable cellTeste = new TextureRegionDrawable(new TextureRegion(textureTeste));

        for (int y = rows - 1; y >= 0; y--) {
            for (int x = 0; x < cols; x++) {
                Table cell = new Table();

                cell.setBackground(cellTeste);

                // LÓGICA DE ESPELHAMENTO VISUAL
                int mirroredX = (cols - 1) - x;
                String unitType = enemyGrid[mirroredX][y];

                if (unitType != null) {
                    Image enemyImg = createUnitImage(unitType, GameConfig.TILE_SIZE);
                    cell.add(enemyImg).size(GameConfig.TILE_SIZE, GameConfig.TILE_SIZE);
                }

                table.add(cell).size(GameConfig.TILE_SIZE, GameConfig.TILE_SIZE);
            }
            table.row();
        }
        return table;
    }


    private Table createGridTable() {
        Table gridTable = new Table();
        //gridTable.setDebug(true); // MODO DEBUG !!!!

        int cols = gridManager.getCols();
        int rows = gridManager.getRows();

        // da pra adicionar isso no skin mais pra frente
        Texture textureTeste = new Texture(Gdx.files.internal("celula_teste.png"));
        TextureRegionDrawable cellTeste = new TextureRegionDrawable(new TextureRegion(textureTeste));

        for (int y = rows - 1; y >= 0; y--) {
            for (int x = 0; x < cols; x++) {
                Table cell = new Table();
                cell.setBackground(cellTeste); // LEMBRAR DE FAZER ISSSOO
                cell.setTouchable(enabled);
                //cell.setDebug(true); // MODO DEBUG !!!!
                gridTable.add(cell).size(GameConfig.TILE_SIZE, GameConfig.TILE_SIZE).fill();

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
                return gridState[cellX][cellY] == null;
            }

            @Override
            public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                String unitType = (String) payload.getObject();
                gridManager.placeBattalion(cellX, cellY, unitType);

                Table targetCell = (Table) getActor();
                targetCell.clearChildren();

                Image placedImage = createUnitImage(unitType, GameConfig.TILE_SIZE);
                placedImage.setTouchable(Touchable.disabled);

                targetCell.add(placedImage).size(GameConfig.TILE_SIZE, GameConfig.TILE_SIZE);
            }
        });
    }

    private Table createPaletteTable() {
        Table paletteTable = new Table();
        Label instLabel = new Label("Arraste as tropas para o tabuleiro:", skin, "small");
        paletteTable.add(instLabel).colspan(2).row();

        paletteTable.add(createDraggableUnit("WARRIOR")).size(GameConfig.UNIT_ICON_SIZE).padRight(GameConfig.PAD_DEFAULT);
        paletteTable.add(createDraggableUnit("ARCHER")).size(GameConfig.UNIT_ICON_SIZE);
        return paletteTable;
    }

    private TextButton createStartButton() {
        return UIFactory.createButton("Iniciar Combate", skin, "button_regular", () -> {
            String[][] playerFormation = gridManager.getGridState();
            game.changeScreen(new CombatScreen(game, skin, playerFormation, enemyGrid));
        });
    }

    private Table createDraggableUnit(final String unitType) {
        Table container = new Table();

        Image icon = createUnitImage(unitType, GameConfig.UNIT_ICON_SIZE);
        String nomePersonagem = "WARRIOR".equals(unitType) ? "Soldado" : "Arqueiro";

        dragAndDrop.addSource(new DragAndDrop.Source(icon) {
            @Override
            public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
                DragAndDrop.Payload payload = new DragAndDrop.Payload();
                payload.setObject(unitType);

                Image dragImage = createUnitImage(unitType, GameConfig.UNIT_ICON_SIZE);
                payload.setDragActor(dragImage);

                dragAndDrop.setDragActorPosition(GameConfig.UNIT_ICON_SIZE / 2f, -(GameConfig.UNIT_ICON_SIZE / 2f));
                return payload;
            }
        });

        Label label = new Label(nomePersonagem, skin, "small");
        label.setAlignment(Align.center);

        container.add(icon).size(GameConfig.UNIT_ICON_SIZE).padBottom(-20f).row();
        container.add(label).expandX().fillX();

        return container;
    }

    private Image createUnitImage(String unitType, float size) {
        Drawable region = "WARRIOR".equals(unitType) ? AnimationManager.warriorIcon : AnimationManager.archerIcon;
        Image img = new Image(region);
        img.setScaling(Scaling.stretch);
        img.setSize(size, size);
        img.setOrigin(size / 2f, size / 2f);
        return img;
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
    public void dispose() {
        stage.dispose();
    }
}

