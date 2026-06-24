package Sim;
import Math.Vec3;

public class Entity {

    private Vec3 pos;
    private Vec3 speed;
    private boolean active;

    public Entity(){

    }

    public Entity(Vec3 pos, Vec3 speed){
        this.pos = pos;
        this.speed = speed;
        active = true;
    }

    public Vec3 getPos(){
        return pos;
    }

    public Vec3 getSpeed(){
        return speed;
    }

    public boolean isActive(){
        return active;
    }

    public void setActive(boolean act){
        active = act;
    }
}
