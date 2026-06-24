package Sim;

public abstract class Component {
    protected Entity parent;

    public Component(){

    }

    public void setParent(Entity parent){
        this.parent = parent;
    }

    public Entity getParent(){
        return parent;
    }

    public abstract void update(int deltaTime);
}
