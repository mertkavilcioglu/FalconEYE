package Sim.Components;

import Mathf.Vec3;
import Core.Component;

public class Velocity extends Component {

    private Vec3 velocity = new Vec3();

    public Velocity(double x, double y, double z){
        velocity.x = x;
        velocity.y = y;
        velocity.z = z;
    }

    public Velocity(Vec3 velocity){
        this.velocity = velocity;
    }

    @Override
    public void update(int deltaTime) {

    }

    public Vec3 getVelocity() {
        return velocity;
    }

    public void setVelocity(double x, double y, double z) {
        this.velocity.x = x;
        this.velocity.y = y;
        this.velocity.z = z;
    }

    public void setVelocity(Vec3 velocity) {
        this.velocity = velocity;
    }
}
