package Sim;

import App.EYEApp;

public class World {

    private EYEApp app;

    public World(EYEApp app){
        this.app = app;
    }

    public void update(int delta){
        // Update all entities in entity List hash, then they will update their components
    }
}
