package me.Math0424.WitheredControl.Arenas;

public enum ArenaType {

    TEAMS(null),
    POINTS(null),
    FFA(ArenaFFA.class);

    private Class<? extends Arena> clazz;

    ArenaType(Class<? extends Arena> clazz) {
        this.clazz = clazz;
    }

    public Class<? extends Arena> getClazz() {
        return this.clazz;
    }

}
