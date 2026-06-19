package io.github.generaldeck;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class AnimationManager {

    // --- ANIMAÇÕES ALIADOS (AZUL) ---
    // Guerreiro
    public static Animation<TextureRegion> warriorRun;
    public static Animation<TextureRegion> warriorAttack;
    public static Animation<TextureRegion> warriorAttack_alt;
    public static Animation<TextureRegion> warriorIdle;
    public static TextureRegionDrawable warriorIcon;

    // Arqueiro
    public static Animation<TextureRegion> archerRun;
    public static Animation<TextureRegion> archerShoot;
    public static Animation<TextureRegion> archerIdle;
    public static TextureRegionDrawable archerIcon;
    public static TextureRegion arrowIcon;

    // Monge
    public static Animation<TextureRegion> monkRun;
    public static Animation<TextureRegion> monkHeal;
    public static Animation<TextureRegion> monkHealEffect;
    public static Animation<TextureRegion> monkIdle;
    public static TextureRegionDrawable monkIcon;


    // --- ANIMAÇÕES INIMIGOS (VERMELHO) ---
    // Guerreiro Inimigo
    public static Animation<TextureRegion> enemyWarriorRun;
    public static Animation<TextureRegion> enemyWarriorAttack;
    public static Animation<TextureRegion> enemyWarriorAttack_alt;
    public static Animation<TextureRegion> enemyWarriorIdle;

    // Arqueiro Inimigo
    public static Animation<TextureRegion> enemyArcherRun;
    public static Animation<TextureRegion> enemyArcherShoot;
    public static Animation<TextureRegion> enemyArcherIdle;

    // Monge Inimigo
    public static Animation<TextureRegion> enemyMonkRun;
    public static Animation<TextureRegion> enemyMonkHeal;
    public static Animation<TextureRegion> enemyMonkIdle;

    // Cenário
    public static Texture battleBackground;

    // --- REFERÊNCIAS DE TEXTURAS (GERENCIAMENTO DE MEMÓRIA) ---
    private static Texture warriorRunSheet, warriorAttackSheet, warriorAttackSheet2, warriorIdleSheet;
    private static Texture archerRunSheet, archerShootSheet, archerIdleSheet, arrowSheet;
    private static Texture monkRunSheet, monkHealSheet, monkHealEffectSheet, monkIdleSheet;

    private static Texture enemyWarriorRunSheet, enemyWarriorAttackSheet, enemyWarriorAttackSheet2, enemyWarriorIdleSheet;
    private static Texture enemyArcherRunSheet, enemyArcherShootSheet, enemyArcherIdleSheet;
    private static Texture enemyMonkRunSheet, enemyMonkHealSheet, enemyMonkIdleSheet;


    public static void load() {
        if (warriorRunSheet != null) return; // Evita carregar duas vezes

        // 1. CARREGAMENTO DAS IMAGENS (ALIADOS)
        warriorRunSheet = new Texture("Warrior_Run.png");
        warriorAttackSheet = new Texture("Warrior_Attack1.png");
        warriorAttackSheet2 = new Texture("Warrior_Attack2.png");
        warriorIdleSheet = new Texture("Warrior_Idle.png");

        archerRunSheet  = new Texture("Archer_Run.png");
        archerShootSheet = new Texture("Archer_Shoot.png");
        archerIdleSheet = new Texture("Archer_Idle.png");

        monkRunSheet = new Texture("Run.png");
        monkHealSheet = new Texture("Heal.png");
        monkHealEffectSheet = new Texture("Heal_Effect.png");
        monkIdleSheet = new Texture("Idle.png");

        arrowSheet = new Texture("Arrow.png");
        battleBackground = new Texture("Background_Combate.png");

        // 2. CARREGAMENTO DAS IMAGENS (INIMIGOS)
        enemyWarriorRunSheet = new Texture("Enemy_Warrior_Run.png");
        enemyWarriorAttackSheet = new Texture("Enemy_Warrior_Attack1.png");
        enemyWarriorAttackSheet2 = new Texture("Enemy_Warrior_Attack2.png");
        enemyWarriorIdleSheet = new Texture("Enemy_Warrior_Idle.png");

        enemyArcherRunSheet  = new Texture("Enemy_Archer_Run.png");
        enemyArcherShootSheet = new Texture("Enemy_Archer_Shoot.png");
        enemyArcherIdleSheet = new Texture("Enemy_Archer_Idle.png");

        enemyMonkRunSheet = new Texture("Enemy_Run.png");
        enemyMonkHealSheet = new Texture("Enemy_Heal.png");
        enemyMonkIdleSheet = new Texture("Enemy_Idle.png");

        // 3. CONSTANTES DE FRAMES
        int WARRIOR_RUN_FRAMES = 6;
        int WARRIOR_ATK_FRAMES = 4;
        int ARCHER_RUN_FRAMES  = 4;
        int ARCHER_SHOOT_FRAMES = 8;
        int MONK_RUN_FRAMES = 4;
        int MONK_HEAL_FRAMES = 11;
        int MONK_EFFECT_FRAMES = 11;
        int IDLE_FRAMES = 6;
        int IDLE_FRAMES_WARRIOR = 8;

        arrowIcon = new TextureRegion(arrowSheet);

        // --- CONSTRUÇÃO DAS ANIMAÇÕES: GUERREIRO ALIADO ---
        TextureRegion[][] wrFrames = TextureRegion.split(warriorRunSheet, warriorRunSheet.getWidth() / WARRIOR_RUN_FRAMES, warriorRunSheet.getHeight());
        warriorRun = new Animation<>(0.1f, wrFrames[0]);
        warriorRun.setPlayMode(Animation.PlayMode.LOOP);

        TextureRegion[][] waFrames = TextureRegion.split(warriorAttackSheet, warriorAttackSheet.getWidth() / WARRIOR_ATK_FRAMES, warriorAttackSheet.getHeight());
        warriorAttack = new Animation<>(0.2f, waFrames[0]);
        warriorAttack.setPlayMode(Animation.PlayMode.NORMAL);

        TextureRegion[][] wa2Frames = TextureRegion.split(warriorAttackSheet2, warriorAttackSheet2.getWidth() / WARRIOR_ATK_FRAMES, warriorAttackSheet2.getHeight());
        warriorAttack_alt = new Animation<>(0.2f, wa2Frames[0]);
        warriorAttack_alt.setPlayMode(Animation.PlayMode.NORMAL);

        TextureRegion[][] wiFrames = TextureRegion.split(warriorIdleSheet, warriorIdleSheet.getWidth() / IDLE_FRAMES_WARRIOR, warriorIdleSheet.getHeight());
        warriorIdle = new Animation<>(0.15f, wiFrames[0]);
        warriorIdle.setPlayMode(Animation.PlayMode.LOOP);

        // --- CONSTRUÇÃO DAS ANIMAÇÕES: GUERREIRO INIMIGO ---
        TextureRegion[][] ewrFrames = TextureRegion.split(enemyWarriorRunSheet, enemyWarriorRunSheet.getWidth() / WARRIOR_RUN_FRAMES, enemyWarriorRunSheet.getHeight());
        enemyWarriorRun = new Animation<>(0.1f, ewrFrames[0]);
        enemyWarriorRun.setPlayMode(Animation.PlayMode.LOOP);

        TextureRegion[][] ewaFrames = TextureRegion.split(enemyWarriorAttackSheet, enemyWarriorAttackSheet.getWidth() / WARRIOR_ATK_FRAMES, enemyWarriorAttackSheet.getHeight());
        enemyWarriorAttack = new Animation<>(0.2f, ewaFrames[0]);
        enemyWarriorAttack.setPlayMode(Animation.PlayMode.NORMAL);

        TextureRegion[][] ewa2Frames = TextureRegion.split(enemyWarriorAttackSheet2, enemyWarriorAttackSheet2.getWidth() / WARRIOR_ATK_FRAMES, enemyWarriorAttackSheet2.getHeight());
        enemyWarriorAttack_alt = new Animation<>(0.2f, ewa2Frames[0]);
        enemyWarriorAttack_alt.setPlayMode(Animation.PlayMode.NORMAL);

        TextureRegion[][] ewiFrames = TextureRegion.split(enemyWarriorIdleSheet, enemyWarriorIdleSheet.getWidth() / IDLE_FRAMES_WARRIOR, enemyWarriorIdleSheet.getHeight());
        enemyWarriorIdle = new Animation<>(0.15f, ewiFrames[0]);
        enemyWarriorIdle.setPlayMode(Animation.PlayMode.LOOP);

        // --- CONSTRUÇÃO DAS ANIMAÇÕES: ARQUEIRO ALIADO ---
        TextureRegion[][] awFrames = TextureRegion.split(archerRunSheet, archerRunSheet.getWidth() / ARCHER_RUN_FRAMES, archerRunSheet.getHeight());
        archerRun = new Animation<>(0.1f, awFrames[0]);
        archerRun.setPlayMode(Animation.PlayMode.LOOP);

        TextureRegion[][] aaFrames = TextureRegion.split(archerShootSheet, archerShootSheet.getWidth() / ARCHER_SHOOT_FRAMES, archerShootSheet.getHeight());
        archerShoot = new Animation<>(0.6f, aaFrames[0]);
        archerShoot.setPlayMode(Animation.PlayMode.NORMAL);

        TextureRegion[][] aiFrames = TextureRegion.split(archerIdleSheet, archerIdleSheet.getWidth() / IDLE_FRAMES, archerIdleSheet.getHeight());
        archerIdle = new Animation<>(0.15f, aiFrames[0]);
        archerIdle.setPlayMode(Animation.PlayMode.LOOP);

        // --- CONSTRUÇÃO DAS ANIMAÇÕES: ARQUEIRO INIMIGO ---
        TextureRegion[][] eawFrames = TextureRegion.split(enemyArcherRunSheet, enemyArcherRunSheet.getWidth() / ARCHER_RUN_FRAMES, enemyArcherRunSheet.getHeight());
        enemyArcherRun = new Animation<>(0.1f, eawFrames[0]);
        enemyArcherRun.setPlayMode(Animation.PlayMode.LOOP);

        TextureRegion[][] eaaFrames = TextureRegion.split(enemyArcherShootSheet, enemyArcherShootSheet.getWidth() / ARCHER_SHOOT_FRAMES, enemyArcherShootSheet.getHeight());
        enemyArcherShoot = new Animation<>(0.6f, eaaFrames[0]);
        enemyArcherShoot.setPlayMode(Animation.PlayMode.NORMAL);

        TextureRegion[][] eaiFrames = TextureRegion.split(enemyArcherIdleSheet, enemyArcherIdleSheet.getWidth() / IDLE_FRAMES, enemyArcherIdleSheet.getHeight());
        enemyArcherIdle = new Animation<>(0.15f, eaiFrames[0]);
        enemyArcherIdle.setPlayMode(Animation.PlayMode.LOOP);

        // --- CONSTRUÇÃO DAS ANIMAÇÕES: MONGE ALIADO ---
        TextureRegion[][] mrFrames = TextureRegion.split(monkRunSheet, monkRunSheet.getWidth() / MONK_RUN_FRAMES, monkRunSheet.getHeight());
        monkRun = new Animation<>(0.1f, mrFrames[0]);
        monkRun.setPlayMode(Animation.PlayMode.LOOP);

        TextureRegion[][] mhFrames = TextureRegion.split(monkHealSheet, monkHealSheet.getWidth() / MONK_HEAL_FRAMES, monkHealSheet.getHeight());
        monkHeal = new Animation<>(0.1f, mhFrames[0]);
        monkHeal.setPlayMode(Animation.PlayMode.NORMAL);

        TextureRegion[][] meFrames = TextureRegion.split(monkHealEffectSheet, monkHealEffectSheet.getWidth() / MONK_EFFECT_FRAMES, monkHealEffectSheet.getHeight());
        monkHealEffect = new Animation<>(0.08f, meFrames[0]);
        monkHealEffect.setPlayMode(Animation.PlayMode.NORMAL);

        TextureRegion[][] miFrames = TextureRegion.split(monkIdleSheet, monkIdleSheet.getWidth() / IDLE_FRAMES, monkIdleSheet.getHeight());
        monkIdle = new Animation<>(0.15f, miFrames[0]);
        monkIdle.setPlayMode(Animation.PlayMode.LOOP);

        // --- CONSTRUÇÃO DAS ANIMAÇÕES: MONGE INIMIGO ---
        TextureRegion[][] emrFrames = TextureRegion.split(enemyMonkRunSheet, enemyMonkRunSheet.getWidth() / MONK_RUN_FRAMES, enemyMonkRunSheet.getHeight());
        enemyMonkRun = new Animation<>(0.1f, emrFrames[0]);
        enemyMonkRun.setPlayMode(Animation.PlayMode.LOOP);

        TextureRegion[][] emhFrames = TextureRegion.split(enemyMonkHealSheet, enemyMonkHealSheet.getWidth() / MONK_HEAL_FRAMES, enemyMonkHealSheet.getHeight());
        enemyMonkHeal = new Animation<>(0.1f, emhFrames[0]);
        enemyMonkHeal.setPlayMode(Animation.PlayMode.NORMAL);

        TextureRegion[][] emiFrames = TextureRegion.split(enemyMonkIdleSheet, enemyMonkIdleSheet.getWidth() / IDLE_FRAMES, enemyMonkIdleSheet.getHeight());
        enemyMonkIdle = new Animation<>(0.15f, emiFrames[0]);
        enemyMonkIdle.setPlayMode(Animation.PlayMode.LOOP);

        // --- ÍCONES PARA A UI ---
        warriorIcon = new TextureRegionDrawable(wrFrames[0][0]);
        archerIcon  = new TextureRegionDrawable(awFrames[0][0]);
        monkIcon    = new TextureRegionDrawable(mrFrames[0][0]);
    }

    public static void dispose() {
        // Libera Aliados da Memória
        if (warriorRunSheet != null) warriorRunSheet.dispose();
        if (warriorAttackSheet != null) warriorAttackSheet.dispose();
        if (warriorAttackSheet2 != null) warriorAttackSheet2.dispose();
        if (warriorIdleSheet != null) warriorIdleSheet.dispose();
        if (archerRunSheet != null) archerRunSheet.dispose();
        if (archerShootSheet != null) archerShootSheet.dispose();
        if (archerIdleSheet != null) archerIdleSheet.dispose();
        if (monkRunSheet != null) monkRunSheet.dispose();
        if (monkHealSheet != null) monkHealSheet.dispose();
        if (monkHealEffectSheet != null) monkHealEffectSheet.dispose();
        if (monkIdleSheet != null) monkIdleSheet.dispose();

        // Libera Inimigos da Memória
        if (enemyWarriorRunSheet != null) enemyWarriorRunSheet.dispose();
        if (enemyWarriorAttackSheet != null) enemyWarriorAttackSheet.dispose();
        if (enemyWarriorAttackSheet2 != null) enemyWarriorAttackSheet2.dispose();
        if (enemyWarriorIdleSheet != null) enemyWarriorIdleSheet.dispose();
        if (enemyArcherRunSheet != null) enemyArcherRunSheet.dispose();
        if (enemyArcherShootSheet != null) enemyArcherShootSheet.dispose();
        if (enemyArcherIdleSheet != null) enemyArcherIdleSheet.dispose();
        if (enemyMonkRunSheet != null) enemyMonkRunSheet.dispose();
        if (enemyMonkHealSheet != null) enemyMonkHealSheet.dispose();
        if (enemyMonkIdleSheet != null) enemyMonkIdleSheet.dispose();

        // Libera Globais
        if (battleBackground != null) battleBackground.dispose();
        if (arrowSheet != null) arrowSheet.dispose();

        // Limpa ponteiros
        warriorRunSheet = warriorAttackSheet = warriorAttackSheet2 = warriorIdleSheet = null;
        archerRunSheet = archerShootSheet = archerIdleSheet = null;
        monkRunSheet = monkHealSheet = monkHealEffectSheet = monkIdleSheet = null;

        enemyWarriorRunSheet = enemyWarriorAttackSheet = enemyWarriorAttackSheet2 = enemyWarriorIdleSheet = null;
        enemyArcherRunSheet = enemyArcherShootSheet = enemyArcherIdleSheet = null;
        enemyMonkRunSheet = enemyMonkHealSheet = enemyMonkIdleSheet = null;

        battleBackground = null;
        arrowSheet = null;
    }
}
