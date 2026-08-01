package Sim;

import Mathf.Vec3;
import Sim.Components.Velocity;

import static Sim.SimSettings.*;

public class RadarContact {

    private int targetID;

    private Vec3 measuredPos;
    private Velocity measuredVel;
    private Vec3 predictedPos;
    private Velocity predictedVel;

    private int confidence;
    private long lastDetectionTime;
    private DisplayState displayState;
    private int lastBeamId = -1;


    public enum DisplayState {
        CONTACT,
        TRACK,
        IDENTIFIED
    }


    public RadarContact(int targetID, Vec3 pos, Velocity vel){
        this.targetID = targetID;

        measuredPos = pos;
        measuredVel = vel;

        predictedPos = new Vec3(pos.x, pos.y, pos.z);

        predictedVel = new Velocity(
                vel.getVelocity().x,
                vel.getVelocity().y,
                vel.getVelocity().z);

        confidence = 0;
        displayState = DisplayState.CONTACT;
        lastDetectionTime = System.currentTimeMillis();
    }

    public void setMeasuredPos(Vec3 measuredPos){
        this.measuredPos = measuredPos;
    }

    public void setMeasuredVel(Velocity measuredVel){
        this.measuredVel = measuredVel;
    }

    public void setPredictedPos(Vec3 predictedPos){
        this.predictedPos = predictedPos;
    }

    public void setPredictedVel(Velocity predictedVel){
        this.predictedVel = predictedVel;
    }

    public void setLastDetectionTime(long time){
        this.lastDetectionTime = time;
    }

    public void increaseConfidence(int amount){
        confidence += amount;
        updateDisplayState();
    }

    public void decreaseConfidence(){

        if(confidence > 0){
            confidence--;
        }

        updateDisplayState();
    }

    private void updateDisplayState(){
        //System.out.println(confidence);
        if(confidence < CONTACT_TO_TRACK){
            displayState = DisplayState.CONTACT;
        }
        else if(confidence < TRACK_TO_IDENTIFIED){
            displayState = DisplayState.TRACK;
        }
        else{
            displayState = DisplayState.IDENTIFIED;
        }
    }

    public int getTargetID(){
        return targetID;
    }

    public Vec3 getMeasuredPos(){
        return measuredPos;
    }

    public Velocity getMeasuredVel(){
        return measuredVel;
    }

    public Vec3 getPredictedPos(){
        return predictedPos;
    }

    public Velocity getPredictedVel(){
        return predictedVel;
    }

    public int getConfidence(){
        return confidence;
    }

    public DisplayState getDisplayState(){
        return displayState;
    }

    public long getLastDetectionTime() {
        return lastDetectionTime;
    }

    public int getLastBeamId() {
        return lastBeamId;
    }

    public void setLastBeamId(int lastBeamId) {
        this.lastBeamId = lastBeamId;
    }
}
