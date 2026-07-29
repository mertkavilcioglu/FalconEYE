package Sim.Components;

import Mathf.Vec3;
import Sim.Component;
import Sim.Entity;
import Sim.RadarContact;

import javax.management.relation.Relation;
import java.util.HashMap;


public class Radar extends Component {
    private int range = 74080; // 40NM
    private double azimuth = 60;
    private double elevation = 4;
    private double heading = 0;
    private HashMap<Integer, RadarContact> contacts = new HashMap<>();

    public Radar(){

    }

    @Override
    public void update(int deltaTime) {
       // System.out.println("RADAR");
        scan();
    }

    public void scan(){

        Transform parentTransform = parent.getComponent(Transform.class);

        for(Entity e : parent.getWorld().getEntities().values()){
            if(e == parent)
                continue;

            Transform targetTransform = e.getComponent(Transform.class);
            Vec3 relativePos = new Vec3(
                    targetTransform.position.x - parentTransform.position.x,
                    targetTransform.position.y - parentTransform.position.y,
                    targetTransform.position.z - parentTransform.position.z
            );

            // check distance
            if(!isInRange(parentTransform, targetTransform))
                continue;


            // check azimuth
            double bearing = getBearing(relativePos);
            double angleDiff = getAngleDifference(bearing, heading);
            if (Math.abs(angleDiff) > azimuth / 2.0)
                continue;





            Velocity targetVelocity = new Velocity(
                    e.getComponent(Velocity.class).getVelocity().x,
                    e.getComponent(Velocity.class).getVelocity().y,
                    e.getComponent(Velocity.class).getVelocity().z
            );

            RadarContact data = new RadarContact(e.getId(), new Vec3(
                    targetTransform.position.x,
                    targetTransform.position.y,
                    targetTransform.position.z),
                    targetVelocity
            );
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

    public boolean isInRange(Transform parent, Transform target){

        double distance = parent.position.distance(target.position);
        return !(distance > range);
    }

    public int getRange(){
        return range;
    }

    public double getAzimuth(){
        return azimuth;
    }

    public HashMap<Integer, RadarContact> getContacts() {
        return contacts;
    }

    private double getBearing(Vec3 relativePos) {
        double bearing = Math.toDegrees(Math.atan2(relativePos.x, relativePos.y));
        return bearing < 0 ? bearing + 360 : bearing;
    }

    private double getAngleDifference(double angle1, double angle2) {
        double diff = angle1 - angle2;

        while (diff > 180)
            diff -= 360;

        while (diff < -180)
            diff += 360;

        return diff;
    }

}
