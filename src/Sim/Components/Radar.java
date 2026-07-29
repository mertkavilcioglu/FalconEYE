package Sim.Components;

import Mathf.Vec3;
import Sim.Component;
import Sim.Entity;
import Sim.RadarContact;

import java.util.HashMap;

public class Radar extends Component {
    private int range = 74080; // 40NM
    private int azimuth = 60;
    private int bars = 4;
    private HashMap<Integer, RadarContact> contacts = new HashMap<>();

    public Radar(){

    }

    @Override
    public void update(int deltaTime) {
       // System.out.println("RADAR");
        scan();
    }

    public void scan(){
        for(Entity e : parent.getWorld().getEntities().values()){
            if(e == parent)
                continue;

            Transform t = e.getComponent(Transform.class);
            Velocity v = new Velocity(
                    e.getComponent(Velocity.class).getVelocity().x,
                    e.getComponent(Velocity.class).getVelocity().y,
                    e.getComponent(Velocity.class).getVelocity().z
            );

            RadarContact data = new RadarContact(e.getId(), new Vec3(t.position.x, t.position.y, t.position.z), v);
            if(!contacts.containsKey(e.getId())){
                contacts.put(e.getId(),data );
            }
            else{
                contacts.replace(e.getId(), data);
            }

            /*

                 if(!contacts.containsKey(e.getId())) yerine

                 RadarContact c = contacts.get(id);

                    if(c == null)
                        createContact();
                    else
                        updateContact();

                   yapabilirsin

             */

        }
    }

    public int getRange(){
        return range;
    }

    public int getAzimuth(){
        return azimuth;
    }

    public HashMap<Integer, RadarContact> getContacts() {
        return contacts;
    }

}
