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
        defineScale(radarRangeMeters);
        drawEntities(g);
    }

    private void drawEntities(Graphics g) {

        for (Entity e : world.getEntities().values()) {
            if (e == world.player)
                continue;
            drawEntity(g, e);
        }
    }

    private void drawEntity(Graphics g, Entity e) {
        Vec3 targetPos = e.getComponent(Transform.class).position;
        Vec3 playerPos = world.player.getComponent(Transform.class).position;
        Vec3 relativePos = new Vec3(targetPos.x-playerPos.x, targetPos.y-playerPos.y, targetPos.z-playerPos.z);

        int centerX = getWidth() / 2;
        int bottomY = getHeight();
        int screenX = (int)(centerX + relativePos.x * scale);
        int screenY = (int)(bottomY - relativePos.y * scale);

        drawContact(g, screenX, screenY, radius);
        //drawTrack(g, e.getComponent(Velocity.class), screenX,screenY,radius);
        //test
        //drawTrackLine(g, Entity.IFF.UNKNOWN, e.getComponent(Velocity.class), screenX, screenY);
    }



    private void drawDot(Graphics g, int centerX, int centerY, int radius) {
        g.fillOval(centerX - radius / 2, centerY - radius / 2, radius, radius);
    }

    //TODO: DOST ise -> contact - iff ******* düşman ise contact - track - iff

    private void drawContact(Graphics g, int centerX, int centerY, int radius) {
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(centerX - radius*3/4 / 2, centerY - radius*3/4 / 2, radius*3/4, radius*3/4);
        g.drawLine(centerX, centerY, centerX, centerY + radius);

    }

    private void drawTrack(Graphics g, Velocity vel, int centerX, int centerY, int radius) {
        g.setColor(Color.YELLOW);
        g.fillRect(centerX - radius / 2, centerY - radius / 2, radius, radius);
        drawTrackLine(g, Entity.IFF.SUSPECT, vel, centerX,centerY);
    }

    private void drawIFF(Graphics g, Velocity vel, Entity.IFF iff, int centerX, int centerY, int radius) {
        switch (iff){
            case FRIEND:
                g.setColor(Color.GREEN);
                g.drawOval(centerX - radius / 2, centerY - radius / 2, radius, radius);
                drawTrackLine(g, Entity.IFF.FRIEND, vel, centerX,centerY);
                break;
            case HOSTILE:
                g.setColor(Color.RED);
                g.drawOval(centerX - radius / 2, centerY - radius / 2, radius, radius);
                //TODO: Oval değil üçgen sembol cizdirt ve headinge döndürt
                drawTrackLine(g, Entity.IFF.HOSTILE, vel, centerX,centerY);
                break;
            case UNKNOWN:
                break;
            case NEUTRAL:
                break;
        }
    }

    private void drawTrackLine(Graphics g, Entity.IFF iff, Velocity vel, int centerX, int centerY) {
        double heading = Math.toDegrees(Math.atan2(vel.getVelocity().x, vel.getVelocity().y));

        if (heading < 0)
            heading += 360;

        int lineLength = radius*10/9;
        int endX = centerX + (int)(Math.sin(Math.toRadians(heading)) * lineLength);
        int endY = centerY - (int)(Math.cos(Math.toRadians(heading)) * lineLength);

        switch (iff){
            case HOSTILE:
                g.setColor(Color.RED);
                break;
            case FRIEND:
                g.setColor(Color.GREEN);
                break;
            case SUSPECT:
                g.setColor(Color.YELLOW);
                break;
            default:
                g.setColor(Color.LIGHT_GRAY);
                break;
        }
        g.drawLine(centerX, centerY, endX, endY);
    }

    public void defineScale(int rangeMeters){
        scale = (double) getHeight() / rangeMeters;
    }
}
