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
            //ops
            //System.out.println("Rigidbody is ready to operate");
            // TODO: 1. hızı oku ve hareket ettir.
        }
        else if(parent != null){
            transform = parent.getComponent(Transform.class);
            velocity = parent.getComponent(Velocity.class);
            //System.out.println("Rigidbody transform and velocity found null in update, assigned. parent is not null btw");
        }
    }
}
