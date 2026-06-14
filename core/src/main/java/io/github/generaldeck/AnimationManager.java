package io.github.generaldeck;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class AnimationManager {

    // Guerreiro
    public static Animation<TextureRegion> warriorWalk;
    public static Animation<TextureRegion> warriorAttack; // adicione Warrior_Attack.png depois
    public static TextureRegionDrawable warriorIcon;

    // Arqueiro
    public static Animation<TextureRegion> archerWalk;    // adicione Archer_Walk.png depois
    public static Animation<TextureRegion> archerShoot;
    public static TextureRegionDrawable archerIcon;

    public static Texture battleBackground;

    public static void load() {
        Texture warriorSheet = new Texture("Warrior_Run.png");
        Texture archerSheet  = new Texture("Archer_Shoot.png");
        battleBackground = new Texture("Background_Combate.png");

        // Log para depurar tamanho real das sheets
        Gdx.app.log("AnimationManager", "Warrior: " + warriorSheet.getWidth() + "x" + warriorSheet.getHeight());
        Gdx.app.log("AnimationManager", "Archer:  " + archerSheet.getWidth()  + "x" + archerSheet.getHeight());

        int WARRIOR_FRAMES = 6;
        int ARCHER_FRAMES  = 8;

        int warriorFrameW = warriorSheet.getWidth() / WARRIOR_FRAMES;
        int archerFrameW  = archerSheet.getWidth()  / ARCHER_FRAMES;

        TextureRegion[][] warriorFrames = TextureRegion.split(warriorSheet, warriorFrameW, warriorSheet.getHeight());
        TextureRegion[][] archerFrames  = TextureRegion.split(archerSheet,  archerFrameW,  archerSheet.getHeight());

        // Monta guerreiro
        TextureRegion[] wFrames = new TextureRegion[WARRIOR_FRAMES];
        for (int i = 0; i < WARRIOR_FRAMES; i++) wFrames[i] = warriorFrames[0][i];
        warriorWalk   = new Animation<>(0.1f, wFrames);
        warriorAttack = warriorWalk; // fallback até ter Warrior_Attack.png
        warriorIcon   = new TextureRegionDrawable(wFrames[0]);

        // Monta arqueiro
        TextureRegion[] aFrames = new TextureRegion[ARCHER_FRAMES];
        for (int i = 0; i < ARCHER_FRAMES; i++) aFrames[i] = archerFrames[0][i];
        archerShoot = new Animation<>(0.1f, aFrames);
        archerWalk  = archerShoot; // fallback até ter Archer_Walk.png
        archerIcon  = new TextureRegionDrawable(aFrames[0]);
    }
}
