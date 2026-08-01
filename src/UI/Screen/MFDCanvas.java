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
    private int overlayThickness = 0;

    private int selectedEntityId = -1;


    public MFDCanvas(World world){
        this.world = world;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        defineScale(world.getPlayer().getComponent(Radar.class).getRange());

        drawRadarDebug(g2);
        drawRollReference(g2);
        drawAzimuthRangeLines(g2, world.getPlayer().getComponent(Radar.class));
        drawEntities(g2);
    }

    private void drawEntities(Graphics2D g2) {

        Shape oldClip = g2.getClip();
        g2.setClip(getRadarDisplayArea());

        for (RadarContact c : world.getPlayer().getComponent(Radar.class).getContacts().values()) {

            if (c.getTargetID() == world.getPlayer().getId())
                continue;
            drawEntity(g2, c);
        }
        g2.setClip(oldClip);
    }

    private void drawEntity(Graphics2D g2, RadarContact contact) {  // TODO: relative pos hesaplaması canvasın işi değil

        Entity entity = world.getEntities().get(contact.getTargetID());

        if(entity == null){
            return;
        }

        Vec3 targetPos = contact.getPredictedPos();
        Vec3 playerPos = world.getPlayer().getComponent(Transform.class).position;
        Vec3 relativePos = new Vec3(targetPos.x-playerPos.x, targetPos.y-playerPos.y, targetPos.z-playerPos.z);

        Rectangle displayArea = getRadarDisplayArea();

        int centerX = displayArea.x + displayArea.width / 2;
        int bottomY = displayArea.y + displayArea.height;

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

        if(contact.getTargetID() == selectedEntityId){
            drawSelectionBracket(g2, screenX, screenY);
        }

        if(contact.getDisplayState() == RadarContact.DisplayState.TRACK || contact.getDisplayState() == RadarContact.DisplayState.IDENTIFIED){
            drawAltitudeLabel(g2, contact, screenX, screenY);
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

        gRot.fillRect(
                centerX - radius / 2,
                centerY - radius / 2,
                radius,
                radius
        );

        int lineStart = radius / 2;
        int lineLength = radius * 2 / 3;

        gRot.drawLine(
                centerX,
                centerY - lineStart,
                centerX,
                centerY - lineStart - lineLength
        );

        gRot.dispose();
    }

    private void drawIdentified(Graphics2D g2,
                                Velocity vel,
                                Entity.IFF iff,
                                int centerX,
                                int centerY,
                                int radius) {

        double heading = getHeading(vel);

        Graphics2D gRot = (Graphics2D) g2.create();
        gRot.rotate(Math.toRadians(heading), centerX, centerY);

        switch (iff){

            case FRIEND:

                gRot.setColor(Color.GREEN);

                int lineStart = radius / 2;
                int lineLength = radius * 2 / 3;

                gRot.drawOval(
                        centerX - radius / 2,
                        centerY - radius / 2,
                        radius,
                        radius
                );

                gRot.drawLine(
                        centerX,
                        centerY - lineStart,
                        centerX,
                        centerY - lineStart - lineLength
                );

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

                gRot.drawLine(
                        centerX,
                        centerY - nose,
                        centerX,
                        centerY - nose - radius
                );

                break;

            case UNKNOWN:
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
        Rectangle displayArea = getRadarDisplayArea();
        scale = (double)displayArea.height / rangeMeters;
    }

    private double getHeading(Velocity vel) {
        double heading = Math.toDegrees(Math.atan2(vel.getVelocity().x, vel.getVelocity().y));
        return heading < 0 ? heading + 360 : heading;
    }

    private void drawRadarDebug(Graphics2D g2){
        Radar radar = world.getPlayer().getComponent(Radar.class);


        drawAzimuthScale(g2, radar);
        drawBarScale(g2, radar);
        drawBeamIndicator(g2, radar);
    }


    private void drawAzimuthScale(Graphics2D g2, Radar radar){
        int w = getWidth();
        int h = getHeight();

        int baseY = (int)(h * 0.9);

        g2.setColor(Color.CYAN);

        int tickCount = 7;

        double left = w * 0.26;
        double right = w * 0.74;
        double width = right - left;

        Stroke oldStroke = g2.getStroke();
        g2.setStroke(new BasicStroke(1.5f));

        for (int i = 0; i < tickCount; i++) {

            double t;

            if (tickCount == 1)
                t = 0.5;
            else
                t = (double) i / (tickCount - 1);

            int x = (int)(left + t * width);

            int tickLength = h / 40;

            if (i == tickCount / 2)
                tickLength = (int)(tickLength * 1.5);

            g2.drawLine(x, baseY, x, baseY - tickLength);
        }

        g2.setStroke(oldStroke);
    }

    private void drawBeamIndicator(Graphics2D g2, Radar radar){

        int w = getWidth();
        int h = getHeight();

        int stem = h / 42;
        int top = w / 130;

        double fullLeft = -overlayThickness + top*10;
        double fullRight = w + overlayThickness - top*10;

        double canvasLeft = w * 0.26;
        double canvasRight = w * 0.74;

        double left;
        double right;

        if (radar.getAzimuth() >= 120) {

            left = fullLeft;
            right = fullRight;

        } else {

            double ratio = radar.getAzimuth() / 60.0;
            double width = (canvasRight - canvasLeft) * ratio;
            double center = (canvasLeft + canvasRight) / 2.0;

            left = center - width / 2.0;
            right = center + width / 2.0;
        }

        double normalized = (radar.getBeamOffset() + radar.getAzimuth() / 2.0) / radar.getAzimuth();

        int x = (int)(left + normalized * (right - left));
        int y = (int)(h * 0.94);

        g2.setColor(Color.CYAN);

        Stroke oldStroke = g2.getStroke();
        g2.setStroke(new BasicStroke(4f));

        int bottomTrim = stem / 4;
        g2.drawLine(x, y - bottomTrim, x, y - stem);
        g2.drawLine(x - top, y - stem, x + top, y - stem);
        g2.setStroke(oldStroke);
    }

    private void drawBarScale(Graphics2D g2, Radar radar){

        int w = getWidth();
        int h = getHeight();

        int x = (int)(w * 0.1);

        int lineCount = 7;

        int top = (int)(h * 0.26);
        int bottom = (int)(h * 0.74);

        double step = (double)(bottom - top) / (lineCount - 1);

        g2.setColor(Color.CYAN);
        Stroke oldStroke = g2.getStroke();
        g2.setStroke(new BasicStroke(1.5f));

        for (int i = 0; i < lineCount; i++) {

            int y = (int)(top + i * step);
            int lineLength = (i == lineCount / 2) ? (int)(w / 40.0 * 1.5) : (w / 40);

            g2.drawLine(x, y, x + lineLength, y);
            g2.drawLine(x, y, x + lineLength, y);
        }

        int centerY = (top + bottom) / 2;
        int spacing = (int)step;

        g2.setStroke(oldStroke);
        drawBarIndicator(g2, radar, x-w/70, centerY, spacing);

    }

    private void drawBarIndicator(Graphics2D g2, Radar radar, int x, int centerY, int spacing){

        int bars = radar.getBars();
        int currentBar = radar.getCurrentBar();
        double offset;

        if (bars == 1) {
            offset = 0;
        } else {
            offset = currentBar - (bars - 1) / 2.0;
        }

        double movementScale = 0.25;

        int y = (int)(centerY + offset * spacing * movementScale);
        int len = getWidth() / 60;

        g2.setColor(Color.CYAN);
        Stroke oldStroke = g2.getStroke();
        g2.setStroke(new BasicStroke(4f));

        g2.drawLine(x - len, y, x, y);
        g2.drawLine(x, y - len / 2, x, y + len / 2);
        g2.setStroke(oldStroke);
    }

    private Rectangle getRadarDisplayArea() {

        int left = overlayThickness + 18;
        int top = overlayThickness + 12;

        int right = getWidth() - overlayThickness - 18;
        int bottom = getHeight() - overlayThickness - 22;

        return new Rectangle(
                left,
                top,
                right - left,
                bottom - top
        );
    }

    public void setOverlayThickness(int overlayThickness){
        this.overlayThickness = overlayThickness;
    }

    private void drawRollReference(Graphics2D g2){

        int w = getWidth();
        int h = getHeight();

        int centerX = w / 2;
        int centerY = h / 2;

        int leftEdge = (int)(w * 0.26);
        int rightEdge = (int)(w * 0.74);

        int gap = w / 40;
        int wingLength = centerX - gap - leftEdge;
        int dropLength = h / 60;



        g2.setColor(Color.CYAN);

        Stroke oldStroke = g2.getStroke();
        g2.setStroke(new BasicStroke(2f));


        g2.drawLine(
                centerX - gap,
                centerY,
                centerX - gap - wingLength,
                centerY
        );

        g2.drawLine(
                centerX - gap - wingLength,
                centerY,
                centerX - gap - wingLength,
                centerY + dropLength
        );


        g2.drawLine(
                centerX + gap,
                centerY,
                centerX + gap + wingLength,
                centerY
        );

        g2.drawLine(
                centerX + gap + wingLength,
                centerY,
                centerX + gap + wingLength,
                centerY + dropLength
        );


        g2.setStroke(oldStroke);
    }

    private void drawAzimuthRangeLines(Graphics2D g2, Radar radar){

        int w = getWidth();
        int h = getHeight();

        double azimuth = radar.getAzimuth();

        double leftRatio;
        double rightRatio;


        if(azimuth == 60.0){

            leftRatio = 0.261;
            rightRatio = 0.74;

        }
        else if(azimuth == 20.0){

            leftRatio = 0.42;
            rightRatio = 0.58;

        }
        else{
            return;
        }


        int x1 = (int)(w * leftRatio);
        int x2 = (int)(w * rightRatio);


        int topOffset = (int)(h * 0.08);
        int bottomOffset = (int)(h * 0.12);


        int startY = topOffset;
        int endY = h - bottomOffset;


        g2.setColor(Color.CYAN);


        Stroke oldStroke = g2.getStroke();
        g2.setStroke(new BasicStroke(1.5f));

        g2.drawLine(x1, startY, x1, endY);

        g2.drawLine(x2, startY, x2, endY);


        g2.setStroke(oldStroke);
    }

    private void drawSelectionBracket(Graphics2D g2, int centerX, int centerY){

        int gap = radius-2;
        int height = radius + 4;

        Stroke old = g2.getStroke();

        g2.setColor(Color.LIGHT_GRAY);
        g2.setStroke(new BasicStroke(2f));

        g2.drawLine(centerX - gap, centerY - height / 2, centerX - gap, centerY + height / 2);
        g2.drawLine(centerX + gap, centerY - height / 2, centerX + gap, centerY + height / 2);

        g2.setStroke(old);
    }

    private void drawAltitudeLabel(Graphics2D g2, RadarContact contact, int centerX, int centerY){

        Entity entity = world.getEntities().get(contact.getTargetID());

        if(entity == null)
            return;

        Transform transform = entity.getComponent(Transform.class);

        if(transform == null)
            return;

        int altitude = (int)Math.round(metersToFeet(transform.position.z) / 1000.0);

        String text = String.valueOf(altitude);

        Font oldFont = g2.getFont();

        switch(contact.getDisplayState()){

            case CONTACT:
                g2.setColor(Color.LIGHT_GRAY);
                break;

            case TRACK:
                g2.setColor(Color.YELLOW);
                break;

            case IDENTIFIED:

                switch(entity.getIff()){

                    case FRIEND:
                        g2.setColor(Color.GREEN);
                        break;

                    case HOSTILE:
                        g2.setColor(Color.RED);
                        break;

                    default:
                        g2.setColor(Color.LIGHT_GRAY);
                        break;
                }

                break;
        }

        g2.setFont(oldFont.deriveFont(Font.BOLD, 20f));

        FontMetrics fm = g2.getFontMetrics();

        int x = centerX - fm.stringWidth(text) / 2;
        int y = centerY + radius + 18;


        g2.drawString(text, x, y);

        g2.setFont(oldFont);
    }

    public void refreshRadarDisplay(){
        repaint();
    }

    public void setSelectedEntityId(int id){
        selectedEntityId = id;
    }

    public int getSelectedEntityId(){
        return selectedEntityId;
    }

    private double metersToFeet(double meters){
        return meters * 3.28084;
    }
}
