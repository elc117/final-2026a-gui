package io.github.generaldeck;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

// essa classe serve para simplificar a criação de botões usando callback
public class UIFactory {
    public static TextButton createButton(String text, Skin skin, Runnable callback) {
        TextButton button = new TextButton(text, skin);
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                callback.run();
            }
        });

        return button;
    }
}
