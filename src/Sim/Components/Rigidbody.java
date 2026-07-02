package Sim.Components;

import Sim.Component;

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
            movement();
        }
        else if(parent != null){
            transform = parent.getComponent(Transform.class);
            velocity = parent.getComponent(Velocity.class);
        }
    }

    public void movement(){
        transform.position.x += velocity.getVelocity().x;
        transform.position.y += velocity.getVelocity().y;
        transform.position.z += velocity.getVelocity().z;
        //System.out.println("new pos: " + transform.position.x + " " + transform.position.y + " " + transform.position.z);
    }
}
