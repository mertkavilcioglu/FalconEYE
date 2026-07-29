package Sim;

import Mathf.Vec3;
import Sim.Components.Velocity;

public class RadarContact {

    private int targetID;
    private Vec3 targetPos;
    private Velocity targetVel;
    private int confidence; //TODO: tarama olunca arttır dönüş yoksa azalt
    private long lastDetectionTime;
    private DisplayState displayState;

    public enum DisplayState {
        CONTACT,
        TRACK,
        IDENTIFIED
    }

    public RadarContact(int targetID, Vec3 targetPos, Velocity targetVel){
        this.targetID = targetID;
        this.targetPos = targetPos;
        this.targetVel = targetVel;
    }

    public int getTargetID(){
        return targetID;
    }

    public Vec3 getTargetPos(){
        return targetPos;
    }

    public Velocity getTargetVel(){
        return targetVel;
    }
}
