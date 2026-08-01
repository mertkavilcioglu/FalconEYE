package Sim;

import App.EYEApp;
import Mathf.Vec3;
import Sim.Components.Radar;
import Sim.Components.Rigidbody;
import Sim.Components.Transform;
import Sim.Components.Velocity;

import static Mathf.UnitConverter.nmToMeters;
import static Sim.SimSettings.*;

import java.util.HashMap;
import java.util.Random;

public class World {

    private EYEApp app;
    private HashMap<Integer, Entity> entities = new HashMap<>();
    private int entityId = 1;

    public static final double START_ALTITUDE = 4572.0; // 15.000 feet
    public static final double WORLD_ORIGIN = 100000.0;
    private Entity player;

    private final Random random = new Random();


    public enum SpawnRange {
        CLOSE,
        MEDIUM,
        LONG
    }

    public World(EYEApp app){
        this.app = app;

        createEntity(Entity.IFF.HOSTILE, new Vec3(100000.0, 120000.0, START_ALTITUDE), new Vec3(0.0, 270.0, 0.0));
        createEntity(Entity.IFF.FRIEND, new Vec3(100000.0, 173000.0, START_ALTITUDE), new Vec3(0.0, -280.0, 0.0));
        createEntity(Entity.IFF.HOSTILE, new Vec3(100000.0, 176000.0, START_ALTITUDE), new Vec3(-180.0, -150.0, 0.0));
        createEntity(Entity.IFF.HOSTILE, new Vec3(140000.0, 140000.0, START_ALTITUDE-500), new Vec3(-180.0, -150.0, 0.0));
        createEntity(Entity.IFF.HOSTILE, new Vec3(100000.0, 100000.0, 85000.0), new Vec3(-180.0, -150.0, 0.0));


        player = createPlayer();
    }

    public void update(int delta){
        for(Entity e : entities.values()){
            e.update(delta);
        }
    }

    public Entity createEntity(Entity.IFF iff, Vec3 pos, Vec3 velocity){
        Entity e = new Entity(this, iff);

        e.addComponent(new Transform(pos));
        e.addComponent(new Velocity(velocity));
        e.addComponent(new Rigidbody());

        e.setId(entityId++);
        entities.put(e.getId(), e);
        return e;
    }

    public Entity createPlayer(){
        Entity e = new Entity(this, Entity.IFF.FRIEND);

        Vec3 pos = new Vec3(WORLD_ORIGIN, WORLD_ORIGIN, START_ALTITUDE);
        e.addComponent(new Transform(pos));

        Vec3 vel = new Vec3(0.0, 250.0, 0.0);
        e.addComponent(new Velocity(vel));

        e.addComponent(new Rigidbody());

        Radar radar = new Radar();
        e.addComponent(radar);

        e.setId(entityId++);
        entities.put(e.getId(), e);
        return e;
    }

    public void removeEntity(int id){

        Entity e = entities.remove(id);
        if(e != null){
            e.setActive(false);
        }
    }

    public void removeAllEntities(Entity.IFF iff){

        entities.values().removeIf(entity ->
                entity != player &&
                        entity.getIff() == iff
        );
    }

    public Entity createRandomEntity(Entity.IFF iff, SpawnRange range){

        Transform playerTransform = player.getComponent(Transform.class);
        Radar radar = player.getComponent(Radar.class);


        // Position
        double bearingDeg = -SPAWN_BEARING_LIMIT_DEG + random.nextDouble() * (SPAWN_BEARING_LIMIT_DEG * 2.0);
        double minRangeNM;
        double maxRangeNM;

        switch (range){

            case CLOSE:
                minRangeNM = SPAWN_CLOSE_MIN_RANGE_NM;
                maxRangeNM = SPAWN_CLOSE_MAX_RANGE_NM;
                break;

            case MEDIUM:
                minRangeNM = SPAWN_MEDIUM_MIN_RANGE_NM;
                maxRangeNM = SPAWN_MEDIUM_MAX_RANGE_NM;
                break;

            case LONG:
            default:
                minRangeNM = SPAWN_LONG_MIN_RANGE_NM;
                maxRangeNM = SPAWN_LONG_MAX_RANGE_NM;
                break;
        }

        double rangeNM =
                minRangeNM +
                        random.nextDouble() * (maxRangeNM - minRangeNM);

        double rangeMeters = nmToMeters(rangeNM);

        double worldBearing = radar.getHeading() + bearingDeg;
        double bearingRad = Math.toRadians(worldBearing);

        double x = playerTransform.position.x + Math.sin(bearingRad) * rangeMeters;
        double y = playerTransform.position.y + Math.cos(bearingRad) * rangeMeters;


        // Altitude
        double playerAltitudeFt = playerTransform.position.z * 3.28084;
        double altitudeOffsetFt;

        if(rangeNM < SPAWN_CLOSE_RANGE_NM){
            altitudeOffsetFt = -SPAWN_CLOSE_ALTITUDE_OFFSET_FT + random.nextDouble() * (SPAWN_CLOSE_ALTITUDE_OFFSET_FT * 2.0);
        }
        else if(rangeNM < SPAWN_MEDIUM_RANGE_NM){
            altitudeOffsetFt = -SPAWN_MEDIUM_ALTITUDE_OFFSET_FT + random.nextDouble() * (SPAWN_MEDIUM_ALTITUDE_OFFSET_FT * 2.0);
        }
        else{
            altitudeOffsetFt = -SPAWN_LONG_ALTITUDE_OFFSET_FT + random.nextDouble() * (SPAWN_LONG_ALTITUDE_OFFSET_FT * 2.0);
        }

        double altitudeFt = playerAltitudeFt + altitudeOffsetFt;
        altitudeFt = Math.max(SPAWN_MIN_ALTITUDE_FT, Math.min(SPAWN_MAX_ALTITUDE_FT, altitudeFt));

        double z = altitudeFt / 3.28084;


        // Velocity
        double headingDeg = random.nextDouble() * 360.0;

        double speedKt = SPAWN_MIN_SPEED_KT + random.nextDouble() * (SPAWN_MAX_SPEED_KT - SPAWN_MIN_SPEED_KT);

        double speedMS = speedKt / 1.94384;

        double headingRad = Math.toRadians(headingDeg);

        Vec3 velocity = new Vec3(Math.sin(headingRad) * speedMS, Math.cos(headingRad) * speedMS, 0.0);

        // Create
        return createEntity(iff, new Vec3(x, y, z), velocity);
    }

    public HashMap<Integer, Entity> getEntities() {
        return entities;
    }

    public Entity getPlayer(){
        return player;
    }


}
