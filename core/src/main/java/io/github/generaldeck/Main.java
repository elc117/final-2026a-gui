package io.github.generaldeck;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    private AssetManager assetManager;
    private Skin skin;
    public ArmyModifiers playerModifiers;

    @Override
    public void create() {
        assetManager = new AssetManager();
        assetManager.load("uiskin.json", Skin.class);
        assetManager.finishLoading();
        skin = assetManager.get("uiskin.json", Skin.class);

        AnimationManager.load(); // carrega todas as animações dos sprites logo ao iniciar

        Pixmap pixmap = new Pixmap(Gdx.files.internal("ponteiro.png"));
        int xHotspot = 0;
        int yHotspot = 0;
        Cursor cursorPersonalizado = Gdx.graphics.newCursor(pixmap, xHotspot, yHotspot);
        Gdx.graphics.setCursor(cursorPersonalizado);

        pixmap.dispose();

        playerModifiers = new ArmyModifiers();

        this.setScreen(new MainMenuScreen(this, skin));
    }

    public void changeScreen(Screen newScreen) {
        Screen oldScreen = this.getScreen();

        this.setScreen(newScreen);

        if(oldScreen != null) {
            oldScreen.dispose();
        }
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        AnimationManager.dispose();
    }
}
