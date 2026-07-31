package Sim.Components;

import Mathf.Vec3;
import Sim.Component;
import Sim.Entity;
import Sim.RadarContact;

import java.util.HashMap;
import java.util.Iterator;


public class Radar extends Component {
    private int range = 74080; // 40NM

    private final int[] rangeOptions = {
            9260,      // 5 NM
            18520,     // 10 NM
            37040,     // 20 NM
            74080,     // 40 NM
            148160,    // 80 NM
            296320     // 160 NM
    };

    private double azimuth = 120;
    private double elevation = 4;

    private double heading = 0; // horizontal for azimuth
    private double pitch = 0; // vertical for elevation
    private HashMap<Integer, RadarContact> contacts = new HashMap<>();

    private double beamWidth = 2.0;
    private double beamOffset = -30.0;
    private double beamSpeed = 60.0;
    private boolean sweepingRight = true;

    private long contactTimeout = 5000; // ms

    private int bars = 4;
    private int currentBar = 0;
    private boolean movingUp = true;

    public Radar(){

    }

    @Override
    public void update(int deltaTime) {
        double deltaSeconds = deltaTime / 1000.0;
        updateBeam(deltaSeconds);
        predictContacts(deltaSeconds);
        //decreaseConfidence();
        scan();
        removeExpiredContacts();
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

            RadarContact contact = contacts.get(e.getId());

            if(contact == null){
                createContact(e);
            }
            else{
                updateContact(e);
            }
        }
    }

    private void updateBeam(double deltaSeconds){

        if(sweepingRight){
            beamOffset += beamSpeed * deltaSeconds;

            if(beamOffset >= azimuth / 2.0){
                beamOffset = azimuth / 2.0;
                sweepingRight = false;
                onSweepFinished();
            }
        }
        else{

            beamOffset -= beamSpeed * deltaSeconds;

            if(beamOffset <= -azimuth / 2.0){
                beamOffset = -azimuth / 2.0;
                sweepingRight = true;
                onSweepFinished();
            }
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
                new Vec3(transform.position.x,
                        transform.position.y,
                        transform.position.z),
                new Velocity(velocity.getVelocity().x,
                        velocity.getVelocity().y,
                        velocity.getVelocity().z));

        contacts.put(e.getId(), contact);
    }


    private void updateContact(Entity e) {

        RadarContact c = contacts.get(e.getId());

        Transform transform = e.getComponent(Transform.class);
        Velocity velocity = e.getComponent(Velocity.class);

        c.setMeasuredPos(new Vec3(
                transform.position.x,
                transform.position.y,
                transform.position.z));

        c.setMeasuredVel(new Velocity(
                velocity.getVelocity().x,
                velocity.getVelocity().y,
                velocity.getVelocity().z));

        c.setPredictedPos(new Vec3(
                transform.position.x,
                transform.position.y,
                transform.position.z));

        c.setPredictedVel(new Velocity(
                velocity.getVelocity().x,
                velocity.getVelocity().y,
                velocity.getVelocity().z));

        c.setLastDetectionTime(System.currentTimeMillis());
        c.increaseConfidence();
        c.setDetectedThisSweep(true);
    }

    private void predictContacts(double deltaSeconds){
        for (RadarContact c : contacts.values()) {
            Vec3 pos = c.getPredictedPos();
            Vec3 vel = c.getPredictedVel().getVelocity();

            pos.x += vel.x * deltaSeconds;
            pos.y += vel.y * deltaSeconds;
            pos.z += vel.z * deltaSeconds;
        }
    }

    public void removeExpiredContacts(){
        Iterator<RadarContact> it = contacts.values().iterator();
        long now = System.currentTimeMillis();

        while(it.hasNext()){

            RadarContact c = it.next();

            if(now - c.getLastDetectionTime() > contactTimeout){
                it.remove();
            }
        }
    }

    private void decreaseConfidence() {

        Iterator<RadarContact> it = contacts.values().iterator();

        while (it.hasNext()) {

            RadarContact c = it.next();

            if (!c.wasDetectedThisSweep()) {
                c.decreaseConfidence();
            }

            c.setDetectedThisSweep(false);

            if (c.getConfidence() <= 0) {
                it.remove();
            }
        }
    }

    public void onSweepFinished(){
        decreaseConfidence();
        nextBar();
    }

    private void updatePitch(){
        pitch = (-elevation / 2) + currentBar * (elevation / (bars - 1));
    }

    private void nextBar(){
        if (movingUp) {
            currentBar++;

            if (currentBar >= bars - 1) {
                currentBar = bars - 1;
                movingUp = false;
            }
        }
        else {
            currentBar--;

            if (currentBar <= 0) {
                currentBar = 0;
                movingUp = true;
            }
        }
        updatePitch();
    }

    public void increaseRange(){

        int index = getRangeIndex();

        if(index < rangeOptions.length - 1){
            range = rangeOptions[index + 1];
        }
    }


    public void decreaseRange(){

        int index = getRangeIndex();

        if(index > 0){
            range = rangeOptions[index - 1];
        }
    }


    private int getRangeIndex(){

        for(int i = 0; i < rangeOptions.length; i++){

            if(range == rangeOptions[i])
                return i;
        }

        return 0;
    }

    public int getRangeNM(){

        return range / 1852;
    }


    public double getBeamOffset(){
        return beamOffset;
    }
    public double getPitch(){
        return pitch;
    }
    public int getCurrentBar(){
        return currentBar;
    }
    public int getBars(){
        return bars;
    }
    public double getElevation(){
        return elevation;
    }

}
