package Core;

import java.util.HashMap;

public class Entity {

    private int id;
    private boolean active;
    private IFF iff;
    private World world;

    private HashMap<Class<? extends Component>, Component> componentList = new HashMap<>();

    public enum IFF {
        FRIEND,
        HOSTILE,
        SUSPECT,
        UNKNOWN,
        NEUTRAL
    }

    public Entity(World world, IFF iff){
        active = true;
        this.iff = iff;
        this.world = world;
    }

    public void update(int deltaTime) {
        for (Component component : componentList.values()) {
            component.update(deltaTime);
        }
    }

    public void addComponent(Component component) {
        component.setParent(this);
        componentList.put(component.getClass(), component);
    }

    public <T extends Component> T getComponent(Class<T> componentClass) {
        Component component = componentList.get(componentClass);

        if (component == null) {
            return null;
        }

        return componentClass.cast(component);
    }

    public boolean hasComponent(Class<? extends Component> componentClass) {
        return componentList.containsKey(componentClass);
    }

    public void removeComponent(Class<? extends Component> componentClass) {
        componentList.remove(componentClass);
    }


    public boolean isActive(){
        return active;
    }

    public void setActive(boolean act){
        active = act;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public IFF getIff(){
        return iff;
    }

    public World getWorld(){
        return world;
    }
}
