package io.github.generaldeck;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.scenes.scene2d.Touchable;

import static com.badlogic.gdx.scenes.scene2d.Touchable.enabled;

public class PreparationScreen extends BaseScreen {
    private final Stage stage;
    private final DragAndDrop dragAndDrop;
    private final GridManager gridManager;
    private final Skin skin;
    private final String[][] enemyGrid;
    private final int currentLevel;
    private int currentFunds;
    private Label fundsLabel;
    private Texture coinTexture;
    private TextButton startButton;

    private Table detailsCard;

    private Texture backgroundTexture;

    public PreparationScreen(Main game, Skin skin, GridManager gridManager, int level) {
        super(game);

        this.stage = new Stage(new FitViewport(GameConfig.V_WIDTH, GameConfig.V_HEIGHT));
        this.dragAndDrop = new DragAndDrop();
        this.dragAndDrop.setKeepWithinStage(false);

        this.gridManager = gridManager;
        this.skin = skin;
        this.currentLevel = level;

        this.enemyGrid = LevelManager.getEnemyCampaignFormation(level, gridManager.getCols(), gridManager.getRows());
        this.currentFunds = LevelManager.getLevelBudget(level);

        buildUI();
    }

    private void buildUI() {
        // --- SISTEMA DINÂMICO DE CENÁRIOS ---
        String bgFileName;
        switch (currentLevel) {
            case 1:
                bgFileName = "Background_Combate.png";
                break;
            case 2:
                bgFileName = "Background_Combate2.png";
                break;
            case 3:
                bgFileName = "Background_Combate3.png";
                break;
            default:
                bgFileName = "Background_Combate.png";
        }

        backgroundTexture = new Texture(Gdx.files.internal("background_preparation.png"));
        Image bgImage = new Image(backgroundTexture);
        bgImage.setFillParent(true);
        stage.addActor(bgImage);

        Table rootTable = new Table();
        rootTable.setFillParent(true);
        stage.addActor(rootTable);

        Table playerGridTable = createGridTable();
        Table enemyGridTable = createEnemyGridPreview();
        Table paletteTable = createPaletteTable();
        startButton = createStartButton();

        Table gridsContainer = new Table();
        gridsContainer.add(playerGridTable).padRight(50);
        gridsContainer.add(enemyGridTable).padRight(50);

        // O cabeçalho agora fica super limpo, apenas com o título do Nível centralizado
        Table headerTable = new Table();
        Label titleLabel = new Label("Nível " + currentLevel, skin, "text_blue_ribbon");
        headerTable.add(titleLabel).expandX().center().padTop(20);

        // --- PREPARAÇÃO DO PAINEL FIXO DE INFORMAÇÕES (CENTRO) ---
        detailsCard = new Table();
        detailsCard.setBackground(skin.get("button_regular", TextButton.TextButtonStyle.class).up);
        updateDetailsCard(null);

        // --- MONTAGEM DA INTERFACE CENTRAL ---
        rootTable.add(headerTable).fillX().padBottom(GameConfig.PAD_DEFAULT).row();
        rootTable.add(gridsContainer).expand().center().row();
        rootTable.add(paletteTable).padBottom(15f).row();
        rootTable.add(detailsCard).size(650, 125).padBottom(GameConfig.PAD_DEFAULT);

        // --- NOVA CAMADA FLUTUANTE (OVERLAY) PARA OS CANTOS INFERIORES ---
        Table overlayTable = new Table();
        overlayTable.setFillParent(true);
        overlayTable.bottom();

        // 1. Caixa de Ouro (Canto Esquerdo)
        coinTexture = new Texture(Gdx.files.internal("Icon_03.png")); // Carrega o seu ícone
        Image coinImg = new Image(coinTexture);

        Table fundsBox = new Table();
        fundsBox.setBackground(skin.get("button_regular", TextButton.TextButtonStyle.class).up);
        // Padding para deixar a caixa pequenininha e justa ao redor da moeda
        fundsBox.pad(5f).padLeft(15f).padRight(20f);

        // Usamos a fonte menor e damos um leve zoom
        fundsLabel = new Label(String.valueOf(currentFunds), skin, "small");
        fundsLabel.setFontScale(1.3f);

        fundsBox.add(coinImg).size(36, 36).padRight(10); // Tamanho da moeda
        fundsBox.add(fundsLabel);

        overlayTable.add(fundsBox).padBottom(30).padLeft(30).left();

        // Empurra o próximo item (o botão de combate) tudo para a direita
        overlayTable.add().expandX();

        // 2. Botão Iniciar Combate (Canto Direito)
        overlayTable.add(startButton).size(240, 75).padBottom(30).padRight(30).right();

        stage.addActor(overlayTable);
        updateStartButtonState();
    }

    private void updateDetailsCard(String unitType) {
        detailsCard.clearChildren(); // Limpa o card atual

        if (unitType == null) {
            Label hint = new Label("Passe o mouse sobre uma tropa para ver os detalhes", skin, "small");
            detailsCard.add(hint).center();
            return;
        }

        String nome = ""; String desc = ""; int hp = 0, dano = 0, cura = 0;
        int custo = Unit.getCost(unitType);
        Animation<TextureRegion> anim = null;

        switch (unitType) {
            case "WARRIOR":
                nome = "Soldado"; desc = "Infantaria. Alta resistência para a linha de frente.";
                hp = 140; dano = 10; anim = AnimationManager.warriorIdle; break;
            case "ARCHER":
                nome = "Arqueiro"; desc = "Atirador. Dano massivo, mas frágil.";
                hp = 70; dano = 15; anim = AnimationManager.archerIdle; break;
            case "MONK":
                nome = "Monge"; desc = "Suporte. Cura os aliados mais feridos no combate.";
                hp = 80; cura = 25; anim = AnimationManager.monkIdle; break;
        }

        AnimatedImage img = new AnimatedImage(anim);
        img.setScaling(Scaling.fit);

        Table textTable = new Table();

        Table titleRow = new Table();

        Label nameLabel = new Label(nome, skin, "small");
        nameLabel.setFontScale(1.15f);

        Image titleCoinImg = new Image(coinTexture);

        Label costLabel = new Label(String.valueOf(custo), skin, "small");
        costLabel.setFontScale(1.15f);

        titleRow.add(nameLabel).left();
        titleRow.add(titleCoinImg).size(22, 22).padLeft(8).padRight(5);
        titleRow.add(costLabel).left();

        Label statsLabel = new Label(cura > 0 ? ("HP: " + hp + " | Cura: " + cura) : ("HP: " + hp + " | Dano: " + dano), skin, "small");

        Label descLabel = new Label(desc, skin, "small");
        descLabel.setWrap(true);

        textTable.add(titleRow).left().padBottom(3).row();
        textTable.add(statsLabel).left().padBottom(3).row();
        textTable.add(descLabel).left().expandX().fillX();

        detailsCard.add(img).size(80, 80).padLeft(20).padRight(20);
        detailsCard.add(textTable).expandX().fillX().padRight(20);
    }

    private void updateFundsDisplay() {
        fundsLabel.setText(String.valueOf(currentFunds));
    }

    private Table createEnemyGridPreview() {
        Table table = new Table();
        int cols = enemyGrid.length;
        int rows = enemyGrid[0].length;

        Texture textureTeste = new Texture(Gdx.files.internal("celula_teste_red.png"));
        TextureRegionDrawable cellTeste = new TextureRegionDrawable(new TextureRegion(textureTeste));

        for (int y = rows - 1; y >= 0; y--) {
            for (int x = 0; x < cols; x++) {
                Table cell = new Table();
                cell.setBackground(cellTeste);

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
        int cols = gridManager.getCols();
        int rows = gridManager.getRows();

        Texture textureTeste = new Texture(Gdx.files.internal("celula_teste.png"));
        TextureRegionDrawable cellTeste = new TextureRegionDrawable(new TextureRegion(textureTeste));

        for (int y = rows - 1; y >= 0; y--) {
            for (int x = 0; x < cols; x++) {
                Table cell = new Table();
                cell.setBackground(cellTeste);
                cell.setTouchable(enabled);
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
                DragInfo info = (DragInfo) payload.getObject();
                String[][] gridState = gridManager.getGridState();

                if (gridState[cellX][cellY] != null) return false;

                if (info.isFromShop()) {
                    return currentFunds >= Unit.getCost(info.unitType);
                }
                return true;
            }

            @Override
            public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                DragInfo info = (DragInfo) payload.getObject();

                if (info.isFromShop()) {
                    currentFunds -= Unit.getCost(info.unitType);
                    updateFundsDisplay();
                } else {
                    gridManager.removeBattalion(info.originX, info.originY);
                }

                gridManager.placeBattalion(cellX, cellY, info.unitType);

                Table targetCell = (Table) getActor();
                targetCell.clearChildren();
                Image placedImage = createUnitImage(info.unitType, GameConfig.TILE_SIZE);
                targetCell.add(placedImage).size(GameConfig.TILE_SIZE, GameConfig.TILE_SIZE);

                updateStartButtonState();
            }
        });

        dragAndDrop.addSource(new DragAndDrop.Source(cell) {
            @Override
            public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
                String[][] gridState = gridManager.getGridState();
                String unitType = gridState[cellX][cellY];

                if (unitType == null) return null;

                DragAndDrop.Payload payload = new DragAndDrop.Payload();
                payload.setObject(new DragInfo(unitType, cellX, cellY));

                Image dragImage = createUnitImage(unitType, GameConfig.TILE_SIZE);
                payload.setDragActor(dragImage);
                dragAndDrop.setDragActorPosition(GameConfig.TILE_SIZE / 2f, -(GameConfig.TILE_SIZE / 2f));

                cell.clearChildren();
                return payload;
            }

            @Override
            public void dragStop(InputEvent event, float x, float y, int pointer, DragAndDrop.Payload payload, DragAndDrop.Target target) {
                if (target == null) {
                    DragInfo info = (DragInfo) payload.getObject();
                    Image restoredImage = createUnitImage(info.unitType, GameConfig.TILE_SIZE);
                    cell.add(restoredImage).size(GameConfig.TILE_SIZE, GameConfig.TILE_SIZE);
                }
            }
        });
    }

    private Table createPaletteTable() {
        Table paletteTable = new Table();
        Label instLabel = new Label("Arraste as tropas para o tabuleiro:", skin, "small");
        paletteTable.add(instLabel).colspan(2).row();

        paletteTable.add(createDraggableUnit("WARRIOR")).size(GameConfig.UNIT_ICON_SIZE).padRight(GameConfig.PAD_DEFAULT);
        paletteTable.add(createDraggableUnit("ARCHER")).size(GameConfig.UNIT_ICON_SIZE);
        paletteTable.add(createDraggableUnit("MONK")).size(GameConfig.UNIT_ICON_SIZE);

        dragAndDrop.addTarget(new DragAndDrop.Target(paletteTable) {
            @Override
            public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                DragInfo info = (DragInfo) payload.getObject();
                return !info.isFromShop();
            }

            @Override
            public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                DragInfo info = (DragInfo) payload.getObject();

                currentFunds += Unit.getCost(info.unitType);
                updateFundsDisplay();
                gridManager.removeBattalion(info.originX, info.originY);

                updateStartButtonState();
            }
        });

        return paletteTable;
    }

    private TextButton createStartButton() {
        TextButton btn = UIFactory.createButton("Iniciar Combate", skin, "button_regular", () -> {
            String[][] playerFormation = gridManager.getGridState();
            game.changeScreen(new CombatScreen(game, skin, playerFormation, enemyGrid, currentLevel));
        });

        btn.getLabel().setFontScale(0.7f);

        return btn;
    }

    private Table createDraggableUnit(final String unitType) {
        Table container = new Table();

        Image icon = createUnitImage(unitType, GameConfig.UNIT_ICON_SIZE);
        String nomePersonagem;
        switch(unitType) {
            case "WARRIOR": nomePersonagem = "Soldado"; break;
            case "ARCHER": nomePersonagem = "Arqueiro"; break;
            case "MONK": nomePersonagem = "Monge"; break;
            default: nomePersonagem = "Erro";
        }

        dragAndDrop.addSource(new DragAndDrop.Source(icon) {
            @Override
            public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
                DragAndDrop.Payload payload = new DragAndDrop.Payload();
                payload.setObject(new DragInfo(unitType, -1, -1));

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

        // SENSOR DE MOUSE ---
        container.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                // Ao colocar o mouse no container, desenha a carta da tropa
                updateDetailsCard(unitType);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                // Apenas limpa a tela se o mouse saiu DE FATO para fora do botão
                // (evita piscar a tela quando o mouse passa por cima do texto do botão)
                if (toActor == null || !container.isAscendantOf(toActor)) {
                    updateDetailsCard(null);
                }
            }
        });

        return container;
    }

    private Image createUnitImage(String unitType, float size) {
        Animation<TextureRegion> anim;

        switch(unitType) {
            case "WARRIOR": anim = AnimationManager.warriorIdle; break;
            case "ARCHER":  anim = AnimationManager.archerIdle; break;
            case "MONK":    anim = AnimationManager.monkIdle; break;
            default:        anim = AnimationManager.warriorIdle; break;
        }

        AnimatedImage img = new AnimatedImage(anim);
        img.setScaling(Scaling.stretch);
        img.setSize(size, size);
        img.setOrigin(size / 2f, size / 2f);

        return img;
    }

    private static class DragInfo {
        final String unitType;
        final int originX;
        final int originY;

        DragInfo(String unitType, int originX, int originY) {
            this.unitType = unitType;
            this.originX = originX;
            this.originY = originY;
        }

        boolean isFromShop() {
            return originX == -1 && originY == -1;
        }
    }

    private static class AnimatedImage extends Image {
        private final Animation<TextureRegion> animation;
        private float stateTime = 0f;

        public AnimatedImage(Animation<TextureRegion> animation) {
            super(new TextureRegionDrawable(animation.getKeyFrame(0)));
            this.animation = animation;
        }

        @Override
        public void act(float delta) {
            super.act(delta);
            stateTime += delta;
            ((TextureRegionDrawable) getDrawable()).setRegion(animation.getKeyFrame(stateTime, true));
        }
    }

    private boolean hasPlayerPlacedUnits() {
        String[][] gridState = gridManager.getGridState();
        for (int x = 0; x < gridState.length; x++) {
            for (int y = 0; y < gridState[x].length; y++) {
                if (gridState[x][y] != null) {
                    return true; // Encontrou pelo menos um personagem!
                }
            }
        }
        return false; // Tabuleiro está vazio
    }

    private void updateStartButtonState() {
        if (startButton == null) return;

        if (hasPlayerPlacedUnits()) {
            startButton.setTouchable(Touchable.enabled);
            startButton.getColor().a = 1.0f; // 100% visível
        } else {
            startButton.setTouchable(Touchable.disabled);
            startButton.getColor().a = 0.5f; // 50% transparente (bloqueado)
        }
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
        if (backgroundTexture != null) backgroundTexture.dispose();
        if (coinTexture != null) coinTexture.dispose();
    }
}
