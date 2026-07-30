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
        drawRadarDebug(g2);
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

    private void drawRadarDebug(Graphics2D g2){
        Radar radar = world.player.getComponent(Radar.class);


        drawAzimuthScale(g2, radar);
        drawBarScale(g2, radar);
        drawBeamIndicator(g2, radar);
    }


    private void drawAzimuthScale(Graphics2D g2, Radar radar){
        int w = getWidth();
        int h = getHeight();

        int baseY = (int)(h * 0.97);

        g2.setColor(Color.DARK_GRAY);

        int tickCount = 12;

        for(int i=0;i<=tickCount;i++){

            double t = (double)i / tickCount;

            int x = (int)(w * 0.15 + t * w * 0.70);

            g2.drawLine(x, baseY, x, baseY - h/40);
        }
    }

    private void drawBeamIndicator(Graphics2D g2, Radar radar){

        int w = getWidth();
        int h = getHeight();

        double normalized = (radar.getBeamOffset() + radar.getAzimuth()/2.0) / radar.getAzimuth();

        int x = (int)(w * 0.15 + normalized * w * 0.70);
        int y = (int)(h * 0.97);

        int stem = h/30;
        int top = w/80;

        g2.setColor(Color.GREEN);

        g2.drawLine(x, y, x, y-stem);
        g2.drawLine(x-top, y-stem, x+top, y-stem);
    }

    private void drawBarScale(Graphics2D g2, Radar radar){

        int w = getWidth();
        int h = getHeight();

        int bars = radar.getBars();
        int x = (int)(w*0.05);

        int top = (int)(h*0.20);
        int bottom = (int)(h*0.80);

        g2.setColor(Color.DARK_GRAY);

        for(int i=0;i<bars;i++){
            double t;
            if(bars==1)
                t=0.5;
            else
                t=(double)i/(bars-1);

            int y=(int)(top+t*(bottom-top));
            g2.drawLine(x, y, x+w/40, y);
        }

        drawBarIndicator(g2, radar, x, top, bottom);
    }

    private void drawBarIndicator(Graphics2D g2, Radar radar, int x, int top, int bottom){

        int bars = radar.getBars();
        int currentBar = radar.getCurrentBar();

        double normalized;

        if (bars == 1) {
            normalized = 0.5;
        } else {
            normalized = (double) currentBar / (bars - 1);
        }

        int y = (int)(top + normalized * (bottom - top));

        int len = getWidth() / 60;

        g2.setColor(Color.GREEN);

        g2.drawLine(x - len, y, x, y);
        g2.drawLine(x, y - len / 2, x, y + len / 2);
    }
}
