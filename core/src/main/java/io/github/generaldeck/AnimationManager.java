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
    public static Animation<TextureRegion> warriorIdle;

    // Arqueiro
    public static Animation<TextureRegion> archerRun;
    public static Animation<TextureRegion> archerShoot;
    public static TextureRegionDrawable archerIcon;
    public static TextureRegion arrowIcon;
    public static Animation<TextureRegion> archerIdle;

    // Monge
    public static Animation<TextureRegion> monkRun;
    public static Animation<TextureRegion> monkHeal;
    public static Animation<TextureRegion> monkHealEffect;
    public static TextureRegionDrawable monkIcon;
    public static Animation<TextureRegion> monkIdle;

    public static Texture battleBackground;

    // Referências de memória das Texturas base
    private static Texture warriorRunSheet;
    private static Texture warriorAttackSheet;
    private static Texture warriorAttackSheet2;
    private static Texture archerRunSheet;
    private static Texture archerShootSheet;
    private static Texture arrowSheet;
    private static Texture monkRunSheet;
    private static Texture monkHealSheet;
    private static Texture monkHealEffectSheet;
    private static Texture warriorIdleSheet;
    private static Texture archerIdleSheet;
    private static Texture monkIdleSheet;

    public static void load() {
        if (warriorRunSheet != null) return;

        warriorRunSheet = new Texture("Warrior_Run.png");
        warriorAttackSheet = new Texture("Warrior_Attack1.png");
        warriorAttackSheet2 = new Texture("Warrior_Attack2.png");
        archerRunSheet  = new Texture("Archer_Run.png");
        archerShootSheet = new Texture("Archer_Shoot.png");
        battleBackground = new Texture("Background_Combate.png");
        arrowSheet = new Texture("Arrow.png");

        monkRunSheet = new Texture("Run.png");
        monkHealSheet = new Texture("Heal.png");
        monkHealEffectSheet = new Texture("Heal_Effect.png");

        warriorIdleSheet = new Texture("Warrior_Idle.png");
        archerIdleSheet = new Texture("Archer_Idle.png");
        monkIdleSheet = new Texture("Idle.png");

        int WARRIOR_RUN_FRAMES = 6;
        int WARRIOR_ATK_FRAMES = 4;
        int ARCHER_RUN_FRAMES  = 4;
        int ARCHER_SHOOT_FRAMES = 8;
        int MONK_RUN_FRAMES = 4;
        int MONK_HEAL_FRAMES = 11;
        int MONK_EFFECT_FRAMES = 11;
        int IDLE_FRAMES = 6; // animação para quando está parado/respirando
        int IDLE_FRAMES_WARRIOR = 8;

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

        // WARRIOR ATTACK 2 (Bug corrigido)
        TextureRegion[][] wa2Frames = TextureRegion.split(warriorAttackSheet2,
            warriorAttackSheet2.getWidth() / WARRIOR_ATK_FRAMES, warriorAttackSheet2.getHeight());
        warriorAttack_alt = new Animation<>(0.2f, wa2Frames[0]);
        warriorAttack_alt.setPlayMode(Animation.PlayMode.NORMAL);

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

        // MONK RUN
        TextureRegion[][] mrFrames = TextureRegion.split(monkRunSheet,
            monkRunSheet.getWidth() / MONK_RUN_FRAMES, monkRunSheet.getHeight());
        monkRun = new Animation<>(0.1f, mrFrames[0]);
        monkRun.setPlayMode(Animation.PlayMode.LOOP);

        // MONK HEAL
        TextureRegion[][] mhFrames = TextureRegion.split(monkHealSheet,
            monkHealSheet.getWidth() / MONK_HEAL_FRAMES, monkHealSheet.getHeight());
        monkHeal = new Animation<>(0.1f, mhFrames[0]);
        monkHeal.setPlayMode(Animation.PlayMode.NORMAL);

        // MONK HEAL EFFECT
        TextureRegion[][] meFrames = TextureRegion.split(monkHealEffectSheet,
            monkHealEffectSheet.getWidth() / MONK_EFFECT_FRAMES, monkHealEffectSheet.getHeight());
        monkHealEffect = new Animation<>(0.08f, meFrames[0]);
        monkHealEffect.setPlayMode(Animation.PlayMode.NORMAL);

        TextureRegion[][] wiFrames = TextureRegion.split(warriorIdleSheet, warriorIdleSheet.getWidth() / IDLE_FRAMES_WARRIOR, warriorIdleSheet.getHeight());
        warriorIdle = new Animation<>(0.15f, wiFrames[0]); // 0.15f deixa a respiração suave
        warriorIdle.setPlayMode(Animation.PlayMode.LOOP);

        TextureRegion[][] aiFrames = TextureRegion.split(archerIdleSheet, archerIdleSheet.getWidth() / IDLE_FRAMES, archerIdleSheet.getHeight());
        archerIdle = new Animation<>(0.15f, aiFrames[0]);
        archerIdle.setPlayMode(Animation.PlayMode.LOOP);

        TextureRegion[][] miFrames = TextureRegion.split(monkIdleSheet, monkIdleSheet.getWidth() / IDLE_FRAMES, monkIdleSheet.getHeight());
        monkIdle = new Animation<>(0.15f, miFrames[0]);
        monkIdle.setPlayMode(Animation.PlayMode.LOOP);

        // Ícones para a UI
        warriorIcon = new TextureRegionDrawable(wrFrames[0][0]);
        archerIcon  = new TextureRegionDrawable(awFrames[0][0]);
        monkIcon    = new TextureRegionDrawable(mrFrames[0][0]);
    }

    // Metodo para ser chamado quando sair da tela de combate (Gerenciamento de Memória)
    public static void dispose() {
        if (warriorRunSheet != null) warriorRunSheet.dispose();
        if (warriorAttackSheet != null) warriorAttackSheet.dispose();
        if (warriorAttackSheet2 != null) warriorAttackSheet2.dispose();
        if (archerRunSheet != null) archerRunSheet.dispose();
        if (archerShootSheet != null) archerShootSheet.dispose();
        if (battleBackground != null) battleBackground.dispose();
        if (arrowSheet != null) arrowSheet.dispose();
        if (monkRunSheet != null) monkRunSheet.dispose();
        if (monkHealSheet != null) monkHealSheet.dispose();
        if (monkHealEffectSheet != null) monkHealEffectSheet.dispose();
        if (warriorIdleSheet != null) warriorIdleSheet.dispose();
        if (archerIdleSheet != null) archerIdleSheet.dispose();
        if (monkIdleSheet != null) monkIdleSheet.dispose();

        warriorRunSheet = null;
        warriorAttackSheet = null;
        warriorAttackSheet2 = null;
        archerRunSheet = null;
        archerShootSheet = null;
        battleBackground = null;
        arrowSheet = null;
        monkRunSheet = null;
        monkHealSheet = null;
        monkHealEffectSheet = null;
        warriorIdleSheet = null;
        archerIdleSheet = null;
        monkIdleSheet = null;
    }
}
