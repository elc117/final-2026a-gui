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

        this.dragAndDrop.setKeepWithinStage(false); // personagem pode vazar para fora da tela

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
                    Image enemyImg;

                    // 1. Escolhemos a arte correta no Gestor de Animações
                    if ("WARRIOR".equals(unitType)) {
                        enemyImg = new Image(AnimationManager.warriorIcon);
                    } else {
                        enemyImg = new Image(AnimationManager.archerIcon);
                    }

                    // 2. Aplicamos as regras de escala para o desenho crescer livremente
                    enemyImg.setScaling(com.badlogic.gdx.utils.Scaling.stretch);
                    enemyImg.setSize(GameConfig.TILE_SIZE, GameConfig.TILE_SIZE);
                    enemyImg.setOrigin(GameConfig.TILE_SIZE / 2f, GameConfig.TILE_SIZE / 2f);

                    // 3. Removemos o preenchimento e travamos o tamanho interno
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

                Image placedImage;
                if ("WARRIOR".equals(unitType)) {
                    placedImage = new Image(AnimationManager.warriorIcon);
                } else {
                    placedImage = new Image(AnimationManager.archerIcon);
                }

                placedImage.setScaling(com.badlogic.gdx.utils.Scaling.stretch);
                placedImage.setSize(GameConfig.TILE_SIZE, GameConfig.TILE_SIZE);
                placedImage.setOrigin(GameConfig.TILE_SIZE / 2f, GameConfig.TILE_SIZE / 2f);
                placedImage.setTouchable(com.badlogic.gdx.scenes.scene2d.Touchable.disabled);

                targetCell.add(placedImage).size(GameConfig.TILE_SIZE, GameConfig.TILE_SIZE);
            }
        });
    }

    private Table createPaletteTable() {
        Table paletteTable = new Table();
        paletteTable.add(new Label("Arraste as tropas para o tabuleiro:", skin))
            .colspan(2)
            .padBottom(GameConfig.PAD_SMALL)
            .row();

        paletteTable.add(createDraggableUnit("WARRIOR"))
            .size(GameConfig.UNIT_ICON_SIZE)
            .padRight(GameConfig.PAD_DEFAULT);

        paletteTable.add(createDraggableUnit("ARCHER"))
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

    private com.badlogic.gdx.scenes.scene2d.ui.Table createDraggableUnit(final String unitType) {

        com.badlogic.gdx.scenes.scene2d.ui.Table container = new com.badlogic.gdx.scenes.scene2d.ui.Table();

        Image icon;
        String nomePersonagem;

        if ("WARRIOR".equals(unitType)) {
            icon = new Image(AnimationManager.warriorIcon);
            nomePersonagem = "Soldado";
        } else {
            icon = new Image(AnimationManager.archerIcon);
            nomePersonagem = "Arqueiro";
        }

        icon.setScaling(com.badlogic.gdx.utils.Scaling.stretch);
        icon.setSize(GameConfig.UNIT_ICON_SIZE, GameConfig.UNIT_ICON_SIZE);
        icon.setOrigin(GameConfig.UNIT_ICON_SIZE / 2f, GameConfig.UNIT_ICON_SIZE / 2f);
        icon.setScale(1f);

        // 3. Adicionamos a lógica de arrastar APENAS na imagem, para você puxar pelo desenho
        dragAndDrop.addSource(new DragAndDrop.Source(icon) {
            @Override
            public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
                DragAndDrop.Payload payload = new DragAndDrop.Payload();
                payload.setObject(unitType);

                Image dragImage;
                if ("WARRIOR".equals(unitType)) {
                    dragImage = new Image(AnimationManager.warriorIcon);
                } else {
                    dragImage = new Image(AnimationManager.archerIcon);
                }

                dragImage.setScaling(com.badlogic.gdx.utils.Scaling.stretch);
                dragImage.setSize(GameConfig.UNIT_ICON_SIZE, GameConfig.UNIT_ICON_SIZE);
                dragImage.setOrigin(GameConfig.UNIT_ICON_SIZE / 2f, GameConfig.UNIT_ICON_SIZE / 2f);

                payload.setDragActor(dragImage);

                dragAndDrop.setDragActorPosition(
                    GameConfig.UNIT_ICON_SIZE / 2f,
                    -(GameConfig.UNIT_ICON_SIZE / 2f)
                );

                return payload;
            }
        });

        // 4. Criamos o Texto (Label) usando o Skin do seu jogo
        com.badlogic.gdx.scenes.scene2d.ui.Label label = new com.badlogic.gdx.scenes.scene2d.ui.Label(nomePersonagem, skin);
        label.setAlignment(com.badlogic.gdx.utils.Align.center);

        // 5. Montamos o layout: Imagem em cima, espaço extra embaixo, e o Texto na nova linha
        container.add(icon).size(GameConfig.UNIT_ICON_SIZE).padBottom(-40f).row();
        container.add(label).expandX().fillX();

        return container;
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

