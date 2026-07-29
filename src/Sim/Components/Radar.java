package Sim.Components;

import Mathf.Vec3;
import Sim.Component;
import Sim.Entity;
import Sim.RadarContact;

import java.util.HashMap;


public class Radar extends Component {
    private int range = 74080; // 40NM
    private double azimuth = 60;
    private double elevation = 4;

    private double heading = 0; // horizontal for azimuth
    private double pitch = 0; // vertical for elevation
    private HashMap<Integer, RadarContact> contacts = new HashMap<>();

    private double beamWidth = 2.0;
    private double beamOffset = -30.0;
    private double beamSpeed = 120.0;
    private boolean sweepingRight = true;

    public Radar(){

    }

    @Override
    public void update(int deltaTime) {
       // System.out.println("RADAR");
        double deltaSeconds = deltaTime / 1000.0;
        updateBeam(deltaSeconds);
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
            if(!isInAzimuth(relativePos))
                continue;

            // check elevation
            if(!isInElevation(relativePos))
                continue;







//            Velocity targetVelocity = new Velocity(
//                    e.getComponent(Velocity.class).getVelocity().x,
//                    e.getComponent(Velocity.class).getVelocity().y,
//                    e.getComponent(Velocity.class).getVelocity().z
//            );
//
//            RadarContact data = new RadarContact(e.getId(), new Vec3(
//                    targetTransform.position.x,
//                    targetTransform.position.y,
//                    targetTransform.position.z),
//                    targetVelocity
//            );


            RadarContact contact = contacts.get(e.getId());

            if(contact == null){
                createContact(e);
            }
            else{
                updateContact(contact, e);
            }
        }
    }

    private void updateBeam(double deltaSeconds){

        if(sweepingRight){
            beamOffset += beamSpeed * deltaSeconds;

            if(beamOffset >= azimuth / 2.0){
                beamOffset = azimuth / 2.0;
                sweepingRight = false;
            }
        }
        else{

            beamOffset -= beamSpeed * deltaSeconds;

            if(beamOffset <= -azimuth / 2.0){
                beamOffset = -azimuth / 2.0;
                sweepingRight = true;
            }
        }

        //System.out.println(beamHeading);
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

    public boolean isInAzimuth(Vec3 relativePos){

        double bearing = getBearing(relativePos);
        double currentBeamHeading = heading + beamOffset;
        double angleDiff = getAngleDifference(bearing, currentBeamHeading);

        return Math.abs(angleDiff) <= beamWidth / 2.0;
    }


    private double getElevationAngle(Vec3 relativePos) {

        double horizontalDistance = Math.sqrt(relativePos.x * relativePos.x + relativePos.y * relativePos.y);

        return Math.toDegrees(Math.atan2(relativePos.z, horizontalDistance));
    }

    public boolean isInElevation(Vec3 relativePos){
        double elevationAngle = getElevationAngle(relativePos);
        double angleDiff = getAngleDifference(elevationAngle, pitch);
        return Math.abs(angleDiff) <= elevation / 2.0;
    }

    private void createContact(Entity e){
        Transform transform = e.getComponent(Transform.class);
        Velocity velocity = e.getComponent(Velocity.class);
        RadarContact contact = new RadarContact(e.getId(),
                new Vec3(transform.position.x, transform.position.y, transform.position.z),
                new Velocity(velocity.getVelocity().x, velocity.getVelocity().y, velocity.getVelocity().z));
        contacts.put(e.getId(), contact);
    }


    private void updateContact(RadarContact c, Entity e){

    }

}
