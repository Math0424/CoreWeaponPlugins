package me.Math0424.WitheredGunGame.Arenas;

public enum ArenaType {

    TEAMS(ArenaTEAM.class),
    POINTS(ArenaPOINT.class),
    INFECTED(null),
    FFA(ArenaFFA.class);

    private final Class<? extends Arena> clazz;

    ArenaType(Class<? extends Arena> clazz) {
        this.clazz = clazz;
    }

    public Class<? extends Arena> getClazz() {
        return this.clazz;
    }

}
