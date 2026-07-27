package UI.Screen;

import Mathf.Vec3;
import Sim.Components.Transform;
import Sim.Components.Velocity;
import Sim.Entity;
import Sim.World;

import javax.swing.*;
import java.awt.*;

public class MFDCanvas extends JPanel {

    private World world;
    private int radius = 24;
    private int radarRangeMeters = 74080; // 40NM //TODO: BUNU BURADA TUTMA RADAR COMPONENTİNDEN AL VE GÜNCELLE
    private double scale;

    public MFDCanvas(World world){
        this.world = world;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        defineScale(radarRangeMeters);
        drawEntities(g2);
    }

    private void drawEntities(Graphics2D g2) {

        for (Entity e : world.getEntities().values()) {
            if (e == world.player)
                continue;
            drawEntity(g2, e);
        }
    }

    private void drawEntity(Graphics2D g2, Entity e) {
        Vec3 targetPos = e.getComponent(Transform.class).position;
        Vec3 playerPos = world.player.getComponent(Transform.class).position;
        Vec3 relativePos = new Vec3(targetPos.x-playerPos.x, targetPos.y-playerPos.y, targetPos.z-playerPos.z);

        int centerX = getWidth() / 2;
        int bottomY = getHeight();
        int screenX = (int)(centerX + relativePos.x * scale);
        int screenY = (int)(bottomY - relativePos.y * scale);

        //TODO: RADARDAN GELEN DATAYA GÖRE RADAR BUNLARDAN BİRİNİ ÇİZDİRECECK,
        // TODO: FONKSİYON RADAR REQUESET PARAMETRESİ ALABİLİR BELKİ
        //test cases

        //drawContact(g2, screenX, screenY, radius);
        //drawTrack(g2, e.getComponent(Velocity.class), screenX,screenY,radius);
        drawIFF(g2, e.getComponent(Velocity.class),e.getIff(), screenX,screenY,radius);
    }



    private void drawDot(Graphics g, int centerX, int centerY, int radius) {
        g.fillOval(centerX - radius / 2, centerY - radius / 2, radius, radius);
    }

    //TODO: DOST ise -> contact - iff ******* düşman ise contact - track - iff

    private void drawContact(Graphics2D g2, int centerX, int centerY, int radius) {
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(centerX - radius*3/4 / 2, centerY - radius*3/4 / 2, radius*3/4, radius*3/4);
        g2.drawLine(centerX, centerY, centerX, centerY + radius);

    }

    private void drawTrack(Graphics2D g2, Velocity vel, int centerX, int centerY, int radius) {
        double heading = getHeading(vel);

        Graphics2D gRot = (Graphics2D) g2.create();
        gRot.rotate(Math.toRadians(heading), centerX, centerY);

        gRot.setColor(Color.YELLOW);
        gRot.fillRect(centerX - radius / 2, centerY - radius / 2, radius, radius);
        drawTrackLine(g2, Entity.IFF.SUSPECT, heading, centerX,centerY);
        gRot.dispose();
    }

    private void drawIFF(Graphics2D g2, Velocity vel, Entity.IFF iff, int centerX, int centerY, int radius) {

        double heading = getHeading(vel);

        Graphics2D gRot = (Graphics2D) g2.create();
        gRot.rotate(Math.toRadians(heading), centerX, centerY);

        switch (iff){
            case FRIEND:
                gRot.setColor(Color.GREEN);
                gRot.drawOval(centerX - radius / 2, centerY - radius / 2, radius, radius);
                drawTrackLine(g2, Entity.IFF.FRIEND, heading, centerX,centerY);
                break;
            case HOSTILE:
                gRot.setColor(Color.RED);
                gRot.drawOval(centerX - radius / 2, centerY - radius / 2, radius, radius);
                //TODO: Oval değil üçgen sembol cizdirt
                drawTrackLine(g2, Entity.IFF.HOSTILE, heading, centerX,centerY);
                break;
            case UNKNOWN:
                break;
            case NEUTRAL:
                break;
        }
        gRot.dispose();
    }

    private void drawTrackLine(Graphics2D g2, Entity.IFF iff, double heading, int centerX, int centerY) {

        int lineLength = radius*10/9;
        int endX = centerX + (int)(Math.sin(Math.toRadians(heading)) * lineLength);
        int endY = centerY - (int)(Math.cos(Math.toRadians(heading)) * lineLength);

        switch (iff){
            case HOSTILE:
                g2.setColor(Color.RED);
                break;
            case FRIEND:
                g2.setColor(Color.GREEN);
                break;
            case SUSPECT:
                g2.setColor(Color.YELLOW);
                break;
            default:
                g2.setColor(Color.LIGHT_GRAY);
                break;
        }
        g2.drawLine(centerX, centerY, endX, endY);
    }

    public void defineScale(int rangeMeters){
        scale = (double) getHeight() / rangeMeters;
    }

    private double getHeading(Velocity vel) {
        double heading = Math.toDegrees(
                Math.atan2(vel.getVelocity().x, vel.getVelocity().y));

        return heading < 0 ? heading + 360 : heading;
    }
}
