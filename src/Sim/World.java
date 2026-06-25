package Sim;

import App.EYEApp;
import Mathf.Vec3;

import java.util.HashMap;

public class World {

    private EYEApp app;
    private HashMap<Integer, Entity> entities = new HashMap<>();
    private int entityId = 1;


    public World(EYEApp app){
        this.app = app;
    }

    public void update(int delta){
        for(Entity e : entities.values()){
            e.update(delta);
        }
    }

    public void createEntity(Vec3 pos, Vec3 velocity){
        Entity e = new Entity(pos, velocity);
        e.setId(entityId++);
        entities.put(e.getId(), e);
    }

    public void removeEntity(int id){
        Entity e = entities.remove(id);
        e.setActive(false);
    }

    public HashMap<Integer, Entity> getEntities() {
        return entities;
    }

}
