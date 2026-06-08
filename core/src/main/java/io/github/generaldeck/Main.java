package io.github.gameprototipo;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    // assetManager gerencia os recursos. centraliza o carregamento das imagens, sprites, sons
    private AssetManager assetManager;
    // o Skin funciona como um stylesheet
    // vamos criar arquivos .atlas e .json para controlar a direção de arte do jogo
    // por enquanto esses arquivos são "defaults" que eu peguei do próprio libgdx, não representam o resultado final
    // depois fica fácil de trocar o estilo do jogo por que é só trocar esses arquivos
    private Skin skin;

    @Override
    public void create() {
        assetManager = new AssetManager();
        assetManager.load("uiskin.json", Skin.class);
        // talvez seja necessário chamar uma tela de "Loading" caso os assets sejam muito grandes
        // por enquanto dá pra deixar assim
        assetManager.finishLoading();
        skin = assetManager.get("uiskin.json", Skin.class);

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

    }
}
