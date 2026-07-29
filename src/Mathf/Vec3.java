package Mathf;

public class Vec3 {

    public double x,y,z;

    public Vec3(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3(){ }

    public double distance(Vec3 other){
        double dx = other.x - x;
        double dy = other.y - y;
        double dz = other.z - z;
        return Math.sqrt(dx*dx + dy*dy + dz*dz);
    }

}
