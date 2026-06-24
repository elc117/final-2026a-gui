package io.github.generaldeck;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class CardVampirismo implements SkillCard {
    private static final Drawable ICON = new TextureRegionDrawable(AnimationManager.warriorIcon);

    @Override
    public String getName() { return "Sangue Frio"; }

    @Override
    public String getDescription() { return "Guerreiros ganham +20 HP e curam 30% do dano causado."; }

    @Override
    public Drawable getIcon() {

        return ICON;
    }

    @Override
    public void applyEffect(ArmyModifiers modifiers) {

        modifiers.warriorBonusHealth += 20f;

        modifiers.warriorOnHitEffects.add(new LifeStealEffect(0.3f));
    }
}
