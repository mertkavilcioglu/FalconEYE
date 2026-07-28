package Sim;

public class RadarContact {

    private Entity target;
    private int confidence; //TODO: tarama olunca arttır dönüş yoksa azalt
    private long lastDetectionTime;
    private DisplayState displayState;

    public enum DisplayState {
        CONTACT,
        TRACK,
        IDENTIFIED
    }

    public RadarContact(Entity e){
        target = e;
    }

    public Entity getTarget(){
        return target;
    }
}
