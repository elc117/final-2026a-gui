package io.github.generaldeck;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.math.Interpolation;

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

        for (int i = 0; i < randomCards.size; i++) {
            final SkillCard card = randomCards.get(i);
            Drawable cardIcon = card.getIcon();

            final CardWidget widget = new CardWidget(card, skin, cardBg, cardIcon);

            widget.setTransform(true);
            widget.setOrigin(com.badlogic.gdx.utils.Align.center);

            float tempRotation = 0f;
            if (i == 0) tempRotation = 4f;       // Carta da esquerda
            else if (i == 2) tempRotation = -4f; // Carta da direita

            final float initialRotation = tempRotation;
            widget.setRotation(initialRotation);

            widget.addListener(new ClickListener() {
                @Override
                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    super.enter(event, x, y, pointer, fromActor);
                    if (pointer == -1) {
                        widget.toFront();
                        widget.clearActions();
                        widget.addAction(Actions.parallel(
                            Actions.scaleTo(1.4f, 1.4f, 0.15f, Interpolation.smooth),
                            Actions.rotateTo(0f, 0.15f, Interpolation.smooth)
                        ));
                    }
                }

                @Override
                public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                    super.exit(event, x, y, pointer, toActor);
                    if (pointer == -1) {
                        widget.clearActions();
                        widget.addAction(Actions.parallel(
                            Actions.scaleTo(1.0f, 1.0f, 0.2f, Interpolation.smooth),
                            Actions.rotateTo(initialRotation, 0.2f, Interpolation.smooth)
                        ));
                    }
                }

                @Override
                public void clicked(InputEvent event, float x, float y) {
                    selecionarCarta(card);
                }
            });

            widget.getColor().a = 0f;
            widget.setTouchable(Touchable.disabled);

            widget.addAction(Actions.sequence(
                Actions.delay(i * 0.2f),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {

                        float targetX = widget.getX();
                        float targetY = widget.getY();


                        widget.setPosition(GameConfig.V_WIDTH / 2f - widget.getWidth() / 2f, -widget.getHeight() - 50f);
                        widget.getColor().a = 1f;
                        widget.setScale(0.5f);

                        widget.addAction(Actions.sequence(
                            Actions.parallel(
                                Actions.moveTo(targetX, targetY, 0.6f, Interpolation.swingOut),
                                Actions.scaleTo(1f, 1f, 0.6f, Interpolation.swingOut)
                            ),
                            Actions.touchable(Touchable.enabled)
                        ));
                    }
                })
            ));

            root.add(widget).pad(25);
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
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        if (backgroundTexture != null) {
            backgroundTexture.dispose();
        }
        stage.dispose();
    }
}
