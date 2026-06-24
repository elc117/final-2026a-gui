package io.github.generaldeck;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public interface SkillCard {
    String getName();
    String getDescription();

    Drawable getIcon();

    void applyEffect(ArmyModifiers modifiers);
}
