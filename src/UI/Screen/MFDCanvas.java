package UI.Screen;

import Mathf.Vec3;
import Sim.Components.Radar;
import Sim.Components.Transform;
import Sim.Components.Velocity;
import Sim.Entity;
import Sim.RadarContact;
import Sim.World;

import javax.swing.*;
import java.awt.*;

public class MFDCanvas extends JPanel {

    private World world;
    private int radius = 24;
    private double scale;

    public MFDCanvas(World world){
        this.world = world;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        defineScale(world.player.getComponent(Radar.class).getRange());
        drawEntities(g2);
    }

    private void drawEntities(Graphics2D g2) {

        for (RadarContact c : world.player.getComponent(Radar.class).getContacts().values()) {
            if (c.getTargetID() == world.player.getId())
                continue;
            drawEntity(g2, c);
        }
    }

    private void drawEntity(Graphics2D g2, RadarContact contact) {  // TODO: relative pos hesaplaması canvasın işi değil
        Vec3 targetPos = contact.getPredictedPos();
        Vec3 playerPos = world.player.getComponent(Transform.class).position;
        Vec3 relativePos = new Vec3(targetPos.x-playerPos.x, targetPos.y-playerPos.y, targetPos.z-playerPos.z);

        int centerX = getWidth() / 2;
        int bottomY = getHeight();
        int screenX = (int)(centerX + relativePos.x * scale);
        int screenY = (int)(bottomY - relativePos.y * scale);

        RadarContact.DisplayState displayState = contact.getDisplayState();
        if(displayState == RadarContact.DisplayState.CONTACT){
            drawContact(g2,screenX, screenY, radius);
        }
        else if(displayState == RadarContact.DisplayState.TRACK){
            drawTrack(g2, contact.getPredictedVel(), screenX,screenY,radius);
        }
        else if(displayState == RadarContact.DisplayState.IDENTIFIED){
            drawIdentified(g2,
                    contact.getPredictedVel(),
                    world.getEntities().get(contact.getTargetID()).getIff(),
                    screenX, screenY, radius);
        }
    }


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

    private void drawIdentified(Graphics2D g2, Velocity vel, Entity.IFF iff, int centerX, int centerY, int radius) {

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

                int nose = 2 * radius / 3;
                int base = radius / 3;
                int halfWidth = (int)(radius / Math.sqrt(3));

                Polygon triangle = new Polygon();

                triangle.addPoint(centerX, centerY - nose);
                triangle.addPoint(centerX - halfWidth, centerY + base);
                triangle.addPoint(centerX + halfWidth, centerY + base);

                gRot.drawPolygon(triangle);
                gRot.drawLine(centerX, centerY - nose, centerX, centerY - nose - radius);

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
        g2.drawLine(centerX+(endX-centerX)/2*19/20, centerY+(endY-centerY)/2*19/20, endX, endY);
    }

    public void defineScale(int rangeMeters){
        scale = (double) getHeight() / rangeMeters;
    }

    private double getHeading(Velocity vel) {
        double heading = Math.toDegrees(Math.atan2(vel.getVelocity().x, vel.getVelocity().y));
        return heading < 0 ? heading + 360 : heading;
    }
}
