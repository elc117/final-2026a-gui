package io.github.generaldeck;

public class UnitStats {
    public static final UnitProfile ARCHER = new UnitProfile(UnitClass.RANGED, 100f, 25f, 500f, 80f, 1f);
    public static final UnitProfile WARRIOR = new UnitProfile(UnitClass.MELEE, 250f, 40f, 20f, 60f, 1f);

    private UnitStats() {
        throw new UnsupportedOperationException("Esta classe não deve ser instaciada. " +
            "Serve apenas para conter os stats das unidades");
    }
}
