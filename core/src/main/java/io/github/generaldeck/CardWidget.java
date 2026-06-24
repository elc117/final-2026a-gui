package io.github.generaldeck;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;

public class CardWidget extends Table {

    public CardWidget(SkillCard cardData, Skin skin, Drawable cardBackground, Drawable cardIcon) {

        this.setBackground(cardBackground);
        this.align(Align.top);

        // --- Título ---
        Label titleLabel = new Label(cardData.getName(), skin, "small");
        titleLabel.setAlignment(Align.center);
        titleLabel.setFontScale(0.90f);
        titleLabel.setWrap(true);

        // --- Ícone ---
        Image iconImage = new Image(cardIcon);

        // --- Descrição ---
        Label descLabel = new Label(cardData.getDescription(), skin, "small");
        descLabel.setAlignment(Align.center);
        descLabel.setFontScale(0.78f);
        descLabel.setWrap(true);

        // ==========================================
        // MONTAGEM DO LAYOUT (Ordem Visual Correta)
        // ==========================================

        this.add(iconImage).size(85, 85).padTop(25).expandY().center().row();
        this.add(titleLabel).width(165).padBottom(8).center().row();
        this.add(descLabel).width(165).height(90).padBottom(10).center();

        this.setSize(200, 300);
    }

    // ==========================================
    // A SOLUÇÃO DEFINITIVA DE COLISÃO
    // ==========================================
    @Override
    public com.badlogic.gdx.scenes.scene2d.Actor hit(float x, float y, boolean touchable) {
        // Se a carta inteira estiver desativada pelo jogo, ignora
        if (touchable && this.getTouchable() == com.badlogic.gdx.scenes.scene2d.Touchable.disabled) return null;

        // Se a coordenada (x, y) do mouse cair dentro do tamanho 200x300 da Carta,
        // a Carta assume 100% da autoria do clique, blindando os textos internos!
        return x >= 0 && x < getWidth() && y >= 0 && y < getHeight() ? this : null;
    }
}
