import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;

// A carta é, na verdade, uma Tabela (Table) que se organiza sozinha!
public class CardWidget extends Table {

    public CardWidget(SkillCard cardData, Skin skin, Drawable cardBackground, Drawable cardIcon) {

        // 1. O Fundo da carta (Um PNG em branco, apenas com a moldura bonita que você desenhar)
        this.setBackground(cardBackground);

        // 2. O Título da Carta (Usando a sua Skin!)
        Label titleLabel = new Label(cardData.getName(), skin, "title"); // ou "default" se não tiver "title"
        titleLabel.setAlignment(Align.center);

        // 3. A Imagem/Ícone da habilidade
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
        this.add(titleLabel).padTop(15).expandX().fillX().row();

        // Adiciona o ícone no meio, com um tamanho fixo (ex: 64x64)
        this.add(iconImage).size(64, 64).padTop(10).padBottom(10).row();

        // Adiciona o texto descritivo.
        // O width define onde o texto deve começar a quebrar de linha.
        this.add(descLabel).width(160).padBottom(15).expandY().top();

        // (Opcional) Força a carta inteira a ter um tamanho fixo (ex: 200x300)
        this.setSize(200, 300);
    }
}
