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
    public static TextureRegionDrawable warriorIcon;

    // Arqueiro
    public static Animation<TextureRegion> archerRun;
    public static Animation<TextureRegion> archerShoot;
    public static TextureRegionDrawable archerIcon;

    public static Texture battleBackground;

    private static Texture warriorRunSheet;
    private static Texture warriorAttackSheet;
    private static Texture archerRunSheet;
    private static Texture archerShootSheet;

    public static void load() {
        if (warriorRunSheet != null) return;

        warriorRunSheet = new Texture("Warrior_Run.png");
        warriorAttackSheet = new Texture("Warrior_Attack1.png");
        archerRunSheet  = new Texture("Archer_Run.png");
        archerShootSheet = new Texture("Archer_Shoot.png");
        battleBackground = new Texture("Background_Combate.png");

        // Log para depurar tamanho real das sheets
        Gdx.app.log("AnimationManager", "Warrior: " + warriorRunSheet.getWidth() + "x" + warriorRunSheet.getHeight());
        Gdx.app.log("AnimationManager", "Archer:  " + archerRunSheet.getWidth()  + "x" + archerRunSheet.getHeight());

        int WARRIOR_RUN_FRAMES = 6;
        int WARRIOR_ATK_FRAMES = 4;
        int ARCHER_RUN_FRAMES  = 4;
        int ARCHER_SHOOT_FRAMES = 8;

        int warriorFrameW = warriorRunSheet.getWidth() / WARRIOR_RUN_FRAMES;
        int archerFrameW  = archerRunSheet.getWidth()  / ARCHER_RUN_FRAMES;

        // WARRIOR RUN
        TextureRegion[][] wrFrames = TextureRegion.split(warriorRunSheet,
            warriorRunSheet.getWidth() / WARRIOR_RUN_FRAMES, warriorRunSheet.getHeight());
        warriorRun = new Animation<>(0.1f, wrFrames[0]);
        warriorRun.setPlayMode(Animation.PlayMode.LOOP);

        // WARRIOR ATTACK
        TextureRegion[][] waFrames = TextureRegion.split(warriorAttackSheet,
            warriorAttackSheet.getWidth() / WARRIOR_ATK_FRAMES, warriorAttackSheet.getHeight());
        warriorAttack = new Animation<>(0.2f, waFrames[0]);
        warriorAttack.setPlayMode(Animation.PlayMode.NORMAL);

        // ARCHER RUN
        TextureRegion[][] awFrames = TextureRegion.split(archerRunSheet,
            archerRunSheet.getWidth() / ARCHER_RUN_FRAMES, archerRunSheet.getHeight());
        archerRun = new Animation<>(0.1f, awFrames[0]);
        archerRun.setPlayMode(Animation.PlayMode.LOOP);

        // ARCHER SHOOT
        TextureRegion[][] aaFrames = TextureRegion.split(archerShootSheet,
            archerShootSheet.getWidth() / ARCHER_SHOOT_FRAMES, archerShootSheet.getHeight());
        archerShoot = new Animation<>(0.4f, aaFrames[0]);
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
        if (archerRunSheet != null) archerRunSheet.dispose();
        if (archerShootSheet != null) archerShootSheet.dispose();
        if (battleBackground != null) battleBackground.dispose();

        warriorRunSheet = null;
        archerRunSheet = null;
        battleBackground = null;
    }
}
