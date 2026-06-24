package Sim;

import App.EYEApp;

public class World {

    private EYEApp app;

    //TODO: Entity list ekle ve update icinde bunları update et.

    public World(EYEApp app){
        this.app = app;
    }

    public void update(int delta){
        // Update all entities in entity List hash, then they will update their components
    }
}
