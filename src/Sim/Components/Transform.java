package Sim.Components;

import Sim.Component;
import Mathf.Vec3;

public class Transform extends Component {

    public Vec3 position = new Vec3(); // meters
    public Vec3 rotation;

    public Transform(double posX, double posY, double posZ){
        position.x = posX;
        position.y = posY;
        position.z = posZ;
        this.rotation = new Vec3(0, 0, 0);;
    }

    public Transform(Vec3 position){
        this.position = position;
        this.rotation = new Vec3(0, 0, 0);
    }

    @Override
    public void update(int deltaTime) {

    }

    public void setPosition(double x, double y, double z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
    }

    public void setPosition(Vec3 position) {
        this.position = position;
    }

    public void setRotation(double x, double y, double z) {
        this.rotation.x = x;
        this.rotation.y = y;
        this.rotation.z = z;
    }

    public void setRotation(Vec3 rotation) {
        this.rotation = rotation;
    }
}
