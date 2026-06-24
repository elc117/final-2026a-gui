package io.github.generaldeck;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class DraftScreen extends BaseScreen {
    private final Stage stage;
    private final Skin skin;
    private final int level;
    private Texture backgroundTexture;

    public DraftScreen(Main game, Skin skin, int level) {
        super(game);
        this.skin = skin;
        this.level = level;
        this.stage = new Stage(new FitViewport(GameConfig.V_WIDTH, GameConfig.V_HEIGHT));

        buildUI();
    }

    private void buildUI() {
        backgroundTexture = new Texture(Gdx.files.internal("Background_CombateEXTRA.png"));
        Image bgImage = new Image(backgroundTexture);
        bgImage.setFillParent(true);
        stage.addActor(bgImage);

        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        Label title = new Label("Escolha uma Melhoria para o Exército", skin, "text_red_ribbon");
        root.add(title).padBottom(60).colspan(3).row();

        Drawable cardBg = skin.getDrawable("card_ouro");

        Array<SkillCard> randomCards = CardManager.getRandomCards(3);

        // desenha as cartas na tela
        for (int i = 0; i < randomCards.size; i++) {
            final SkillCard card = randomCards.get(i);

            // PRECISA FAZER OS ICONES AINDA
            Drawable cardIcon = card.getIcon();

            // Monta o Widget Visual
            CardWidget widget = new CardWidget(card, skin, cardBg, cardIcon);

            widget.setTransform(true);
            widget.setOrigin(com.badlogic.gdx.utils.Align.center);
            widget.addListener(new ClickListener() {
                @Override
                public void enter(InputEvent event, float x, float y, int pointer, com.badlogic.gdx.scenes.scene2d.Actor fromActor) {
                    super.enter(event, x, y, pointer, fromActor);
                    if (pointer == -1) {
                        widget.toFront();
                        widget.clearActions();
                        widget.addAction(com.badlogic.gdx.scenes.scene2d.actions.Actions.scaleTo(1.1f, 1.1f, 0.15f, com.badlogic.gdx.math.Interpolation.fade));
                    }
                }
                @Override
                public void exit(InputEvent event, float x, float y, int pointer, com.badlogic.gdx.scenes.scene2d.Actor toActor) {
                    super.exit(event, x, y, pointer, toActor);
                    if (pointer == -1) {
                        widget.clearActions();
                        widget.addAction(com.badlogic.gdx.scenes.scene2d.actions.Actions.scaleTo(1.0f, 1.0f, 0.15f, com.badlogic.gdx.math.Interpolation.fade));
                    }
                }
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    selecionarCarta(card);
                }
            });

            root.add(widget).pad(20);
        }
    }

    private void selecionarCarta(SkillCard card) {
        card.applyEffect(game.playerModifiers);

        Gdx.app.log("DRAFT", "Carta escolhida: " + card.getName());

        game.setScreen(new PreparationScreen(game, skin, new GridManager(5, 5), level));
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        // Limpa a tela (Coloque a sua textura de background se quiser)
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
