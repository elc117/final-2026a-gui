package io.github.generaldeck;

public class UnitStats {
    // Stats dos Arqueiros
    public static final float ARCHER_HP = 100f;
    public static final float ARCHER_DAMAGE = 10f;
    public static final float ARCHER_RANGE = 200f;
    public static final float ARCHER_SPEED = 5f;

    // Stats dos Guerreiros
    public static final float WARRIOR_HP = 200f;
    public static final float WARRIOR_DAMAGE = 15f;
    public static final float WARRIOR_RANGE = 30f;
    public static final float WARRIOR_SPEED = 3f;

    private UnitStats() {
        throw new UnsupportedOperationException("Esta classe não deve ser instaciada. " +
            "Serve apenas para conter os stats das unidades");
    }
}
