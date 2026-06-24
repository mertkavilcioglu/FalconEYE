package Mathf;

public class Vec3 {

    public double x,y,z;

    public Vec3(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3(){ }

    public double getDistanceBetween(Vec3 vector1, Vec3 vector2){
        double dx = vector2.x - vector1.x;
        double dy = vector2.y - vector1.y;
        double dz = vector2.z - vector1.z;

        return Math.sqrt(dx*dx + dy*dy + dz*dz);
    }


}
