package io.github.generaldeck;

public class LevelManager {

    public static String[][] getEnemyCampaignFormation(int level, int cols, int rows) {
        String[][] enemyFormation = new String[GameConfig.GRID_ROWS][GameConfig.GRID_COLS];

        // configura as tropas do inimigo dependendo do nível
        // precisa achar um jeito de fazer as cartas e builds também
        switch(level) {
            case 1:
                enemyFormation[cols - 1][rows / 2] = "WARRIOR";
                enemyFormation[cols - 2][rows / 2] = "ARCHER";
                break;
            default:
                enemyFormation[2][2] = "WARRIOR";
                break;
        }
        return enemyFormation;
    }
}
