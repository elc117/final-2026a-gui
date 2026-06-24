package io.github.generaldeck;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;

// A carta é, na verdade, uma Tabela (Table) que se organiza sozinha!
public class CardWidget extends Table {

    public CardWidget(SkillCard cardData, Skin skin, Drawable cardBackground, Drawable cardIcon) {

        this.setBackground(cardBackground);

        // Título
        Label titleLabel = new Label(cardData.getName(), skin, "small");
        titleLabel.setAlignment(Align.center);

        // Icone
        Image iconImage = new Image(cardIcon);

        // 4. A Descrição (A Mágica do Wrap)
        Label descLabel = new Label(cardData.getDescription(), skin, "small");
        descLabel.setAlignment(Align.center);
        // O setWrap(true) é vital! Ele faz o texto quebrar de linha sozinho
        // se a frase for maior que a largura da carta.
        descLabel.setWrap(true);

        // ==========================================
        // MONTAGEM DO LAYOUT (De cima para baixo)
        // ==========================================

        // Adiciona o título, dá um espaço (pad) em cima e pula de linha (row)
        this.add(titleLabel).padTop(5).expandX().fillX().row();

        // Adiciona o ícone no meio, com um tamanho fixo (ex: 64x64)
        this.add(iconImage).size(128, 128).padTop(10).padBottom(40).row();

        // Adiciona o texto descritivo.
        // O width define onde o texto deve começar a quebrar de linha.
        this.add(descLabel).width(160).padBottom(15).expandY().top();

        // (Opcional) Força a carta inteira a ter um tamanho fixo (ex: 200x300)
        this.setSize(200, 300);
    }
}
