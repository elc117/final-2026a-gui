package io.github.generaldeck;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class AnimationManager {

    // Guerreiro
    public static Animation<TextureRegion> warriorRun;
    public static Animation<TextureRegion> warriorAttack;
    public static Animation<TextureRegion> warriorAttack_alt;
    public static TextureRegionDrawable warriorIcon;

    // Arqueiro
    public static Animation<TextureRegion> archerRun;
    public static Animation<TextureRegion> archerShoot;
    public static TextureRegionDrawable archerIcon;
    public static TextureRegion arrowIcon;

    public static Texture battleBackground;

    private static Texture warriorRunSheet;
    private static Texture warriorAttackSheet;
    private static Texture warriorAttackSheet2;
    private static Texture archerRunSheet;
    private static Texture archerShootSheet;
    private static Texture arrowSheet;

    public static void load() {
        if (warriorRunSheet != null) return;

        warriorRunSheet = new Texture("Warrior_Run.png");
        warriorAttackSheet = new Texture("Warrior_Attack1.png");
        warriorAttackSheet2 = new Texture("Warrior_Attack2.png");
        archerRunSheet  = new Texture("Archer_Run.png");
        archerShootSheet = new Texture("Archer_Shoot.png");
        battleBackground = new Texture("Background_Combate.png");
        arrowSheet = new Texture("Arrow.png");

        int WARRIOR_RUN_FRAMES = 6;
        int WARRIOR_ATK_FRAMES = 4;
        int ARCHER_RUN_FRAMES  = 4;
        int ARCHER_SHOOT_FRAMES = 8;

        arrowIcon = new TextureRegion(arrowSheet);

        // WARRIOR RUN
        TextureRegion[][] wrFrames = TextureRegion.split(warriorRunSheet,
            warriorRunSheet.getWidth() / WARRIOR_RUN_FRAMES, warriorRunSheet.getHeight());
        warriorRun = new Animation<>(0.1f, wrFrames[0]);
        warriorRun.setPlayMode(Animation.PlayMode.LOOP);

        // WARRIOR ATTACK 1
        TextureRegion[][] waFrames = TextureRegion.split(warriorAttackSheet,
            warriorAttackSheet.getWidth() / WARRIOR_ATK_FRAMES, warriorAttackSheet.getHeight());
        warriorAttack = new Animation<>(0.2f, waFrames[0]);
        warriorAttack.setPlayMode(Animation.PlayMode.NORMAL);

        // WARRIOR ATTACK 2
        TextureRegion[][] wa2Frames = TextureRegion.split(warriorAttackSheet2,
            warriorAttackSheet2.getWidth() / WARRIOR_ATK_FRAMES, warriorAttackSheet2.getHeight());
        warriorAttack_alt = new Animation<>(0.2f, wa2Frames[0]);
        warriorAttack.setPlayMode(Animation.PlayMode.NORMAL);

        // ARCHER RUN
        TextureRegion[][] awFrames = TextureRegion.split(archerRunSheet,
            archerRunSheet.getWidth() / ARCHER_RUN_FRAMES, archerRunSheet.getHeight());
        archerRun = new Animation<>(0.1f, awFrames[0]);
        archerRun.setPlayMode(Animation.PlayMode.LOOP);

        // ARCHER SHOOT
        TextureRegion[][] aaFrames = TextureRegion.split(archerShootSheet,
            archerShootSheet.getWidth() / ARCHER_SHOOT_FRAMES, archerShootSheet.getHeight());
        archerShoot = new Animation<>(0.6f, aaFrames[0]);
        archerShoot.setPlayMode(Animation.PlayMode.NORMAL);

        // icone de guerreirro
        warriorIcon = new TextureRegionDrawable(wrFrames[0][0]);

        // icone de arqueiro
        archerIcon  = new TextureRegionDrawable(awFrames[0][0]);
    }

    // metodo para ser chamado quando sair da tela de combate
    public static void dispose() {
        if (warriorRunSheet != null) warriorRunSheet.dispose();
        if (warriorAttackSheet != null) warriorAttackSheet.dispose();
        if (warriorAttackSheet2 != null) warriorAttackSheet2.dispose();
        if (archerRunSheet != null) archerRunSheet.dispose();
        if (archerShootSheet != null) archerShootSheet.dispose();
        if (battleBackground != null) battleBackground.dispose();
        if (arrowSheet != null) arrowSheet.dispose();

        warriorRunSheet = null;
        warriorAttackSheet = null;
        warriorAttackSheet2 = null;
        archerRunSheet = null;
        archerShootSheet = null;
        battleBackground = null;
    }
}
