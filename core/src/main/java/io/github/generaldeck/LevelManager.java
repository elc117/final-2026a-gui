package io.github.generaldeck;

public class LevelManager {

    // Define o fim do jogo!
    public static final int MAX_LEVEL = 3;
    // Controla qual é o nível máximo que o jogador já alcançou.
    public static int highestLevelUnlocked = 1;

    public static String[][] getEnemyCampaignFormation(int level, int cols, int rows) {
        String[][] enemyFormation = new String[cols][rows];

        // Lógica de progressão: Fases mais avançadas = Inimigos mais fortes/posicionados de forma tática
        switch(level) {
            case 1:
                // Fase 1: Bem fácil, apenas dois soldados para o jogador aprender a jogar
                enemyFormation[cols - 1][rows / 2] = "WARRIOR";
                enemyFormation[cols - 1][(rows / 2) + 1] = "WARRIOR";
                break;
            case 2:
                // Fase 2: Introdução aos Arqueiros na retaguarda
                enemyFormation[cols - 1][rows / 2] = "WARRIOR";
                enemyFormation[cols - 1][(rows / 2) - 1] = "WARRIOR";
                enemyFormation[cols - 2][rows / 2] = "ARCHER";
                break;
            case 3:
                // Fase 3: O desafio real (Soldados, Arqueiros e Monge)
                enemyFormation[cols - 1][rows / 2] = "WARRIOR";
                enemyFormation[cols - 1][(rows / 2) + 1] = "WARRIOR";
                enemyFormation[cols - 1][(rows / 2) - 1] = "WARRIOR";
                enemyFormation[cols - 2][rows / 2] = "ARCHER";
                enemyFormation[cols - 2][(rows / 2) - 1] = "MONK";
                break;
            default:
                // Fallback de segurança
                enemyFormation[2][2] = "WARRIOR";
                break;
        }
        return enemyFormation;
    }

    public static int getLevelBudget(int level) {
        switch(level) {
            case 1:  return 300;
            case 2:  return 500;
            case 3:  return 750; // Mais dinheiro para enfrentar a pedreira final
            default: return 300;
        }
    }
}
