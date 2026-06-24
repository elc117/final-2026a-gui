package io.github.generaldeck;

import com.badlogic.gdx.utils.Array;

public class CardManager {
    // Retorna uma lista com todas as cartas que existem no jogo
    private static Array<SkillCard> getAllCards() {
        Array<SkillCard> deck = new Array<>();

        deck.add(new CardVampirismo());
        deck.add(new CardFlechaPerfurante());
        deck.add(new CardVampirismo());
        // deck.add(new CardVampirismo());
        // deck.add(new CardFlechaPerfurante());
        // deck.add(new CardMuralhaDeEscudos());

        return deck;
    }

    // Sorteia N cartas diferentes
    public static Array<SkillCard> getRandomCards(int amount) {
        Array<SkillCard> deck = getAllCards();
        deck.shuffle(); // embaralha

        Array<SkillCard> hand = new Array<>();
        int max = Math.min(amount, deck.size);
        for (int i = 0; i < max; i++) {
            hand.add(deck.get(i));
        }
        return hand;
    }
}
