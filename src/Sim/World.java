package Sim;

import App.EYEApp;
import Mathf.Vec3;
import Sim.Components.Radar;
import Sim.Components.Rigidbody;
import Sim.Components.Transform;
import Sim.Components.Velocity;
import UI.Screen.MFDCanvas;

import java.awt.*;
import java.util.HashMap;

public class World {

    private EYEApp app;
    private HashMap<Integer, Entity> entities = new HashMap<>();
    private int entityId = 1;

    public static final double START_ALTITUDE = 3048.0; // 10.000 feet
    public static final double WORLD_ORIGIN = 100000.0;
    public Entity player;


    public World(EYEApp app){
        this.app = app;

        createEntity(Entity.IFF.HOSTILE, new Vec3(100000.0, 120000.0, 3048.0), new Vec3(0.0, 270.0, 0.0));
        createEntity(Entity.IFF.FRIEND, new Vec3(100000.0, 173000.0, 3048.0), new Vec3(0.0, -280.0, 0.0));
        createEntity(Entity.IFF.HOSTILE, new Vec3(100000.0, 176000.0, 3048.0), new Vec3(-180.0, -150.0, 0.0));
        createEntity(Entity.IFF.HOSTILE, new Vec3(140000.0, 140000.0, 3048.0), new Vec3(-180.0, -150.0, 0.0));
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
        e.setActive(false);
    }

    public HashMap<Integer, Entity> getEntities() {
        return entities;
    }


}
