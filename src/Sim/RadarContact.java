package Sim;

import Mathf.Vec3;
import Sim.Components.Velocity;

public class RadarContact {

    private int targetID;

    private Vec3 measuredPos;
    private Velocity measuredVel;
    private Vec3 predictedPos;
    private Velocity predictedVel;

    private int confidence; //TODO: tarama olunca arttır dönüş yoksa azalt
    private long lastDetectionTime;
    private DisplayState displayState;



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

        confidence = 1;
        displayState = DisplayState.CONTACT;
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

    public void increaseConfidence(){
        confidence++;
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
}
