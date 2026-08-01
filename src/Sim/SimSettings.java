package Sim;

public final class SimSettings {

    private SimSettings(){}

    // World
    public static final double WORLD_ORIGIN = 100000.0;
    public static final double START_ALTITUDE = 4572.0; // 15.000 ft


    // Entity Spawn
    public static final double SPAWN_BEARING_LIMIT_DEG = 50.0;
    public static final double SPAWN_CLOSE_MIN_RANGE_NM = 15.0;
    public static final double SPAWN_CLOSE_MAX_RANGE_NM = 30.0;
    public static final double SPAWN_MEDIUM_MIN_RANGE_NM = 30.0;
    public static final double SPAWN_MEDIUM_MAX_RANGE_NM = 50.0;
    public static final double SPAWN_LONG_MIN_RANGE_NM = 50.0;
    public static final double SPAWN_LONG_MAX_RANGE_NM = 100.0;
    public static final double SPAWN_CLOSE_RANGE_NM = 30.0;
    public static final double SPAWN_MEDIUM_RANGE_NM = 50.0;
    public static final double SPAWN_MIN_ALTITUDE_FT = 8000.0;
    public static final double SPAWN_MAX_ALTITUDE_FT = 30000.0;
    public static final double SPAWN_CLOSE_ALTITUDE_OFFSET_FT = 2000.0;
    public static final double SPAWN_MEDIUM_ALTITUDE_OFFSET_FT = 5000.0;
    public static final double SPAWN_LONG_ALTITUDE_OFFSET_FT = 8000.0;
    public static final double SPAWN_MIN_SPEED_KT = 220.0;
    public static final double SPAWN_MAX_SPEED_KT = 520.0;
    public static final double FORMATION_SPACING_NM = 1.5;
}