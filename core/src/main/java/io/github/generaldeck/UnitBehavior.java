package io.github.generaldeck;

public enum UnitBehavior {
    SEEK_CLOSEST, // PERSEGUE O INIMIGO MAIS PRÓXIMO
    SEEK_LOWEST_HP, // FOCA NO ALVO MAIS FRACO
    FLEE, // FOGE DO INIMIGO MAIS PRÓXIMO
    STUNNED, // NÃO FAZ NADA
}
