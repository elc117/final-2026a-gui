package io.github.generaldeck;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class CardFlechaPerfurante implements SkillCard {

    // Você pode trocar para AnimationManager.arrowIcon se tiver um ícone só da flecha!
    private static final Drawable ICON = new TextureRegionDrawable(AnimationManager.archerIcon);

    @Override
    public String getName() {
        return "Flecha Perfurante";
    }

    @Override
    public String getDescription() {
        return "Arqueiros ganham +5 de dano e suas flechas atravessam 1 inimigo.";
    }

    @Override
    public Drawable getIcon() {
        return ICON;
    }

    @Override
    public void applyEffect(ArmyModifiers modifiers) {
        // 1. Um pequeno buff de dano matemático
        modifiers.archerBonusDamage += 5f;

        // 2. Acumula a quantidade de corpos que a flecha consegue atravessar
        modifiers.archerBonusPierce += 1;
    }
}
