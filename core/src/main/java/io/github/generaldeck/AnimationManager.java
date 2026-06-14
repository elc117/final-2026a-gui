package io.github.generaldeck;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class AnimationManager {
    // A animação para o combate
    public static Animation<TextureRegion> warriorRun;
    public static TextureRegionDrawable warriorIcon;

    public static Animation<TextureRegion> archerShoot;
    public static TextureRegionDrawable archerIcon;

    public static void load() {
        Texture sheet = new Texture("Warrior_Run.png");
        Texture archerSheet = new Texture("Archer_Shoot.png");

        int WARRIOR_FRAMES = 6; // Warrior_Run.png tem 6 frames
        int ARCHER_FRAMES  = 8; // Archer_Shoot.png tem 8 frames

        // Calcula o tamanho de cada frame dinamicamente — sem hardcode
        int warriorFrameSize = sheet.getWidth() / WARRIOR_FRAMES;
        int archerFrameSize  = archerSheet.getWidth() / ARCHER_FRAMES;

        TextureRegion[][] pedacos         = TextureRegion.split(sheet,       warriorFrameSize, sheet.getHeight());
        TextureRegion[][] pedacosArqueiro = TextureRegion.split(archerSheet, archerFrameSize,  archerSheet.getHeight());

        // Monta o Arqueiro (8 frames)
        TextureRegion[] framesArqueiro = new TextureRegion[ARCHER_FRAMES];
        for (int i = 0; i < ARCHER_FRAMES; i++) {
            framesArqueiro[i] = pedacosArqueiro[0][i];
        }
        archerShoot = new Animation<>(0.1f, framesArqueiro);
        archerIcon  = new TextureRegionDrawable(framesArqueiro[0]);

        // Monta o Guerreiro (6 frames, não 8!)
        TextureRegion[] frames = new TextureRegion[WARRIOR_FRAMES];
        for (int i = 0; i < WARRIOR_FRAMES; i++) {
            frames[i] = pedacos[0][i];
        }
        warriorRun  = new Animation<>(0.1f, frames);
        warriorIcon = new TextureRegionDrawable(frames[0]);
    }
}
