package Sim.Components;

import Sim.Component;
import Sim.Entity;

public class Rigidbody extends Component {

    private Transform transform = null;
    private Velocity velocity = null;

    public Rigidbody(){
        if(parent != null){
            transform = parent.getComponent(Transform.class);
            velocity = parent.getComponent(Velocity.class);
        }
        else{
            //System.out.println("Parent is null, initialized Rigidbody without parent");
        }
    }

    @Override
    public void update(int deltaTime) {
        if(transform != null && velocity != null){
            // rb operations
            movement(deltaTime);
        }
    }

    public void movement(int deltaTime){
        double deltaSeconds = deltaTime / 1000.0;
        transform.position.x += velocity.getVelocity().x * deltaSeconds;
        transform.position.y += velocity.getVelocity().y * deltaSeconds;
        transform.position.z += velocity.getVelocity().z * deltaSeconds;
        //System.out.println("new pos: " + transform.position.x + " " + transform.position.y + " " + transform.position.z);
    }

    @Override
    public void setParent(Entity parent){
        super.setParent(parent);

        transform = parent.getComponent(Transform.class);
        velocity = parent.getComponent(Velocity.class);
    }
}
