package UI.Screen;

import Mathf.Vec3;
import Sim.Components.Transform;
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

    private void drawTrack(Graphics g, int centerX, int centerY, int radius) {
        g.setColor(Color.YELLOW);
        g.fillRect(centerX - radius / 2, centerY - radius / 2, radius, radius);
        // TODO: headinge göre drawLine
    }

    private void drawIFF(Graphics g, Entity.IFF iff, int centerX, int centerY, int radius) {
        switch (iff){
            case FRIEND:
                g.setColor(Color.GREEN);
                g.drawOval(centerX - radius / 2, centerY - radius / 2, radius, radius);
                // TODO: headinge göre drawLine
                break;
            case HOSTILE:
                g.setColor(Color.RED);
                g.drawOval(centerX - radius / 2, centerY - radius / 2, radius, radius);
                // TODO: headinge göre drawLine
                break;
            case UNKNOWN:
                break;
            case NEUTRAL:
                break;
        }
    }

    public void defineScale(int rangeMeters){
        scale = (double) getHeight() / rangeMeters;
    }
}
